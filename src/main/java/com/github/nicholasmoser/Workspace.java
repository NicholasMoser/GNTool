package com.github.nicholasmoser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;

/**
 * A workspace for GNTool. Represented by a directory of decompressed game files.
 */
public interface Workspace {

  /**
   * @return The workspace directory.
   */
  public Path getWorkspaceDirectory();

  /**
   * @return The root directory in the workspace.
   */
  public Path getRootDirectory();

  /**
   * @return The uncompressed directory in the workspace.
   */
  public Path getUncompressedDirectory();

  /**
   * @return The workspace state protobuf binary file.
   */
  public Path getWorkspaceState();

  /**
   * Initializes the workspace state.
   * 
   * @throws IOException If any I/O exception occurs.
   */
  public void initState() throws IOException;

  /**
   * Loads the existing workspace state file.
   * 
   * @throws IOException If any I/O exception occurs.
   */
  public void loadExistingState() throws IOException;

  /**
   * Sets whether or not changes have been made to the workspace.
   * 
   * @param isDirty If the workspace has been changed.
   */
  public void setDirty(boolean isDirty);

  /**
   * @return If changes have been made to the workspace.
   */
  public boolean isDirty();

  /**
   * Finds the list of files that are missing from the workspace.
   * 
   * @param newGntFiles The GNTFiles to compare to the existing workspace files.
   * @return The list of files that are missing from the workspace.
   */
  public Set<GNTFile> getMissingFiles(GNTFiles newGntFiles);

  /**
   * Returns the files that have been changed. It is recommended to call
   * {@link #allFilesPresent(GNTFiles)} first, as this assumes that all files are present.
   * 
   * @param newGntFiles The GNTFiles to see which changed in.
   * @return The collection of changed files.
   */
  public Set<GNTFile> getChangedFiles(GNTFiles newGntFiles);

  /**
   * Returns the GNTChildFile list for a given FPK file path.
   * 
   * @param filePath The FPK file path.
   * @return The children of the FPK.
   */
  List<GNTChildFile> getFPKChildren(String filePath);

  /**
   * Attempts to find the parent FPK file path of a child file path.
   * 
   * @param changedFile The child file path.
   * @return The parent FPK file.
   */
  Optional<GNTFile> getParentFPK(String changedFile);
}
