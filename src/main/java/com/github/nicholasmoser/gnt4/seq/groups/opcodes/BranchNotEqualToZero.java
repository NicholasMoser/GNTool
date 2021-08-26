package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

public class BranchNotEqualToZero implements Opcode {

  private final int offset;
  private final int destination;

  public BranchNotEqualToZero(int offset, int destination) {
    this.offset = offset;
    this.destination = destination;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x34, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public String toString() {
    return String.format("%05X | bnez 0x%X {01330000 %08X}", offset, destination, destination);
  }
}
