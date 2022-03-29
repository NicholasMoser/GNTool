package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchLessThanOrEqualToZero implements Opcode {

  private final static String MNEMONIC = "blez";
  private final int offset;
  private final int destination;

  public BranchLessThanOrEqualToZero(int offset, int destination) {
    this.offset = offset;
    this.destination = destination;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x38, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {01380000 %08X}", offset, MNEMONIC, destination, destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", destination);
    return div(attrs(id))
        .with(span(String.format("%05X | blez ", offset)))
        .with(a(String.format("0x%X", destination)).withHref(dest))
        .with(span(String.format(" {01380000 %08X}", destination)));
  }
}
