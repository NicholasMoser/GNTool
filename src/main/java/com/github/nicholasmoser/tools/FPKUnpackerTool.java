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
    unpack(fpk, outputPath.get(), false, true);
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
    unpack(fpk, outputPath.get(), true, true);
  }

  public static void unpackPS2FPK() {

    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    Path fpk = optionalFPK.get();
    Optional<Path> outputPath = Choosers.getOutputWorkspaceDirectory(fpk.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    unpack(fpk, outputPath.get(), true, false);
  }

  /**
   * Unpacks an fpk file to the giben output path.
   *
   * @param fpkPath The path to the fpk file.
   * @param outputPath The path to the output directory.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  private static void unpack(Path fpkPath, Path outputPath, boolean longPaths, boolean bigEndian) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        updateMessage(String.format("Unpacking %s", fpkPath.getFileName()));
        try {
          FPKUnpacker.extractFPK(fpkPath, outputPath, Optional.empty(), longPaths, bigEndian);
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
      Message.info("FPK Unpacked", "FPK unpacking complete.");
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Unpack FPK", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }
}
