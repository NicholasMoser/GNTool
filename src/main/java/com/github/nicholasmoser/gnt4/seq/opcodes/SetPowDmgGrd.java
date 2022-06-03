package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class SetPowDmgGrd implements Opcode {

  private final static String MNEMONIC = "set_pow_dmg_grd";
  private final int offset;
  private final byte[] bytes;
  private final short pow;
  private final short dmg;
  private final short grd;
  private final String info;

  public SetPowDmgGrd(int offset, byte[] bytes, short pow, short dmg, short grd, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.pow = pow;
    this.dmg = dmg;
    this.grd = grd;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(bytes, ByteUtils.fromUint16(pow), ByteUtils.fromUint16(dmg),
        ByteUtils.fromUint16(grd), new byte[]{0x0, 0x0});
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
    return String.format("%s 0x%04X, 0x%04X, 0x%04X", MNEMONIC, pow, dmg, grd);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (POW: 0x%X, DMG: 0x%X, GRD: 0x%X) ", offset, MNEMONIC, info,
            pow, dmg, grd))
        .with(formatRawBytesHTML(getBytes()));
  }
}
