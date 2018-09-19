package com.github.nicholasmoser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Reads the CRC32 base hash values from the associated resources file. This
 * class can be used to compare to a new set of hash values to see which files
 * have been modified.
 */
public class CRC32BaseValues
{
	private static final Logger LOGGER = Logger.getLogger(CRC32BaseValues.class.getName());

	Map<String, String> baseCRC32Values;

	public CRC32BaseValues()
	{
		baseCRC32Values = new HashMap<String, String>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("crc32.csv"), StandardCharsets.UTF_8)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] keyValuePair = line.split(",");
				baseCRC32Values.put(keyValuePair[0], keyValuePair[1]);
			}
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, e.toString(), e);
			Alert alert = new Alert(AlertType.ERROR, "There was an issue with reading the CRC32 values file.");
			alert.setHeaderText("Issue with CRC32 File");
			alert.setTitle("Issue with CRC32 File");
			alert.showAndWait();
			baseCRC32Values = Collections.emptyMap();
		}
	}

	/**
	 * Compare a new set of CRC32 hash values to the base values. Returns the files
	 * that have changed.
	 * 
	 * @param comparisonCRC32Values The new CRC32 hash value to compare.
	 * @return The files that have changed.
	 * @throws IOException If a particular entry does not exist, implying an invalid
	 * file structure.
	 */
	public List<String> getFilesChanges(Map<String, String> comparisonCRC32Values) throws IOException
	{
		List<String> filesChanges = new ArrayList<String>();
		Iterator<Map.Entry<String, String>> entries = comparisonCRC32Values.entrySet().iterator();
		while (entries.hasNext())
		{
			Map.Entry<String, String> entry = entries.next();
			String key = entry.getKey();
			String value = baseCRC32Values.get(key);
			if (value == null)
			{
				throw new IOException(
						String.format("%s is not a valid file. Is that file in the correct location?", key));
			}
			if (!value.equals(entry.getValue()))
			{
				filesChanges.add(key);
			}
		}
		return filesChanges;
	}
}
