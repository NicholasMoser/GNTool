package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class SetHitboxTimer implements Opcode {

  private final static String MNEMONIC = "set_hitbox_timer";
  private final int offset;
  private final byte[] bytes;
  private final short startFrame;
  private final short endFrame;
  private final String info;

  public SetHitboxTimer(int offset, byte[] bytes, short startFrame, short endFrame, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.startFrame = startFrame;
    this.endFrame = endFrame;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromUint16(startFrame), ByteUtils.fromUint16(endFrame));
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(getBytes()));
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%04X, 0x%04X",MNEMONIC,startFrame,endFrame);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String syncTimerAfterHitbox = bytes[2] == 0 ? "Start of action" : "Neutral";
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (Frames 0x%X - 0x%X, %s) ", offset, MNEMONIC, info,
            startFrame, endFrame, syncTimerAfterHitbox))
        .with(formatRawBytesHTML(getBytes()));
  }
}
