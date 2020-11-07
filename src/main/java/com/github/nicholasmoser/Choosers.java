package com.github.nicholasmoser;

import com.github.nicholasmoser.gamecube.GameCubeISO;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import com.github.nicholasmoser.gnt4.GNT4Files;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Choosers {

  /**
   * Asks the user to select a file.
   *
   * @return An optional file. Empty if none is chosen.
   */
  public static Optional<Path> getFile(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select File");
    fileChooser.setInitialDirectory(initialDirectory);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an input workspace directory.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional workspace directory. Empty if none is chosen.
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
      } else if (GameCubeISO.isOldGNToolWorkspace(workspaceDirectory.toPath())) {
        String message = "GNTool 2.x workspaces are no longer supported in GNTool.";
        Message.info("Workspace No Longer Supported", message);
      } else if (new File(workspaceDirectory, GNT4Files.COMPRESSED_DIRECTORY).isDirectory()) {
        validDirectory = true;
      } else {
        String message = "Please select a valid workspace.\nIt must contain a folder named compressed.";
        Message.info("Please Select Valid Workspace", message);
      }
    }
    return Optional.of(workspaceDirectory.toPath());
  }

  /**
   * Asks the user to select an output workspace directory.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional output workspace directory. Empty if none is chosen.
   */
  public static Optional<Path> getOutputWorkspaceDirectory(File initialDirectory) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Output Workspace Directory");
    directoryChooser.setInitialDirectory(initialDirectory);
    File selection = directoryChooser.showDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an input ISO file.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional input ISO. Empty if none is chosen.
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
   * Asks the user to select an input audio file.
   *
   * @return An optional input audio file. Empty if none is chosen.
   */
  public static Optional<Path> getAudioFile(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Input Audio File");
    fileChooser.setInitialDirectory(initialDirectory);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an output .dsp audio file.
   *
   * @return An optional output .dsp audio file. Empty if none is chosen.
   */
  public static Optional<Path> getDspAudioFile(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Output Audio File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("DSP Audio (*.dsp)", "*.dsp");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an output .trk audio file.
   *
   * @return An optional output .trk audio file. Empty if none is chosen.
   */
  public static Optional<Path> getTrkAudioFile(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Output Audio File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("TRK Audio (*.trk)", "*.trk");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select DSPADPCM.exe from the Nintendo GameCube SDK.
   *
   * @return An optional input DSPADPCM.exe. Empty if none is chosen.
   */
  public static Optional<Path> getDspAdpcm(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select DSPADPCM.exe");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("DSPADPCM", "DSPADPCM.exe");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select dtkmake.exe from the Nintendo GameCube SDK.
   *
   * @return An optional input dtkmake.exe. Empty if none is chosen.
   */
  public static Optional<Path> getDtkMake(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select dtkmake.exe");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("dtkmake", "dtkmake.exe");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select SEQKage.exe.
   *
   * @return An optional input SEQKage.exe. Empty if none is chosen.
   */
  public static Optional<Path> getSeqKage(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select SEQKage.exe");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("SEQKage", "SEQKage.exe");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an output ISO file.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional output ISO. Empty if none is chosen.
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

  /**
   * Asks the user to select an input FPK file.
   *
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional input FPK. Empty if none is chosen.
   */
  public static Optional<Path> getInputFPK(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Original FPK File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("FPK File (*.fpk)", "*.fpk");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an output FPK file.
   *
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional output FPK. Empty if none is chosen.
   */
  public static Optional<Path> getOutputFPK(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Create Output FPK File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("FPK File (*.fpk)", "*.fpk");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showSaveDialog(null);

    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select an input txt file.
   *
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional input txt file. Empty if none is chosen.
   */
  public static Optional<Path> getInputTxt(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Input TXT File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("TXT File (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }

  /**
   * Asks the user to select a patch zip file.
   *
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return An optional patch zip file. Empty if none is chosen.
   */
  public static Optional<Path> getPatchZip(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Patch Zip File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Patch Zip (*.zip)", "*.zip");
    fileChooser.getExtensionFilters().add(fileExtensions);
    File selection = fileChooser.showOpenDialog(null);
    return selection != null ? Optional.of(selection.toPath()) : Optional.empty();
  }
}
