package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class BinaryData implements Opcode {

  private final int offset;
  private final byte[] bytes;

  public BinaryData(int offset, byte[] bytes) {
    this.offset = offset;
    this.bytes = bytes;
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
    return String.format("%05X | binary data, 0x%x bytes %s", offset, bytes.length, formatRawBytes(bytes));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id)).withText(toString());
  }
}
