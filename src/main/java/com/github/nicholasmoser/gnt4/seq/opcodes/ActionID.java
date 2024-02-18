package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;

public class ActionID implements Opcode {

  private final static String MNEMONIC = "Action ID";
  private final int offset;
  private final byte[] bytes;
  private final int actionId;
  private final int actionOffset;
  private String info;

  public enum Type {
    NORMAL,
    RESET,
    UNUSED
  }

  public ActionID(int offset, byte[] bytes, int actionId, Type type) {
    this.offset = offset;
    this.bytes = bytes;
    this.actionId = actionId;
    this.actionOffset = ByteUtils.toInt32(bytes);
    info = " " + Seq.getActionDescription(actionId);
    if (type == Type.RESET || type == Type.UNUSED) {
      // not really worth differentiating these, making RESET and UNUSED the same helps readability
      info += String.format(" (unused)", actionId);
    }
  }

  public String getInfo() {
    return info;
  }

  public int getActionId() {
    return actionId;
  }

  public int getActionOffset() {
    return actionOffset;
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
    return String.format("%05X | %s 0x%X at offset 0x%X %s%s", offset, MNEMONIC, actionId, actionOffset, formatRawBytes(bytes), info);
  }

  @Override
  public String toAssembly() {
    return String.format("%s",MNEMONIC);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", actionOffset);
    return div(attrs(id))
        .withText(String.format("%05X | Action ID 0x%X at offset ", offset, actionId))
        .with(a(String.format("0x%X", actionOffset)).withHref(dest))
        .withText(" ")
        .with(formatRawBytesHTML(bytes))
        .withText(String.format(" %s", info));
  }
}
