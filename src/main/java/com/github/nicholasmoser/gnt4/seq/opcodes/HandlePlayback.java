package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import j2html.tags.ContainerTag;

public class HandlePlayback implements Opcode {

  private final static String MNEMONIC = "handle_playback";
  private final int offset;
  private final boolean updatePC;

  public HandlePlayback(int offset, boolean updatePC) {
    this.offset = offset;
    this.updatePC = updatePC;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    byte option = (byte) (updatePC ? 0x00 : 0x01);
    return new byte[]{0x26, (byte) 0xE5, 0x00, option};
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s {00010000}", offset, MNEMONIC);
  }

  @Override
  public String toAssembly() {
    String info = updatePC ? "(update pc)" : "(update cr)";
    return String.format("%s %s", MNEMONIC, info);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String info = updatePC ? "(update pc)" : "(update cr)";
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(getBytes()));
  }
}
