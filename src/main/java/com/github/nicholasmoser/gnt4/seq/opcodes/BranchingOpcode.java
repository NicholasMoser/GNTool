package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;
import java.util.Map;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class BranchingOpcode implements Opcode {

  private final String mnemonic;
  private final byte[] bytes;
  private final int offset;
  private final Destination destination;

public BranchingOpcode(String mnemonic, byte[] bytes, int offset, Destination destination) {
    this.mnemonic = mnemonic;
    this.bytes = bytes;
    this.offset = offset;
    this.destination = destination;
  }

  public String getMnemonic() {
    return mnemonic;
  }

  public Destination getDestination() {
    return destination;
  }

  public void resolveDestination(int position, Map<String, Integer> labels) {
    // TODO
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromInt32(destination.offset()));
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s {%s %08X}", offset, mnemonic, destination, ByteUtils.bytesToHexString(bytes), destination.bytes());
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s", mnemonic, destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = destination.toString();
    return div(attrs(id))
            .withText(String.format("%05X | %s ", offset, mnemonic))
            .with(a(dest).withHref(dest))
            .withText(" ")
            .with(formatRawBytesHTML(getBytes()));
  }

}
