package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class BranchLinkReturnNotEqualZero implements Opcode {

  private final static String MNEMONIC = "blrnez";
  private final int offset;

  public BranchLinkReturnNotEqualZero(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[] { 0x01, 0x47, 0x00, 0x00 };
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s {01470000}", offset, MNEMONIC);
  }

  @Override
  public String toAssembly() {
    return String.format("%s",MNEMONIC);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(formatRawBytesHTML(getBytes()));
  }
}
