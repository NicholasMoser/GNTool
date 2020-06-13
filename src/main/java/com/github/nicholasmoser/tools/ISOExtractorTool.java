package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.utils.GUIUtils;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * A tool to extract ISOs.
 */
public class ISOExtractorTool {

  private static final Logger LOGGER = Logger.getLogger(ISOExtractorTool.class.getName());

  /**
   * Extracts a GameCube ISO.
   */
  public static void extractGameCubeISO() {
    Optional<Path> inputPath = Choosers.getInputISO(GNTool.USER_HOME);
    if (inputPath.isEmpty()) {
      return;
    }
    Path iso = inputPath.get();
    Optional<Path> outputPath = Choosers.getOutputWorkspaceDirectory(iso.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    extract(iso, outputPath.get());
  }

  /**
   * Extract an ISO to an outputPath asynchronously. Includes a loading screen.
   *
   * @param iso The ISO to extract.
   * @param outputPath The path to output the ISO contents to.
   */
  private static void extract(Path iso, Path outputPath) {

    Task<Object> task = new Task<>() {
      @Override
      public Workspace call() throws Exception {
        try {
          updateMessage("Extracting ISO...");
          GameCubeISO.exportFiles(iso, outputPath);
          updateMessage("Complete.");
          updateProgress(1, 1);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Extracting ISO", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Extracting ISO", task);

    task.setOnSucceeded(event -> {
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Extract ISO", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }
}
