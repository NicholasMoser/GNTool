package com.github.nicholasmoser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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

  /**
   * Unpacks and uncompresses FPK files. First will prompt the user for an input and output
   * directory. The input directory will be copied to the output directory and then each FPK file
   * will have the contained files inside of it unpacked to their relative directories. This will
   * uncompress the files from their Eighting PRS compressed format.
   * 
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  public static void unpack(File inputDirectory) throws IOException {
    LOGGER.info("Unpacking FPKs...");
    extractDirectory(inputDirectory);
    LOGGER.info("Finished unpacking FPKs.");
  }

  /**
   * A recursive method to extract and uncompress the files inside an FPK from a given directory.
   * This method will call itself recursively for each directory it encounters.
   * 
   * @param directory The directory to search and extract from.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private static void extractDirectory(File directory) throws IOException {
    for (File fileEntry : directory.listFiles()) {
      if (fileEntry.isDirectory()) {
        extractDirectory(fileEntry);
      } else {
        String fileName = fileEntry.getName();
        if (fileName.endsWith(".fpk")) {
          extractFPK(Paths.get(directory.getAbsolutePath()).resolve(fileName));
          fileEntry.delete();
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
  public static void extractFPK(Path filePath) throws IOException {
    int bytesRead = 0;
    String fileName = "Unknown"; // Default for error message
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<FPKFileHeader>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(readFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        fileName = header.getFileName();
        int offset = header.getOffset();
        int compressedSize = header.getCompressedSize();
        int uncompressedSize = header.getUncompressedSize();

        // Skip to the next offset if we are not already there
        if (bytesRead < offset) {
          int bytesToMove = offset - bytesRead;
          is.skip(bytesToMove);
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        is.read(fileBytes);
        bytesRead += compressedSize;

        // Create directories from fileName and get output directory
        Path inputDirectory = filePath.getParent();
        Path outputFilePath = inputDirectory.resolve(fileName);
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

  /**
   * Reads the header of the FPK file itself. This will return only the number of files from the
   * header. Make sure to only call this method on a newly opened FPK file, since the header is
   * first in the file. This method will always read exactly 16 bytes.
   * 
   * @param is The input stream to read it from.
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  protected static int readFPKHeader(InputStream is) throws IOException {
    byte[] fileCountWord = new byte[4];
    is.skip(4);
    is.read(fileCountWord);
    is.skip(8);
    return ByteBuffer.wrap(fileCountWord).getInt();
  }

  /**
   * Reads an individual file header from the FPK file. This will return the relevant FPKFileHeader
   * object. Make sure to only call this method once you have already read the FPK header (first 16
   * bytes of the file). You will want to call this equivalent to the number of files contained in
   * the FPK file. This method will always read exactly 32 bytes.
   * 
   * @param is The input stream to read it from.
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  protected static FPKFileHeader readFPKFileHeader(InputStream is) throws IOException {
    byte[] fileNameWord = new byte[16];
    byte[] offsetWord = new byte[4];
    byte[] compressedSizeWord = new byte[4];
    byte[] uncompressedSizeWord = new byte[4];
    is.read(fileNameWord);
    is.skip(4);
    is.read(offsetWord);
    is.read(compressedSizeWord);
    is.read(uncompressedSizeWord);
    String fileName = new String(fileNameWord, Charset.forName("Windows-1252")).trim();
    int offset = ByteBuffer.wrap(offsetWord).getInt();
    int compressedSize = ByteBuffer.wrap(compressedSizeWord).getInt();
    int uncompressedSize = ByteBuffer.wrap(uncompressedSizeWord).getInt();
    return new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize);
  }
}
