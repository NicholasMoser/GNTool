package com.github.nicholasmoser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import com.github.nicholasmoser.gnt4.GNT4ModReady;
import com.github.nicholasmoser.utils.FPKUtils;

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
    FileUtils.deleteDirectory(inputDirectory.resolve("fpack").toFile());
    GNT4ModReady adjustor = new GNT4ModReady(inputDirectory);
    adjustor.prepare();
    LOGGER.info("Finished unpacking FPKs.");
  }
  
  /**
   * A recursive method to extract and uncompress the files inside an FPK from a given directory.
   * This method will call itself recursively for each directory it encounters.
   * @param directory The directory to search and extract from.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractDirectory(File directory) throws IOException {
    for (File fileEntry : directory.listFiles()) {
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
   * @param filePath The FPK file to extract.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractFPK(Path filePath) throws IOException {
    int bytesRead = 0;
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<FPKFileHeader>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        String fileName = fixBrokenFileName(header.getFileName());
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
  
  /**
   * Fixes and returns filenames for compressed files that are cut off.
   * When compressed into an FPK, certain files will have their paths cut off.
   * One such example can be found in seq0000.fpk which has a compressed file with
   * the name hr/ank/0000.seq that is not correct. The first directory should be
   * chr, not hr. Therefore this method returns the filename with this fixed.
   * @param fileName The file name to fix.
   * @return The fixed file name or original if no fix is required.
   */
  private String fixBrokenFileName(String fileName) {
    if (fileName.startsWith("aki/")) {
      fileName = fileName.replace("aki/", "maki/");
    } else if (fileName.startsWith("ame/")) {
      fileName = fileName.replace("ame/", "game/");
    } else if (fileName.startsWith("g/")) {
      fileName = fileName.replace("g/", "stg/");
    } else if (fileName.startsWith("hr/")) {
      fileName = fileName.replace("hr/", "chr/");
    } else if (fileName.startsWith("ki/")) {
      fileName = fileName.replace("ki/", "maki/");
    } else if (fileName.startsWith("me/")) {
      fileName = fileName.replace("me/", "game/");
    } else if (fileName.startsWith("ru/")) {
      fileName = fileName.replace("ru/", "furu/");
    } else if (fileName.startsWith("te/")) {
      fileName = fileName.replace("te/", "unite/");
    } else if (fileName.startsWith("tg/")) {
      fileName = fileName.replace("tg/", "stg/");
    } else if (fileName.startsWith("uro/")) {
      fileName = fileName.replace("uro/", "kuro/");
    }
    return fileName;
  }
}
