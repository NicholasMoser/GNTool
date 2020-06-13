package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * GUI utilities.
 */
public class GUIUtils {
  private static final Logger LOGGER = Logger.getLogger(GUIUtils.class.getName());

  public static final String FONT_SIZE_CSS = "-fx-font-size: 26px;";

  public static final String BORDER = "-fx-effect: innershadow(gaussian, #039ed3, 2, 1.0, 0, 0);";

  private static final Image NARU_16 = new Image(GUIUtils.class.getResourceAsStream("naru16.gif"));

  private static final Image NARU_32 = new Image(GUIUtils.class.getResourceAsStream("naru32.gif"));

  private static final Image NARU_64 = new Image(GUIUtils.class.getResourceAsStream("naru64.gif"));

  private static final Image NARU_128 =
      new Image(GUIUtils.class.getResourceAsStream("naru128.gif"));

  private static final Path DARK_MODE_DISABLED = Paths.get("DARK_MODE_DISABLED");

  /**
   * Creates a new loading window for a specified task.
   * 
   * @param title The title of the window.
   * @param task The task to perform.
   * @return The loading window.
   */
  public static Stage createLoadingWindow(String title, Task<?> task) {
    Stage loadingWindow = new Stage();
    loadingWindow.initModality(Modality.APPLICATION_MODAL);
    loadingWindow.initStyle(StageStyle.UNDECORATED);
    loadingWindow.setTitle(title);
    GUIUtils.setIcons(loadingWindow);

    GridPane flow = new GridPane();
    flow.setAlignment(Pos.CENTER);
    flow.setVgap(20);

    Text text = new Text();
    text.setStyle(FONT_SIZE_CSS);

    ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);

    GridPane.setHalignment(text, HPos.CENTER);
    GridPane.setHalignment(progressIndicator, HPos.CENTER);
    flow.add(text, 0, 0);
    flow.add(progressIndicator, 0, 1);
    flow.setStyle(BORDER);

    Scene dialogScene = new Scene(flow, 450, 200);
    loadingWindow.setScene(dialogScene);
    loadingWindow.show();

    progressIndicator.progressProperty().bind(task.progressProperty());
    text.textProperty().bind(task.messageProperty());

    return loadingWindow;
  }

  /**
   * Sets the application icons on the stage.
   * 
   * @param primaryStage The primary stage to set the icons for.
   */
  public static void setIcons(Stage primaryStage) {
    ObservableList<Image> icons = primaryStage.getIcons();
    icons.add(NARU_16);
    icons.add(NARU_32);
    icons.add(NARU_64);
    icons.add(NARU_128);
  }

  public static void initDarkMode(Scene scene) {
    if (!Files.exists(DARK_MODE_DISABLED)) {
      toggleDarkMode(scene);
    }
  }

  /**
   * Sets dark theme on the scene.
   *
   * @param scene The scene to make dark themed.
   */
  public static void toggleDarkMode(Scene scene) {
    List<String> stylesheets = scene.getStylesheets();
    if (stylesheets.isEmpty()) {
      scene.getStylesheets().add(GUIUtils.class.getResource("stylesheet.css").toExternalForm());
      try {
        Files.deleteIfExists(DARK_MODE_DISABLED);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Failed to delete dark mode config file", e);
      }
    } else {
      stylesheets.clear();
      if (!Files.exists(DARK_MODE_DISABLED)) {
        try {
          Files.createFile(DARK_MODE_DISABLED);
        } catch (IOException e) {
          LOGGER.log(Level.SEVERE, "Failed to create dark mode config file", e);
        }
      }
    }
  }

  /**
   * Gets a node at a specified row and column in a GridPane.
   * https://stackoverflow.com/questions/20655024/javafx-gridpane-retrieve-specific-cell-content/20656861#20656861
   *
   * @param gridPane The GridPane to search.
   * @param col The column of the node.
   * @param row The row of the node.
   * @return The Optional node requested.
   */
  public static Optional<Node> getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return Optional.of(node);
      }
    }
    return Optional.empty();
  }
}
