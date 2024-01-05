package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class SetText implements Opcode {

  private final static String MNEMONIC = "set_text_p";
  private final int offset;
  private final byte[] textBytes;
  private final String info;

  public SetText(int offset, byte[] textBytes, String info) {
    this.offset = offset;
    this.textBytes = textBytes;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[]{0x31, 0x00, 0x00, 0x00}, textBytes);
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(getBytes()));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s",MNEMONIC,info);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(getBytes()));
  }
}
