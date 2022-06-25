package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import j2html.tags.ContainerTag;

public class FinishRecording implements Opcode {

  private final static String MNEMONIC = "finish_recording";
  private final int offset;

  public FinishRecording(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return new byte[]{0x26, (byte) 0xE9, 0x00, 0x01};
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
        .with(span("26E90001").withClass("g"));
  }
}
