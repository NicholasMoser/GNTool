package com.github.nicholasmoser.gnt4.seq.opcodes.tcg;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import j2html.tags.ContainerTag;

public class TcgBlt implements Opcode {

  private final static String MNEMONIC = "tcg_blt";
  private final int offset;
  private final byte[] bytes;
  private final String info;

  public TcgBlt(int offset, byte[] bytes, String info) {
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
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s",MNEMONIC,info);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(bytes));
  }
}
