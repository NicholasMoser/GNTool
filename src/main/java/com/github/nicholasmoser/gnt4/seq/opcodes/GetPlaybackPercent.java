package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import j2html.tags.ContainerTag;

public class GetPlaybackPercent implements Opcode {

  private final static String MNEMONIC = "get_playback_percent";
  private final int offset;

  public GetPlaybackPercent(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[]{0x26, (byte) 0xEA, 0x00, 0x01};
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s {26E90001}", offset, MNEMONIC);
  }

  @Override
  public String toAssembly() {
    return String.format("%s",MNEMONIC);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s ", offset, MNEMONIC))
        .with(span("26EA0001").withClass("g"));
  }
}
