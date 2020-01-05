package com.github.nicholasmoser.gnt4;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKPacker;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.utils.ProtobufUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {
  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

  private static final String ABOUT_URL = "https://github.com/NicholasMoser/GNTool";

  private Workspace workspace;

  @FXML
  private ListView<String> changedFiles;

  @FXML
  private ListView<String> missingFiles;

  @FXML
  private CheckBox audioFixCode;

  @FXML
  private CheckBox skipCutscenesCode;

  /**
   * Toggles the code for fixing the audio.
   * 
   * @param event The action event.
   */
  @FXML
  protected void audioFixCode(ActionEvent event) {
    boolean selected = audioFixCode.isSelected();
    Path uncompressedDirectory = workspace.getUncompressedDirectory();
    if (selected) {
      GNT4Codes.activateAudioFixCode(uncompressedDirectory);
    } else {
      GNT4Codes.inactivateAudioFixCode(uncompressedDirectory);
    }
  }

  /**
   * Toggles the code for skipping cutscenes.
   * 
   * @param event The action event.
   */
  @FXML
  protected void skipCutscenesCode(ActionEvent event) {
    boolean selected = skipCutscenesCode.isSelected();
    Path uncompressedDirectory = workspace.getUncompressedDirectory();
    if (selected) {
      GNT4Codes.activateSkipCutscenesCode(uncompressedDirectory);
    } else {
      GNT4Codes.inactivateSkipCutscenesCode(uncompressedDirectory);
    }
  }

  /**
   * Refreshes the current workspace for any changes having occurred outside of GNTool.
   * 
   * @param event The action event.
   */
  @FXML
  protected void refresh(ActionEvent event) {
    asyncRefresh();
  }

  /**
   * Builds the GNT4 ISO for the current workspace.
   * 
   * @param event The action event.
   */
  @FXML
  protected void build(ActionEvent event) {
    // Force refresh
    try {
      syncRefresh();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Refreshing Workspace", e);
      Message.error("Error Refreshing Workspace", "See the log for more information.");
      return;
    }

    // Prevent build if files are missing
    if (!missingFiles.getItems().isEmpty()) {
      Message.error("Missing Files",
          "You cannot build the ISO while files are missing.\nSee the Missing Files tab.");
      return;
    }

    // Warn user if audio fix not selected
    if (!audioFixCode.isSelected()) {
      String message =
          "The audio fix code is not currently selected. It is recommended for it to be enabled. Do you still wish to continue?";
      boolean choice = Message.warnConfirmation("Audio Fix Not Selected", message);
      if (!choice) {
        return;
      }
    }

    // Warn user if no files have changed
    final boolean repack;
    if (changedFiles.getItems().isEmpty()) {
      String message =
          "There are no changed files in your workspace. Do you still wish to build an ISO?";
      boolean choice = Message.warnConfirmation("No Changed Files", message);
      if (choice) {
        repack = false;
      } else {
        return;
      }
    } else {
      repack = true;
    }

    // Get output ISO path
    Optional<Path> isoResponse = Choosers.getOutputISO(GNTool.USER_HOME);
    if (isoResponse.isEmpty()) {
      return;
    }

    // Create task to repack FPKs and build ISO
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        if (repack) {
          updateMessage("Repacking FPKs...");
          FPKPacker fpkPacker = new FPKPacker(workspace);
          fpkPacker.pack(changedFiles.getItems());
        }
        updateMessage("Building ISO...");
        GameCubeISO.importFiles(workspace.getRootDirectory(), isoResponse.get());
        updateProgress(1, 1);
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Building ISO", task);

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent event) {
        Message.info("ISO Build Complete", "The new ISO was successfully created.");
        loadingWindow.close();
        saveWorkspaceState();
        asyncRefresh();
      }
    });
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        Message.error("ISO Build Failure", "See the log for more information.");
        loadingWindow.close();
        // Don't save workspace state to make debugging easier
      }
    });
    new Thread(task).start();
  }

  /**
   * Quits GNTool.
   * 
   * @param event The action event.
   */
  @FXML
  protected void quit(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Opens the Github repository web page for GNTool, which serves as the about page.
   * 
   * @param event The action event.
   */
  @FXML
  protected void about(ActionEvent event) {
    try {
      Desktop.getDesktop().browse(new URI(ABOUT_URL));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening About Page", e);
      Message.error("Error Opening About Page", "See the log for more information.");
    }
  }

  /**
   * Opens the uncompressed files directory in the workspace using the sytem file browser.
   * 
   * @param event The action event.
   */
  @FXML
  protected void openDirectory(ActionEvent event) {
    try {
      Desktop.getDesktop().open(workspace.getUncompressedDirectory().toFile());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Workspace Directory", e);
      Message.error("Error Opening Workspace Directory", "See the log for more information.");
    }
  }

  @FXML
  protected void changedFileSelected(MouseEvent event) {
    EventTarget result = event.getTarget();
    if (event.getButton() == MouseButton.SECONDARY && result instanceof Text) {
      try {
        Text text = (Text) result;
        Path filePath = workspace.getUncompressedDirectory().resolve(text.getText());
        Desktop.getDesktop().open(filePath.toFile());
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error Opening File", e);
        Message.error("Error Opening File", "See the log for more information.");
      }
    }
  }

  /**
   * Initializes with a workspace.
   * 
   * @param workspace The workspace to add.
   */
  public void init(Workspace workspace) {
    this.workspace = workspace;
    asyncRefresh();
  }

  /**
   * Saves the workspace state. This means that refresh will be cleared of changes.
   */
  private void saveWorkspaceState() {
    try {
      workspace.initState();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to save workspace state.", e);
    }
  }

  /**
   * Refresh the workspace synchronously. Will not create any windows.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void syncRefresh() throws IOException {
    GNTFiles newFiles = ProtobufUtils.createBinary(workspace.getUncompressedDirectory());
    refreshMissingFiles(newFiles);
    refreshChangedFiles(newFiles);
  }

  /**
   * Refresh the workspace asynchronously. Will create a loading window for progress.
   */
  private void asyncRefresh() {
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        try {
          updateMessage("Refreshing workspace...");
          syncRefresh();
          updateProgress(1, 1);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to refresh workspace.", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Refreshing Workspace", task);

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent event) {
        loadingWindow.close();
      }
    });
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        Message.error("Error Refreshing Workspace", "See the log for more information.");
        loadingWindow.close();
      }
    });
    new Thread(task).start();
  }

  /**
   * Refreshes the missing files tab from a set of GNTFiles.
   * 
   * @param newFiles The GNTFiles to check against.
   */
  private void refreshMissingFiles(GNTFiles newFiles) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        List<String> missingFilenames = workspace.getMissingFiles(newFiles).stream()
            .map(file -> file.getFilePath()).collect(Collectors.toList());
        missingFiles.getItems().setAll(missingFilenames);
      }
    });
  }

  /**
   * Refreshes the changed files tab from a set of GNTFiles.
   * 
   * @param newFiles The GNTFiles to check against.
   */
  private void refreshChangedFiles(GNTFiles newFiles) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        List<String> changedFilenames = workspace.getChangedFiles(newFiles).stream()
            .map(file -> file.getFilePath()).collect(Collectors.toList());
        changedFiles.getItems().setAll(changedFilenames);
      }
    });
  }
}
