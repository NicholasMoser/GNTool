package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.utils.ProtobufUtils;
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
  
  private Path uncompressedDirectory;
  
  private Path workspaceState;
  
  private GNTFiles gntFiles;
  
  public GNT4Files(Path uncompressedDirectory, Path workspaceState) {
    this.uncompressedDirectory = uncompressedDirectory;
    this.workspaceState = workspaceState;
  }

  /**
   * Initializes the workspace state file. Used when creating a new workspace.
   * 
   * @throws IOException If an I/O error occurs.
   */
  public void initState() throws IOException {
    this.gntFiles = ProtobufUtils.createBinary(uncompressedDirectory);
    try (OutputStream os = Files.newOutputStream(workspaceState)) {
      gntFiles.writeTo(os);
    }
  }

  /**
   * Loads an existing workspace state file. Used when loading an existing workspace.
   * 
   * @throws IOException If an I/O error occurs.
   */
  public void loadExistingState() throws IOException {
    try(InputStream is = Files.newInputStream(workspaceState)) {
      this.gntFiles = GNTFiles.parseFrom(is);
    } catch (IOException e) {
      String message = "Error reading " + FILE_NAME;
      LOGGER.log(Level.SEVERE, message, e);
      Message.error(message, "See log file for more details.");
    }
  }

  /**
   * Finds the list of files that are missing from the workspace.
   * It will take each file that should be in the workspace and see
   * if it is in the list of GNTFiles passed in.
   * 
   * @param newGntFiles The GNTFiles to compare to the existing workspace files.
   * @return The list of files that are missing from the workspace.
   */
  public Set<GNTFile> getMissingFiles(GNTFiles newGntFiles) {
    Set<GNTFile> missingFiles = new HashSet<GNTFile>();
    Set<String> newFilePaths = newGntFiles.getGntFileList().stream()
        .map(newFile -> newFile.getFilePath())
        .collect(Collectors.toSet());
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      if (!newFilePaths.contains(gntFile.getFilePath())) {
        missingFiles.add(gntFile);
      }
    }
    return missingFiles;
  }
  
  /**
   * Finds the collection of files that have changed for a new set of files and the existing workspace.
   * It is recommended to call {@link #allFilesPresent(GNTFiles)} first, as this will not check
   * whether or not the files exist or not. It will simply use the hashCode function to test
   * equality of the elements.
   * @param newGntFiles The new files to compare to the workspace.
   * @return The collection of files that have changed.
   */
  public Set<GNTFile> getChangedFiles(GNTFiles newGntFiles) {
    Set<GNTFile> filesChanged = new HashSet<GNTFile>();
    for (GNTFile newFile : newGntFiles.getGntFileList()) {
      Optional<GNTFile> oldFile = getOldFile(newFile.getFilePath());
      if (oldFile.isPresent()) {
        if (newFile.getHash() != oldFile.get().getHash()) {
          filesChanged.add(newFile);
        }
      }
    }
    return filesChanged;
  }
  
  private Optional<GNTFile> getOldFile(String filePath) {
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return Optional.of(gntFile);
      }
    }
    return Optional.empty();
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
  
  public void updateState() {
    
  }
}
