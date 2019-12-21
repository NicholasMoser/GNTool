package com.github.nicholasmoser;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import com.github.nicholasmoser.gnt4.GNT4Files;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Choosers {
  /**
   * Asks the user to select an input workspace directory. This method will return null if no
   * directory is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The input workspace directory or null if none is chosen.
   */
  public static Optional<Path> getInputWorkspaceDirectory(File initialDirectory) {
    boolean validDirectory = false;
    File workspaceDirectory = null;
    while (!validDirectory) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Workspace Directory");
      directoryChooser.setInitialDirectory(initialDirectory);
      workspaceDirectory = directoryChooser.showDialog(null);
      if (workspaceDirectory == null) {
        return Optional.empty();
      } else if (new File(workspaceDirectory, GNT4Files.ROOT_DIRECTORY).isDirectory()) {
        validDirectory = true;
      } else {
        String message = "Please select a valid workspace.\nIt must contain a folder named root.";
        Message.info("Please Select Valid Workspace", message);
      }
    }
    return Optional.of(workspaceDirectory.toPath());
  }

  /**
   * Asks the user to select an output workspace directory. This method will return null if no
   * directory is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output workspace directory or null if none is chosen.
   */
  public static Optional<Path> getOutputWorkspaceDirectory(File initialDirectory) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Output Workspace Directory");
    directoryChooser.setInitialDirectory(initialDirectory);
    File selection = directoryChooser.showDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }
  
  /**
   * Asks the user to select an input ISO file. This method will return null if no file is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The input ISO or null if none is chosen.
   */
  public static Optional<Path> getInputISO(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Input ISO File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an output ISO file. This method will return null if no file is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output ISO or null if none is chosen.
   */
  public static Optional<Path> getOutputISO(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Create Output ISO File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showSaveDialog(null);
    
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }
}
