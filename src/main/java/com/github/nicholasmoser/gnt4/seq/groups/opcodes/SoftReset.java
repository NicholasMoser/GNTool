package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class SoftReset implements Opcode {

  private final int offset;

  public SoftReset(int offset) {
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
    return String.format("%05X | soft_reset {00000000}", offset);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id)).withText(toString());
  }
}
