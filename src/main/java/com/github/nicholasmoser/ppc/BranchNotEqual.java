package com.github.nicholasmoser.ppc;

import com.github.nicholasmoser.utils.ByteUtils;

public class BranchNotEqual {
  /**
   * Gets the branch if not equal instruction bytes for moving from one address to another address.
   *
   * @param from The from address.
   * @param to   The to address.
   * @return The branch if not equal instruction bytes.
   */
  public static byte[] getBytes(long from, long to) {
    if (from % 4 != 0) {
      throw new IllegalArgumentException("from address must be multiple of 4: " + from);
    } else if (to % 4 != 0) {
      throw new IllegalArgumentException("to address must be multiple of 4: " + to);
    }
    int byteDistance = (int) (to - from);
    return getBytes(byteDistance);
  }

  /**
   * Gets the branch if not equal instruction bytes for moving the given distance of instructions.
   *
   * @param distance The number of instructions to move forwards or backwards. Each instruction is
   *                 four bytes long. A positive distance moves forward, negative backwards.
   * @return The branch if not equal instruction bytes.
   */
  public static byte[] getBytes(int distance) {
    if (distance > 0x7FFCL) {
      throw new IllegalArgumentException("Branch cannot go over 0x7FFC: " + distance);
    } else if (distance < -0x8000L) {
      throw new IllegalArgumentException("Branch cannot go under -0x8000: " + distance);
    } else if (distance % 4 != 0) {
      throw new IllegalArgumentException("Branch distance must be a multiple of 4: " + distance);
    }
    int bits = 0x4082;
    // Branch bytes start with hex 0x4082
    bits <<= 0x10;
    distance &= 0xA0FFFC;
    bits |= distance;
    return ByteUtils.fromInt32(bits);
  }
}
