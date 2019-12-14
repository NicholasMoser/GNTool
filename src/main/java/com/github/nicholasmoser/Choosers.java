package com.github.nicholasmoser;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import com.github.nicholasmoser.gnt4.GNT4Files;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Choosers {

  /**
   * Asks the user to select an input directory. The directory selected must be the root folder of
   * the exported ISO files and be named root. This method will return null if no directory is
   * chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The input directory or null if none is chosen.
   */
  public static Path getInputRootDirectory(File initialDirectory) {
    return getInputRootDirectory(initialDirectory, false);
  }

  /**
   * Asks the user to select an input directory. The directory selected must be the root folder of
   * the exported ISO files and be named root. This method will return null if no directory is
   * chosen. Includes the ability to require that the directory contains no fpk files; this is used
   * for repacking fpks.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @param checkForFpk Whether or not to require there are no fpks in the directory.
   * @return The input directory or null if none is chosen.
   */
  public static Path getInputRootDirectory(File initialDirectory, boolean checkForFpk) {
    boolean rootSelected = false;
    File inputDirectory = null;
    DirectoryChooser directoryChooser = null;
    while (!rootSelected) {
      directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Input Root Directory");
      directoryChooser.setInitialDirectory(initialDirectory);
      inputDirectory = directoryChooser.showDialog(null);
      if (inputDirectory == null || !inputDirectory.isDirectory()) {
        return null;
      } else if (!inputDirectory.getAbsolutePath().endsWith(GNT4Files.ROOT_DIRECTORY)) {
        Message.info("Please Select Root",
            "Please select the \"root\" folder of the GameCube files. It must be named root.");
        initialDirectory = inputDirectory;
      } else if (checkForFpk && containsFpk(inputDirectory)) {
        Message.info("Wrong Root Selected",
            "This directory is not an \"unpacked\" root, as it contains an fpk file. Please choose the \"unpacked\" root.");
        initialDirectory = inputDirectory.getParentFile().getParentFile();
      } else {
        rootSelected = true;
      }
    }
    return inputDirectory.toPath();
  }

  /**
   * Asks the user to select an input ISO file. This method will return null if no file is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The input ISO or null if none is chosen.
   */
  public static Path getInputISO(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Input ISO File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
    fileChooser.getExtensionFilters().add(fileExtensions);
    return fileChooser.showOpenDialog(null).toPath();
  }

  /**
   * Asks the user to select an output workspace directory. This method will return null if no
   * directory is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output workspace directory or null if none is chosen.
   */
  public static Path getOutputWorkspaceDirectory(File initialDirectory) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Output Workspace Directory");
    directoryChooser.setInitialDirectory(initialDirectory);
    return directoryChooser.showDialog(null).toPath();
  }

  /**
   * Asks the user to select an input workspace directory. This method will return null if no
   * directory is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The input workspace directory or null if none is chosen.
   */
  public static Path getInputWorkspaceDirectory(File initialDirectory) {
    boolean validDirectory = false;
    File workspaceDirectory = null;
    while (!validDirectory) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Workspace Directory");
      directoryChooser.setInitialDirectory(initialDirectory);
      workspaceDirectory = directoryChooser.showDialog(null);
      if (workspaceDirectory == null) {
        break;
      } else if (new File(workspaceDirectory, GNT4Files.ROOT_DIRECTORY).isDirectory()) {
        validDirectory = true;
      } else {
        String message = "Please select a valid workspace.\nIt must contain a folder named root.";
        Message.info("Please Select Valid Workspace", message);
      }
    }
    return workspaceDirectory.toPath();
  }

  /**
   * Asks the user to select an output directory. The directory selected must be the root folder of
   * the exported ISO files and be named root. This method will return null if no directory is
   * chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output directory or null if none is chosen.
   */
  public static Path getOutputRootDirectory(File initialDirectory) {
    return getOutputRootDirectory(initialDirectory, false);
  }

  /**
   * Asks the user to select an output directory. The directory selected must be the root folder of
   * the exported ISO files and be named root. This method will return null if no directory is
   * chosen. Includes the ability to require that the directory contains fpk files; this is used for
   * repacking fpks.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @param checkForFpk Whether or not to require there is at least one fpk in the directory.
   * @return The output directory or null if none is chosen.
   */
  public static Path getOutputRootDirectory(File initialDirectory, boolean checkForFpk) {
    boolean rootSelected = false;
    File outputDirectory = null;
    DirectoryChooser directoryChooser = null;
    while (!rootSelected) {
      directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Output Root Directory");
      directoryChooser.setInitialDirectory(initialDirectory);
      outputDirectory = directoryChooser.showDialog(null);
      if (outputDirectory == null || !outputDirectory.isDirectory()) {
        return null;
      } else if (!outputDirectory.getAbsolutePath().endsWith(GNT4Files.ROOT_DIRECTORY)) {
        Message.info("Please Select Root",
            "Please select the \"root\" folder of the GameCube files. It must be named root.");
        initialDirectory = outputDirectory;
      } else if (checkForFpk && !containsFpk(outputDirectory)) {
        Message.info("Wrong Root Selected",
            "This directory is not a \"packed\" root, as it does not contain fpk files. Please choose the \"packed\" root.");
        initialDirectory = outputDirectory.getParentFile().getParentFile();
      } else {
        rootSelected = true;
      }
    }
    return outputDirectory.toPath();
  }

  /**
   * Asks the user to select an output directory. The directory selected must not have a folder
   * named root in the same directory. This method will return null if no directory is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output directory or null if none is chosen.
   */
  public static Path getOutputNonRootDirectory(File initialDirectory) {
    boolean rootNotSelected = true;
    File outputDirectory = null;
    DirectoryChooser directoryChooser = null;
    while (rootNotSelected) {
      directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Output Directory");
      directoryChooser.setInitialDirectory(initialDirectory);
      outputDirectory = directoryChooser.showDialog(null);
      if (outputDirectory == null || !outputDirectory.isDirectory()) {
        return null;
      } else if (outputDirectory.toPath().resolve(GNT4Files.ROOT_DIRECTORY).toFile().isDirectory()) {
        Message.info("Select a Different Directory",
            "There cannot be a \"root\" folder in the directory you select.");
        initialDirectory = outputDirectory;
      } else {
        rootNotSelected = false;
      }
    }
    return outputDirectory.toPath();
  }

  /**
   * Asks the user to select an output ISO file. This method will return null if no file is chosen.
   * 
   * @param initialDirectory The location to set the directory chooser to start at.
   * @return The output ISO or null if none is chosen.
   */
  public static Path getOutputISO(File initialDirectory) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Create Output ISO File");
    fileChooser.setInitialDirectory(initialDirectory);
    ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
    fileChooser.getExtensionFilters().add(fileExtensions);
    return fileChooser.showSaveDialog(null).toPath();
  }

  /**
   * Checks if a given directory contains any fpk files. Will always be recursive.
   * 
   * @param directory The directory to check.
   * @return If there are any fpk files.
   */
  private static boolean containsFpk(File directory) {
    String[] fpkFilter = {"fpk"};
    return FileUtils.iterateFiles(directory, fpkFilter, true).hasNext();
  }
}
