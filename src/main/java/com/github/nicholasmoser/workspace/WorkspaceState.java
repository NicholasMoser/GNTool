package com.github.nicholasmoser.workspace;

import com.github.nicholasmoser.fpk.FPKOptions;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface WorkspaceState {

  void init(Path workspaceDir, FPKOptions options) throws IOException;

  void insertFile(String filePath, int hash, long modifyDtTm, String fpkFilePath) throws IOException;

  List<WorkspaceFile> getAllFiles() throws IOException;

  List<String> getFPKFilePaths() throws IOException;

  Map<String, Long> getFilePathToModifiedDtTm() throws IOException;

  void close();
}
