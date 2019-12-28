package com.github.nicholasmoser.gnt4;

import java.awt.Desktop;
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
import com.github.nicholasmoser.gnt4.GNT4Code.ID;
import com.github.nicholasmoser.utils.ProtobufUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class MenuController {
  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

  private Workspace workspace;

  @FXML
  private ListView<String> changedFiles;

  @FXML
  private ListView<String> missingFiles;

  /**
   * Toggles the code for fixing the audio.
   * 
   * @param event The action event.
   */
  @FXML
  protected void audioFixCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.AUDIO_FIX);
  }

  /**
   * Toggles the code for unlocking everything.
   * 
   * @param event The action event.
   */
  @FXML
  protected void unlockEverythingCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.UNLOCK_EVERYTHING);
  }

  /**
   * Toggles the code for skipping cutscenes.
   * 
   * @param event The action event.
   */
  @FXML
  protected void skipCutscenesCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.SKIP_CUTSCENES);
  }

  /**
   * Toggles the code for setting the Aspect Ratio to 16:9.
   * 
   * @param event The action event.
   */
  @FXML
  protected void aspectRatioCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.ASPECT_RATIO);
  }

  /**
   * Refreshes the current workspace for any changes having occurred outside of GNTool.
   * 
   * @param event The action event.
   */
  @FXML
  protected void refresh(ActionEvent event) {
    try {
      // Refresh missing files tab list
      GNTFiles newFiles = ProtobufUtils.createBinary(workspace.getUncompressedDirectory());
      List<String> missingFilenames = workspace.getMissingFiles(newFiles).stream()
          .map(file -> file.getFilePath()).collect(Collectors.toList());
      missingFiles.getItems().clear();
      missingFiles.getItems().addAll(missingFilenames);
      
      // Refresh changed files tab list
      List<String> changedFilenames = workspace.getChangedFiles(newFiles).stream()
          .map(file -> file.getFilePath()).collect(Collectors.toList());
      changedFiles.getItems().clear();
      changedFiles.getItems().addAll(changedFilenames);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Refreshing Workspace", e.getMessage());
    }
  }

  /**
   * Builds the GNT4 ISO for the current workspace.
   * 
   * @param event The action event.
   */
  @FXML
  protected void build(ActionEvent event) {
    // Force refresh
    refresh(null);
    
    // Prevent build if files are missing
    if (!missingFiles.getItems().isEmpty()) {
      Message.error("Missing Files", "You cannot build the ISO while files are missing.\nSee the Missing Files tab.");
      return;
    }
    
    // Warn user if no files have changed
    boolean repack = true;
    if (changedFiles.getItems().isEmpty()) {
      String message = "There are no changed files in your workspace. Do you still wish to build an ISO?";
      boolean choice = Message.warnYesNo("No Changed Files", message);
      if (choice) {
        repack = false;
      } else {
        return;
      }
    }
    
    // Get output ISO path
    Optional<Path> isoResponse = Choosers.getOutputISO(GNTool.USER_HOME);
    if (isoResponse.isEmpty()) {
      return;
    }
    
    // Repack FPKs
    if (repack) {
      try {
        FPKPacker fpkPacker = new FPKPacker(workspace);
        fpkPacker.pack(changedFiles.getItems());
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, e.toString(), e);
        Message.error("Error Repacking FPKs", e.getMessage());
        return;
      }
    }
    
    // Build ISO
    try {
      GameCubeISO.importFiles(workspace.getRootDirectory(), isoResponse.get());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Creating ISO", e.getMessage());
    }
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
      Desktop.getDesktop().browse(new URI("https://github.com/NicholasMoser/GNTool"));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Opening About Page", e.getMessage());
    }
  }

  /**
   * Initializes with a workspace.
   * 
   * @param workspace The workspace to add.
   */
  public void init(Workspace workspace) {
    this.workspace = workspace;
  }

  /**
   * Toggles the given code.
   * 
   * @param event The action event.
   * @param codeId The code id.
   */
  private void toggleCode(ActionEvent event, ID codeId) {
    Object source = event.getSource();
    if (source instanceof CheckBox) {
      CheckBox checkBox = (CheckBox) source;
      if (checkBox.isSelected()) {
        System.out.println("Patch: " + codeId);
        // CodePatcher.patchFile(filePath, code);
      } else {
        System.out.println("Unpatch: " + codeId);
        // CodePatcher.unpatchFile(filePath, code);
      }
    }
  }
}
