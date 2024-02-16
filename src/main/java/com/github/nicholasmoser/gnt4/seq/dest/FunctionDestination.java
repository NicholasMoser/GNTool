package com.github.nicholasmoser.gnt4.seq.dest;

import com.github.nicholasmoser.utils.ByteUtils;

/**
 * A destination represented as an absolute offset.
 *
 * @param offset The absolute offset.
 */
public record FunctionDestination(int offset, String functionName) implements Destination {
  @Override
  public byte[] bytes() {
    return ByteUtils.fromInt32(offset);
  }

  @Override
  public String toString() {
    return functionName;
  }
}
