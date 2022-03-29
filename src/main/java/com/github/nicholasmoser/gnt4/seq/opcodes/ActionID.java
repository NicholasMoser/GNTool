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

  public ActionID(int offset, byte[] bytes, int actionId, boolean unused) {
    this.offset = offset;
    this.bytes = bytes;
    this.actionId = actionId;
    this.actionOffset = ByteUtils.toInt32(bytes);
    switch (actionId) {
      case Seq.ACTION_5B -> this.info = " 5B";
      case Seq.ACTION_6B -> this.info = " 6B";
      case Seq.ACTION_4B -> this.info = " 4B";
      case Seq.ACTION_2B -> this.info = " 2B";
      case Seq.ACTION_RB -> this.info = " RB";
      case Seq.ACTION_5A -> this.info = " 5A";
      case Seq.ACTION_6A -> this.info = " 6A";
      case Seq.ACTION_4A -> this.info = " 4A";
      case Seq.ACTION_2A -> this.info = " 2A";
      case Seq.ACTION_RA -> this.info = " RA";
      case Seq.ACTION_RKNJ_GROUND -> this.info = " RKNJ Ground";
      case Seq.ACTION_RKNJ_AIR -> this.info = " RKNJ Air";
      case Seq.ACTION_LKNJ -> this.info = " LKNJ";
      case Seq.ACTION_ZKNJ_INCOMING -> this.info = " ZKNJ Incoming";
      case Seq.ACTION_5Z_OUTGOING -> this.info = " 5Z Outgoing";
      case Seq.ACTION_4Z_INCOMING -> this.info = " 4Z Incoming";
      case Seq.ACTION_5Z_INCOMING -> this.info = " 5Z Incoming";
      case Seq.ACTION_ZKNJ_OUTGOING -> this.info = " ZKNJ Outgoing";
      case Seq.ACTION_4Z_OUTGOING -> this.info = " 4Z Outgoing";
      case Seq.ACTION_JB -> this.info = " JB";
      case Seq.ACTION_JA -> this.info = " JA";
      case Seq.ACTION_8B -> this.info = " 8B";
      case Seq.ACTION_8A -> this.info = " 8A";
      case Seq.ACTION_5X -> this.info = " 5X";
      case Seq.ACTION_2X -> this.info = " 2X";
      case Seq.ACTION_COMBO_START -> this.info = " Combo start";
      case Seq.ACTION_GROUND_THROW -> this.info = " Ground throw";
      case Seq.ACTION_BACK_GROUND_THROW -> this.info = " Back ground throw";
      case Seq.ACTION_AIR_THROW -> this.info = " Air throw";
      case Seq.ACTION_ACTIVATED_X -> this.info = " Activated X";
      default -> this.info = "";
    }
    if (unused) {
      info += " (UNUSED)";
    }
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
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", actionOffset);
    return div(attrs(id))
        .with(span(String.format("%05X | Action ID 0x%X at offset ", offset, actionId)))
        .with(a(String.format("0x%X", actionOffset)).withHref(dest))
        .with(span(String.format(" %s%s", formatRawBytes(bytes), info)));
  }
}
