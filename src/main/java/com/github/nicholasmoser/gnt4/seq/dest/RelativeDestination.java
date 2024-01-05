package com.github.nicholasmoser.gnt4.seq.dest;

import com.github.nicholasmoser.utils.ByteUtils;

public class RelativeDestination implements Destination {
  private final int relativeOffset;
  private boolean isResolved;
  private int offset;

  public RelativeDestination(int relativeOffset) {
    this.relativeOffset = relativeOffset;
    this.offset = 0xFFFFFFFF;
    this.isResolved = false;
  }

  @Override
  public int offset() {
    return offset;
  }

  @Override
  public byte[] bytes() {
    return ByteUtils.fromInt32(offset);
  }

  @Override
  public String toString() {
    if (isResolved) {
      return String.format("0x%X", offset);
    }
    if (relativeOffset < 0) {
      return String.format("-0x%X", Math.abs(relativeOffset));
    }
    return String.format("+0x%X", relativeOffset);
  }

  public void resolve(int branchStart) {
    offset = branchStart + relativeOffset;
    isResolved = true;
  }
}
