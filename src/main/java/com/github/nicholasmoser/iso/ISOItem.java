package com.github.nicholasmoser.iso;

public class ISOItem {

  private int directoryIndex;

  private int pos;

  private int len;

  private boolean isDirectory;

  private String name;

  private String gamePath;

  public ISOItem(int directoryIndex, int pos, int len, boolean isDirectory,
      String name, String gamePath) {
    this.directoryIndex = directoryIndex;
    this.pos = pos;
    this.len = len;
    this.isDirectory = isDirectory;
    this.name = name;
    this.gamePath = gamePath;
  }

  /**
   * @return The table of contents index for this item that correlates to the directory it belongs
   * in.
   */
  public int getDirectoryIndex() {
    return directoryIndex;
  }

  /**
   * @return The position in the ISO where the data for this item starts.
   */
  public int getPos() {
    return pos;
  }

  /**
   * @return The length of the item.
   */
  public int getLen() {
    return len;
  }

  /**
   * Updates the length of the item.
   *
   * @param len The new length.
   */
  public void updateLen(int len) {
    this.len = len;
  }

  /**
   * @return <code>true</code> if this item is a directory, <code>false</code> if it is a file.
   */
  public boolean isDirectory() {
    return isDirectory;
  }

  /**
   * @return The name of the item.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The game path for the item.
   */
  public String getGamePath() {
    return gamePath;
  }

  @Override
  public String toString() {
    return "ISOItem{" +
        "directoryIndex=" + String.format("%08X", directoryIndex) +
        ", pos=" + String.format("%08X", pos) +
        ", len=" + String.format("%08X", len) +
        ", isDirectory=" + isDirectory +
        ", name='" + name + '\'' +
        ", gamePath='" + gamePath + '\'' +
        '}';
  }
}
