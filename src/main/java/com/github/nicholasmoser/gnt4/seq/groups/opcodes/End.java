package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

public class End implements Opcode {

  private final int offset;

  public End(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[4];
  }

  @Override
  public String toString() {
    return String.format("%05X | end {00000000}", offset);
  }
}
