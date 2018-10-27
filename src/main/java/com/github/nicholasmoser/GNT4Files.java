package com.github.nicholasmoser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javafx.util.Pair;

public class GNT4Files
{
    // GNT4 has 2585 relevant crc32 files
    private Map<String, String> fileToCrc32 = Maps.newHashMapWithExpectedSize(2585);

    // GNT4 has 439 FPK files.
    private Map<String, Set<String>> fpkToChildren = Maps.newHashMapWithExpectedSize(439);

    // GNT4 has 2466 child FPK files.
    // First element of the pair is the parent path, the second is the child id
    private Map<String, Pair<String, String>> childrenInfo = Maps.newHashMapWithExpectedSize(2466);

    // GNT4 has 109 uncompressed FPK children.
    private List<String> uncompressedFpkChildren = new ArrayList<String>(109);

    /**
     * Creates an object containing information about the files in GNT4.
     */
    public GNT4Files()
    {
        JSONTokener json = new JSONTokener(getClass().getResourceAsStream("gnt4Files.json"));
        JSONArray jsonArray = new JSONArray(json);
        for (Object fileObject : jsonArray)
        {
            JSONObject file = (JSONObject) fileObject;
            if (file.has("children"))
            {
                String fpkPath = file.getString("path");
                JSONArray children = file.getJSONArray("children");
                Set<String> compressedFiles = Sets.newHashSetWithExpectedSize(children.length());
                for (Object childObject : children)
                {
                    JSONObject child = (JSONObject) childObject;
                    String path = child.getString("path");
                    String crc32 = child.getString("crc32");
                    String id = child.getString("id");
                    if (child.has("compressed") && !child.getBoolean("compressed"))
                    {
                        uncompressedFpkChildren.add(id);
                    }
                    fileToCrc32.put(path, crc32);
                    childrenInfo.put(path, new Pair<String, String>(fpkPath, id));
                    compressedFiles.add(path);
                }
                fpkToChildren.put(fpkPath, compressedFiles);
            }
            else
            {
                fileToCrc32.put(file.getString("path"), file.getString("crc32"));
            }
        }
    }

    /**
     * Retrieves the parent of a compressed file. Returns empty string if not found.
     * 
     * @param file The compressed file.
     * @return The parent of the compressed file.
     */
    public String getParentFPK(String file)
    {
        Pair<String, String> childInfo = childrenInfo.get(file);
        return childInfo == null ? "" : childInfo.getKey();
    }

    /**
     * Retrieves all compressed files in GNT4. Returns an empty array if none are found. It is more efficient to retrieve specific parent/children instead of this method. GNT4 has 119 non-FPK files.
     * 
     * @return All compressed files in GNT4.
     */
    public String[] getFPKChildren()
    {
        List<String> allChildren = new ArrayList<String>(119);
        for (Set<String> children : fpkToChildren.values())
        {
            allChildren.addAll(children);
        }
        return allChildren.toArray(new String[allChildren.size()]);
    }

    /**
     * Retrieves all compressed files for a specific FPK. Returns an empty array if none are found.
     * 
     * @param file The FPK to get the children of.
     * @return The children of the FPK.
     */
    public String[] getFPKChildren(String file)
    {
        Set<String> children = fpkToChildren.get(file);
        return children == null ? new String[0] : children.toArray(new String[children.size()]);
    }

    /**
     * Compare a new set of CRC32 hash values to the base values. Returns the files that have changed.
     * 
     * @param comparisonCRC32Values The new CRC32 hash value to compare.
     * @return The files that have changed.
     * @throws IOException If a particular entry does not exist, implying an invalid file structure.
     */
    public List<String> getFilesChanges(Map<String, String> comparisonCRC32Values)
    {
        List<String> filesChanged = new ArrayList<String>();
        for (Map.Entry<String, String> fileEntry : comparisonCRC32Values.entrySet())
        {
            String changedFile = fileEntry.getKey();
            String crc32Value = fileEntry.getValue();
            String oldCrc32Value = fileToCrc32.get(changedFile);
            if (!crc32Value.equals(oldCrc32Value))
            {
                filesChanged.add(changedFile);
            }
        }
        return filesChanged;
    }

    /**
     * Retrieves the id of a given file (specifically fpk child). These are the String identifiers used in the fpk itself.
     * 
     * @param file The child path
     * @return The id of the child
     */
    public String getId(String file)
    {
        Pair<String, String> childInfo = childrenInfo.get(file);
        return childInfo == null ? "" : childInfo.getValue();
    }

    /**
     * Whether or not the child file is normally compressed.
     * This should only be called on fpk children.
     * 
     * @param file The file to check
     * @return If it is compressed
     */
    public boolean isChildCompressed(String file)
    {
        if (file.endsWith(".fpk"))
        {
            
        }
        return !uncompressedFpkChildren.contains(file);
    }
}
