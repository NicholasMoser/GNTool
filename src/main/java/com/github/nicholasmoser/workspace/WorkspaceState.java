package com.github.nicholasmoser.workspace;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKOptions;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that tracks the current state of the workspace and the files in it. Responsible for
 * tracking what files should be built into the ISO and which are part of FPK archive files.
 */
public interface WorkspaceState {

  /**
   * Initializes a workspace given the provided FPK options.
   *
   * @param workspaceDir The directory of the workspace.
   * @param options      The FPK options.
   * @throws IOException If any I/O exception occurs.
   */
  void init(Path workspaceDir, FPKOptions options) throws IOException;

  /**
   * Inserts a new file into the workspace state.
   *
   * @param file The new file to insert.
   * @throws IOException If any I/O exception occurs.
   */
  void addFile(WorkspaceFile file) throws IOException;

  /**
   * Removes a file from the workspace state.
   *
   * @param filePath The path to the file.
   * @return If any files were deleted.
   * @throws IOException If any I/O exception occurs.
   */
  boolean removeFile(String filePath) throws IOException;

  /**
   * Deletes the entire workspace state.
   */
  void delete() throws IOException;

  /**
   * Gets a workspace file given the file path.
   *
   * @param filePath The file path to retrieve the file for.
   * @return The workspace file.
   * @throws IOException If any I/O exception occurs.
   */
  WorkspaceFile getFile(String filePath) throws IOException;

  /**
   * @return All files in the workspace state
   * @throws IOException If any I/O exception occurs.
   */
  List<WorkspaceFile> getAllFiles() throws IOException;

  /**
   * @return All file paths in the workspace state.
   * @throws IOException If any I/O exception occurs.
   */
  Set<String> getFilePaths() throws IOException;

  /**
   * @return All FPK file paths in the workspace state.
   * @throws IOException If any I/O exception occurs.
   */
  List<String> getFPKFilePaths() throws IOException;

  /**
   * @return A mapping of all file paths to their modified date/times in the workspace state.
   * @throws IOException If any I/O exception occurs.
   */
  Map<String, Long> getFilePathToModifiedDtTm() throws IOException;

  /**
   * @return If the workspace is currently saving the state of dol codes.
   * @throws IOException If any I/O exception occurs.
   */
  boolean isSavingCodeState() throws IOException;

  /**
   * Closes the workspace state.
   */
  void close();

  /**
   * Inserts a GNT file protobuf binary into the workspace state. Used for passivity with older
   * GNTool workspaces.
   *
   * @param workspaceDir The directory of the workspace.
   * @param gntFiles     The GNT file protobuf binary.
   * @throws IOException If any I/O exception occurs.
   * @deprecated Eventually will be removed when the passivity is no longer necessary
   */
  @Deprecated
  void insertGNTFiles(Path workspaceDir, GNTFiles gntFiles) throws IOException;
}
