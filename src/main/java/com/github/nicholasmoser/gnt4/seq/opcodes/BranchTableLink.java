package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;
import java.util.List;

public class BranchTableLink implements Opcode {

  private final static String MNEMONIC = "branch_table_link";
  private final int offset;
  private final byte[] bytes;
  private final String info;
  private final List<Integer> offsets;

  public BranchTableLink(int offset, byte[] bytes, String info, List<Integer> offsets) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
    this.offsets = offsets;
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
    return String.format("%05X | %s %s %s (%d branches)", offset, offsets.size(), MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    DivTag div =  div(attrs(id))
        .withText(String.format("%05X | %s %s (%d branches):", offset, MNEMONIC, info, offsets.size()));
    for (int offset : offsets) {
      String dest = String.format("#%X", offset);
      div.withText(" ");
      div.with(a(String.format("0x%X", offset)).withHref(dest));
    }
    div.withText(" ");
    return div.with(formatRawBytesHTML(getBytes()));
  }
}
