package com.github.nicholasmoser.gnt4;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.CodePatcher;
import com.github.nicholasmoser.FPKPacker;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.gnt4.GNT4Code.ID;
import com.google.common.io.Files;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class MenuController {
  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());
  
  private Path workspace;

  /**
   * Toggles the code for fixing the audio.
   * @param event The action event.
   */
  @FXML
  protected void audioFixCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.AUDIO_FIX);
  }

  /**
   * Toggles the code for unlocking everything.
   * @param event The action event.
   */
  @FXML
  protected void unlockEverythingCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.UNLOCK_EVERYTHING);
  }

  /**
   * Toggles the code for skipping cutscenes.
   * @param event The action event.
   */
  @FXML
  protected void skipCutscenesCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.SKIP_CUTSCENES);
  }

  /**
   * Toggles the code for setting the Aspect Ratio to 16:9.
   * @param event The action event.
   */
  @FXML
  protected void aspectRatioCode(ActionEvent event) {
    toggleCode(event, GNT4Code.ID.ASPECT_RATIO);
  }

  /**
   * Refreshes the current workspace for any changes having occurred outside of GNTool.
   * @param event The action event.
   */
  @FXML
  protected void refresh(ActionEvent event) {
    System.out.println("refresh");
  }

  /**
   * Builds the GNT4 ISO for the current workspace.
   * @param event The action event.
   */
  @FXML
  protected void build(ActionEvent event) {
    try
    {
      File isoFile = Choosers.getOutputISO(GNTool.USER_HOME);
      if (isoFile == null) {
        return;
      }
      Path uncompressedDirectory = workspace.resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
      Path compressedDirectory = workspace.resolve(GNT4Files.ROOT_DIRECTORY);
      FPKPacker fpkPacker = new FPKPacker(uncompressedDirectory, compressedDirectory);
      Optional<Path> compressedPath = fpkPacker.pack();
      if (compressedPath.isPresent()) {
        GameCubeISO.importFiles(compressedPath.get().toFile(), isoFile);
      }
    }
    catch (IOException e)
    {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Building ISO", e.getMessage());
    }
  }

  /**
   * Quits GNTool.
   * @param event The action event.
   */
  @FXML
  protected void quit(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Opens the Github repository web page for GNTool, which serves as the about page.
   * @param event The action event.
   */
  @FXML
  protected void about(ActionEvent event) {
    try {
      Desktop.getDesktop().browse(new URI("https://github.com/NicholasMoser/GNTool"));
    } catch (IOException | URISyntaxException e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Opening About Page", e.getMessage());
    }
  }
  
  /**
   * Adds a workspace to the menu controller.
   * @param workspace The workspace to add.
   */
  public void addWorkspace(Workspace workspace) {
    this.workspace = workspace.getWorkspaceDirectory().toPath();
  }

  /**
   * Toggles the given code.
   * @param event The action event.
   * @param codeId The code id.
   */
  private void toggleCode(ActionEvent event, ID codeId) {
    Object source = event.getSource();
    if (source instanceof CheckBox) {
      CheckBox checkBox = (CheckBox) source;
      if (checkBox.isSelected()) {
        System.out.println("Patch: " + codeId);
        //CodePatcher.patchFile(filePath, code);
      } else
      {
        System.out.println("Unpatch: " + codeId);
        //CodePatcher.unpatchFile(filePath, code);
      }
    }
  }
}
