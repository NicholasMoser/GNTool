package com.github.nicholasmoser;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.Verify;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;

/**
 * Packs FPK files. This includes compressing them with the Eighting PRS algorithm and modding the
 * Start.dol with the audio fix.
 */
public class FPKPacker {

  private static final Logger LOGGER = Logger.getLogger(FPKPacker.class.getName());

  // Metadata describing the files of GNT4.
  private GNT4Files gnt4Files;
  
  // The root containing unpacked files. e.g. C:/workspace/root
  private Path compressedDirectory;
  
  // The folder container unpacked files. e.g. C:/workspace/uncompressed
  private Path uncompressedDirectory;
  
  public FPKPacker(Path uncompressedDirectory, Path compressedDirectory) {
    this.compressedDirectory = compressedDirectory;
    this.uncompressedDirectory = uncompressedDirectory; 
  }

  /**
   * Packs and compresses FPK files. First will prompt the user for an input and output directory.
   * The dol will then be patched with the audio fix. The input directory will be checked for any
   * modifications using the CRC32 hash function. Any files that have been changed will be packed
   * and compressed into their original FPK file. This new FPK file will override the FPK file in
   * the output directory.
   * @return The path to the packed files.
   * @throws IOException If there is an I/O issue repacking or moving the files.
   */
  public void pack(List<String> changedFiles) throws IOException {
    Set<String> changedFPKs = new HashSet<String>();
    Set<String> changedNonFPKs = new HashSet<String>();
    for (String changedFile : changedFiles) {
      Optional<String> parent = gnt4Files.getParentFPK(changedFile);
      // If there is no parent, it does not belong to an FPK
      if (parent.isEmpty()) {
        changedNonFPKs.add(changedFile);
      } else {
        changedFPKs.add(parent.get());
      }
    }

    LOGGER.info(String.format("The follow files FPKs need to be packed: %s",
        changedFPKs.isEmpty() ? "None" : changedFPKs));
    for (String changedNonFPK : changedNonFPKs) {
     copyFile(changedNonFPK);
    }
    LOGGER.info(String.format("The following files were copied: %s",
        changedNonFPKs.isEmpty() ? "None" : changedNonFPKs));

    for (String changedFPK : changedFPKs) {
      LOGGER.info(String.format("Packing %s...", changedFPK));
      repackFPK(changedFPK);
      LOGGER.info(String.format("Packed %s", changedFPK));
    }
    String message = String.format("FPK files have been packed at %s.", compressedDirectory);
    LOGGER.info(message);
    Message.info("FPK Files Packed", message);
  }

  /**
   * Simply copies and overwrites a single file from one directory to another. This should be used
   * for files not associated with an FPK in any way.
   * 
   * @param changedNonFPK The changed non-FPK asssociated file.
   * @throws IOException If there is an issue replacing the file.
   */
  private void copyFile(String changedNonFPK) throws IOException {
    Path input = uncompressedDirectory.resolve(changedNonFPK);
    Path output = compressedDirectory.resolve(changedNonFPK);
    Files.copy(input, output, REPLACE_EXISTING);
  }

  /**
   * Repacks the given FPK file. Finds the children of the FPK and individually compresses them from
   * the input directory and packs them into an FPK file at the output directory. If the file
   * already exists in the output directory it will be overridden. The input directory must have the
   * uncompressed child files.
   * 
   * @param fpk The name of the FPK file.
   * @return The path to the repacked FPK file.
   * @throws IOException If there is an issue reading/writing bytes to the file.
   */
  public Path repackFPK(String fpk) throws IOException {
    List<String> fpkChildren = gnt4Files.getFPKChildren(fpk);
    List<FPKFile> newFPKs = new ArrayList<FPKFile>(fpkChildren.size());
    for (String child : fpkChildren) {
      String childName = gnt4Files.getId(child);
      byte[] input = Files.readAllBytes(uncompressedDirectory.resolve(child));
      byte[] output;

      if (gnt4Files.isChildCompressed(childName)) {
        PRSCompressor compressor = new PRSCompressor(input);
        output = compressor.compress();
      } else {
        output = input;
      }

      // Set the offset to -1 for now, we cannot figure it out until we have all of
      // the files
      FPKFileHeader header = new FPKFileHeader(childName, output.length, input.length);
      newFPKs.add(new FPKFile(header, output));
      LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.", child,
          input.length, output.length));
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
    Path outputFPK = compressedDirectory.resolve(fpk);
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

  /**
   * Gets the CRC32 hash values from files in a given directory. This function works recursively. It
   * will map the file name to the CRC32 value in the given map parameter.
   * 
   * @param directory The directory to search for files.
   * @param fileCRC32Values The map between file names to CRC32 values.
   * @return The map between file names to CRC32 values.
   */
  private static Map<String, String> getCRC32Values(File directory,
      Map<String, String> fileCRC32Values) {
    Verify.verifyNotNull(directory);
    Verify.verifyNotNull(fileCRC32Values);

    File[] files = directory.listFiles();
    HashFunction crc32 = Hashing.crc32();
    for (File fileEntry : files) {
      if (fileEntry.isDirectory()) {
        getCRC32Values(fileEntry, fileCRC32Values);
      } else {
        try {
          String[] fileParts = fileEntry.getAbsolutePath().split("root\\\\");
          String fileKey = fileParts[fileParts.length - 1];
          HashCode hashValue = crc32.hashBytes(Files.readAllBytes(fileEntry.toPath()));
          fileCRC32Values.put(fileKey, hashValue.toString());
        } catch (IOException e) {
          LOGGER.log(Level.SEVERE, e.toString(), e);
        }
      }
    }
    return fileCRC32Values;
  }
}
