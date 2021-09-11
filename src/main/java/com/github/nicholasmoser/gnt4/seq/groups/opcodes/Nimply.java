package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

public class Nimply implements Opcode {

  private final int offset;
  private final byte[] bytes;
  private final String info;

  public Nimply(int offset, byte[] bytes, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public String toString() {
    return String.format("%05X | nimply %s %s", offset, formatRawBytes(bytes), info);
  }
}
