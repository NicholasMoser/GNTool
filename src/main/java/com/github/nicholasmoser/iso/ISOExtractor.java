package com.github.nicholasmoser.iso;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ISOExtractor {

  private final Path isoPath;
  private final Path outputPath;
  private final Path filesPath;

  /**
   * Create an ISOExtractor using the path to the ISO file and the path to output the files from the
   * ISO to.
   *
   * @param isoPath    The path to the ISO file.
   * @param outputPath The path to save files from the ISO.
   */
  public ISOExtractor(Path isoPath, Path outputPath) {
    this.isoPath = isoPath;
    this.outputPath = outputPath;
    filesPath = outputPath.resolve("files");
  }

  /**
   * Extract an ISO.
   *
   * @throws IOException If an I/O error occurs.
   */
  public void extract() throws IOException {
    ISOParser parser = new ISOParser(isoPath);
    List<ISOItem> isoItems = parser.getISOItems();
    extract(isoItems);
  }

  /**
   * Extract an ISO using a specific table of contents.
   *
   * @param isoItems The ISO items.
   * @throws IOException If an I/O error occurs.
   */
  public void extract(List<ISOItem> isoItems)
      throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(isoPath.toFile(), "r")) {
      for (ISOItem item : isoItems) {
        raf.seek(item.pos);
        Path fullPath = getFullPath(item.gamePath);
        if (item.isDir) {
          Files.createDirectories(fullPath);
        } else {
          Files.createDirectories(fullPath.getParent());
          byte[] bytes = new byte[item.len];
          raf.read(bytes);
          Files.write(fullPath, bytes);
        }
      }
    }
  }

  /**
   * Returns the path on your file system to save a file. All ISO system files should be saved under
   * compressed/sys. All other files will be saved under compressed/files.
   *
   * @param gamePath The game path.
   * @return The full path.
   */
  private Path getFullPath(String gamePath) {
    if (gamePath.startsWith("sys")) {
      return outputPath.resolve(gamePath);
    } else {
      return filesPath.resolve(gamePath);
    }
  }
}
