package com.github.nicholasmoser.workspace;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public interface WorkspaceState {

  void init(Path compressedDir) throws IOException;

  void insertFPK(String filePath, int hash) throws IOException;

  void insertFile(String filePath, int hash, int modifyDtTm, String fpkFilePath) throws IOException;

  void close();
}
