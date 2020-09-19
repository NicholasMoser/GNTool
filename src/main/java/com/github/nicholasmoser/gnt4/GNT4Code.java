package com.github.nicholasmoser.gnt4;

import java.util.Arrays;
import java.util.Objects;

/**
 * An immutable POJO for a GNT4Code. If it is a code change it will have both an old instruction
 * and a new instruction.
 */
public class GNT4Code {
  private final String filePath;
  private final int offset;
  private final int length;
  private byte[] oldInstruction;
  private byte[] newInstruction;

  /**
   * Create a new GNT4Code for a data value change.
   * @param filePath The path to the file.
   * @param offset The offset to where the change goes.
   */
  public GNT4Code(String filePath, int offset) {
    this.filePath = filePath;
    this.offset = offset;
    this.length = 4;
  }

  /**
   * Create a new GNT4Code for a data value change with the given length of bytes.
   * @param filePath The path to the file.
   * @param offset The offset to where the change goes.
   */
  public GNT4Code(String filePath, int offset, int length) {
    this.filePath = filePath;
    this.offset = offset;
    this.length = length;
  }

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
    this.length = newInstruction.length;
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

  /**
   * @return The number of bytes for the code.
   */
  public int getLength() { return length; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GNT4Code gnt4Code = (GNT4Code) o;
    return offset == gnt4Code.offset &&
        Objects.equals(filePath, gnt4Code.filePath) &&
        Arrays.equals(oldInstruction, gnt4Code.oldInstruction) &&
        Arrays.equals(newInstruction, gnt4Code.newInstruction);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(filePath, offset);
    result = 31 * result + Arrays.hashCode(oldInstruction);
    result = 31 * result + Arrays.hashCode(newInstruction);
    return result;
  }
}
