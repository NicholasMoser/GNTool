package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKUnpacker;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class FPKUnpackerTool {

  private static final Logger LOGGER = Logger.getLogger(FPKUnpackerTool.class.getName());

  public static void unpackGamecubeFPK() {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    Path fpk = optionalFPK.get();
    Optional<Path> outputPath = Choosers.getOutputWorkspaceDirectory(fpk.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    unpack(fpk, outputPath.get(), false);
  }

  public static void unpackWiiFPK() {

    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    Path fpk = optionalFPK.get();
    Optional<Path> outputPath = Choosers.getOutputWorkspaceDirectory(fpk.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    unpack(fpk, outputPath.get(), true);
  }

  /**
   * Unpacks an fpk file to the giben output path.
   *
   * @param fpkPath The path to the fpk file.
   * @param outputPath The path to the output directory.
   * @param isWii If the fpk is a Wii fpk or not (GameCube otherwise).
   */
  private static void unpack(Path fpkPath, Path outputPath, boolean isWii) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        updateMessage(String.format("Unpacking %s", fpkPath.getFileName()));
        try {
          FPKUnpacker.extractFPK(fpkPath, outputPath, Optional.empty(), isWii);
        } catch (IOException ex) {
          LOGGER.log(Level.SEVERE, "Error", ex);
          throw new RuntimeException(ex);
        }
        updateMessage("Complete");
        updateProgress(1, 1);
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Unpacking FPK", task);
    task.setOnSucceeded(event -> {
      loadingWindow.close();
      Message.info("FPK Unpacked", "FPK unpacking complete.");
    });
    task.setOnFailed(event -> {
      loadingWindow.close();
      Message.error("Failed to Unpack FPK", "See log for more information.");
    });
    new Thread(task).start();
  }
}
