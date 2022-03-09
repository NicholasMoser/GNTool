package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.mot.AnimationList;
import com.github.nicholasmoser.mot.Motion;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * A tool to launch the MOTRepacker.
 */
public class MOTRepackerTool {

  private static final Logger LOGGER = Logger.getLogger(MOTUnpackerTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  public static Optional<Path> run(File startingDirectory) {
    currentDirectory = startingDirectory;
    return run();
  }

  public static Optional<Path> run() {
    boolean invalid = true;
    Path dir = null;
    while (invalid) {
      Optional<Path> inputPath = Choosers.getInputDirectory(currentDirectory);
      if (inputPath.isEmpty()) {
        return Optional.empty();
      }
      dir = inputPath.get();
      if (!AnimationList.isValidDirectory(dir)) {
        Message.error("Invalid Directory", "Missing file: " + AnimationList.NAME);
      } else {
        invalid = false;
      }
    }
    Optional<Path> outputPath = Choosers.getOutputMot(dir.toFile());
    if (outputPath.isEmpty()) {
      return Optional.empty();
    }
    currentDirectory = dir.toFile();
    repack(dir, outputPath.get());
    return outputPath;
  }

  public static void repack(Path inputDir, Path mot) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        try {
          updateMessage("Repacking MOT file...");
          Motion motion = Motion.parseFromDirectory(inputDir);
          motion.pack(mot);
        } catch (Exception e) {
          updateMessage("Failed");
          updateProgress(1, 1);
          LOGGER.log(Level.SEVERE, "Error", e);
          throw new RuntimeException(e);
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Repacking MOT File", task);
    task.setOnSucceeded(event -> loadingWindow.close());
    task.setOnFailed(event -> {
      Message.error("Failed to Repack MOT File", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }
}
