package com.github.nicholasmoser;

import com.github.nicholasmoser.gnt4.GNT4ModReady;
import com.github.nicholasmoser.utils.FPKUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Unpacks FPK files. This includes uncompressing them with the Eighting PRS algorithm.
 */
public class FPKUnpacker {

  private static final Logger LOGGER = Logger.getLogger(FPKUnpacker.class.getName());

  private Path inputDirectory;

  public FPKUnpacker(Path inputDirectory) {
    this.inputDirectory = inputDirectory;
  }

  /**
   * Unpacks and uncompresses FPK files. First will prompt the user for an input and output
   * directory. The input directory will be copied to the output directory and then each FPK file
   * will have the contained files inside of it unpacked to their relative directories. This will
   * uncompress the files from their Eighting PRS compressed format.
   *
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  public void unpack() throws IOException {
    LOGGER.info("Unpacking FPKs...");
    extractDirectory(inputDirectory.toFile());
    MoreFiles.deleteRecursively(inputDirectory.resolve("files/fpack"), RecursiveDeleteOption.ALLOW_INSECURE);
    LOGGER.info("Finished unpacking FPKs.");
  }

  /**
   * A recursive method to extract and uncompress the files inside an FPK from a given directory.
   * This method will call itself recursively for each directory it encounters.
   *
   * @param directory The directory to search and extract from.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractDirectory(File directory) throws IOException {
    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }
    for (File fileEntry : files) {
      if (fileEntry.isDirectory()) {
        extractDirectory(fileEntry);
      } else {
        String fileName = fileEntry.getName();
        if (fileName.endsWith(".fpk")) {
          extractFPK(Paths.get(directory.getAbsolutePath()).resolve(fileName));
        }
      }
    }
  }

  /**
   * Opens the given FPK file and extracts it contents. This includes uncompressing them from
   * Eighting PRS compression.
   *
   * @param filePath The FPK file to extract.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractFPK(Path filePath) throws IOException {
    int bytesRead = 0;
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        String fileName = GNT4ModReady.fixBrokenFileName(header.getFileName());
        int offset = header.getOffset();
        int compressedSize = header.getCompressedSize();
        int uncompressedSize = header.getUncompressedSize();

        // Skip to the next offset if we are not already there
        if (bytesRead < offset) {
          int bytesToMove = offset - bytesRead;
          if (is.skip(bytesToMove) != bytesToMove) {
            String errorMessage = String.format("Failed to skip to binary data of %s", fileName);
            throw new IOException(errorMessage);
          }
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        if (is.read(fileBytes) != compressedSize) {
          String errorMessage = String.format("Failed to read all binary data of %s", fileName);
          throw new IOException(errorMessage);
        }
        bytesRead += compressedSize;

        // Create directories from fileName and get output directory
        Path filesPath = inputDirectory.resolve("files");
        Path outputFilePath = filesPath.resolve(fileName);
        Files.createDirectories(outputFilePath.getParent());

        // Files with the same compressed and uncompressed size are not compressed
        if (compressedSize == uncompressedSize) {
          Files.write(outputFilePath, fileBytes);
        } else {
          PRSUncompressor uncompressor = new PRSUncompressor(fileBytes, uncompressedSize);
          byte[] output = uncompressor.uncompress();
          Files.write(outputFilePath, output);
        }
      }
    }
  }
}
