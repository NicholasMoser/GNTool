package com.github.nicholasmoser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Reads the filename mappings from the associated resources file. This class
 * can be used to get the filename used for FPK compressed files from the
 * relative path of the file from root after extracted. This is used for the
 * repacking of FPK files.
 */
public class Filenames
{
	private static final Logger LOGGER = Logger.getLogger(Filenames.class.getName());

	Map<String, String> fileNames;

	public Filenames()
	{
		fileNames = new HashMap<String, String>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("filenames.csv"), StandardCharsets.UTF_8)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] keyValuePair = line.split(",");
				fileNames.put(keyValuePair[0], keyValuePair[1]);
			}
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert = new Alert(AlertType.ERROR, "There was an issue with reading the filenames CSV.");
			alert.setHeaderText("Issue with Filenames CSV");
			alert.setTitle("Issue with Filenames CSV");
			alert.showAndWait();
			fileNames = Collections.emptyMap();
		}
	}

	/**
	 * Retrieves the file name for an FPK-packed file using the full path from root.
	 * 
	 * @param fullPath The full path from root.
	 * @return The associated file name.
	 */
	public String getFilename(String fullPath)
	{
		return fileNames.get(fullPath);
	}
}
