package com.github.nicholasmoser.gnt4.seq.dest;

import com.github.nicholasmoser.utils.ByteUtils;

/**
 * A destination represented as an absolute offset.
 *
 * @param offset The absolute offset.
 */
public record AbsoluteDestination(int offset) implements Destination {
  @Override
  public byte[] bytes() {
    return ByteUtils.fromInt32(offset);
  }

  @Override
  public String toString() {
    return String.format("0x%X", offset);
  }
}
