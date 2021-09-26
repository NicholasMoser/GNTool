package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h2;

import j2html.tags.ContainerTag;

public class SectionTitle implements Opcode {

  private final int offset;
  private final byte[] bytes;
  private final String title;

  public SectionTitle(int offset, byte[] bytes, String title) {
    this.offset = offset;
    this.bytes = bytes;
    this.title = title;
  }

  public String getTitle() {
    return title;
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
    return String.format("%05X | Section Title: %s %s", offset, title, formatRawBytes(bytes));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return h2(attrs(id)).withText(toString());
  }
}
