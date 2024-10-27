package com.github.nicholasmoser.gnt4.seq.opcodes.tcg;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import j2html.tags.ContainerTag;

public class TcgBranchingOpcode implements Opcode {

  private final String mnemonic;
  private final byte[] bytes;
  private final int offset;
  private final String info;
  private Destination destination;

public TcgBranchingOpcode(String mnemonic, byte[] bytes, int offset, String info, Destination destination) {
    this.mnemonic = mnemonic;
    this.bytes = bytes;
    this.offset = offset;
    this.info = info;
    this.destination = destination;
  }

  public String getMnemonic() {
    return mnemonic;
  }

  public Destination getDestination() {
    return destination;
  }

  public void setDestination(Destination destination) {
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
    return String.format("%05X | %s %s %s", offset, mnemonic, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s %s", mnemonic, destination, info);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String destName = destination.toString();
    String destOffset = String.format("#%X", destination.offset());
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, mnemonic))
        .with(a(destName).withHref(destOffset))
        .withText(String.format(" %s ", info))
        .with(formatRawBytesHTML(bytes));
  }
}
