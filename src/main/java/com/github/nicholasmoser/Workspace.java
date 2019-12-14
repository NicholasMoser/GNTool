package com.github.nicholasmoser;

import java.io.File;
import java.nio.file.Path;

/**
 * A workspace for GNTool. Represented by a directory of decompressed game files.
 */
public interface Workspace {

  /**
   * @return The workspace directory.
   */
  public File getWorkspaceDirectory();

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
}
