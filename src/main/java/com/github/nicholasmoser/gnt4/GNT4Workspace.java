package com.github.nicholasmoser.gnt4;

import java.io.File;
import java.nio.file.Path;
import com.github.nicholasmoser.Workspace;

/**
 * A Workspace for GNT4 decompressed files.
 */
public class GNT4Workspace implements Workspace {

  private File directory;

  /**
   * @param directory The directory of GNT4 decompressed files.
   */
  public GNT4Workspace(File directory) {
    this.directory = directory;
  }

  @Override
  public File getWorkspaceDirectory() {
    return directory;
  }

  @Override
  public Path getRootDirectory() {
    return directory.toPath().resolve(GNT4Files.ROOT_DIRECTORY);
  }

  @Override
  public Path getUncompressedDirectory() {
    return directory.toPath().resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
  }

  @Override
  public Path getWorkspaceState() {
    return directory.toPath().resolve(GNT4Files.WORKSPACE_STATE);
  }
}
