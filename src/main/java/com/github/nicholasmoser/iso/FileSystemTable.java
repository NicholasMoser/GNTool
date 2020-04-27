package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.CountingOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FileSystemTable {

  /**
   * Rewrites a fst.bin file given an input directory and the ISOHeader representing it.
   * This most importantly will update offsets in cases where the sizes of files have changed.
   *
   * @param inputPath The input directory of the ISO files and directories.
   * @param isoHeader The ISOHeader for the input directory.
   * @throws IOException If an I/O error occurs.
   */
  public static void rewrite(Path inputPath, ISOHeader isoHeader) throws IOException {
    Map<String, Integer> directoryToOffset = new HashMap<>();
    Path fstBinPath = inputPath.resolve(isoHeader.getFstBin().getGamePath());
    List<ISOItem> items = isoHeader.getFiles();
    int numEntries = items.size();
    int stringTableOffset = numEntries * 12;
    int currentStringTableOffset = 0;
    System.out.println(stringTableOffset);
    try (CountingOutputStream os = new CountingOutputStream(Files.newOutputStream(fstBinPath))) {
      // Write the rest of the entries
      for (ISOItem item : items) {
        int nameLength = item.getName().length() + 1; // Add 1 for the null terminator
        if (item.isDirectory()) {
          ISODirectory directory = (ISODirectory) item;
          directoryToOffset.put(directory.getGamePath(), (int) os.getCount());
          if (directory.isRoot()) {
            os.write(new byte[]{1, 0, 0, 0, 0, 0, 0, 0});
            os.write(ByteUtils.fromUint32(numEntries));
          } else {
            os.write(1);
            os.write(ByteUtils.fromUint24(currentStringTableOffset));
            currentStringTableOffset += nameLength;
            int parentOffset = directoryToOffset.get(item.getParent());
            os.write(ByteUtils.fromUint32(parentOffset / 12));
            os.write(ByteUtils.fromUint32(directory.getFstExitIndex()));
          }
        } else {
          ISOFile file = (ISOFile) item;
          os.write(0);
          os.write(ByteUtils.fromUint24(currentStringTableOffset));
          currentStringTableOffset += nameLength;
          os.write(ByteUtils.fromUint32(file.getPos()));
          os.write(ByteUtils.fromUint32(file.getLen()));
        }
      }
      // Write the string table
      for (ISOItem item : items) {
        if (!item.isRoot()) {
          os.write(ByteUtils.toNullTerminatedBytes(item.getName()));
        }
      }
    }
  }

  /**
   * Reads the ISO items from a fst.bin file.
   *
   * @param fstBin The fsiBin file path.
   * @return The list of ISO items.
   * @throws IOException If an I/O error occurs.
   */
  public static List<ISOItem> read(Path fstBin) throws IOException {
    if (!Files.isRegularFile(fstBin)) {
      throw new IOException(fstBin + " is not a file.");
    }
    try (RandomAccessFile raf = new RandomAccessFile(fstBin.toFile(), "r")) {
      return read(raf);
    }
  }

  /**
   * Reads the ISO items from the fst.bin. This method assumes that the RandomAccessFile is
   * currently pointing to the start of the fst.bin data. This method will not close the
   * RandomAccessFile when done or reset the position. The position when done reading will be
   * somewhere between the start and end of the fst.bin data.
   *
   * @param raf The RandomAccessFile to read the fst.bin data from.
   * @return The list of ISO items.
   * @throws IOException If an I/O error occurs.
   */
  public static List<ISOItem> read(RandomAccessFile raf) throws IOException {
    List<ISOItem> items = new ArrayList<>(ISO.GNT4_ISO_ITEMS_SIZE);
    LinkedList<ISODirectory> directoryStack = new LinkedList<>();
    Map<Integer, String> offsetToDirectory = new HashMap<>();
    int fstBinPosition = (int) raf.getFilePointer();

    // Root directory
    ISODirectory root = new ISODirectory.Builder()
        .setParent("")
        .setName("root")
        .setGamePath("")
        .setIsRoot(true)
        .build();
    items.add(root);
    offsetToDirectory.put(0, ""); // Root
    if (ByteUtils.readUint32LE(raf) != 1) {
      throw new IOException("Multiple FST image not supported.");
    }
    if (ByteUtils.readUint32LE(raf) != 0) {
      throw new IOException("Multiple FST image not supported.");
    }
    int numberOfEntries = ByteUtils.readUint32(raf);
    int stringTableOffset = fstBinPosition + (numberOfEntries * 12);

    // Read rest of files until the string table is reached
    for (int entryNumber = 1; entryNumber < numberOfEntries; entryNumber++) {
      // Check if this item is a directory and retrieve the name of it. The first byte of this
      // value is if it is a directory, the next three bytes are the item name position in the
      // item name table.
      int value = ByteUtils.readUint32(raf);
      boolean isDirectory = false;
      if (value >> 24 == 1) {
        isDirectory = true;
      }
      int stringTablePosition = value & 0x00FFFFFF;

      long savedPosition = raf.getFilePointer();
      raf.seek(stringTableOffset + stringTablePosition);
      String name = ByteUtils.readString(raf);
      raf.seek(savedPosition);

      // Remove directories from stack that now have all their items
      ListIterator<ISODirectory> itr = directoryStack.listIterator();
      while (itr.hasNext()) {
        ISODirectory directory = itr.next();
        if (entryNumber >= directory.getFstExitIndex()) {
          itr.remove();
        }
      }

      if (isDirectory) {
        // Read and add a directory
        int parentOffset = ByteUtils.readUint32(raf);
        int nextOffset = ByteUtils.readUint32(raf);
        String parent = offsetToDirectory.get(parentOffset);
        if (parent == null) {
          throw new IOException(name + " parent is null");
        }
        String gamePath = getDirectoryGamePath(parent, name);
        ISODirectory dir = new ISODirectory.Builder()
            .setFstExitIndex(nextOffset)
            .setName(name).setGamePath(gamePath).setParent(parent).build();
        items.add(dir);
        directoryStack.addFirst(dir);
        offsetToDirectory.put(entryNumber, dir.getGamePath());
      } else {
        // Read and add a file
        int fileOffset = ByteUtils.readUint32(raf);
        int fileLength = ByteUtils.readUint32(raf);
        String currentDirectory =
            directoryStack.isEmpty() ? "" : directoryStack.getFirst().getGamePath();
        String gamePath = getFileGamePath(currentDirectory, name);
        ISOFile file = new ISOFile.Builder()
            .setPos(fileOffset)
            .setLen(fileLength)
            .setName(name).setGamePath(gamePath).setParent(currentDirectory).build();
        items.add(file);
      }
    }
    return items;
  }

  /**
   * Return the game path of a directory. This will be the game path of the parent plus the
   * directory name plus a slash. An empty optional parent represents root, which has a game path
   * equivalent to empty String.
   *
   * @param parent The optional parent; empty is root.
   * @param name   The name of the directory.
   * @return The game path of the directory.
   */
  private static String getDirectoryGamePath(String parent, String name) {
    return "".equals(parent) ? name + "/" : parent + name + "/";
  }

  /**
   * Return the game path of a file. This will be the game path of the parent plus the file name. An
   * empty optional parent represents root, which has a game path equivalent to empty String.
   *
   * @param parent The optional parent; empty is root.
   * @param name   The name of the file.
   * @return The game path of the file.
   */
  private static String getFileGamePath(String parent, String name) {
    return "".equals(parent) ? name : parent + name;
  }
}
