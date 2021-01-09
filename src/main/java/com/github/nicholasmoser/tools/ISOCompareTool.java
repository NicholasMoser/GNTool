package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.utils.GUIUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class ISOCompareTool {

  private static final Logger LOGGER = Logger.getLogger(ISOCompareTool.class.getName());

  /**
   * Compares twp GameCube ISOs.
   */
  public static void compareGameCubeISO() {
    Optional<Path> isoOptional = Choosers.getInputISO(GNTool.USER_HOME);
    if (isoOptional.isEmpty()) {
      return;
    }
    Path iso1 = isoOptional.get();
    Optional<Path> isoOptional2 = Choosers.getInputISO(iso1.getParent().toFile());
    if (isoOptional2.isEmpty()) {
      return;
    }
    compare(iso1, isoOptional2.get());
  }

  /**
   * Extract the two ISOs to the temp directory asynchronously. Then compare them file by file.
   * Includes a loading screen.
   *
   * @param iso1 The first ISO to compare.
   * @param iso2 The second ISO to compare.
   */
  private static void compare(Path iso1, Path iso2) {

    Task<String> task = new Task<>() {
      @Override
      public String call() throws Exception {
        Path temp1 = null;
        Path temp2 = null;
        String name1 = iso1.getFileName().toString();
        String name2 = iso2.getFileName().toString();
        if (name1.equals(name2)) {
          // If they have the same name, just use the full path
          name1 = iso1.toString();
          name2 = iso2.toString();
        }
        try {
          updateMessage("Extracting the first ISO...");
          temp1 = Files.createTempDirectory("ISO1");
          GameCubeISO.exportFiles(iso1, temp1);
          LOGGER.info("Extracted the first ISO to " + temp1);
          updateMessage("Extracting the second ISO...");
          temp2 = Files.createTempDirectory("ISO2");
          GameCubeISO.exportFiles(iso2, temp2);
          LOGGER.info("Extracted the second ISO to " + temp2);
          updateMessage("Comparing the ISOs...");
          String comparisonMessage = getDifference(temp1, temp2, name1, name2);
          updateMessage("Complete");
          updateProgress(1, 1);
          return comparisonMessage;
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Comparing ISOs", e);
          throw e;
        } finally {
          if (temp1 != null && Files.exists(temp1)) {
            MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
          }
          if (temp2 != null && Files.exists(temp2)) {
            MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
          }
        }
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Comparing ISOs", task);

    task.setOnSucceeded(event -> {
      String message = task.getValue();
      long newlines = message.chars().filter(ch -> ch == '\n').count();
      if (newlines > 20) {
        message = "Report too long, please save it to a file.";
      }
      Message.info("Compare Successful", message);
      askUserToSave(task.getValue());
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Compare ISOs", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Asks the user to specify an output TXT file. If they chose to do so, saves the given message
   * parameter to that output file.
   *
   * @param message The message to save.
   */
  private static void askUserToSave(String message) {
    Optional<Path> output = Choosers.getOutputTXT(GNTool.USER_HOME);
    if (output.isPresent()) {
      try(OutputStream os = Files.newOutputStream(output.get())) {
        os.write(message.getBytes(StandardCharsets.UTF_8));
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Saving Comparison", e);
      }
    }
  }

  /**
   * Gets the difference between two directories as a String. The String will be newline separated
   * and categorized by "Files only in (name1)", "Files only in (name2)", and "Changed Files". The
   * name1 and name2 in the categories is provided by the name1 and name2 parameters. It uses these
   * parameters instead of the directory name since the directory may be using the temp directory
   * which has a unique name (one that may not be desirable for display).
   *
   * @param dir1  The first directory to compare.
   * @param dir2  The second directory to compare.
   * @param name1 The name of the first directory.
   * @param name2 The name of the second directory.
   * @return The difference message.
   * @throws IOException If an I/O error occurs
   */
  public static String getDifference(Path dir1, Path dir2, String name1, String name2)
      throws IOException {
    String message = findMissingFiles(dir1, dir2, name1);
    message += findMissingFiles(dir2, dir1, name2);
    message += findChangedFiles(dir1, dir2);
    return message;
  }

  /**
   * Return the files in dir1 that are not in dir2. Each file will be newline separated with a
   * "Files only in (baseName)" header. The baseName in the header is determined by the baseName
   * parameter passed into this method. It uses this parameter instead of the directory name since
   * the directory may be using the temp directory, which has a unique name (one that may not be
   * desirable for display).
   *
   * @param baseDir       The directory to use the files from.
   * @param comparisonDir The directory to see if the files exist in.
   * @param baseName      The name of the baseDir base directory.
   * @return The find missing files message.
   * @throws IOException If an I/O error occurs
   */
  private static String findMissingFiles(Path baseDir, Path comparisonDir, String baseName)
      throws IOException {
    StringBuilder message = new StringBuilder("\nFiles only in ");
    message.append(baseName);
    int separatorLen = 14 + baseName.length();
    message.append('\n');
    message.append("-".repeat(separatorLen));
    message.append('\n');
    Files.walkFileTree(baseDir, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult visitFile(Path file,
          BasicFileAttributes attrs)
          throws IOException {
        FileVisitResult result = super.visitFile(file, attrs);
        Path relativize = baseDir.relativize(file);
        Path fileInOther = comparisonDir.resolve(relativize);
        if (!Files.isRegularFile(fileInOther)) {
          message.append(relativize);
          message.append('\n');
        }
        return result;
      }
    });
    return message.toString();
  }

  /**
   * Return the files that have been modified between dir1 and dir2. It will not include any files
   * that only exist in one of the two directories. If even a single byte is different it will be
   * considered changed.
   *
   * @param dir1 The first directory to compare.
   * @param dir2 The second directory to compare.
   * @return The find changed files message.
   * @throws IOException If an I/O error occurs
   */
  private static String findChangedFiles(Path dir1, Path dir2) throws IOException {
    StringBuilder message = new StringBuilder("\nChanged Files\n");
    message.append("-------------\n");
    Files.walkFileTree(dir1, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult visitFile(Path file,
          BasicFileAttributes attrs)
          throws IOException {
        FileVisitResult result = super.visitFile(file, attrs);
        Path relativize = dir1.relativize(file);
        Path fileInOther = dir2.resolve(relativize);
        if (Files.isRegularFile(fileInOther)) {
          byte[] theseBytes = Files.readAllBytes(file);
          byte[] otherBytes = Files.readAllBytes(fileInOther);
          if (!Arrays.equals(otherBytes, theseBytes)) {
            message.append(relativize);
            message.append('\n');
          }
        }
        return result;
      }
    });
    return message.toString();
  }
}
