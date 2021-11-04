package com.github.nicholasmoser.gnt4.dol;

import com.github.nicholasmoser.dol.DolUtil;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.ppc.Branch;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class for defragging hijacked codes in a dol.
 */
public class DolDefragger {

  private static final Logger LOGGER = Logger.getLogger(DolDefragger.class.getName());
  private final Path dolPath;
  private final byte[] hijackedBytes;
  private final List<ActiveInsertAsmCode> activeInsertAsmCodes;

  public DolDefragger(Path dolPath, List<GeckoCodeGroup> codeGroups, byte[] hijackedBytes) {
    this.dolPath = dolPath;
    this.hijackedBytes = hijackedBytes;
    this.activeInsertAsmCodes = getActiveInsertAsmCodes(codeGroups);
  }

  /**
   * Runs the defragging process. This will update the code objects for any codes that are moved
   * in the dol as a result. The algorithm used simply iterates over each code and moves it
   * immediately after the previous code. The first code will be moved to START_RAM_ADDRESS.
   *
   * @throws IOException If any I/O issues occur
   */
  public void run() throws IOException {
    if (activeInsertAsmCodes.isEmpty()) {
      return;
    }
    long oldEndOfHijacking = getEndOfHijacking(activeInsertAsmCodes);

    // Update the codes and the dol bytes
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "rw")) {
      // Adjust the first code if necessary
      ActiveInsertAsmCode firstCode = activeInsertAsmCodes.get(0);
      if (firstCode.getHijackedAddress() != DolHijack.START_RAM_ADDRESS) {
        LOGGER.info("Defragging code:     " + firstCode);
        writeBytesAndUpdateCode(raf, firstCode, DolHijack.START_RAM_ADDRESS);
        LOGGER.info("Defragging complete: " + firstCode);
      }
      // Adjust the rest of the codes if necessary (and they exist)
      for (int i = 1; i < activeInsertAsmCodes.size(); i++) {
        ActiveInsertAsmCode prevCode = activeInsertAsmCodes.get(i - 1);
        ActiveInsertAsmCode currCode = activeInsertAsmCodes.get(i);

        // Check for empty space between the end of the previous code and the current code
        long hijackedAddress = currCode.getHijackedAddress();
        long endOfPrevCode = prevCode.getHijackedAddress() + prevCode.getBytes().length;
        long diff = hijackedAddress - endOfPrevCode;
        if (diff < 0) {
          throw new IllegalStateException(
              String.format("Codes are not in order, see code at %08X", hijackedAddress));
        } else if (diff == 0) {
          // No space to remove, continue
          continue;
        }

        LOGGER.info("Defragging code:     " + currCode);
        writeBytesAndUpdateCode(raf, currCode, endOfPrevCode);
        LOGGER.info("Defragging complete: " + currCode);
      }

      if (hasDefraggingOccurred(oldEndOfHijacking)) {
        updateBytesAfterDefrag(raf, oldEndOfHijacking);
      }
    }
  }

  /**
   * Move the current code to the end of the previous code. Write the new code to the given file.
   * Update the code object to reflect this new information.
   *
   * @param raf The dol file to write the changes to.
   * @param currCode The current code to move.
   * @param endOfPrevCode The end of the previous code to move to.
   * @throws IOException If any I/O issues occur
   */
  private void writeBytesAndUpdateCode(RandomAccessFile raf, ActiveInsertAsmCode currCode, long endOfPrevCode) throws IOException {
    byte[] bytes = currCode.getBytes();
    long targetAddress = currCode.getTargetAddress();
    // Move the current code so that it is immediately after the previous code
    // Start by writing the new branch bytes
    byte[] branchBytes = Branch.getBranchInstruction(targetAddress, endOfPrevCode);
    raf.seek(DolUtil.ram2dol(targetAddress));
    raf.write(branchBytes);
    // Write the code bytes to the new hijacked address
    int hijackLength = bytes.length;
    byte[] bytesToWriteFull = Arrays.copyOf(bytes, hijackLength);
    long branchAddress = endOfPrevCode + hijackLength - 4;
    byte[] branch = Branch.getBranchInstruction(branchAddress, targetAddress + 4);
    System.arraycopy(branch, 0, bytesToWriteFull, hijackLength - 4, 4);
    raf.seek(DolUtil.ram2dol(endOfPrevCode));
    raf.write(bytesToWriteFull);
    // Update code
    currCode.setHijackedAddress(endOfPrevCode);
    updateHijackedBytes(currCode);
  }

  /**
   * Update the given code with the correct hijacked bytes from an original dol.
   *
   * @param currCode The code to update.
   */
  private void updateHijackedBytes(ActiveInsertAsmCode currCode) {
    int offset = (int) (DolUtil.ram2dol(currCode.getHijackedAddress()) - DolHijack.START_DOL_OFFSET);
    int length = currCode.getBytes().length;
    byte[] originalBytes = Arrays.copyOfRange(hijackedBytes, offset, offset + length);
    currCode.setHijackedBytes(originalBytes);
  }

  /**
   * Get a list of the active asm insertion codes from a list of gecko code groups.
   *
   * @param codeGroups The gecko code groups.
   * @return The list of active asm insertion codes.
   */
  private List<ActiveInsertAsmCode> getActiveInsertAsmCodes(List<GeckoCodeGroup> codeGroups) {
    List<ActiveInsertAsmCode> activeInsertAsmCodes = new ArrayList<>();
    for (GeckoCodeGroup codeGroup : codeGroups) {
      for (GeckoCode code : codeGroup.getCodes()) {
        if (code instanceof ActiveInsertAsmCode) {
          activeInsertAsmCodes.add((ActiveInsertAsmCode) code);
        }
      }
    }
    return activeInsertAsmCodes;
  }

  /**
   * Return the current end of hijacked bytes in the dol.
   *
   * @param activeInsertAsmCodes The active asm insertion codes.
   * @return The end of hijacked bytes in the dol.
   */
  public static long getEndOfHijacking(List<ActiveInsertAsmCode> activeInsertAsmCodes) {
    long furthestEnd = DolHijack.START_RAM_ADDRESS;
    for (ActiveInsertAsmCode insertCode : activeInsertAsmCodes) {
      long hijackedAddress = insertCode.getHijackedAddress();
      int length = insertCode.getHijackedBytes().length;
      long end = hijackedAddress + length;
      if (end > DolHijack.END_RAM_ADDRESS) {
        throw new IllegalStateException("codeGroups extends past max hijack: " + activeInsertAsmCodes);
      } else if (end > furthestEnd) {
        furthestEnd = end;
      }
    }
    return furthestEnd;
  }

  /**
   * Check if defragging has occurred by comparing the old end of hijacked bytes to the new end.
   * If the new end is closer to 0, then it has defragged as new space has freed up. If the new
   * hijacked end address matches the old, then no defragging as occurred.
   *
   * @param oldEndOfHijacking The old end of hijacking address.
   * @return If defragging has occurred.
   */
  private boolean hasDefraggingOccurred(long oldEndOfHijacking) {
    long newEndOfHijacking = getEndOfHijacking(activeInsertAsmCodes);
    long diff = oldEndOfHijacking - newEndOfHijacking;
    if (diff < 0) {
      String msg = "Somehow the new end of hijacking is further than before, this is unexpected.";
      msg += String.format("\nOld: 0x%X New: 0x%X", oldEndOfHijacking, newEndOfHijacking);
      throw new IllegalStateException(msg);
    }
    return diff > 0; // 0 means no defragging occurred
  }

  private void updateBytesAfterDefrag(RandomAccessFile raf, long oldEndOfHijacking) throws IOException {
    long newEndOfHijacking = getEndOfHijacking(activeInsertAsmCodes);
    long diff = oldEndOfHijacking - newEndOfHijacking;
    long dolStartOffset = DolUtil.ram2dol(newEndOfHijacking);
    int hijackOffset = (int) (dolStartOffset - DolHijack.START_DOL_OFFSET);
    int hijackEnd = (int) (hijackOffset + diff);

    // Write the original bytes from the dol back to the dol
    byte[] originalBytes = Arrays.copyOfRange(hijackedBytes, hijackOffset, hijackEnd);
    raf.seek(dolStartOffset);
    raf.write(originalBytes);
  }
}
