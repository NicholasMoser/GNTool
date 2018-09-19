package com.github.nicholasmoser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * A list of files in GNT4 that are not compressed.
 */
public class UncompressedFiles
{
	private static final Logger LOGGER = Logger.getLogger(UncompressedFiles.class.getName());

	private List<String> uncompressedFiles;

	public UncompressedFiles()
	{
		uncompressedFiles = new ArrayList<String>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("uncompressedFiles.dat"), StandardCharsets.UTF_8)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				uncompressedFiles.add(line.trim());
			}
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert = new Alert(AlertType.ERROR, "There was an issue with reading the uncompressed files file.");
			alert.setHeaderText("Issue with Uncompressed Files File");
			alert.setTitle("Issue with Uncompressed Files File");
			alert.showAndWait();
			uncompressedFiles = Collections.emptyList();
		}
	}

	public List<String> getFiles()
	{
		return uncompressedFiles;
	}
}
