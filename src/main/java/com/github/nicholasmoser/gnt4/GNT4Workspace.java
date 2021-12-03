package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.Workspace;

/**
 * A Workspace for GNT4 decompressed files.
 */
public class GNT4Workspace implements Workspace {

  private final Path directory;

  private final Path compressed;

  private final Path uncompressed;

  private final Path workspaceState;

  private final GNT4Files gnt4Files;

  /**
   * @param directory The directory of GNT4 decompressed files.
   */
  public GNT4Workspace(Path directory) {
    this.directory = directory;
    this.compressed = directory.resolve(GNT4Files.COMPRESSED_DIRECTORY);
    this.uncompressed = directory.resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
    this.workspaceState = directory.resolve(GNT4Files.WORKSPACE_STATE);
    this.gnt4Files = new GNT4Files(uncompressed, workspaceState);
  }

  @Override
  public Path getWorkspaceDirectory() {
    return directory;
  }

  @Override
  public Path getCompressedDirectory() {
    return compressed;
  }

  @Override
  public Path getUncompressedDirectory() {
    return uncompressed;
  }

  @Override
  public Path getWorkspaceState() {
    return workspaceState;
  }

  @Override
  public void initState() throws IOException {
    gnt4Files.initState();
  }

  @Override
  public void loadExistingState() throws IOException {
    gnt4Files.loadExistingState();
  }

  @Override
  public Set<GNTFile> getMissingFiles(GNTFiles newGntFiles) {
    return gnt4Files.getMissingFiles(newGntFiles);
  }

  @Override
  public Set<GNTFile> getChangedFiles(GNTFiles newGntFiles) {
    return gnt4Files.getChangedFiles(newGntFiles);
  }

  @Override
  public List<GNTChildFile> getFPKChildren(String filePath) {
    return gnt4Files.getFPKChildren(filePath);
  }

  @Override
  public List<GNTFile> getParentFPKs(String changedFile) {
    return gnt4Files.getParentFPKs(changedFile);
  }

  @Override
  public void revertFile(String filePath) throws IOException {
    gnt4Files.revertFile(uncompressed, compressed, filePath);
  }

  @Override
  public GNTFiles getNewWorkspaceState() throws IOException {
    return gnt4Files.getNewWorkspaceState();
  }
}
