package com.github.nicholasmoser;

import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.gnt4.GNT4Extractor;
import com.github.nicholasmoser.gnt4.GNT4Workspace;
import com.github.nicholasmoser.gnt4.GNT4WorkspaceView;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.workspace.SQLiteWorkspaceState;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A tool that allows you to modify files in a Naruto GNT ISO file.
 * 
 * @author Nicholas Moser
 */
public class GNTool extends Application {

  public static final File USER_HOME = new File(System.getProperty("user.home"));

  private static final Logger LOGGER = Logger.getLogger(GNTool.class.getName());

  private static final long MILLIS_WAIT_AFTER_CREATE = 1500;

  private static final String FONT_SIZE_CSS = "-fx-font-size: 26px;";

  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    LOGGER.info("Application has started.");
    this.primaryStage = primaryStage;
    setLoggingProperties();
    createGUI(primaryStage);
  }

  /**
   * Creates the GUI for the application.
   * 
   * @param primaryStage The stage to use.
   */
  private void createGUI(Stage primaryStage) {
    GUIUtils.setIcons(primaryStage);
    GridPane buttonPane = createButtonGrid();
    Scene scene = new Scene(buttonPane);
    GUIUtils.initDarkMode(scene);
    primaryStage.setTitle("GNTool");
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  /**
   * Creates the grid of buttons for the application.
   * 
   * @return The created button grid.
   */
  private GridPane createButtonGrid() {
    ComboBox<Game> gameList = new ComboBox<>();
    gameList.getItems().addAll(Game.values());
    gameList.getSelectionModel().selectFirst();
    gameList.setStyle(FONT_SIZE_CSS);
    gameList.setTooltip(new Tooltip("The game you wish to mod against."));

    Button createWorkspaceButton = new Button();
    createWorkspaceButton.setText("Create Workspace");
    createWorkspaceButton.setStyle(FONT_SIZE_CSS);
    createWorkspaceButton
        .setTooltip(new Tooltip("Create a new modding workspace for the selected game."));
    createWorkspaceButton.setOnAction(event -> {
      LOGGER.fine("Create workspace button pressed.");
      Game game = gameList.getSelectionModel().getSelectedItem();
      if (game != null) {
        createWorkspace(game);
      } else {
        Message.error("Issue with Game Selected", "No valid game was selected.");
      }
    });

    Button loadWorkspaceButton = new Button();
    loadWorkspaceButton.setText("Load Workspace");
    loadWorkspaceButton.setStyle(FONT_SIZE_CSS);
    loadWorkspaceButton
        .setTooltip(new Tooltip("Loads an existing modding workspace for the selected game."));
    loadWorkspaceButton.setOnAction(event -> {
      LOGGER.fine("Load workspace button pressed.");
      Game game = gameList.getSelectionModel().getSelectedItem();
      if (game != null) {
        loadWorkspace(game);
      } else {
        Message.error("Issue with Game Selected", "No valid game was selected.");
      }
    });

    Button toolsButton = new Button();
    toolsButton.setText("\uD83D\uDD27");
    toolsButton.setStyle(FONT_SIZE_CSS);
    toolsButton
        .setTooltip(new Tooltip("Other tools for various games."));
    toolsButton.setOnAction(event -> {
      LOGGER.fine("Tools button pressed.");
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tools.fxml"));
        Scene scene = new Scene(loader.load());
        GUIUtils.initDarkMode(scene);
        ToolController controller = loader.getController();
        controller.init();
        Stage stage = new Stage();
        GUIUtils.setIcons(stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("Other Tools");
        stage.centerOnScreen();
        stage.show();
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Tools button pressed.", e);
      }
    });

    GridPane buttonPane = new GridPane();
    buttonPane.setAlignment(Pos.CENTER);
    buttonPane.setVgap(10);
    buttonPane.setPadding(new Insets(12, 12, 12, 12));
    buttonPane.add(gameList, 0, 0);
    buttonPane.add(toolsButton, 0, 0);
    GridPane.setHalignment(toolsButton, HPos.RIGHT);
    buttonPane.add(createWorkspaceButton, 0, 1);
    buttonPane.add(loadWorkspaceButton, 0, 2);

    return buttonPane;
  }

  /**
   * Query the user for a Workspace directory to load. The Workspace must match the provided Game
   * else it will display a message to the user and return.
   * 
   * @param game The Game to create the workspace for.
   */
  private void loadWorkspace(Game game) {
    Optional<Path> directoryResponse = Choosers.getInputWorkspaceDirectory(USER_HOME);
    if (directoryResponse.isPresent()) {
      if (game == Game.GNT4) {
        try {
          Path directory = directoryResponse.get();
          GameCubeISO.checkWorkspace(directory);
          if (Files.exists(directory.resolve(SQLiteWorkspaceState.FILE_NAME))) {
            // Load the sqlite state into the workspace
            Workspace workspace = GNT4Workspace.load(directory);
            WorkspaceView workspaceView = new GNT4WorkspaceView(workspace);
            workspaceView.init(primaryStage);
          } else {
            // Convert old protobuf state to new sqlite state and load it into the workspace
            LOGGER.log(Level.INFO, "Converting old protobuf workspace to sqlite workspace");
            GNT4Workspace workspace = GNT4Workspace.create(directory);
            workspace.insertGNTFiles();
            WorkspaceView workspaceView = new GNT4WorkspaceView(workspace);
            workspaceView.init(primaryStage);
          }
        } catch (IllegalStateException e) {
          LOGGER.log(Level.SEVERE, "Invalid Workspace", e);
          Message.error("Invalid Workspace", e.getMessage());
        } catch (IOException e) {
          LOGGER.log(Level.SEVERE, "Error Loading Workspace", e);
          Message.error("Error Loading Workspace", e.getMessage());
        }
      }
    }
  }

  /**
   * Query the user for an ISO and a directory to extract it to. This will also decompress all files
   * extracted. The ISO must match the provided Game else it will display a message to the user and
   * return.
   * 
   * @param game The Game to create the workspace for.
   */
  private void createWorkspace(Game game) {
    Optional<Path> isoResponse = Choosers.getInputISO(USER_HOME);
    if (isoResponse.isPresent()) {
      Path iso = isoResponse.get();
      Optional<Path> workspaceResponse =
          Choosers.getOutputWorkspaceDirectory(iso.getParent().toFile());
      if (workspaceResponse.isPresent()) {
        if (game == Game.GNT4) {
          extract(new GNT4Extractor(iso, workspaceResponse.get()));
        }
      }
    }
  }

  /**
   * Extracts the ISO given the provided extractor and decompressed the game files. The Workspace
   * will also be loaded for the user.
   * 
   * @param extractor The extractor for the Game.
   */
  private void extract(Extractor extractor) {

    Task<Workspace> task = new Task<>() {
      @Override
      public Workspace call() throws Exception {
        Workspace workspace;
        try {
          updateMessage("Extracting ISO...");
          extractor.extractISO();
          updateMessage("Unpacking FPKs...");
          workspace = extractor.unpackFPKs();
          updateMessage("Saving workspace state...");
          workspace.initState();
          updateMessage("Workspace created.");
          updateProgress(1, 1);
          Thread.sleep(MILLIS_WAIT_AFTER_CREATE);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Extracting ISO", e);
          throw e;
        }
        return workspace;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Creating Workspace", task);

    task.setOnSucceeded(event -> {
      loadingWindow.close();
      WorkspaceView workspaceView = new GNT4WorkspaceView(task.getValue());
      try {
        workspaceView.init(primaryStage);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Loading Workspace", e);
        Message.error("Error Loading Workspace", "Error loading workspace defintion.");
      }
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Create Workspace", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Sets the custom logging properties from the logging.properties included resource file.
   */
  private void setLoggingProperties() {
    try (InputStream properties = getClass().getResourceAsStream("logging.properties")) {
      LogManager.getLogManager().readConfiguration(properties);
    } catch (SecurityException | IOException e) {
      LOGGER.log(Level.SEVERE, "Unable to load logging.properties", e);
      Message.error("Logging Error", "Unable to load logging.properties");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
