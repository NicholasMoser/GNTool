package com.github.nicholasmoser;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.gnt4.GNT4ModReady;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

/**
 * Packs FPK files. This includes compressing them with the Eighting PRS algorithm and modding the
 * Start.dol with the audio fix.
 */
public class FPKPacker {

  private static final Logger LOGGER = Logger.getLogger(FPKPacker.class.getName());

  private Path compressedDirectory;

  private Path uncompressedDirectory;

  private Workspace workspace;

  /**
   * Creates a new FPK packer for a workspace.
   * 
   * @param workspace The workspace to pack the FPKs for.
   */
  public FPKPacker(Workspace workspace) {
    this.workspace = workspace;
    this.compressedDirectory = workspace.getRootDirectory();
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
  }

  /**
   * Packs and compresses FPK files. First will prompt the user for an input and output directory.
   * The dol will then be patched with the audio fix. The input directory will be checked for any
   * modifications using the CRC32 hash function. Any files that have been changed will be packed
   * and compressed into their original FPK file. This new FPK file will override the FPK file in
   * the output directory.
   * 
   * @return The path to the packed files.
   * @throws IOException If there is an I/O issue repacking or moving the files.
   */
  public void pack(List<String> changedFiles) throws IOException {
    Set<GNTFile> changedFPKs = new HashSet<GNTFile>();
    Map<String, String> changedNonFPKs = new HashMap<String, String>();
    boolean recreateISOHeader = false;

    for (String changedFile : changedFiles) {
      if (GNT4ModReady.isSplitFiles(changedFile)) {
        recreateISOHeader = true;
      } else {
        String fixedPath = GNT4ModReady.fromModReadyPath(changedFile);
        Optional<GNTFile> parent = workspace.getParentFPK(fixedPath);
        // If there is no parent, it does not belong to an FPK
        if (parent.isPresent()) {
          changedFPKs.add(parent.get());
        } else {
          changedNonFPKs.put(changedFile, fixedPath);
        }
      }
    }

    if (recreateISOHeader) {
      recreateISOHeader();
    }
    for (Entry<String, String> changedNonFPK : changedNonFPKs.entrySet()) {
      Path newFile = uncompressedDirectory.resolve(changedNonFPK.getKey());
      Path oldFile = compressedDirectory.resolve(changedNonFPK.getValue());
      Files.copy(newFile, oldFile, REPLACE_EXISTING);
    }
    LOGGER.info(String.format("The following files were copied: %s",
        changedNonFPKs.isEmpty() ? "None" : changedNonFPKs.values()));

    LOGGER.info(String.format("%d FPK file(s) need to be packed.", changedFPKs.size()));
    for (GNTFile changedFPK : changedFPKs) {
      LOGGER.info(String.format("Packing %s...", changedFPK.getFilePath()));
      repackFPK(changedFPK);
      LOGGER.info(String.format("Packed %s", changedFPK.getFilePath()));
    }
    LOGGER.info("FPK files have been packed at " + compressedDirectory);
  }

  private void recreateISOHeader() throws IOException {
    Path boot = uncompressedDirectory.resolve("sys/boot.bin");
    Path bi2 = uncompressedDirectory.resolve("sys/bi2.bin");
    byte[] bootBytes = Files.readAllBytes(boot);
    byte[] bi2Bytes = Files.readAllBytes(bi2);
    byte[] isoHeaderBytes = Bytes.concat(bootBytes, bi2Bytes);
    Path isoHeader = compressedDirectory.resolve("&&systemdata/ISO.hdr");
    Files.write(isoHeader, isoHeaderBytes);
  }

  /**
   * Repacks the given FPK file. Finds the children of the FPK and individually compresses them from
   * the input directory and packs them into an FPK file at the output directory. If the file
   * already exists in the output directory it will be overridden. The input directory must have the
   * uncompressed child files.
   * 
   * @param fpk The FPK GNTFile.
   * @return The path to the repacked FPK file.
   * @throws IOException If there is an issue reading/writing bytes to the file.
   */
  public Path repackFPK(GNTFile fpk) throws IOException {
    List<GNTChildFile> fpkChildren = fpk.getGntChildFileList();
    List<FPKFile> newFPKs = new ArrayList<FPKFile>(fpkChildren.size());
    for (GNTChildFile child : fpkChildren) {
      String modReadyPath = GNT4ModReady.toModReadyPath(child.getFilePath());
      byte[] input = Files.readAllBytes(uncompressedDirectory.resolve(modReadyPath));
      byte[] output;

      if (child.getCompressed()) {
        PRSCompressor compressor = new PRSCompressor(input);
        output = compressor.compress();
      } else {
        output = input;
      }

      // Set the offset to -1 for now, we cannot figure it out until we have all of
      // the files
      FPKFileHeader header =
          new FPKFileHeader(child.getCompressedPath(), output.length, input.length);
      newFPKs.add(new FPKFile(header, output));
      LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.",
          child.getFilePath(), input.length, output.length));
    }

    int outputSize = 16; // FPK header is 16 bytes so start with that.
    outputSize += newFPKs.size() * 32; // Each FPK file header is 32 bytes
    for (FPKFile file : newFPKs) {
      FPKFileHeader header = file.getHeader();
      header.setOffset(outputSize);
      int compressedSize = header.getCompressedSize();
      int modDifference = compressedSize % 16;
      if (modDifference == 0) {
        outputSize += compressedSize;
      } else {
        // Make sure the offset is divisible by 16
        outputSize += compressedSize + (16 - modDifference);
      }
    }

    // FPK Header
    byte[] fpkBytes = createFPKHeader(newFPKs.size(), outputSize);
    // File headers
    for (FPKFile file : newFPKs) {
      fpkBytes = Bytes.concat(fpkBytes, file.getHeader().getBytes());
    }
    // File Data
    for (FPKFile file : newFPKs) {
      fpkBytes = Bytes.concat(fpkBytes, file.getData());
    }
    Path outputFPK = compressedDirectory.resolve(fpk.getFilePath());
    if (!Files.isDirectory(outputFPK.getParent())) {
      Files.createDirectories(outputFPK.getParent());
    }
    Files.write(outputFPK, fpkBytes);
    return outputFPK;
  }

  /**
   * Returns the header of the FPK file. The first four bytes are zeroes. The next four are the
   * number of files. The next four is the size of this header, which is always 16. The last is the
   * output size of the whole FPK file. The byte array returned will always be 16 bytes exactly.
   * 
   * @param numberOfFiles The number of files being packed.
   * @param outputSize The total size of the FPK file, including this header.
   * @return The FPK header.
   */
  private static byte[] createFPKHeader(int numberOfFiles, int outputSize) {
    return Bytes.concat(ByteUtils.intToBytes(0), ByteUtils.intToBytes(numberOfFiles),
        ByteUtils.intToBytes(16), ByteUtils.intToBytes(outputSize));
  }
}
