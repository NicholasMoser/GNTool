package com.github.nicholasmoser.ppc;

import com.github.nicholasmoser.utils.ByteUtils;

public class Branch {

  /**
   * Gets the branch instruction bytes for moving from one address to another address.
   *
   * @param from The from address.
   * @param to   The to address.
   * @return The branch instruction bytes.
   */
  public static byte[] getBranchInstruction(long from, long to) {
    if (from % 4 != 0) {
      throw new IllegalArgumentException("from address must be multiple of 4: " + from);
    } else if (to % 4 != 0) {
      throw new IllegalArgumentException("to address must be multiple of 4: " + to);
    }
    int byteDistance = (int) (to - from);
    return getBranchInstruction(byteDistance);
  }

  /**
   * Gets the branch instruction bytes for moving the given distance of instructions.
   *
   * @param distance The number of instructions to move forwards or backwards. Each instruction is
   *                 four bytes long. A positive distance moves forward, negative backwards.
   * @return The branch instruction bytes.
   */
  public static byte[] getBranchInstruction(int distance) {
    if (distance > 0x1FFFFFC) {
      throw new IllegalArgumentException("Branch cannot go over 0x1FFFFFC: " + distance);
    } else if (distance < -0x2000000) {
      throw new IllegalArgumentException("Branch cannot go under -0x2000000: " + distance);
    } else if (distance % 4 != 0) {
      throw new IllegalArgumentException("Branch distance must be a multiple of 4: " + distance);
    }
    int bits = 18;
    // Branch bytes start with binary 010010
    bits <<= 26;
    // Zero out top 6 bits and bottom 2 bits from distance
    // The bottom 2 bits should already be zero since distance is a multiple of four
    distance &= 0x3FFFFFC;
    bits |= distance;
    return ByteUtils.fromInt32(bits);
  }
}
