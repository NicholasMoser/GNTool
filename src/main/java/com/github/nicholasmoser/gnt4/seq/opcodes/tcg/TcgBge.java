package com.github.nicholasmoser.gnt4.seq.opcodes.tcg;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import j2html.tags.ContainerTag;

public class TcgBge implements Opcode {

  private final static String MNEMONIC = "tcg_bge";
  private final int offset;
  private final byte[] bytes;
  private final String info;
  private final int destination;

  public TcgBge(int offset, byte[] bytes, String info, int destination) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
    this.destination = destination;
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
    String destName = String.format("0x%X", destination);
    String destOffset = String.format("#%X", destination);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(a(destName).withHref(destOffset))
        .withText(String.format(" %s ", info))
        .with(formatRawBytesHTML(bytes));
  }
}
