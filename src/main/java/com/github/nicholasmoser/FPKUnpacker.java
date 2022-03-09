package com.github.nicholasmoser;

import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.utils.FPKUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class for unpacking fpk files. Fpk files are compressed archives of files used by Eighting that
 * use a proprietary PRS based compression algorithm. This class can be used as an object to unpack
 * an entire directory or to simply unpack a single fpk file. An optional file names object is used
 * for cases where the file name in the fpk is truncated and must be fixed.
 */
public class FPKUnpacker {

  private static final Logger LOGGER = Logger.getLogger(FPKUnpacker.class.getName());

  private final Path inputDir;
  private final Path filesDir;
  private final Path outputDir;
  private final Optional<FileNames> fileNames;
  private final boolean longPaths;
  private final boolean bigEndian;

  /**
   * Create a new FPKUnpacker object to unpack an entire directory.
   *
   * @param inputDir  The input directory to unpack.
   * @param outputDir The output directory to unpack to.
   * @param fileNames The optional list of file names to fix for the unpacking.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  public FPKUnpacker(Path inputDir, Path outputDir, Optional<FileNames> fileNames,
      boolean longPaths, boolean bigEndian) {
    if (!Files.isDirectory(inputDir)) {
      throw new IllegalArgumentException(inputDir + " is not a directory.");
    }
    this.inputDir = inputDir;
    this.filesDir = outputDir.resolve("files");
    this.outputDir = outputDir;
    this.fileNames = fileNames;
    this.longPaths = longPaths;
    this.bigEndian = bigEndian;
  }

  /**
   * Copy files from the input directory to the output directory, unpacking all FPK files in the
   * process. The contents will be stored in the "files" directory in the output directory.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public void unpackDirectory() throws IOException {
    LOGGER.info("Unpacking FPKs...");
    extractDirectory(inputDir);
    LOGGER.info("Finished unpacking FPKs.");
  }

  /**
   * Copy files from the given directory to the output directory, unpacking all FPK files in the
   * process. This process will run recursively for each subdirectory.
   *
   * @param directory The directory to search and extract from.
   * @throws IOException If there is an I/O related exception.
   */
  private void extractDirectory(Path directory) throws IOException {
    try (Stream<Path> paths = Files.list(directory)) {
      paths.forEach(path -> {
            try {
              if (Files.isDirectory(path)) {
                extractDirectory(path);
              } else if (path.toString().toLowerCase().endsWith(".fpk")) {
                extractFPK(path, filesDir, fileNames, longPaths, bigEndian);
              } else {
                Path relativePath = inputDir.relativize(path);
                Path outputPath = outputDir.resolve(relativePath);
                Files.createDirectories(outputPath.getParent());
                Files.copy(path, outputPath);
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
      );
    }
  }


  /**
   * Extracts the given Wii or GameCube fpk file to the given output directory path.
   *
   * @param fpkPath         The path to the fpk file.
   * @param outputDirectory The path to the output directory.
   * @param fileNames       Optional full file names to fix truncation.
   * @param longPaths       If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian       If the FPK is big-endian (instead of little-endian).
   * @throws IOException If there is an I/O related exception.
   */
  public static void extractFPK(Path fpkPath, Path outputDirectory, Optional<FileNames> fileNames,
      boolean longPaths, boolean bigEndian) throws IOException {
    int bytesRead = 0;
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is, bigEndian);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is, longPaths, bigEndian));
        bytesRead += longPaths ? 48 : 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        String fileName = header.getFileName();
        if (fileNames.isPresent()) {
          fileName = fileNames.get().fix(fileName);
        }
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
        Path outputFilePath = outputDirectory.resolve(fileName);
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
