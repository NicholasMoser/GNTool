package com.github.nicholasmoser.workspace;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.utils.CRC32;
import com.github.nicholasmoser.utils.FPKUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SQLiteWorkspaceState implements WorkspaceState {

  public static final String DEFAULT_NAME = "state.db";
  /**
   * The primary key is a combination of the file_path and the fpk_file_path because both
   * game0003.fpk and game0004.fpk have a file named omake\0002.txg
   */
  public static final String CREATE_FILE_TABLE = """
      CREATE TABLE file (
      	file_path TEXT,
      	hash INTEGER NOT NULL,
      	modified_dt_tm INTEGER,
        fpk_file_path TEXT,
        compressed INTEGER,
        PRIMARY KEY (file_path, fpk_file_path)
      );
      """;
  public static final String INSERT_FILE = "INSERT INTO file(file_path,hash,modified_dt_tm,fpk_file_path,compressed) VALUES(?,?,?,?,?)";
  public static final String SELECT_DISTINCT_FPK_FILE_PATHS = "SELECT DISTINCT fpk_file_path FROM file";
  public static final String SELECT_FILE = "SELECT file_path,hash,modified_dt_tm,fpk_file_path,compressed FROM FILE WHERE file_path = ?";
  public static final String SELECT_ALL_FILES = "SELECT file_path,hash,modified_dt_tm,fpk_file_path,compressed FROM FILE";
  public static final String SELECT_ALL_FILE_PATHS = "SELECT file_path FROM FILE";
  public static final String SELECT_MODIFIED_DT_TM = "SELECT file_path,modified_dt_tm FROM FILE";
  private static final Logger LOGGER = Logger.getLogger(SQLiteWorkspaceState.class.getName());
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
  public static SQLiteWorkspaceState create(Path filePath) throws IOException {
    String url = "jdbc:sqlite:" + filePath;
    try {
      Connection conn = DriverManager.getConnection(url);
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
   * @return The workspace state.
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

  @Override
  public void init(Path workspaceDir, FPKOptions options) throws IOException {
    // TODO: Add FPKOption for GNT Rev3 which has combined compressed and uncompressed dirs
    Path compressedDir = workspaceDir.resolve("compressed");
    Path uncompressedDir = workspaceDir.resolve("uncompressed");
    List<Path> files = Files.walk(compressedDir).filter(Files::isRegularFile)
        .collect(Collectors.toList());
    LOGGER.info("Inserting " + files.size() + " files into the database...");
    // Create a new batch insertion and disable auto commit so that we only commit once
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_FILE)) {
      conn.setAutoCommit(false);
      for (Path filePath : files) {
        String relativePath = relativizePath(compressedDir, filePath);
        if (relativePath.endsWith(".fpk")) {
          // Add each file insert an FPK archive to the database
          List<FPKFileHeader> fileHeaders = getFileHeaders(filePath, options);
          for (FPKFileHeader fileHeader : fileHeaders) {
            String name = "files/" + options.fileNames().fix(fileHeader.getFileName());
            Path path = uncompressedDir.resolve(name);
            if (!Files.exists(path)) {
              throw new IOException(path + " does not exist");
            }
            FileTime time = Files.getLastModifiedTime(path);
            int hash = CRC32.getHash(path);
            boolean compressed = fileHeader.getCompressedSize() != fileHeader.getUncompressedSize();
            stmt.setString(1, name);
            stmt.setInt(2, hash);
            stmt.setLong(3, time.toMillis());
            stmt.setString(4, relativePath);
            stmt.setBoolean(5, compressed);
            stmt.addBatch();
          }
        } else {
          // Add each non-FPK file to the database
          FileTime time = Files.getLastModifiedTime(filePath);
          int hash = CRC32.getHash(filePath);
          stmt.setString(1, relativePath);
          stmt.setInt(2, hash);
          stmt.setLong(3, time.toMillis());
          stmt.setString(4, null);
          stmt.setBoolean(5, false);
          stmt.addBatch();
        }
      }
      stmt.executeBatch();
      conn.commit();
    } catch (SQLException e) {
      throw new IOException(e);
    }
    // Now that we've done the batch insert, re-enable auto commit
    try {
      conn.setAutoCommit(true);
    } catch (SQLException e) {
      throw new IOException(e);
    }
    LOGGER.info("Completed inserting files into the database.");
  }

  @Override
  public void insertFile(WorkspaceFile file)
      throws IOException {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_FILE)) {
      stmt.setString(1, file.filePath());
      stmt.setInt(2, file.hash());
      stmt.setLong(3, file.modifiedDtTm());
      stmt.setString(4, file.fpkFilePath());
      stmt.setBoolean(5, file.compressed());
      stmt.execute();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public WorkspaceFile getFile(String filePath) throws IOException {
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_FILE)) {
      stmt.setString(1, filePath);
      try (ResultSet rs = stmt.executeQuery()) {
        return new WorkspaceFile(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4),
            rs.getBoolean(5));
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public List<WorkspaceFile> getAllFiles() throws IOException {
    List<WorkspaceFile> files = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_FILES);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        files.add(new WorkspaceFile(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4),
            rs.getBoolean(5)));
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
    return files;
  }

  @Override
  public Set<String> getFilePaths() throws IOException {
    Set<String> filePaths = new HashSet<>();
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_FILE_PATHS);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        filePaths.add(rs.getString(1));
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
    return filePaths;
  }

  @Override
  public List<String> getFPKFilePaths() throws IOException {
    List<String> fpkFilePaths = new ArrayList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_DISTINCT_FPK_FILE_PATHS);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        fpkFilePaths.add(rs.getString(1));
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
    return fpkFilePaths;
  }

  @Override
  public Map<String, Long> getFilePathToModifiedDtTm() throws IOException {
    Map<String, Long> mapping = new HashMap<>();
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_MODIFIED_DT_TM);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        String filePath = rs.getString(1);
        long modifiedDtTm = rs.getLong(2);
        mapping.put(filePath, modifiedDtTm);
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
    return mapping;
  }

  @Override
  public void close() {
    try {
      conn.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, "Failed to close SQLite connection", e);
    }
  }

  /**
   * Reads the FPK file headers from an FPK file given the FPK options.
   *
   * @param fpkPath The FPK file to read headers from.
   * @param options The FPK options to use while reading.
   * @return The FPK file headers.
   * @throws IOException If an I/O error occurs
   */
  private static List<FPKFileHeader> getFileHeaders(Path fpkPath, FPKOptions options)
      throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is, options.bigEndian());
      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is, options.longPaths(), options.bigEndian()));
      }
      return fpkHeaders;
    }
  }

  /**
   * Removes the compressed of a path from the file path, as well as the first slash. All
   * backslashes are replaced with forward slashes. So in the below example the file path goes from
   * 1. to 3. and 3. is returned. 1. C:\\compressed\\files\\a.trk 2. /files/a.trk 3. files/a.trk
   *
   * @param compressed The compressed folder.
   * @param filePath   The file path.
   * @return The relativized file path.
   */
  private static String relativizePath(Path compressed, Path filePath) {
    return filePath.toString().replace(compressed.toString(), "").replace('\\', '/').substring(1);
  }

  /**
   * Insert a GNTFiles protobuf into the SQLite database.
   *
   * @param workspaceDir
   * @param gntFiles
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  @Override
  public void insertGNTFiles(Path workspaceDir, GNTFiles gntFiles) throws IOException {
    Path uncompressedDir = workspaceDir.resolve("uncompressed");
    GNTFiles vanilla = GNT4Files.getVanillaFiles();
    // Create a new batch insertion and disable auto commit so that we only commit once
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_FILE)) {
      conn.setAutoCommit(false);
      for (GNTFile file : gntFiles.getGntFileList()) {
        String filePath = file.getFilePath();
        if (filePath.contains("gar/3000")) {
          addGar3000(stmt, file, uncompressedDir);
          continue;
        } else if (filePath.contains("kid/3000")) {
          addKid3000(stmt, file, uncompressedDir);
          continue;
        } else if (filePath.equals("files/omake/0010.txg")) {
          addOmake0010(stmt, file, uncompressedDir);
          continue;
        } else if (filePath.contains("game/3003")) {
          addGame3003(stmt, file, uncompressedDir);
          continue;
        } else if (filePath.contains("stg/010/3000")) {
          addStg00103000(stmt, file, uncompressedDir);
          continue;
        } else if (filePath.contains("files/omake/0002.txg")) {
          addOmake0002(stmt, file, uncompressedDir);
          continue;
        }
        FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
        stmt.setString(1, filePath);
        stmt.setInt(2, file.getHash());
        stmt.setLong(3, time.toMillis());
        String parent = null;
        boolean compressed = false;
        for (GNTFile vanillaFile : vanilla.getGntFileList()) {
          for (GNTChildFile vanillaChild : vanillaFile.getGntChildFileList()) {
            if (filePath.equals(vanillaChild.getFilePath())) {
              parent = vanillaFile.getFilePath();
              compressed = vanillaChild.getCompressed();
              break;
            }
          }
        }
        stmt.setString(4, parent);
        stmt.setBoolean(5, compressed);
        stmt.addBatch();
      }
      stmt.executeBatch();
      conn.commit();
    } catch (SQLException e) {
      throw new IOException(e);
    }
    // Now that we've done the batch insert, re-enable auto commit
    try {
      conn.setAutoCommit(true);
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * chr/gar/3000.poo, chr/gar/3000.pro, chr/gar/3000.sam, and chr/gar/3000.sdi exist in two
   * different FPK files: fpack/story/story0115.fpk and fpack/chr/gar3000.fpk. This method adds both
   * of them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addGar3000(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      boolean compressed = !filePath.endsWith(".sam");
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/chr/gar3000.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/story/story0115.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * chr/kid/3000.poo, chr/kid/3000.pro, chr/kid/3000.sam, and chr/kid/3000.sdi exist in two
   * different FPK files: fpack/story/story0114.fpk and fpack/chr/kid3000.fpk. This method adds both
   * of them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addKid3000(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      boolean compressed = !filePath.endsWith(".sam");
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/chr/kid3000.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/story/story0114.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * files/omake/0010.txg exists in two different FPK files: fpack/gall0000.fpk and
   * fpack/game0008.fpk. This method adds both of them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addOmake0010(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/gall0000.fpk");
      stmt.setBoolean(5, true);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/game0008.fpk");
      stmt.setBoolean(5, true);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * files/game/3003.poo, files/game/3003.pro, files/game/3003.sam, and files/game/3003.sdi exist in
   * two different FPK files: files/fpack/game0001.fpk and files/fpack/game3003.fpk. This method
   * adds both of them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addGame3003(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      boolean compressed = !filePath.endsWith(".sam");
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/game0001.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/game3003.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * files/stg/010/3000.poo, files/stg/010/3000.pro, files/stg/010/3000.sam, and
   * files/stg/010/3000.sdi exist in three separate FPK files: files/fpack/stg/0100000.fpk,
   * files/fpack/story/story0190.fpk, and files/fpack/story/story0191.fpk. This method adds both of
   * them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addStg00103000(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      boolean compressed = !filePath.endsWith(".sam");
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/stg/0100000.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/story/story0190.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/story/story0191.fpk");
      stmt.setBoolean(5, compressed);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  /**
   * files/omake/0002.txg exists in two separate FPK files: files/fpack/game0003.fpk and
   * files/fpack/game0004.fpk. This method adds both of them to the insertions.
   *
   * @param stmt            The statement to append to.
   * @param file            The file to add.
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If an I/O error occurs
   * @deprecated Protobuf in GNTool is no longer supported and is set to eventually be removed
   */
  @Deprecated
  private void addOmake0002(PreparedStatement stmt, GNTFile file, Path uncompressedDir)
      throws IOException {
    try {
      String filePath = file.getFilePath();
      FileTime time = Files.getLastModifiedTime(uncompressedDir.resolve(filePath));
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/game0003.fpk");
      stmt.setBoolean(5, true);
      stmt.addBatch();
      stmt.setString(1, filePath);
      stmt.setInt(2, file.getHash());
      stmt.setLong(3, time.toMillis());
      stmt.setString(4, "files/fpack/game0004.fpk");
      stmt.setBoolean(5, true);
      stmt.addBatch();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }
}
