package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.dol.DolUtil;
import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.gecko.active.ActiveWrite32BitsCode;
import com.github.nicholasmoser.ppc.Branch;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for writing Gecko codes to the dol.
 */
public class GeckoWriter {

  private static final Logger LOGGER = Logger.getLogger(GeckoWriter.class.getName());

  private final Path dolPath;

  public GeckoWriter(Path dolPath) {
    this.dolPath = dolPath;
  }

  /**
   * Writes a list of Gecko codes to the dol. Any codes that require code hijacking will use the
   * given hijackStartAddress. A GeckoCodeGroup will be returned with the given codes and name.
   *
   * @param codes The code to write to the dol.
   * @param name The name of the GeckoCodeGroup.
   * @param hijackStartAddress The starting address of where to begin hijacking code if necessary.
   * @return The new GeckoCodeGroup.
   * @throws IOException If an I/O error occurs
   */
  public GeckoCodeGroup writeCodes(List<GeckoCode> codes, String name, long hijackStartAddress)
      throws IOException {
    if (!Files.isRegularFile(dolPath)) {
      throw new IOException("Cannot find main.dol in sys directory.");
    }
    long hijackAddress = hijackStartAddress;
    List<GeckoCode> newCodes = new ArrayList<>(codes.size());
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "rw")) {
      for (GeckoCode code : codes) {
        if (code instanceof Write32BitsCode) {
          Write32BitsCode write32BitsCode = (Write32BitsCode) code;
          ActiveWrite32BitsCode newCode = writeWrite32BitsCode(write32BitsCode, raf);
          newCodes.add(newCode);
        } else if (code instanceof InsertAsmCode) {
          InsertAsmCode insertAsmCode = (InsertAsmCode) code;
          ActiveInsertAsmCode newCode = writeInsertAsmCode(insertAsmCode, raf, hijackAddress);
          hijackAddress += newCode.getHijackedBytes().length;
          newCodes.add(newCode);
        }
      }
    }
    return new GeckoCodeGroup(name, newCodes);
  }

  /**
   * Writes a Write32BitsCode to the RandomAccessFile.
   *
   * @param code The Write32BitsCode.
   * @param raf The RandomAccessFile of the dol to write to.
   * @return The now active code.
   * @throws IOException If an I/O error occurs
   */
  private ActiveWrite32BitsCode writeWrite32BitsCode(Write32BitsCode code, RandomAccessFile raf)
      throws IOException {
    long targetAddress = code.getTargetAddress();
    long dolOffset = DolUtil.ram2dol(targetAddress);
    raf.seek(dolOffset);
    byte[] originalBytes = new byte[4];
    raf.read(originalBytes);
    raf.seek(dolOffset);
    byte[] bytesToWrite = code.getBytes();
    raf.write(bytesToWrite);
    return new ActiveWrite32BitsCode.Builder()
        .targetAddress(targetAddress)
        .bytes(bytesToWrite)
        .replacedBytes(originalBytes)
        .create();
  }

  /**
   * Writes an InsertAsmCode to the RandomAccessFile at the hijack starting address. This method
   * assumes that bounds checking for the hijacking have already been performed. The target address
   * will have a branch written to it that branches to the hijack start address.
   *
   * @param code               The InsertAsmCode.
   * @param raf                The RandomAccessFile of the dol to write to.
   * @param hijackStartAddress The starting address to hijack code.
   * @return The now active code.
   * @throws IOException If an I/O error occurs
   */
  private ActiveInsertAsmCode writeInsertAsmCode(InsertAsmCode code, RandomAccessFile raf,
      long hijackStartAddress) throws IOException {
    // Add branch to hijacked location
    long targetAddress = code.getTargetAddress();
    long dolOffset = DolUtil.ram2dol(targetAddress);
    raf.seek(dolOffset);
    byte[] originalBytes = new byte[4];
    if (raf.read(originalBytes) != 4) {
      throw new IOException("Could not read 4 byte target address of code: " + code);
    }
    raf.seek(dolOffset);
    byte[] branchToHijack = Branch.getBranchInstruction(targetAddress, hijackStartAddress);
    raf.write(branchToHijack);
    // Add the hijacked code
    long dolHijackOffset = DolUtil.ram2dol(hijackStartAddress);
    raf.seek(dolHijackOffset);
    byte[] bytesToWrite = code.getBytes();
    int hijackLength = bytesToWrite.length;
    byte[] hijackedBytes = new byte[hijackLength];
    if (raf.read(hijackedBytes) != hijackLength) {
      throw new IOException(
          String.format("Could not read %d hijacked bytes for %s", hijackLength, code.toString()));
    }
    raf.seek(dolHijackOffset);
    // Add branch backwards to hijacked code
    byte[] bytesToWriteFull = Arrays.copyOf(bytesToWrite, hijackLength);
    long branchAddress = hijackStartAddress + hijackLength - 4;
    byte[] branch = Branch.getBranchInstruction(branchAddress, targetAddress + 4);
    System.arraycopy(branch, 0, bytesToWriteFull, hijackLength - 4, 4);
    raf.write(bytesToWriteFull);
    // Return new active code
    return new ActiveInsertAsmCode.Builder()
        .targetAddress(targetAddress)
        .bytes(bytesToWrite)
        .replacedBytes(originalBytes)
        .hijackedAddress(hijackStartAddress)
        .hijackedBytes(hijackedBytes)
        .create();
  }

  /**
   * Removes the given GeckoCodeGroup from the dol file path. Returns whether or not the removal was
   * successful. If not successful, any issues will be logged out.
   *
   * @param group The GeckoCodeGroup to remove from the dol.
   * @return Whether the removal was successful.
   * @throws IOException If an I/O error occurs
   */
  public boolean removeCodes(GeckoCodeGroup group) throws IOException {
    if (!Files.isRegularFile(dolPath)) {
      throw new IOException("Cannot find main.dol in sys directory.");
    }
    boolean successful = true;
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "rw")) {
      List<GeckoCode> codes = group.getCodes();
      for (GeckoCode code : codes) {
        if (code instanceof ActiveWrite32BitsCode) {
          ActiveWrite32BitsCode writeCode = (ActiveWrite32BitsCode) code;
          long dolOffset = DolUtil.ram2dol(writeCode.getTargetAddress());
          raf.seek(dolOffset);
          raf.write(writeCode.getReplacedBytes());
        } else if (code instanceof ActiveInsertAsmCode) {
          ActiveInsertAsmCode insertCode = (ActiveInsertAsmCode) code;
          long dolOffset = DolUtil.ram2dol(insertCode.getTargetAddress());
          raf.seek(dolOffset);
          raf.write(insertCode.getReplacedBytes());
          dolOffset = DolUtil.ram2dol(insertCode.getHijackedAddress());
          raf.seek(dolOffset);
          raf.write(insertCode.getHijackedBytes());
        } else {
          LOGGER.log(Level.SEVERE, "Invalid code found: " + code);
          successful = false;
        }
      }
      return successful;
    }
  }
}
