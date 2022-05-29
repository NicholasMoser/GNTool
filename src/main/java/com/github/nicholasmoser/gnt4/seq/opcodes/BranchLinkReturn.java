package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class BranchLinkReturn implements Opcode {

  private final static String MNEMONIC = "blr";
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
    return String.format("%05X | %s {01450000}", offset, MNEMONIC);
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
