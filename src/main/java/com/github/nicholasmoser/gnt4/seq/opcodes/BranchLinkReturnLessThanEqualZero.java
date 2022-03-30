package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class BranchLinkReturnLessThanEqualZero implements Opcode {

  private final static String MNEMONIC = "blrlez";
  private final int offset;

  public BranchLinkReturnLessThanEqualZero(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[] { 0x01, 0x4B, 0x00, 0x00 };
  }

  @Override
  public String toString() {
    return String.format("%05X | %s {014B0000}", offset, MNEMONIC);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(formatRawBytesHTML(getBytes()));
  }
}
