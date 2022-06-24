package com.github.nicholasmoser.gnt4.seq.opcodes;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class FileName implements Opcode {

  private final static String MNEMONIC = "filename";
  private final int offset;
  private final byte[] bytes;
  private final String filename;

  public FileName(int offset, String filename) {
    this.offset = offset;
    this.filename = filename;
    this.bytes = filename.getBytes();
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
    return bytes;
  }

  @Override
  public String toString() {
    return String.format("%05X | %s \"%s\" ", offset, MNEMONIC, filename);
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%X //%s", MNEMONIC, offset, filename);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id)).withText(toString()).with(a()).with(formatRawBytesHTML(bytes));
  }
}
