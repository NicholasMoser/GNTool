package com.github.nicholasmoser.gnt4;

/**
 * An immutable POJO for a GNT4Code. If it is a code change it will have both an old instruction
 * and a new instruction.
 */
public class GNT4Code {
  private final String filePath;
  private final int offset;
  private byte[] oldInstruction;
  private byte[] newInstruction;

  /**
   * Create a new GNT4Code for a code change.
   * @param filePath The path to the file.
   * @param offset The offset to where the change goes.
   * @param oldInstruction The instruction in the original version of the game.
   * @param newInstruction The patched instruction.
   */
  public GNT4Code(String filePath, int offset, byte[] oldInstruction, byte[] newInstruction) {
    this.filePath = filePath;
    this.offset = offset;
    this.oldInstruction = oldInstruction;
    this.newInstruction = newInstruction;
  }

  /**
   * Create a new GNT4Code for a data value change.
   * @param filePath The path to the file.
   * @param offset The offset to where the change goes.
   */
  public GNT4Code(String filePath, int offset) {
    this.filePath = filePath;
    this.offset = offset;
  }

  /**
   * @return The path to the file.
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @return The offset to where the change goes.
   */
  public int getOffset() {
    return offset;
  }

  /**
   * @return The instruction in the original version of the game.
   */
  public byte[] getOldInstruction() {
    return oldInstruction;
  }

  /**
   * @return The patched instruction.
   */
  public byte[] getNewInstruction() {
    return newInstruction;
  }
}
