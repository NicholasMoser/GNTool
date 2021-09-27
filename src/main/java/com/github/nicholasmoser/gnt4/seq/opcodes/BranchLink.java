package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchLink implements Opcode {

  private final int offset;
  private final int destination;

  public BranchLink(int offset, int destination) {
    this.offset = offset;
    this.destination = destination;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x3C, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public String toString() {
    return String.format("%05X | bl 0x%X {013C0000 %08X}", offset, destination, destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", destination);
    return div(attrs(id))
        .with(span(String.format("%05X | bl ", offset)).attr("class=\"bl\""))
        .with(a(String.format("0x%X", destination)).withHref(dest))
        .with(span(String.format(" {013C0000 %08X}", destination)));
  }
}
