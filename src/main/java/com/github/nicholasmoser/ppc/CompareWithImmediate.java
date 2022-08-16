package com.github.nicholasmoser.ppc;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

public class CompareWithImmediate {

  /**
   * Gets the compare with immediate instruction bytes.
   *
   * @param register  The register to compare with.
   * @param immediate The immediate to compare with.
   * @return The compare with immediate instruction bytes.
   */
  public static byte[] getBytes(int register, int immediate) {
    if (immediate > 0x7FFF) {
      throw new IllegalArgumentException("Immediate cannot go over 0x7FFC: " + immediate);
    } else if (immediate < -0x8000) {
      throw new IllegalArgumentException("Immediate cannot go under -0x8000: " + immediate);
    } else if (register < 0) {
      throw new IllegalArgumentException("Register cannot go under 0: " + register);
    } else if (register > 31) {
      throw new IllegalArgumentException("Register cannot go over 31: " + register);
    }
    byte[] bytes = new byte[2];
    bytes[0] = (byte) 0x2C;
    bytes[1] = (byte) register;
    return Bytes.concat(bytes, ByteUtils.fromUint16(immediate));
  }
}
