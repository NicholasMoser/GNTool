package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class Nop implements Opcode {

  private final static String MNEMONIC = "nop";
  private final int offset;
  private final byte[] bytes;
  private final String type;

  public Nop(int offset, byte[] bytes) {
    this.offset = offset;
    this.bytes = bytes;
    this.type = String.format("%02X%02X", bytes[0], bytes[1]);
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
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s_%s %s", offset, MNEMONIC, type, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s_%s",MNEMONIC,type);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s_%s ", offset, MNEMONIC, type))
        .with(formatRawBytesHTML(bytes));
  }
}
