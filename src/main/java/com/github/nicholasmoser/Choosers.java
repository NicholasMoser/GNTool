package com.github.nicholasmoser;

import java.io.File;

import org.apache.commons.io.FileUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Choosers
{
    /**
     * Asks the user to select an input directory. The directory selected must be the root folder of the exported ISO files and be named root. This method will return null if no directory is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The input directory or null if none is chosen.
     */
    public static File getInputRootDirectory(File initialDirectory)
    {
        return getInputRootDirectory(initialDirectory, false);
    }
    
    /**
     * Asks the user to select an input directory. The directory selected must be the root folder of the exported ISO files and be named root. This method will return null if no directory is chosen.
     * Includes the ability to require that the directory contains no fpk files; this is used for repacking fpks.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @param checkForFpk Whether or not to require there are no fpks in the directory.
     * @return The input directory or null if none is chosen.
     */
    public static File getInputRootDirectory(File initialDirectory, boolean checkForFpk)
    {
        boolean rootSelected = false;
        File inputDirectory = null;
        DirectoryChooser directoryChooser = null;
        while (!rootSelected)
        {
            directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Input Root Directory");
            directoryChooser.setInitialDirectory(initialDirectory);
            inputDirectory = directoryChooser.showDialog(null);
            if (inputDirectory == null || !inputDirectory.isDirectory())
            {
                return null;
            }
            else if (!inputDirectory.getAbsolutePath().endsWith("root"))
            {
                Alert alert = new Alert(AlertType.INFORMATION, "Please select the \"root\" folder of the GameCube files. It must be named root.");
                alert.setHeaderText("Please Select Root");
                alert.setTitle("Please Select Root");
                alert.showAndWait();
                initialDirectory = inputDirectory;
            }
            else if (checkForFpk && containsFpk(inputDirectory))
            {
                Alert alert = new Alert(AlertType.INFORMATION, "This directory is not an \"unpacked\" root, as it contains an fpk file. Please choose the \"unpacked\" root.");
                alert.setHeaderText("Wrong Root Selected");
                alert.setTitle("Wrong Root Selected");
                alert.showAndWait();
                initialDirectory = inputDirectory.getParentFile().getParentFile();
            }
            else
            {
                rootSelected = true;
            }
        }
        return inputDirectory;
    }

    /**
     * Asks the user to select an input ISO file. This method will return null if no file is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The input ISO or null if none is chosen.
     */
    public static File getInputISO(File initialDirectory)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input ISO File");
        fileChooser.setInitialDirectory(initialDirectory);
        ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
        fileChooser.getExtensionFilters().add(fileExtensions);
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Asks the user to select an output directory. This method will return null if no directory is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The output directory or null if none is chosen.
     */
    public static File getOutputDirectory(File initialDirectory)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory");
        directoryChooser.setInitialDirectory(initialDirectory);
        return directoryChooser.showDialog(null);
    }

    /**
     * Asks the user to select an output directory. The directory selected must be the root folder of the exported ISO files and be named root. This method will return null if no directory is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The output directory or null if none is chosen.
     */
    public static File getOutputRootDirectory(File initialDirectory)
    {
        return getOutputRootDirectory(initialDirectory, false);
    }

    /**
     * Asks the user to select an output directory. The directory selected must be the root folder of the exported ISO files and be named root. This method will return null if no directory is chosen.
     * Includes the ability to require that the directory contains fpk files; this is used for repacking fpks.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @param checkForFpk Whether or not to require there is at least one fpk in the directory.
     * @return The output directory or null if none is chosen.
     */
    public static File getOutputRootDirectory(File initialDirectory, boolean checkForFpk)
    {
        boolean rootSelected = false;
        File outputDirectory = null;
        DirectoryChooser directoryChooser = null;
        while (!rootSelected)
        {
            directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Output Root Directory");
            directoryChooser.setInitialDirectory(initialDirectory);
            outputDirectory = directoryChooser.showDialog(null);
            if (outputDirectory == null || !outputDirectory.isDirectory())
            {
                return null;
            }
            else if (!outputDirectory.getAbsolutePath().endsWith("root"))
            {
                Alert alert = new Alert(AlertType.INFORMATION, "Please select the \"root\" folder of the GameCube files. It must be named root.");
                alert.setHeaderText("Please Select Root");
                alert.setTitle("Please Select Root");
                alert.showAndWait();
                initialDirectory = outputDirectory;
            }
            else if (checkForFpk && !containsFpk(outputDirectory))
            {
                Alert alert = new Alert(AlertType.INFORMATION, "This directory is not a \"packed\" root, as it does not contain fpk files. Please choose the \"packed\" root.");
                alert.setHeaderText("Wrong Root Selected");
                alert.setTitle("Wrong Root Selected");
                alert.showAndWait();
                initialDirectory = outputDirectory.getParentFile().getParentFile();
            }
            else
            {
                rootSelected = true;
            }
        }
        return outputDirectory;
    }

    /**
     * Asks the user to select an output directory. The directory selected must not have a folder named root in the same directory. This method will return null if no directory is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The output directory or null if none is chosen.
     */
    public static File getOutputNonRootDirectory(File initialDirectory)
    {
        boolean rootNotSelected = true;
        File outputDirectory = null;
        DirectoryChooser directoryChooser = null;
        while (rootNotSelected)
        {
            directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Output Directory");
            directoryChooser.setInitialDirectory(initialDirectory);
            outputDirectory = directoryChooser.showDialog(null);
            if (outputDirectory == null || !outputDirectory.isDirectory())
            {
                return null;
            }
            else if (outputDirectory.toPath().resolve("root").toFile().isDirectory())
            {
                Alert alert = new Alert(AlertType.INFORMATION, "There cannot be a \"root\" folder in the directory you select.");
                alert.setHeaderText("Select a Different Directory");
                alert.setTitle("Select a Different Directory");
                alert.showAndWait();
                initialDirectory = outputDirectory;
            }
            else
            {
                rootNotSelected = false;
            }
        }
        return outputDirectory;
    }

    /**
     * Asks the user to select an output ISO file. This method will return null if no file is chosen.
     * 
     * @param initialDirectory The location to set the directory chooser to start at.
     * @return The output ISO or null if none is chosen.
     */
    public static File getOutputISO(File initialDirectory)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Output ISO File");
        fileChooser.setInitialDirectory(initialDirectory);
        ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("ISO Image (*.iso)", "*.iso");
        fileChooser.getExtensionFilters().add(fileExtensions);
        return fileChooser.showSaveDialog(null);
    }
    
    /**
     * Checks if a given directory contains any fpk files. Will always be recursive.
     * @param directory The directory to check.
     * @return If there are any fpk files.
     */
    private static boolean containsFpk(File directory)
    {
        String[] fpkFilter = { "fpk" };
        return FileUtils.iterateFiles(directory, fpkFilter, true).hasNext();
    }
}
