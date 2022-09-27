package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.utils.CRC32;
import com.github.nicholasmoser.utils.FPKUtils;
import com.github.nicholasmoser.workspace.SQLiteWorkspaceState;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import com.github.nicholasmoser.workspace.WorkspaceState;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
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
    Path dbFile = directory.resolve(SQLiteWorkspaceState.FILE_NAME);
    WorkspaceState state = SQLiteWorkspaceState.create(dbFile);
    return new GNT4Workspace(directory, state);
  }

  public static GNT4Workspace load(Path directory) throws IOException {
    Path dbFile = directory.resolve(SQLiteWorkspaceState.FILE_NAME);
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
  public void updateState() throws IOException {
    state.delete();
    state.init(workspaceDir, new FPKOptions(false, true, new GNT4FileNames()));
  }

  @Override
  public void addFile(WorkspaceFile file) throws IOException {
    state.addFile(file);
  }

  @Override
  public boolean removeFile(String filePath) throws IOException {
    return state.removeFile(filePath);
  }

  @Override
  public List<WorkspaceFile> getAllFiles() throws IOException {
    return state.getAllFiles();
  }

  @Override
  public Set<String> getMissingFiles(List<WorkspaceFile> allFiles) {
    Set<String> missingFiles = new HashSet<>();

    for (WorkspaceFile filePath : allFiles) {
      if (!Files.exists(uncompressed.resolve(filePath.filePath()))) {
        missingFiles.add(filePath.filePath());
      }
    }
    return missingFiles;
  }

  @Override
  public Set<String> getChangedFiles(List<WorkspaceFile> allFiles) throws IOException {
    Set<String> filesChanged = new HashSet<>();
    for (WorkspaceFile file : allFiles) {
      String filePath = file.filePath();
      Path fullPath = uncompressed.resolve(filePath);
      if (!Files.exists(fullPath)) {
        // The file has been changed if it has been removed
        filesChanged.add(filePath);
      } else {
        if (Files.getLastModifiedTime(fullPath).toMillis() > file.modifiedDtTm()) {
          // File has been modified, check hash to confirm there was an actual change
          if (CRC32.getHash(fullPath) != file.hash()) {
            // File has been changed
            filesChanged.add(filePath);
          }
        }
      }
    }
    return filesChanged;
  }

  @Override
  public void revertFiles(Collection<String> filePaths) throws IOException {
    for (String filePath : filePaths) {
      WorkspaceFile file = state.getFile(filePath);
      String fpkFilePath = file.fpkFilePath();
      if (fpkFilePath != null) {
        // FPK child file
        Path saved = compressed.resolve(fpkFilePath);
        Path current = uncompressed.resolve(filePath);
        String compressedPath = options.fileNames().getCompressedName(filePath);
        byte[] bytes = FPKUtils.getChildBytes(saved, compressedPath, options.longPaths(),
            options.bigEndian());
        Files.write(current, bytes);
      } else {
        // Non-FPK file
        Path saved = compressed.resolve(filePath);
        Path current = uncompressed.resolve(filePath);
        Files.copy(saved, current, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

  @Override
  public FPKOptions getFPKOptions() {
    return options;
  }

  /**
   * Inserts GNTFiles into the workspace state.
   *
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  public void insertGNTFiles() throws IOException {
    Path oldState = workspaceDir.resolve(GNT4Files.WORKSPACE_STATE);
    if (!Files.exists(oldState)) {
      throw new IllegalStateException("Old protobuf state workspace.bin does not exist");
    }
    try(InputStream is = Files.newInputStream(oldState)) {
      GNTFiles gntFiles = GNTFiles.parseFrom(is);
      state.insertGNTFiles(workspaceDir, gntFiles);
    }
  }
}
