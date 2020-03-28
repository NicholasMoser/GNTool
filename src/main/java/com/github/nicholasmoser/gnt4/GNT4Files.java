package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.utils.FPKUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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

  // The compressed directory of GNT4 files for creating ISOs in GameCube Rebuilder.
  public static final String COMPRESSED_DIRECTORY = "compressed";

  // The uncompressed directory of GNT4 files.
  public static final String UNCOMPRESSED_DIRECTORY = "uncompressed";

  // The workspace state protobuf binary file.
  public static final String WORKSPACE_STATE = "workspace.bin";

  private static final String VANILLA_STATE = "vanilla_with_fpks.bin";

  private Path uncompressedDirectory;

  private Path workspaceState;

  private GNTFiles gntFiles;

  private GNTFiles vanillaFiles;

  /**
   * Creates a new GNT4Files object from an uncompressed directory and a protobuf workspace state
   * binary.
   *
   * @param uncompressedDirectory The uncompressed directory.
   * @param workspaceState        The protobuf workspace state binary.
   */
  public GNT4Files(Path uncompressedDirectory, Path workspaceState) {
    this.uncompressedDirectory = uncompressedDirectory;
    this.workspaceState = workspaceState;
  }

  /**
   * Initializes the workspace state file. Used when creating a new workspace or when saving a new
   * workspace state.
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
    try (InputStream is = Files.newInputStream(workspaceState)) {
      this.gntFiles = GNTFiles.parseFrom(is);
    }
    loadVanillaState();
  }

  /**
   * Loads the vanilla state file. Used for actions involving comparisons to vanilla GNT4.
   *
   * @throws IOException If an I/O error occurs.
   */
  private void loadVanillaState() throws IOException {
    try (InputStream is = getClass().getResourceAsStream(VANILLA_STATE)) {
      this.vanillaFiles = GNTFiles.parseFrom(is);
    }
  }

  /**
   * Finds the set of files that are missing from the workspace. It will take each file that should
   * be in the workspace and see if it is in the list of GNTFiles passed in.
   *
   * @param newGntFiles The GNTFiles to compare to the existing workspace files.
   * @return The set of files that are missing from the workspace.
   */
  public Set<GNTFile> getMissingFiles(GNTFiles newGntFiles) {
    Verify.verifyNotNull(gntFiles, "Workspace state has not been initialized.");
    Set<GNTFile> missingFiles = new HashSet<>();
    Set<String> newFilePaths = newGntFiles.getGntFileList().stream()
        .map(GNTFile::getFilePath).collect(Collectors.toSet());
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      if (!newFilePaths.contains(gntFile.getFilePath())) {
        missingFiles.add(gntFile);
      }
    }
    return missingFiles;
  }

  /**
   * Finds the set of files that have changed for a new set of files and the existing workspace.
   *
   * @param newGntFiles The new files to compare to the workspace.
   * @return The set of files that have changed.
   */
  public Set<GNTFile> getChangedFiles(GNTFiles newGntFiles) {
    Verify.verifyNotNull(gntFiles, "Workspace state has not been initialized.");
    Set<GNTFile> filesChanged = new HashSet<>();
    for (GNTFile newFile : newGntFiles.getGntFileList()) {
      Optional<GNTFile> oldFile = getWorkspaceFile(newFile.getFilePath());
      if (oldFile.isPresent()) {
        if (newFile.getHash() != oldFile.get().getHash()) {
          filesChanged.add(newFile);
        }
      }
    }
    return filesChanged;
  }

  /**
   * Returns the parent FPK file path of a child file path if it exists.
   *
   * @param changedFile The child file path.
   * @return The parent FPK file if it exists.
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
   * Returns the list of GNTChildFile for a given FPK file path. Will return an empty list if there
   * are no children or the file is not found.
   *
   * @param filePath The FPK file path.
   * @return The children of the FPK or an empty list.
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
   * Returns the GNTFile from the existing workspace state if it exists for a given path. Does not
   * return children files of FPK files.
   *
   * @param filePath The path to the file.
   * @return The GNTFile from the existing workspace state if it exists.
   */
  private Optional<GNTFile> getWorkspaceFile(String filePath) {
    for (GNTFile gntFile : gntFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return Optional.of(gntFile);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the GNTFile from the vanilla workspace state if it exists for a given path. Does not
   * return children files of FPK files.
   *
   * @param filePath The path to the file.
   * @return The GNTFile from the vanilla workspace state if it exists.
   */
  public Optional<GNTFile> getVanillaFile(String filePath) {
    Verify.verifyNotNull(vanillaFiles, "Vanilla state has not been initialized.");
    for (GNTFile gntFile : vanillaFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        return Optional.of(gntFile);
      }
    }
    return Optional.empty();
  }

  /**
   * Reverts a file in the uncompressed directory. The data of this file will be replaced with the
   * data of the file in the compressedDirectory directory. For children of fpk files this data will
   * be extracted from the respective fpk file. The file path must be in the mod ready form.
   *
   * @param uncompressedDirectory The uncompressed directory of the workspace.
   * @param compressedDirectory   The compressed directory of the workspace.
   * @param filePath              The mod ready path of the file to revert.
   * @throws IOException If an I/O error occurs.
   */
  public void revertFile(Path uncompressedDirectory, Path compressedDirectory, String filePath)
      throws IOException {
    Verify.verifyNotNull(vanillaFiles, "Vanilla state has not been initialized.");
    Verify.verifyNotNull(filePath, "File path cannot be null.");
    for (GNTFile gntFile : vanillaFiles.getGntFileList()) {
      if (filePath.equals(gntFile.getFilePath())) {
        Path saved = compressedDirectory.resolve(filePath);
        Path current = uncompressedDirectory.resolve(filePath);
        Files.copy(saved, current, StandardCopyOption.REPLACE_EXISTING);
        return;
      }
      for (GNTChildFile child : gntFile.getGntChildFileList()) {
        if (filePath.equals(child.getFilePath())) {
          Path saved = compressedDirectory.resolve(gntFile.getFilePath());
          Path current = uncompressedDirectory.resolve(filePath);
          byte[] bytes = FPKUtils.getChildBytes(saved, child.getCompressedPath());
          Files.write(current, bytes);
          return;
        }
      }
    }
    throw new IOException(filePath + " not found.");
  }
}
