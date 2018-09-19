package com.github.nicholasmoser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ISOUtils
{
	private static final Logger LOGGER = Logger.getLogger(ISOUtils.class.getName());

	private static final Path currentPath = Paths.get(System.getProperty("user.dir"));

	private static final Path gcrPath = currentPath.resolve("gcr.exe");

	/**
	 * Prompt the user for an input ISO and output directory. The files contained in
	 * the ISO will be stored in a folder named root at the given output directory.
	 * This will be accomplished by using GameCube Rebuilder (gcr.exe) which should
	 * be located in the same directory as the jar. This will only work on Windows,
	 * and will return without effect if it is not.
	 */
	public static void exportFiles()
	{
		if (!canRunISOTools())
			return;
		File inputFile = Choosers.getInputISO(currentPath.toFile());
		if (inputFile == null || !inputFile.isFile())
			return;
		File outputDirectory = Choosers.getOutputDirectory(inputFile.getParentFile());
		if (outputDirectory == null || !outputDirectory.isDirectory())
			return;
		LOGGER.info("Exporting files...");
		runISOTools(inputFile.getAbsolutePath(), outputDirectory.getAbsolutePath(), true);
		LOGGER.info("Finished exporting files.");
	}

	/**
	 * Prompt the user for an output ISO and input directory. The files contained in
	 * the directory will be imported into the given ISO. This will be accomplished
	 * by using GameCube Rebuilder (gcr.exe) which should be located in the same
	 * directory as the jar. This will only work on Windows, and will return without
	 * effect if it is not.
	 */
	public static void importFiles()
	{
		if (!canRunISOTools())
			return;
		File inputDirectory = Choosers.getInputRootDirectory(currentPath.toFile());
		if (inputDirectory == null || !inputDirectory.isDirectory())
			return;
		File outputFile = Choosers.getOutputISO(inputDirectory.getParentFile().getParentFile());
		if (outputFile == null)
			return;
		try
		{
			if (!outputFile.createNewFile())
			{
				throw new IOException("Unable to create new ISO file.");
			}
		}
		catch (IOException e)
		{
			String message = String.format("Error encountered: %s.", e.getMessage());
			LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert = new Alert(AlertType.ERROR, message);
			alert.setHeaderText("File Error");
			alert.setTitle("File Error");
			alert.showAndWait();
			return;
		}
		LOGGER.info("Importing files...");
		runISOTools(inputDirectory.getAbsolutePath(), outputFile.getAbsolutePath(), false);
		LOGGER.info("Finished importing files.");
	}

	/**
	 * Main logic for ISO tools.
	 * 
	 * @param input For export mode this will be the path to an ISO file, for import
	 * mode it will be the path to a directory.
	 * @param output For export mode this will be the path to a directory, for
	 * import mode it will be the path to an ISO file.
	 * @param exportMode The output, be it ISO file or directory. If you are in ISO
	 * export mode (compared to import mode).
	 */
	private static void runISOTools(String input, String output, boolean exportMode)
	{
		LOGGER.info(String.format("Input: %s; Output: %s", input, output));
		try
		{
			Process process = null;
			String message = "";
			if (exportMode)
			{
				process = new ProcessBuilder(gcrPath.toString(), input, "root", "e", output).start();
				message = "You will find your exported files at: " + output;
			}
			else
			{
				process = new ProcessBuilder(gcrPath.toString(), input, output).start();
				message = "You will find your new ISO at: " + output;
			}
			process.waitFor();
			Alert alert = new Alert(AlertType.INFORMATION, message);
			alert.setHeaderText("GameCube Rebuilder has finished processing.");
			alert.setTitle("Process Complete");
			alert.showAndWait();

		}
		catch (IOException | InterruptedException e)
		{
			LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert = new Alert(AlertType.ERROR, "There was an issue with running gcr.exe");
			alert.setHeaderText("Issue with GameCube Rebuilder");
			alert.setTitle("Error");
			alert.showAndWait();
		}
	}

	/**
	 * @return If the running system is Windows and has access to GameCube Rebuilder
	 * (gcr.exe).
	 */
	private static boolean canRunISOTools()
	{
		LOGGER.info("GameCube Rebuilder Path: " + gcrPath);
		if (!isWindows())
		{
			LOGGER.info("Running OS is not Windows and therefore cannot run ISO Tools.");
			Alert alert = new Alert(AlertType.ERROR,
					"ISO Tools require running Windows due to it using GameCube Rebuilder (gcr.exe).");
			alert.setHeaderText("Windows Required for ISO Tools");
			alert.setTitle("Error");
			alert.showAndWait();
			return false;
		}
		if (!canRunGCR())
		{
			LOGGER.info("System cannot find gcr.exe and therefore cannot run ISO Tools.");
			Alert alert = new Alert(AlertType.ERROR,
					"ISO Tools cannot find the GameCube Rebuilder executable. Make sure its filename is gcr.exe and is in the same folder as this jar.");
			alert.setHeaderText("GameCube Rebuilder Required for ISO Tools");
			alert.setTitle("Error");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	/**
	 * @return Whether or not the operating system is Windows.
	 */
	private static boolean isWindows()
	{
		return System.getProperty("os.name").startsWith("Windows");
	}

	/**
	 * Tests whether the GameCube Rebuilder executable exists in the same directory
	 * as the jar and that it is indeed an executable. It will be called gcr.exe.
	 * 
	 * @return whether this jar can run gcr.exe
	 */
	private static boolean canRunGCR()
	{
		return Files.isExecutable(gcrPath);
	}
}
