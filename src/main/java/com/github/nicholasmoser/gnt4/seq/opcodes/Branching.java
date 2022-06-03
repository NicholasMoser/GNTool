package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class Branching implements Opcode {

  private final static String MNEMONIC = "";
  private final int offset;
  private int destination;
  private final byte[] bytes;

  public String getLabel() {
    return label;
  }

  private final String label;

  /*
  public Branching(byte[] bytes, int destination) {
    this.offset = 0;
    this.destination = destination;
    this.bytes = bytes;
    this.label = "";
  }*/

  public Branching(byte[] bytes, String label) {
    this.offset = 0;
    this.destination = 0;
    this.bytes = bytes;
    this.label = label;
  }

  public void setDestination(int destination) {
    this.destination = destination;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromInt32(destination));
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    System.err.println(String.format("Offset: %d\nDestination: %d\nSize: %d", offset, destination, size));
    if (destination < size) {
      return Bytes.concat(bytes, ByteUtils.fromInt32(destination + offset));
    }
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {01320000 %08X}", offset, MNEMONIC, destination, destination);
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%X",MNEMONIC,destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", destination);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(a(String.format("0x%X", destination)).withHref(dest))
        .withText(" ")
        .with(formatRawBytesHTML(getBytes()));
  }
}
