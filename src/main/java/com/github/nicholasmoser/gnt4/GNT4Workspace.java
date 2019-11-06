package com.github.nicholasmoser.gnt4;

import java.io.File;
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
  public File getDirectory() {
    return directory;
  }
}
