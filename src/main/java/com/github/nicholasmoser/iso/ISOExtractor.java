package com.github.nicholasmoser.iso;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class to extract files and directories from an ISO.
 */
public class ISOExtractor {

  private final Path isoPath;
  private final Path outputPath;
  private final Path filesPath;

  /**
   * Create an ISOExtractor with a path to an ISO to extract and a directory to save files and
   * directories to.
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
    ISOHeader isoHeader = parser.getISOHeader();
    extract(isoHeader);
  }

  /**
   * Extract an ISO using an ISOHeader.
   *
   * @param isoHeader The ISOHeader for the ISO.
   * @throws IOException If an I/O error occurs.
   */
  public void extract(ISOHeader isoHeader) throws IOException {
    Files.createDirectories(outputPath.resolve("sys"));
    try (RandomAccessFile raf = new RandomAccessFile(isoPath.toFile(), "r")) {
      extractItem(isoHeader.getBootBin(), raf);
      extractItem(isoHeader.getBi2Bin(), raf);
      extractItem(isoHeader.getApploaderImg(), raf);
      extractItem(isoHeader.getMainDol(), raf);
      extractItem(isoHeader.getFstBin(), raf);
      for (ISOItem item : isoHeader.getFiles()) {
        extractItem(item, raf);
      }
    }
  }

  /**
   * Extract the given ISOItem from the ISO RandomAccessFile.
   *
   * @param item The ISOItem to extract.
   * @param raf The RandomAccessFile to read from.
   * @throws IOException If an I/O error occurs.
   */
  private void extractItem(ISOItem item, RandomAccessFile raf) throws IOException {
    Path fullPath = getFullPath(item.getGamePath());
    if (item.isDirectory()) {
      Files.createDirectories(fullPath);
    } else {
      ISOFile file = (ISOFile) item;
      raf.seek(file.getPos());
      byte[] bytes = new byte[file.getLen()];
      raf.read(bytes);
      Files.write(fullPath, bytes);
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
