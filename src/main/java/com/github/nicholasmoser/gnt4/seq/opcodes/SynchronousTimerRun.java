package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import java.util.Arrays;

public class SynchronousTimerRun implements Opcode {

  private final static String MNEMONIC = "sync_timer_run";
  private final int offset;
  private final byte[] bytes;
  private final String info;

  public SynchronousTimerRun(int offset, byte[] bytes, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
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
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s",MNEMONIC);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(bytes));
  }
}
