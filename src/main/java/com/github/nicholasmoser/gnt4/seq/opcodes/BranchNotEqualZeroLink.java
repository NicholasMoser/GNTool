package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchNotEqualZeroLink implements Opcode, BranchingOpcode {

  private final static String MNEMONIC = "bnezal";
  private final int offset;
  private final int destination;
  private String destFuncName;

  public BranchNotEqualZeroLink(int offset, int destination) {
    this.offset = offset;
    this.destination = destination;
  }

  @Override
  public int getDestination() {
    return destination;
  }

  @Override
  public void setDestinationFunctionName(String destFuncName) {
    this.destFuncName = destFuncName;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x3E, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {013E0000 %08X}", offset, MNEMONIC, destination, destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String destName = destFuncName != null ? destFuncName : String.format("0x%X", destination);
    String destHref = String.format("#%X", destination);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(a(destName).withHref(destHref))
        .withText(" ")
        .with(formatRawBytesHTML(getBytes()));
  }
}
