package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.workspace.SQLiteWorkspaceState;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import com.github.nicholasmoser.workspace.WorkspaceState;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Workspace for GNT4 decompressed files.
 */
public class GNT4Workspace implements Workspace {

  private final Path workspaceDir;

  private final Path compressed;

  private final Path uncompressed;

  private final WorkspaceState state;

  private final FPKOptions options;

  public GNT4Workspace(Path workspaceDir, WorkspaceState state) {
    this.workspaceDir = workspaceDir;
    this.compressed = workspaceDir.resolve("compressed");
    this.uncompressed = workspaceDir.resolve("uncompressed");
    this.state = state;
    this.options = new FPKOptions(false, true, new GNT4FileNames());
  }

  public static GNT4Workspace create(Path directory) throws IOException {
    Path dbFile = directory.resolve(SQLiteWorkspaceState.DEFAULT_NAME);
    WorkspaceState state = SQLiteWorkspaceState.create(dbFile);
    return new GNT4Workspace(directory, state);
  }

  public static GNT4Workspace load(Path directory) throws IOException {
    Path dbFile = directory.resolve(SQLiteWorkspaceState.DEFAULT_NAME);
    WorkspaceState state = SQLiteWorkspaceState.load(dbFile);
    return new GNT4Workspace(directory, state);
  }

  @Override
  public Path getWorkspaceDirectory() {
    return workspaceDir;
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
  public void initState() throws IOException {
    state.init(workspaceDir, new FPKOptions(false, true, new GNT4FileNames()));
  }

  @Override
  public List<WorkspaceFile> getAllFiles() throws IOException {
    return state.getAllFiles();
  }

  @Override
  public Set<String> getMissingFiles(GNTFiles newGntFiles) throws IOException {
    Set<String> filePaths = state.getFilePaths();
    Set<String> missingFiles = new HashSet<>();
    for (String filePath : filePaths) {
      if (!Files.exists(uncompressed.resolve(filePath))) {
        missingFiles.add(filePath);
      }
    }
    return missingFiles;
  }

  @Override
  public Set<String> getChangedFiles(GNTFiles newGntFiles) throws IOException {
    return null;
    //return gnt4Files.getChangedFiles(newGntFiles);
  }

  @Override
  public Set<String> getParentFPKs(String changedFile) throws IOException {
    return null;
    //return gnt4Files.getParentFPKs(changedFile);
  }

  @Override
  public void revertFile(String filePath) throws IOException {
    //gnt4Files.revertFile(uncompressed, compressed, filePath);
  }

  @Override
  public GNTFiles getNewWorkspaceState() throws IOException {
    return null;
    //return gnt4Files.getNewWorkspaceState();
  }

  @Override
  public FPKOptions getFPKOptions() {
    return options;
  }
}
