package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.VerifyException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryParser {

  private final Path inputDirectory;
  private final Path filesDirectory;
  private final boolean pushFilesToEnd;
  private List<ISOItem> files;

  private int currentPosition;
  private List<String> currentPath;

  /**
   * Create a new DirectoryParser for parsing ISOItems. Has an added option to push files to the end
   * of the ISO, similar to the original ISO. This should only be done if trying to match the
   * original ISO or if burning to an ISO.
   *
   * @param inputDirectory The path to the game file directory.
   * @param pushFilesToEnd If the files should be pushed to the end of the ISO.
   */
  public DirectoryParser(Path inputDirectory, boolean pushFilesToEnd) {
    this.inputDirectory = inputDirectory;
    this.filesDirectory = inputDirectory.resolve("files");
    this.pushFilesToEnd = pushFilesToEnd;
  }

  /**
   * Parses the directory and returns the ISOHeader.
   *
   * @return The ISOHeader.
   * @throws IOException If an I/O error occurs.
   */
  public ISOHeader getISOHeader() throws IOException {
    ISOHeader.Builder headerBuilder = new ISOHeader.Builder();
    currentPath = new ArrayList<>();
    files = new ArrayList<>(ISO.GNT4_ISO_ITEMS_SIZE);
    Path apploaderImgPath = resolve("sys/apploader.img");
    Path bi2BinPath = resolve("sys/bi2.bin");
    Path bootBinPath = resolve("sys/boot.bin");
    Path fstBinPath = resolve("sys/fst.bin");
    Path mainDolPath = resolve("sys/main.dol");
    int apploaderLength = (int) Files.size(apploaderImgPath);
    int mainDolLength = (int) Files.size(mainDolPath);
    int fileSystemTableLength = (int) Files.size(fstBinPath);
    int bootBinLength = (int) Files.size(bootBinPath);
    int bi2BinLength = (int) Files.size(bi2BinPath);
    if (bootBinLength != ISO.BOOT_BIN_LEN) {
      throw new IOException(
          String.format("%s is size %d, not %d", bootBinPath, bootBinLength, ISO.BOOT_BIN_LEN));
    } else if (bi2BinLength != ISO.BI_2_LEN) {
      throw new IOException(
          String.format("%s is size %d, not %d", bi2BinPath, bi2BinLength, ISO.BI_2_LEN));
    }
    ISODirectory sys = new ISODirectory.Builder()
        .setParent("")
        .setName("sys")
        .setGamePath("sys/")
        .build();
    ISOFile bootBin = new ISOFile.Builder()
        .setPos(ISO.BOOT_BIN_POS)
        .setLen(ISO.BOOT_BIN_LEN)
        .setParent(sys.getGamePath())
        .setName("boot.bin")
        .setGamePath("sys/boot.bin")
        .build();
    ISOFile bi2Bin = new ISOFile.Builder()
        .setPos(ISO.BI_2_POS)
        .setLen(ISO.BI_2_LEN)
        .setParent(sys.getGamePath())
        .setName("bi2.bin")
        .setGamePath("sys/bi2.bin")
        .build();
    ISOFile apploaderImg = new ISOFile.Builder()
        .setPos(ISO.APPLOADER_POS)
        .setLen(apploaderLength)
        .setParent(sys.getGamePath())
        .setName("apploader.img")
        .setGamePath("sys/apploader.img")
        .build();
    int startDolPosition = ByteUtils.nextAlignedPos(ISO.APPLOADER_POS + apploaderLength, 128);
    ISOFile mainDol = new ISOFile.Builder()
        .setPos(startDolPosition)
        .setLen(mainDolLength)
        .setParent(sys.getGamePath())
        .setName("main.dol")
        .setGamePath("sys/main.dol")
        .build();
    int fileSystemTablePosition = ByteUtils.nextAlignedPos(startDolPosition + mainDolLength, 128);
    ISOFile fstBin = new ISOFile.Builder()
        .setPos(fileSystemTablePosition)
        .setLen(fileSystemTableLength)
        .setParent(sys.getGamePath())
        .setName("fst.bin")
        .setGamePath("sys/fst.bin")
        .build();
    headerBuilder.setSys(sys);
    headerBuilder.setBootBin(bootBin);
    headerBuilder.setBi2Bin(bi2Bin);
    headerBuilder.setApploaderImg(apploaderImg);
    headerBuilder.setMainDol(mainDol);
    headerBuilder.setFstBin(fstBin);

    // Specified start address must be 32KB aligned in "dvdfs.c" on line 1211
    int fstEnd = fileSystemTablePosition + fileSystemTableLength;
    currentPosition = ByteUtils.nextAlignedPos(fstEnd, 32768);

    addFiles(filesDirectory);
    headerBuilder.setFiles(files);

    if (pushFilesToEnd) {
      pushFilesToEndOfISO();
    }

    return headerBuilder.createISOHeader();
  }

  /**
   * Update the position of each file such that they each are pushed towards the end of the ISO.
   * Most GameCube games do this since files will be read faster on the edges of a GameCube disc.
   * This is accomplished by iterating over all files backwards and subtracting from the disc size,
   * while also accounting for byte alignment of each file.
   */
  private void pushFilesToEndOfISO() {
    currentPosition = ISO.DISC_SIZE;
    for (int i = files.size() - 1; i >= 0; i--) {
      ISOItem item = files.get(i);
      if (!item.isDirectory()) {
        ISOFile isoFile = (ISOFile) item;
        updateByteAlignedPosition(isoFile);
      }
    }
  }

  /**
   * Updates the byte-aligned position of a particular ISOFile relative to the current position.
   * All files must be 4-byte aligned. Music files (.trk) are 0x8000-byte aligned.
   *
   * @param isoFile The ISOFile to update the position of.
   */
  private void updateByteAlignedPosition(ISOFile isoFile) {
    currentPosition -= isoFile.getLen();
    if (isoFile.getName().endsWith(".trk")) {
      currentPosition = ByteUtils.previousAlignedPos(currentPosition, 0x8000);
    } else {
      currentPosition = ByteUtils.previousAlignedPos(currentPosition, 4);
    }
    isoFile.updatePosition(currentPosition);
  }

  /**
   * Adds the files directory. This includes all files and directories in it.
   *
   * @param rootPath The path to root.
   * @throws IOException If an I/O error occurs.
   */
  private void addFiles(Path rootPath) throws IOException {
    // Add the root directory
    ISODirectory root = new ISODirectory.Builder()
        .setParent("")
        .setName("root")
        .setGamePath("")
        .setIsRoot(true)
        .build();
    files.add(root);
    // Iterate over and add each file and directory; this cannot be done in parallel
    try (Stream<Path> paths = Files.list(rootPath)) {
      paths.forEach(path -> {
            try {
              if (Files.isDirectory(path)) {
                addDirectory(path, "");
              } else {
                currentPosition = ByteUtils.nextAlignedPos(currentPosition, 4);
                int size = (int) Files.size(path);
                String fileName = path.getFileName().toString();
                ISOFile file = new ISOFile.Builder()
                    .setPos(currentPosition)
                    .setLen(size)
                    .setParent("")
                    .setName(fileName)
                    .setGamePath(getRelativeFilePath(fileName))
                    .build();
                files.add(file);
                currentPosition = currentPosition + size;
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
      );
    }
  }

  /**
   * Recursively add directories and files to the ISOItems. The files in directories will be added
   * before the files in the current directory.
   *
   * @param directoryPath The path to the directory to add.
   * @param parent        The optional parent directory of this directory.
   * @throws IOException If an I/O error occurs.
   */
  private void addDirectory(Path directoryPath, String parent)
      throws IOException {
    // Add this directory
    String directoryName = directoryPath.getFileName().toString();
    String relativeDirectoryPath = getRelativeDirectoryPath(directoryName);
    currentPath.add(directoryName);
    ISODirectory dirItem = new ISODirectory.Builder()
        .setParent(parent)
        .setName(directoryName)
        .setGamePath(relativeDirectoryPath)
        .build();
    files.add(dirItem);

    try (Stream<Path> paths = Files.list(directoryPath)) {
      paths.forEach(path -> {
            try {
              if (Files.isDirectory(path)) {
                addDirectory(path, dirItem.getGamePath());
              } else {
                // Original ISO skips bytes here
                if (pushFilesToEnd && currentPosition == 0x45530000) {
                  currentPosition = 0x45532B80;
                }

                currentPosition = ByteUtils.nextAlignedPos(currentPosition, 4);
                int size = (int) Files.size(path);
                String fileName = path.getFileName().toString();
                ISOFile fileItem = new ISOFile.Builder()
                    .setPos(currentPosition)
                    .setLen(size)
                    .setParent(dirItem.getGamePath())
                    .setName(fileName)
                    .setGamePath(getRelativeFilePath(fileName))
                    .build();
                files.add(fileItem);
                currentPosition = currentPosition + size;
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
      );
    }

    // Now exiting this directory, remove it from the path stack
    dirItem.setFstExitIndex(files.size());
    currentPath.remove(currentPath.size() - 1);
  }

  /**
   * Returns the relative game path of the file using the current path.
   *
   * @param fileName The name of the file.
   * @return The relative game path.
   */
  public String getRelativeFilePath(String fileName) {
    StringBuilder builder = new StringBuilder();
    for (String directory : currentPath) {
      builder.append(directory);
      builder.append('/');
    }
    builder.append(fileName);
    return builder.toString();
  }

  /**
   * Returns the relative game path of the directory using the current path.
   *
   * @param directoryName The name of the directory.
   * @return The relative game path.
   */
  public String getRelativeDirectoryPath(String directoryName) {
    StringBuilder builder = new StringBuilder();
    for (String directory : currentPath) {
      builder.append(directory);
      builder.append('/');
    }
    builder.append(directoryName);
    builder.append('/');
    return builder.toString();
  }

  /**
   * Converts a given path string to a {@code Path} and resolves it against this {@code Path}. A
   * VerifyException will be thrown if the file does not exist.
   *
   * @param filePathText The file path text.
   * @return The file path.
   */
  private Path resolve(String filePathText) {
    Path filePath = inputDirectory.resolve(filePathText);
    if (!Files.exists(filePath)) {
      throw new VerifyException("Cannot find " + filePath);
    }
    return filePath;
  }
}
