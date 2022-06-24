package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class CreateHitbox implements Opcode {

  private final static String MNEMONIC = "create_hitbox";
  private final int offset;
  private final byte[] bytes;
  private final short boneId;
  private final short size;
  private final String info;

  public CreateHitbox(int offset, byte[] bytes, short boneId, short size, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.boneId = boneId;
    this.size = size;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromUint16(boneId), ByteUtils.fromUint16(size),
        new byte[]{0x0, 0x0, 0x0, 0x0});
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(getBytes()));
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%04X, 0x%04X", MNEMONIC, boneId, size);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (Bone ID: 0x%X, Size: 0x%X) ", offset, MNEMONIC, info,
            boneId, size))
        .with(formatRawBytesHTML(getBytes()));
  }
}
