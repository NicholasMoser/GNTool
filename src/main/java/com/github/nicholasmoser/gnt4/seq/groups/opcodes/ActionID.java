package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;

public class ActionID implements Opcode {

  private final int offset;
  private final byte[] bytes;
  private final int actionId;
  private final int actionOffset;

  public ActionID(int offset, byte[] bytes, int actionId) {
    this.offset = offset;
    this.bytes = bytes;
    this.actionId = actionId;
    this.actionOffset = ByteUtils.toInt32(bytes);
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
    return String.format("%05X | Action ID 0x%X at offset 0x%X %s", offset, actionId, actionOffset, formatRawBytes(bytes));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id)).withText(toString());
  }
}
