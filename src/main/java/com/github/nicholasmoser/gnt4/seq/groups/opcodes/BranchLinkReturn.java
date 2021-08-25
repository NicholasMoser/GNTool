package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

public class BranchLinkReturn implements Opcode {

  private final int offset;

  public BranchLinkReturn(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[] { 0x01, 0x45, 0x00, 0x00 };
  }

  @Override
  public String toString() {
    return String.format("%05X | blr {01450000}", offset);
  }
}
