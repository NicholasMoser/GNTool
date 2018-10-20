package com.github.nicholasmoser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Reads the list of files packed in each GNT4 FPK file from the associated resources file. This class can be used to find which FPK file a data file has been packed into.
 */
public class GNT4FPKFiles
{
    private static final Logger LOGGER = Logger.getLogger(GNT4FPKFiles.class.getName());

    Map<String, String[]> fpkFiles;

    /**
     * Creates a new Object containing the base FPK parent-child relationships.
     */
    public GNT4FPKFiles()
    {
        fpkFiles = new HashMap<String, String[]>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("gnt4files.csv"), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] keyValuePair = line.split(";");
                String[] files = keyValuePair[1].split(",");
                fpkFiles.put(keyValuePair[0], files);
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            Alert alert = new Alert(AlertType.ERROR, "There was an issue with reading the GNT4 FPK description.");
            alert.setHeaderText("Issue with GNT4 FPK Description File");
            alert.setTitle("Issue with CGNT4 FPK Description File");
            alert.showAndWait();
            fpkFiles = Collections.emptyMap();
        }
    }

    /**
     * Returns the name of the FPK parent file that has packed the file parameter. Returns empty string if no FPK parent file is found.
     * 
     * @param fileName The name of the file to find the parent FPK for.
     * @return The parent FPK file.
     */
    public String getParentFPK(String fileName)
    {
        Iterator<Map.Entry<String, String[]>> entries = fpkFiles.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, String[]> entry = entries.next();
            if (Arrays.asList(entry.getValue()).contains(fileName))
            {
                return entry.getKey();
            }
        }
        return "";
    }

    /**
     * Returns the name of the child files of a given FPK file. Returns an empty array if no children are found.
     * 
     * @param fpkName The name of the FPK file.
     * @return The children of the FPK file.
     */
    public String[] getFPKChildren(String fpkName)
    {
        Iterator<Map.Entry<String, String[]>> entries = fpkFiles.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, String[]> entry = entries.next();
            if (entry.getKey().equals(fpkName))
            {
                return entry.getValue();
            }
        }
        return new String[0];
    }
}
