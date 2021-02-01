package com.github.nicholasmoser.gamecube;

import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOExtractor;
import com.github.nicholasmoser.utils.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Utility to access GCRebuilder.exe through the command line.
 */
public class GameCubeISO {

  private static final Logger LOGGER = Logger.getLogger(GameCubeISO.class.getName());

  /**
   * Returns whether or not this workspace is a GNTool 2.x workspace. This is determined by the
   * existence of a folder named root.
   *
   * @param directory The workspace directory to check.
   * @return If it is a GNTool 2.x workspace.
   */
  public static boolean isOldGNToolWorkspace(Path directory) {
    return Files.isDirectory(directory.resolve("root"));
  }

  /**
   * Checks that a GameCube ISO workspace has a boot.bin file in the right location.
   *
   * @param directory The directory to check.
   * @throws IOException If the workspace is not valid.
   */
  public static void checkWorkspace(Path directory) throws IOException {
    Path uncompressedDirectory = directory.resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
    if (!Files.isDirectory(uncompressedDirectory)) {
      throw new IOException("uncompressed directory does not exist.");
    }
    Path systemData = uncompressedDirectory.resolve("sys");
    if (!Files.isDirectory(systemData)) {
      throw new IOException("uncompressed/sys folder does not exist.");
    }
    Path bootBin = systemData.resolve("boot.bin");
    if (!Files.isRegularFile(bootBin)) {
      throw new IOException("uncompressed/sys/boot.bin file does not exist.");
    }
  }

  /**
   * Export the files contained in the given ISO to the temp directory if it exists.
   *
   * @param inputISO The given input ISO.
   * @return The temp directory this ISO was exported to.
   * @throws IOException If an I/O error occurs
   */
  public static Path exportFiles(Path inputISO) throws IOException {
    Path tempDir = FileUtils.getTempDirectory();
    Path exportDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(exportDir);
    exportFiles(inputISO, exportDir);
    return exportDir;
  }

  /**
   * Export the files contained in the given ISO to the given output directory.
   *
   * @param inputFile The given input ISO.
   * @param outputDirectory The given output directory to export to.
   * @throws IOException If an I/O error occurs
   */
  public static void exportFiles(Path inputFile, Path outputDirectory) throws IOException {
    if (inputFile == null || !Files.isRegularFile(inputFile)) {
      throw new IllegalArgumentException(inputFile + " is null or not a file.");
    }
    if (outputDirectory == null || !Files.isDirectory(outputDirectory)) {
      throw new IllegalArgumentException(outputDirectory + " is null or not a directory.");
    }
    LOGGER.info("Exporting files...");
    ISOExtractor extractor = new ISOExtractor(inputFile, outputDirectory);
    extractor.extract();
    LOGGER.info("Finished exporting files.");
  }

  /**
   * Import the files contained in the given directory to the given output ISO file.
   *
   * @param inputDirectory The directory to import files from.
   * @param outputISO The output ISO to import files into.
   * @param pushFilesToEnd If the files should be pushed to the end of the ISO.
   * @throws IOException If an I/O error occurs
   */
  public static void importFiles(Path inputDirectory, Path outputISO, boolean pushFilesToEnd) throws IOException {
    LOGGER.info("Importing files...");
    ISOCreator creator = new ISOCreator(inputDirectory, outputISO);
    creator.create(pushFilesToEnd);
    LOGGER.info("Finished importing files.");
  }
}
