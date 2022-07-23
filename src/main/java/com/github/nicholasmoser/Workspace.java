package com.github.nicholasmoser;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

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
   * Initializes the workspace state.
   *
   * @throws IOException If any I/O exception occurs.
   */
  void initState() throws IOException;

  List<WorkspaceFile> getAllFiles() throws IOException;

  /**
   * Finds the list of files that are missing from the workspace.
   *
   * @param newGntFiles The GNTFiles to compare to the existing workspace files.
   * @return The list of files that are missing from the workspace.
   */
  Set<String> getMissingFiles(GNTFiles newGntFiles) throws IOException;

  /**
   * Returns the files that have been changed.
   *
   * @param newGntFiles The GNTFiles to see which changed in.
   * @return The collection of changed files.
   */
  Set<String> getChangedFiles(GNTFiles newGntFiles) throws IOException;

  /**
   * Attempts to find the parent FPK file paths of a child file path. It is possible for there to
   * be more than one, although that is rare.
   *
   * @param changedFile The child file path.
   * @return The parent FPK files.
   */
  Set<String> getParentFPKs(String changedFile) throws IOException;

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

  /**
   * @return The FPK options for this workspace.
   */
  FPKOptions getFPKOptions();
}
