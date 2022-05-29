package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class PointerTableLookup implements Opcode {

  private final static String MNEMONIC = "ptr_table_lookup";
  private final int offset;
  private final byte[] bytes;
  private final int tableOffset;
  private final String info;

  public PointerTableLookup(int offset, byte[] bytes, int tableOffset, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.tableOffset = tableOffset;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() { return bytes; }

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
    String dest = String.format("#%X", tableOffset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (table at ", offset, MNEMONIC, info))
        .with(a(String.format("0x%X", tableOffset)).withHref(dest))
        .withText(") ")
        .with(formatRawBytesHTML(getBytes()));
  }
}
