package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class SetAngDir implements Opcode {

  private final static String MNEMONIC = "set_ang_dir";
  private final int offset;
  private final byte[] bytes;
  private final short ang;
  private final short dir;
  private final String info;

  public SetAngDir(int offset, byte[] bytes, short ang, short dir, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.ang = ang;
    this.dir = dir;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromUint16(ang), ByteUtils.fromUint16(dir));
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(getBytes()));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (ANG: 0x%X, DIR: 0x%X) ", offset, MNEMONIC, info,
            ang, dir))
        .with(formatRawBytesHTML(getBytes()));
  }
}
