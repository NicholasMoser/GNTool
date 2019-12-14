package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.Message;

public class GNT4Files {
  // The root directory for GNT4 files for creating ISOs in GameCube Rebuilder.
  public static final String ROOT_DIRECTORY = "root";
  
  // The directory of uncompressed GNT4 files.
  public static final String UNCOMPRESSED_DIRECTORY = "uncompressed";
  
  // The workspace state protobuf binary file.
  public static final String WORKSPACE_STATE = "workspace.bin";
  
  private static final Logger LOGGER = Logger.getLogger(GNT4Files.class.getName());
  
  private static final String FILE_NAME = "files.dat";

  private static GNT4Files INSTANCE;
  
  private GNTFiles gntFiles;

  private GNT4Files() {
    try(InputStream is = getClass().getResourceAsStream(FILE_NAME)) {
      gntFiles = GNTFiles.parseFrom(is);
    } catch (IOException e) {
      String message = "Error reading " + FILE_NAME;
      LOGGER.log(Level.SEVERE, message, e);
      Message.error(message, "See log file for more details.");
    }
  }

  public static GNT4Files getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GNT4Files();
    }
    return INSTANCE;
  }

  public List<String> getFilesChanges(Map<String, String> fileCRC32Values) {
    // TODO Auto-generated method stub
    return null;
  }

  public Optional<String> getParentFPK(String fileName) {
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      for (GNTChildFile child : gntFile.getGntChildFileList()) {
        if (child.getFilePath().equals(fileName)) {
          return Optional.of(gntFile.getFilePath());
        }
      }
    }
    return Optional.empty();
  }

  public boolean isChildCompressed(String childName) {
    // TODO Auto-generated method stub
    return false;
  }

  public String getId(String child) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<String> getFPKChildren(String fpk) {
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      List<GNTChildFile> children = gntFile.getGntChildFileList();
      System.out.println(children);
    }
    return null;
  }

  public List<String> getAllFPKChildren() {
    List<String> fpkChildren = new ArrayList<String>(2476);
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      for (GNTChildFile child : gntFile.getGntChildFileList()) {
        fpkChildren.add(child.getFilePath());
      }
    }
    return fpkChildren;
  }
  
  
}
