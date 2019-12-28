package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.utils.ProtobufUtils;
import com.google.common.base.Verify;

public class GNT4Files {
  // The root directory for GNT4 files for creating ISOs in GameCube Rebuilder.
  public static final String ROOT_DIRECTORY = "root";
  
  // The directory of uncompressed GNT4 files.
  public static final String UNCOMPRESSED_DIRECTORY = "uncompressed";
  
  // The workspace state protobuf binary file.
  public static final String WORKSPACE_STATE = "workspace.bin";
  
  private static final String VANILLA_STATE = "vanilla_with_fpks.bin";
  
  private Path uncompressedDirectory;
  
  private Path workspaceState;
  
  private GNTFiles gntFiles;
  
  private GNTFiles vanillaFiles;
  
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
    loadVanillaState();
  }

  /**
   * Loads an existing workspace state file. Used when loading an existing workspace.
   * 
   * @throws IOException If an I/O error occurs.
   */
  public void loadExistingState() throws IOException {
    try(InputStream is = Files.newInputStream(workspaceState)) {
      this.gntFiles = GNTFiles.parseFrom(is);
    }
    loadVanillaState();
  }
  
  /**
   * Loads the vanilla state file. Used for
   * @throws IOException
   */
  private void loadVanillaState() throws IOException {
    try(InputStream is = getClass().getResourceAsStream(VANILLA_STATE)) {
      this.vanillaFiles = GNTFiles.parseFrom(is);
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
    Verify.verifyNotNull(gntFiles, "Workspace state has not been initialized.");
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
    Verify.verifyNotNull(gntFiles, "Workspace state has not been initialized.");
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
  
  /**
   * Returns the same GNTFile from the existing workspace state if it exists.
   * 
   * @param filePath The file path of the GNTFile you wish to return.
   * @return The GNTFile from the existing workspace state if it exists.
   */
  private Optional<GNTFile> getOldFile(String filePath) {
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return Optional.of(gntFile);
      }
    }
    return Optional.empty();
  }

  /**
   * Attempts to find the parent FPK file path of a child file path.
   * 
   * @param changedFile The child file path.
   * @return The parent FPK file.
   */
  public Optional<GNTFile> getParentFPK(String changedFile) {
    Verify.verifyNotNull(vanillaFiles, "Vanilla state has not been initialized.");
    Verify.verifyNotNull(changedFile, "Changed file cannot have null path.");
    for (GNTFile gntFile : vanillaFiles.getGntFileList()) {
      for (GNTChildFile child : gntFile.getGntChildFileList()) {
        if (changedFile.equals(child.getFilePath())) {
          return Optional.of(gntFile);
        }  
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the GNTChildFile list for a given FPK file path.
   * 
   * @param filePath The FPK file path.
   * @return The children of the FPK.
   */
  public List<GNTChildFile> getFPKChildren(String filePath) {
    Verify.verifyNotNull(vanillaFiles, "Vanilla state has not been initialized.");
    Verify.verifyNotNull(filePath, "FPK file path cannot be null.");
    for (GNTFile gntFile : vanillaFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return gntFile.getGntChildFileList();
      }
    }
    return Collections.emptyList();
  }
  
  /**
   * Returns the the vanilla GNTFile. Does not return children files of FPK files.
   * 
   * @param filePath The path to the file.
   * @return The vanilla GNTFile.
   */
  public Optional<GNTFile> getFile(String filePath) {
    Verify.verifyNotNull(vanillaFiles, "Vanilla state has not been initialized.");
    for (GNTFile gntFile : vanillaFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return Optional.of(gntFile);
      }
    }
    return Optional.empty();
  }
}
