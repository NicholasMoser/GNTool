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
  Path getWorkspaceDirectory();

  /**
   * @return The compressed directory in the workspace.
   */
  Path getCompressedDirectory();

  /**
   * @return The uncompressed directory in the workspace.
   */
  Path getUncompressedDirectory();

  /**
   * @return The workspace state protobuf binary file.
   */
  Path getWorkspaceState();

  /**
   * Initializes the workspace state.
   *
   * @throws IOException If any I/O exception occurs.
   */
  void initState() throws IOException;

  /**
   * Loads the existing workspace state file.
   *
   * @throws IOException If any I/O exception occurs.
   */
  void loadExistingState() throws IOException;

  /**
   * Finds the list of files that are missing from the workspace.
   *
   * @param newGntFiles The GNTFiles to compare to the existing workspace files.
   * @return The list of files that are missing from the workspace.
   */
  Set<GNTFile> getMissingFiles(GNTFiles newGntFiles);

  /**
   * Returns the files that have been changed.
   *
   * @param newGntFiles The GNTFiles to see which changed in.
   * @return The collection of changed files.
   */
  Set<GNTFile> getChangedFiles(GNTFiles newGntFiles);

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

  /**
   * Reverts a changed file.
   *
   * @param filePath The file path.
   */
  void revertFile(String filePath) throws IOException;

  /**
   * @return A new GNTFiles object reflecting the current workspace state.
   */
  GNTFiles getNewWorkspaceState() throws IOException;
}
