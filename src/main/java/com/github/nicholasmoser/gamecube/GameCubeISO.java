package com.github.nicholasmoser.gamecube;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.Game;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4Files;

/**
 * Utility to access GCRebuilder.exe through the command line.
 */
public class GameCubeISO {

  private static final Logger LOGGER = Logger.getLogger(GameCubeISO.class.getName());

  private static final Path currentPath = Paths.get(System.getProperty("user.dir"));

  private static final Path gcrPath = currentPath.resolve("gcr.exe");

  /**
   * Retrieves the Game ID of a given ISO file. This String is simply the first six bytes of the
   * file.
   * 
   * @param iso The iso to retrieve the Game ID from.
   * @return The Game ID.
   * @throws IOException If an I/O error occurs.
   */
  public static String getGameId(File iso) throws IOException {
    try (InputStream is = Files.newInputStream(iso.toPath())) {
      byte[] bytes = new byte[6];
      if (is.read(bytes) != 6) {
        throw new IOException("Unable to read the Game ID from the ISO file.");
      }
      return new String(bytes, StandardCharsets.US_ASCII);
    }
  }

  /**
   * Checks a GameCube ISO workspace in that it has an ISO.hdr file and that the game ID
   * matches the expected game ID.
   * 
   * @param directory The directory to check.
   * @param game The expected game.
   * @throws IOException If the workspace is not valid.
   */
  public static void checkWorkspace(File directory, Game game) throws IOException {
    File uncompressedDirectory = new File(directory, GNT4Files.UNCOMPRESSED_DIRECTORY);
    if (!uncompressedDirectory.isDirectory()) {
      throw new IOException("uncompressed directory does not exist.");
    }
    File systemData = new File(uncompressedDirectory, "sys");
    if (!systemData.isDirectory()) {
      throw new IOException("uncompressed/sys folder does not exist.");
    }
    File isoHeader = new File(systemData, "ISO.hdr");
    if (!isoHeader.isFile()) {
      throw new IOException("uncompressed/sys/ISO.hdr file does not exist.");
    }
    String gameId;
    try {
      gameId = getGameId(isoHeader);
    } catch (IOException e) {
      throw new IOException("Unable to read uncompressed/sys/ISO.hdr", e);
    }
    String expectedGameId = game.getGameId();
    if (!expectedGameId.equals(gameId)) {
      String message = String.format("uncompressed/sys/ISO.hdr has game ID %s but should have %s",
          gameId, expectedGameId);
      throw new IOException(message);
    }
  }

  /**
   * Prompt the user for an input ISO and output directory. The files contained in the ISO will be
   * stored in a folder named root at the given output directory. This will be accomplished by using
   * GameCube Rebuilder (gcr.exe) which should be located in the same directory as the jar. This
   * will only work on Windows, and will return without effect if it is not.
   * 
   * @throws IOException
   */
  public static void exportFiles(File inputFile, File outputDirectory) throws IOException {
    if (!canRunISOTools()) {
      Message.error("Unable to Run ISO Tools",
          "Please verify that you are running on Windows and have access to GameCube Rebuilder.");
      throw new IllegalStateException(
          "GameCube Rebuilder is not in the directory of this executable.");
    }
    if (inputFile == null || !inputFile.isFile()) {
      throw new IllegalArgumentException(inputFile + " is null or not a file.");
    }
    if (outputDirectory == null || !outputDirectory.isDirectory()) {
      throw new IllegalArgumentException(outputDirectory + " is null or not a directory.");
    }
    LOGGER.info("Exporting files...");
    runISOTools(inputFile.getAbsolutePath(), outputDirectory.getAbsolutePath(), true);
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
  public static void importFiles(File inputDirectory, File outputFile) throws IOException {
    if (!canRunISOTools()) {
      Message.error("Unable to Run ISO Tools",
          "Please verify that you are running on Windows and have access to GameCube Rebuilder.");
      throw new IllegalStateException(
          "GameCube Rebuilder is not in the directory of this executable.");
    }
    LOGGER.info("Importing files...");
    runISOTools(inputDirectory.getAbsolutePath(), outputFile.getAbsolutePath(), false);
    LOGGER.info("Finished importing files.");
  }

  /**
   * Main logic for ISO tools.
   * 
   * @param input For export mode this will be the path to an ISO file, for import mode it will be
   *        the path to a directory.
   * @param output For export mode this will be the path to a directory, for import mode it will be
   *        the path to an ISO file.
   * @param exportMode The output, be it ISO file or directory. If you are in ISO export mode
   *        (compared to import mode).
   * @throws IOException If there is an issue with GameCube Rebuilder.
   */
  private static void runISOTools(String input, String output, boolean exportMode)
      throws IOException {
    LOGGER.info(String.format("Input: %s; Output: %s", input, output));
    try {
      Process process = null;
      if (exportMode) {
        process = new ProcessBuilder(gcrPath.toString(), input, GNT4Files.ROOT_DIRECTORY, "e", output).start();
      } else {
        process = new ProcessBuilder(gcrPath.toString(), input, output).start();
      }
      process.waitFor();

    } catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Issue with GameCube Rebuilder", "There was an issue with running gcr.exe");
      throw new IOException(e);
    }
  }

  /**
   * @return If the running system is Windows and has access to GameCube Rebuilder (gcr.exe).
   */
  private static boolean canRunISOTools() {
    LOGGER.info("GameCube Rebuilder Path: " + gcrPath);
    if (!isWindows()) {
      LOGGER.info("Running OS is not Windows and therefore cannot run ISO Tools.");
      Message.error("Windows Required for ISO Tools",
          "ISO Tools require running Windows due to it using GameCube Rebuilder (gcr.exe).");
      return false;
    }
    if (!canRunGCR()) {
      String message =
          "ISO Tools cannot find the GameCube Rebuilder executable. Make sure its filename is gcr.exe and is in the same folder as this jar.";
      LOGGER.info(message);
      Message.error("GameCube Rebuilder Required for ISO Tools", message);
      return false;
    }
    return true;
  }

  /**
   * @return Whether or not the operating system is Windows.
   */
  private static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows");
  }

  /**
   * Tests whether the GameCube Rebuilder executable exists in the same directory as the jar and
   * that it is indeed an executable. It will be called gcr.exe.
   * 
   * @return whether this jar can run gcr.exe
   */
  private static boolean canRunGCR() {
    return Files.isExecutable(gcrPath);
  }
}
