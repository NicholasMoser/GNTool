package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

public class BranchAndLink implements Opcode {

  private final int offset;
  private final int destination;

  public BranchAndLink(int offset, int destination) {
    this.offset = offset;
    this.destination = destination;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x3C, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public String toString() {
    return String.format("%05X | bl 0x%X {013C0000 %08X}", offset, destination, destination);
  }
}
