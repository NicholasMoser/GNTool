package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import java.util.Arrays;

public class SynchronousTimer implements Opcode {

  private final static String MNEMONIC = "sync_timer";
  private final int offset;
  private final byte[] bytes;
  private final String info;

  public SynchronousTimer(int offset, byte[] bytes, String info) {
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
    int frames = ByteUtils.toInt32(Arrays.copyOfRange(bytes, 4, 8));
    return String.format("%05X | %s (%d frames) %s %s", offset, MNEMONIC, frames, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s",MNEMONIC,info);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    int frames = ByteUtils.toInt32(Arrays.copyOfRange(bytes, 4, 8));
    return div(attrs(id))
        .withText(String.format("%05X | %s (%d frames) %s ", offset, MNEMONIC, frames, info))
        .with(formatRawBytesHTML(bytes));
  }
}
