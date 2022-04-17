package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.mot.Motion;
import com.github.nicholasmoser.utils.GUIUtils;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * A tool to launch the MOTUnpacker.
 */
public class MOTUnpackerTool {

  private static final Logger LOGGER = Logger.getLogger(MOTUnpackerTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  public static void run(File startingDirectory) {
    currentDirectory = startingDirectory;
    run();
  }

  public static void run() {
    Optional<Path> inputPath = Choosers.getInputMot(currentDirectory);
    if (inputPath.isEmpty()) {
      return;
    }
    Path mot = inputPath.get();
    Optional<Path> outputPath = Choosers.getOutputDirectory(mot.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    currentDirectory = mot.getParent().toFile();
    unpack(mot, outputPath.get());
  }

  private static void unpack(Path mot, Path outputDir) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        try {
          updateMessage("Unpacking MOT file...");
          Motion motion = Motion.parseFromFile(mot);
          motion.unpack(outputDir);
        } catch (Exception e) {
          updateMessage("Failed");
          updateProgress(1, 1);
          LOGGER.log(Level.SEVERE, "Error", e);
          throw new RuntimeException(e);
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Unpacking MOT File", task);
    task.setOnSucceeded(event -> {
      tryToOpen(outputDir);
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Unpack MOT File", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
    }

  private static void tryToOpen(Path outputDir) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(outputDir.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable,oldValue, e) -> {
      if (e!=null){
        LOGGER.log(Level.SEVERE, "Failed to Open Directory", e);
        Message.error("Failed to Open Directory", e.getMessage());
      }
    });
    new Thread(task).start();
  }
}
