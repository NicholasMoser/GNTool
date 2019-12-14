package com.github.nicholasmoser.gnt4;

import java.nio.file.Path;
import com.github.nicholasmoser.Workspace;

/**
 * A Workspace for GNT4 decompressed files.
 */
public class GNT4Workspace implements Workspace {

  private Path directory;

  /**
   * @param directory The directory of GNT4 decompressed files.
   */
  public GNT4Workspace(Path directory) {
    this.directory = directory;
  }

  @Override
  public Path getWorkspaceDirectory() {
    return directory;
  }

  @Override
  public Path getRootDirectory() {
    return directory.resolve(GNT4Files.ROOT_DIRECTORY);
  }

  @Override
  public Path getUncompressedDirectory() {
    return directory.resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
  }

  @Override
  public Path getWorkspaceState() {
    return directory.resolve(GNT4Files.WORKSPACE_STATE);
  }
}
