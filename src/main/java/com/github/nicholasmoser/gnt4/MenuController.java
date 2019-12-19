package com.github.nicholasmoser.gnt4;

import java.awt.Desktop;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKPacker;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
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

public class MenuController {
  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

  private Workspace workspace;

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
      System.out.println("isDirty: " + workspace.isDirty());
      GNTFiles newFiles = ProtobufUtils.createBinary(workspace.getUncompressedDirectory());
      Set<GNTFile> missingFiles = workspace.getMissingFiles(newFiles);
      if (!missingFiles.isEmpty()) {
        String separatedFiles = missingFiles.stream()
            .map(file -> file.getFilePath())
            .collect(Collectors.joining("; "));
        Message.error("Files Missing", "The following files are missing from the workspace: " + separatedFiles);
      }
      Set<GNTFile> changedFiles = workspace.getChangedFiles(newFiles);
      if (!changedFiles.isEmpty()) {
        String separatedFiles = changedFiles.stream()
            .map(file -> file.getFilePath())
            .collect(Collectors.joining("; "));
        Message.error("Files Changed", "The following files have been changed: " + separatedFiles);
      }
      System.out.println("isDirty: " + workspace.isDirty());
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
    try {
      Path isoFile = Choosers.getOutputISO(GNTool.USER_HOME);
      if (isoFile == null) {
        return;
      }
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      Path compressedDirectory = workspace.getRootDirectory();
      FPKPacker fpkPacker = new FPKPacker(uncompressedDirectory, compressedDirectory);
      Optional<Path> compressedPath = fpkPacker.pack();
      if (compressedPath.isPresent()) {
        GameCubeISO.importFiles(compressedPath.get(), isoFile);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Building ISO", e.getMessage());
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
   * Adds a workspace to the menu controller.
   * 
   * @param workspace The workspace to add.
   */
  public void addWorkspace(Workspace workspace) {
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
