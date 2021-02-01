package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.zip.PatchZip;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * A tool to patch ISOs.
 */
public class ISOPatcher {

  private static final Logger LOGGER = Logger.getLogger(ISOExtractorTool.class.getName());

  /**
   * Patches a GameCube ISO.
   */
  public static void patchGameCubeISO() {
    Optional<Path> inputPath = Choosers.getInputISO(GNTool.USER_HOME);
    if (inputPath.isEmpty()) {
      return;
    }
    Path iso = inputPath.get();
    Optional<Path> patchPath = Choosers.getPatchZip(iso.getParent().toFile());
    if (patchPath.isEmpty()) {
      return;
    }
    Optional<Path> outputPath = Choosers.getOutputISO(iso.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    patch(iso, patchPath.get(), outputPath.get());
  }

  /**
   * Patches an ISO asynchronously. Includes a loading screen.
   *
   * @param input The ISO to patch.
   * @param patchZip The patch zip.
   * @param output The patched ISO.
   */
  private static void patch(Path input, Path patchZip, Path output) {

    Task<Object> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        try {
          updateMessage("Extracting ISO...");
          Path exportDir = GameCubeISO.exportFiles(input);
          try {
            updateMessage("Patching files...");
            PatchZip.patch(patchZip, exportDir);
            updateMessage("Creating new ISO...");
            // This tool only supports pushing files to the back of the ISO currently.
            // This is to prevent user confusion when patching SCON4. If you have a need
            // to patch without pushing to the back of the ISO please log an issue on Github.
            GameCubeISO.importFiles(exportDir, output, true);
          } finally {
            MoreFiles.deleteRecursively(exportDir, RecursiveDeleteOption.ALLOW_INSECURE);
          }
          updateProgress(1, 1);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Extracting ISO", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Patching ISO", task);

    task.setOnSucceeded(event -> {
      Message.info("Patched Successful", "Successfully Patched ISO at: " + output);
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Patch ISO", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

}
