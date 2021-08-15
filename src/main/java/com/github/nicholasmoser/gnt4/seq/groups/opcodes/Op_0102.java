package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import com.google.common.primitives.Bytes;

public class Op_0102 implements Opcode {

  private final int offset;
  private final byte[] bytes;

  public Op_0102(int offset, byte[] bytes) {
    this.offset = offset;
    this.bytes = bytes;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes);
  }

  @Override
  public String toString() {
    return String.format("%05X | op_0102 %s", offset, formatRawBytes(bytes));
  }
}
