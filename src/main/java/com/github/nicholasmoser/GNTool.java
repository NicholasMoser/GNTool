package com.github.nicholasmoser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.gnt4.GNT4Extractor;
import com.github.nicholasmoser.gnt4.GNT4Workspace;
import com.github.nicholasmoser.gnt4.GNT4WorkspaceView;
import com.github.nicholasmoser.utils.GUIUtils;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A tool that allows you to modify files in a Naruto GNT ISO file.
 * 
 * @author Nicholas Moser
 */
public class GNTool extends Application {

  public static final File USER_HOME = new File(System.getProperty("user.home"));

  private static final Logger LOGGER = Logger.getLogger(GNTool.class.getName());

  private final String FONT_SIZE_CSS = "-fx-font-size: 26px;";

  private final long MILLIS_WAIT_AFTER_LOAD = 1500;
  
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

    primaryStage.setTitle("GNTool");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Creates the grid of buttons for the application.
   * 
   * @return The created button grid.
   */
  private GridPane createButtonGrid() {
    ComboBox<Game> gameList = new ComboBox<Game>();
    gameList.getItems().addAll(Game.values());
    gameList.getSelectionModel().selectFirst();
    gameList.setStyle(FONT_SIZE_CSS);
    gameList.setTooltip(new Tooltip("The game you wish to mod against."));

    Button createWorkspaceButton = new Button();
    createWorkspaceButton.setText("Create Workspace");
    createWorkspaceButton.setStyle(FONT_SIZE_CSS);
    createWorkspaceButton
        .setTooltip(new Tooltip("Create a new modding workspace for the selected game."));
    createWorkspaceButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        LOGGER.fine("Create workspace button pressed.");
        Game game = gameList.getSelectionModel().getSelectedItem();
        if (game != null) {
          createWorkspace(game);
        } else {
          Message.error("Issue with Game Selected", "No valid game was selected.");
        }
      }
    });

    Button loadWorkspaceButton = new Button();
    loadWorkspaceButton.setText("Load Workspace");
    loadWorkspaceButton.setStyle(FONT_SIZE_CSS);
    loadWorkspaceButton
        .setTooltip(new Tooltip("Loads an existing modding workspace for the selected game."));
    loadWorkspaceButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        LOGGER.fine("Load workspace button pressed.");
        Game game = gameList.getSelectionModel().getSelectedItem();
        if (game != null) {
          loadWorkspace(game);
        } else {
          Message.error("Issue with Game Selected", "No valid game was selected.");
        }
      }
    });

    GridPane buttonPane = new GridPane();
    buttonPane.setAlignment(Pos.CENTER);
    buttonPane.setVgap(10);
    buttonPane.setPadding(new Insets(12, 12, 12, 12));
    buttonPane.add(gameList, 0, 0);
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
    File directory = Choosers.getInputWorkspaceDirectory(USER_HOME);
    if (directory != null) {
      if (game == Game.GNT4) {
        try {
          GameCubeISO.checkWorkspace(directory, game);
          Workspace workspace = new GNT4Workspace(directory);
          WorkspaceView workspaceView = new GNT4WorkspaceView(workspace);
          workspaceView.init(primaryStage);
        } catch (IllegalStateException e) {
          LOGGER.log(Level.SEVERE, e.toString(), e);
          Message.error("Invalid Workspace", e.getMessage());
        } catch (IOException e) {
          LOGGER.log(Level.SEVERE, e.toString(), e);
          Message.error("Error Loading Workspace", "Error loading workspace defintion.");
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
    File iso = Choosers.getInputISO(USER_HOME);
    if (iso != null) {
      try {
        String gameId = GameCubeISO.getGameId(iso);
        if (game.getGameId().equals(gameId)) {
          File workspacePath = Choosers.getOutputWorkspaceDirectory(iso.getParentFile());
          if (workspacePath != null) {
            if (game == Game.GNT4) {
              extract(new GNT4Extractor(iso, workspacePath));
            }
          }
        } else {
          String message = String.format("%s Game ID does not match selected Game ID %s.", iso,
              Game.GNT4.getGameId());
          Message.error("Wrong Game ISO", message);
        }
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, e.toString(), e);
        Message.error("Issue with Opening ISO", "An error was encountered opening " + iso);
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
    Stage loadingWindow = new Stage();
    loadingWindow.initModality(Modality.APPLICATION_MODAL);
    loadingWindow.initStyle(StageStyle.UNDECORATED);
    loadingWindow.setTitle("Creating workspace...");
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

    Scene dialogScene = new Scene(flow, 300, 200);
    loadingWindow.setScene(dialogScene);
    loadingWindow.show();

    Task<Workspace> task = new Task<Workspace>() {
      @Override
      public Workspace call() {
        Workspace workspace = null;
        final int max = 1;
        try {
          updateMessage("Extracting ISO...");
          extractor.extractISO();
          updateMessage("Unpacking FPKs...");
          workspace = extractor.unpackFPKs();
          updateMessage("Workspace created.");
          updateProgress(1, max);
          Thread.sleep(MILLIS_WAIT_AFTER_LOAD);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, e.toString(), e);
          Message.error("Issue with Extracting ISO",
              "An error was encountered extracting " + extractor.getISO());
        }
        return workspace;
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent event) {
        loadingWindow.close();
        WorkspaceView workspaceView = new GNT4WorkspaceView(task.getValue());
        try
        {
          workspaceView.init(primaryStage);
        }
        catch(IOException e)
        {
          LOGGER.log(Level.SEVERE, e.toString(), e);
          Message.error("Error Loading Workspace", "Error loading workspace defintion.");
        }
      }
    });
    progressIndicator.progressProperty().bind(task.progressProperty());
    text.textProperty().bind(task.messageProperty());
    new Thread(task).start();
  }

  /**
   * Sets the custom logging properties from the logging.properties included resource file.
   */
  private void setLoggingProperties() {
    try (InputStream properties = getClass().getResourceAsStream("logging.properties")) {
      LogManager.getLogManager().readConfiguration(properties);
    } catch (SecurityException | IOException e) {
      String errorMessage =
          String.format("Unable to load logging.properties, fatal error: %s", e.toString());
      LOGGER.log(Level.SEVERE, errorMessage, e);
      Message.error("Logging Error", errorMessage);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
