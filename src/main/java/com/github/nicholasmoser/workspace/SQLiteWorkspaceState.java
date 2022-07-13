package com.github.nicholasmoser.workspace;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteWorkspaceState implements WorkspaceState {

  private static final Logger LOGGER = Logger.getLogger(SQLiteWorkspaceState.class.getName());

  public static final String CREATE_FILE_TABLE = """
      CREATE TABLE file (
      	file_path TEXT PRIMARY KEY,
      	hash INTEGER NOT NULL,
      	modified_dt_tm INTEGER,
          fpk_file_path TEXT,
          FOREIGN KEY(fpk_file_path) REFERENCES fpk(file_path)
      );
      """;
  public static final String CREATE_FPK_TABLE = """
      CREATE TABLE fpk (
      	file_path TEXT PRIMARY KEY,
      	hash INTEGER NOT NULL,
      	modified_dt_tm INTEGER NOT NULL
      );
      """;
  public static final String INSERT_FILE = "INSERT INTO file(file_path,hash,modified_dt_tm,fpk_file_path) VALUES(?,?,?,?)";
  public static final String INSERT_FPK = "INSERT INTO fpk(file_path,hash) VALUES(?,?)";
  private final Connection conn;

  private SQLiteWorkspaceState(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create a new SQLite backed workspace at the given file path.
   *
   * @param filePath The file path to create the SQLite backed workspace file at.
   * @return The workspace state.
   * @throws IOException If any I/O issues occur
   */
  public static WorkspaceState create(Path filePath) throws IOException {
    String url = "jdbc:sqlite:" + filePath;
    try {
      Connection conn = DriverManager.getConnection(url);
      try (PreparedStatement stmt = conn.prepareStatement(CREATE_FPK_TABLE)) {
        stmt.execute();
      }
      try (PreparedStatement stmt = conn.prepareStatement(CREATE_FILE_TABLE)) {
        stmt.execute();
      }
      return new SQLiteWorkspaceState(conn);
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * Load a new SQLite backed workspace at the given file path.
   *
   * @param filePath The file path to load the SQLite backed workspace file from.
   * @return
   * @throws IOException If any I/O issues occur
   */
  public static WorkspaceState load(Path filePath) throws IOException {
    String url = "jdbc:sqlite:" + filePath;
    try {
      Connection conn = DriverManager.getConnection(url);
      return new SQLiteWorkspaceState(conn);
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  public void insertGNTFiles(Path workspace) {

  }

  @Override
  public void init(Path compressedDir) throws IOException {

  }

  @Override
  public void insertFPK(String filePath, int hash) throws IOException {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_FPK)) {
      stmt.setString(1, filePath);
      stmt.setInt(2, hash);
      stmt.execute();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void insertFile(String filePath, int hash, int modifyDtTm, String fpkFilePath) throws IOException {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_FILE)) {
      stmt.setString(1, filePath);
      stmt.setInt(2, hash);
      stmt.setInt(3, modifyDtTm);
      stmt.setString(4, fpkFilePath);
      stmt.execute();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() {
    try {
      conn.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, "Failed to close SQLite connection" , e);
    }
  }
}
