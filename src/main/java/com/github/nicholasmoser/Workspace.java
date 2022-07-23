package com.github.nicholasmoser;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
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
   * @param allFiles All files currently in the workspace state.
   * @return The list of files that are missing from the workspace.
   */
  Set<String> getMissingFiles(List<WorkspaceFile> allFiles);

  /**
   * Returns the files that have been changed.
   *
   * @param allFiles All files currently in the workspace state.
   * @return The collection of changed files.
   */
  Set<String> getChangedFiles(List<WorkspaceFile> allFiles) throws IOException;

  /**
   * Reverts changed files.
   *
   * @param filePaths The file paths.
   */
  void revertFiles(Collection<String> filePaths) throws IOException;

  /**
   * @return The FPK options for this workspace.
   */
  FPKOptions getFPKOptions();
}
