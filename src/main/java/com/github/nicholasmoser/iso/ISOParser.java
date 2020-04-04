package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Class to parse an ISO and extract files.
 */
public class ISOParser {

  private static final Logger LOGGER = Logger.getLogger(ISOParser.class.getName());

  private Path isoPath;

  /**
   * Create a new ISOParser.
   *
   * @param isoPath The path to the ISO to parse.
   */
  public ISOParser(Path isoPath) {
    this.isoPath = isoPath;
  }

  /**
   * Returns the table of contents for an ISO. This describes each file in the ISO.
   *
   * @return The table of contents for the ISO.
   * @throws IOException If an I/O error occurs.
   */
  public List<ISOItem> getISOItems() throws IOException {
    List<ISOItem> isoItems = new ArrayList<>(100);
    if (!Files.exists(isoPath)) {
      throw new IOException(isoPath + " does not exist.");
    }
    try (RandomAccessFile raf = new RandomAccessFile(isoPath.toFile(), "r")) {
      checkGameCubeMagicNumber(raf);
      int gameTocPosition = readISOHeader(raf, isoItems);
      readTOC(raf, isoItems, gameTocPosition);
    }
    return isoItems;
  }

  /**
   * Reads the ISO header data. It will add the ISO header, apploader, main dol, and table of
   * contents files to the TOC object passed in. The table of contents file position in the file
   * will be returned.
   *
   * @param raf             The RandomAccessFile for the ISO to read from.
   * @param isoItems The ISO items.
   * @return The table of contents file position.
   * @throws IOException If an I/O error occurs.
   */
  private int readISOHeader(RandomAccessFile raf, List<ISOItem> isoItems) throws IOException {
    raf.seek(1024);
    int apploaderLength = readInt(raf);
    raf.skipBytes(28);
    int startDolPosition = readInt(raf);
    int fileSystemTablePosition = readInt(raf);
    int fileSystemTableLength = readInt(raf);
    int startDolLength = fileSystemTablePosition - startDolPosition;
    raf.skipBytes(8);
    int dataStart = readInt(raf);

    isoItems.add(
        new ISOItem(0, 0, 0, true, "root", ""));
    isoItems.add(
        new ISOItem(0, 0, 0, true, "sys", "sys/"));
    isoItems.add(
        new ISOItem(1, ISO.BOOT_BIN_POS, ISO.BOOT_BIN_LEN, false, "boot.bin", "sys/boot.bin"));
    isoItems.add(
        new ISOItem(1, ISO.BI_2_POS, ISO.BI_2_LEN, false, "bi2.bin", "sys/bi2.bin"));
    isoItems.add(
        new ISOItem(1, ISO.APPLOADER_POS, apploaderLength, false, "apploader.img",
            "sys/apploader.img"));
    isoItems.add(new ISOItem(1, startDolPosition, startDolLength, false, "main.dol",
        "sys/main.dol"));
    isoItems.add(new ISOItem(1, fileSystemTablePosition, fileSystemTableLength, false, "fst.bin",
        "sys/fst.bin"));

    // Logging for debugging ISOParser
    if (LOGGER.isLoggable(Level.FINEST)) {
      LOGGER.log(Level.FINEST, "apploaderLength: " + String.format("%08X", apploaderLength));
      LOGGER.log(Level.FINEST, "startDolPosition: " + String.format("%08X", startDolPosition));
      LOGGER.log(Level.FINEST, "startDolLength: " + String.format("%08X", startDolLength));
      LOGGER.log(Level.FINEST, "gameTocPosition: " + String.format("%08X", fileSystemTablePosition));
      LOGGER.log(Level.FINEST, "gameTocLength: " + String.format("%08X", fileSystemTableLength));
      LOGGER.log(Level.FINEST, "dataStart: " + String.format("%08X", dataStart));
    }

    return fileSystemTablePosition;
  }

  /**
   * Reads the table of contents data and copies it into the table of contents object passed in. The
   * table of contents defines each file in the ISO, where they are located, and the length of
   * each.
   *
   * @param raf             The RandomAccessFile for the ISO to read from.
   * @param isoItems The ISO items.
   * @param gameTocPosition The locate of the table of contents data in the file.
   * @throws IOException If an I/O error occurs.
   */
  private void readTOC(RandomAccessFile raf, List<ISOItem> isoItems, int gameTocPosition)
      throws IOException {
    raf.seek(gameTocPosition);
    if (readIntLE(raf) != 1) {
      throw new IOException("Multiple FST image not supported.");
    }
    if (readIntLE(raf) != 0) {
      throw new IOException("Multiple FST image not supported.");
    }
    int gameTocEntries = readInt(raf) - 1;
    int itemNamesOffset = gameTocEntries * 12 + 12;

    int[] array = new int[512];
    array[1] = 99999999;
    int num = 0;
    int tocIndex = isoItems.size();
    int num4 = isoItems.size() - 1;

    for (int i = 0; i < gameTocEntries; i++) {
      boolean isDirectory = false;

      // The first byte of this value is if it is a directory, the next three bytes are the
      // item name position in the item name table.
      int value = readInt(raf);
      if (value >> 24 == 1) {
        isDirectory = true;
      }
      int itemNamePosition = value & 0x00FFFFFF;

      int num8 = readInt(raf);
      int length = readInt(raf);
      long savedOffset = raf.getFilePointer();
      long itemNameOffset = itemNamesOffset + itemNamePosition;
      raf.seek(gameTocPosition + itemNameOffset);
      String itemName = readString(raf);
      raf.seek(savedOffset);
      while (array[num + 1] <= tocIndex) {
        num -= 2;
      }
      if (isDirectory) {
        num += 2;
        array[num] = ((num8 > 0) ? (num8 + num4) : num8);
        num8 += num4;
        length += num4;
        array[num + 1] = length;
      }
      StringBuilder itemPathBuilder = new StringBuilder(itemName);
      int directoryIndex = array[num];
      for (int j = 0; j < 256; j++) {
        if (directoryIndex == 0) {
          if (isDirectory) {
            itemPathBuilder.append('/');
          }
          break;
        }
        itemPathBuilder.insert(0, '/');
        itemPathBuilder.insert(0, isoItems.get(directoryIndex).getName());
        directoryIndex = isoItems.get(directoryIndex).getDirectoryIndex();
      }
      String itemPath = itemPathBuilder.toString();
      ISOItem item = new ISOItem(array[num], num8, length, isDirectory, itemName,
          itemPath);
      isoItems.add(item);
      if (isDirectory) {
        array[num] = tocIndex;
      }
      tocIndex++;
    }
  }

  /**
   * Checks the GameCube ISO magic number at offset 28. It should be 0xC2339F3D. An IOException will
   * be thrown if this magic number is not present.
   *
   * @param raf The RandomAccessFile to read from.
   * @throws IOException If the RandomAccessFile is not a GameCube ISO.
   */
  public void checkGameCubeMagicNumber(RandomAccessFile raf) throws IOException {
    raf.seek(28);
    if (readInt(raf) != 0xC2339F3D) {
      throw new IOException("Not a GameCube ISO.");
    }
  }

  /**
   * Read an ASCII String from a RandomAccessFile terminated by a null byte (0).
   *
   * @param raf The RandomAccessFile to read from.
   * @return The ASCII String.
   * @throws IOException If an I/O error occurs.
   */
  public String readString(RandomAccessFile raf) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte current = raf.readByte();
    while (current != 0) {
      baos.write(current);
      current = raf.readByte();
    }
    return baos.toString(StandardCharsets.US_ASCII);
  }

  /**
   * Reads a big-endian 4-byte uint from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The big-endian 4-byte uint.
   * @throws IOException If an I/O error occurs.
   */
  public int readInt(RandomAccessFile raf) throws IOException {
    byte[] buffer = new byte[4];
    raf.read(buffer);
    return ByteUtils.toUint32(buffer);
  }

  /**
   * Reads a little-endian 4-byte uint from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The little-endian 4-byte uint.
   * @throws IOException If an I/O error occurs.
   */
  public int readIntLE(RandomAccessFile raf) throws IOException {
    byte[] buffer = new byte[4];
    raf.read(buffer);
    return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }
}
