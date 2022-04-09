package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import j2html.tags.ContainerTag;

public class HardReset implements Opcode {

  private final static String MNEMONIC = "hard_reset";
  private final int offset;

  public HardReset(int offset) {
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
    return String.format("%05X | %s {00010000}", offset, MNEMONIC);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(span("00010000").withClass("g"));
  }
}
