package com.github.nicholasmoser.gamecube;

import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOExtractor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.Game;
import com.github.nicholasmoser.gnt4.GNT4Files;

/**
 * Utility to access GCRebuilder.exe through the command line.
 */
public class GameCubeISO {

  private static final Logger LOGGER = Logger.getLogger(GameCubeISO.class.getName());

  private static final Path currentPath = Paths.get(System.getProperty("user.dir"));

  /**
   * Retrieves the Game ID of a given ISO file. This String is simply the first six bytes of the
   * file.
   *
   * @param iso The iso to retrieve the Game ID from.
   * @return The Game ID.
   * @throws IOException If an I/O error occurs.
   */
  public static String getGameId(Path iso) throws IOException {
    try (InputStream is = Files.newInputStream(iso)) {
      byte[] bytes = new byte[6];
      if (is.read(bytes) != 6) {
        throw new IOException("Unable to read the Game ID from the ISO file.");
      }
      return new String(bytes, StandardCharsets.US_ASCII);
    }
  }

  /**
   * Checks a GameCube ISO workspace in that it has an ISO.hdr file and that the game ID matches the
   * expected game ID.
   *
   * @param directory The directory to check.
   * @param game      The expected game.
   * @throws IOException If the workspace is not valid.
   */
  public static void checkWorkspace(Path directory, Game game) throws IOException {
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
    String gameId;
    try {
      gameId = getGameId(bootBin);
    } catch (IOException e) {
      throw new IOException("Unable to read uncompressed/sys/boot.bin", e);
    }
    String expectedGameId = game.getGameId();
    if (!expectedGameId.equals(gameId)) {
      String message = String.format("uncompressed/sys/boot.bin has game ID %s but should have %s",
          gameId, expectedGameId);
      throw new IOException(message);
    }
  }

  /**
   * Prompt the user for an input ISO and output directory. The files contained in the ISO will be
   * stored in a folder named compressed at the given output directory. This will be accomplished by
   * using GameCube Rebuilder (gcr.exe) which should be located in the same directory as the jar.
   * This will only work on Windows, and will return without effect if it is not.
   *
   * @throws IOException If there is an issue with GameCube Rebuilder.
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
   * Prompt the user for an output ISO and input directory. The files contained in the directory
   * will be imported into the given ISO. This will be accomplished by using GameCube Rebuilder
   * (gcr.exe) which should be located in the same directory as the jar. This will only work on
   * Windows, and will return without effect if it is not.
   *
   * @throws IOException If there is an issue with GameCube Rebuilder.
   */
  public static void importFiles(Path inputDirectory, Path outputFile) throws IOException {
    LOGGER.info("Importing files...");
    ISOCreator creator = new ISOCreator(inputDirectory, outputFile);
    creator.create();
    LOGGER.info("Finished importing files.");
  }
}
