package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TskSendMsg implements Opcode {

  private final static String MNEMONIC = "TskSendMsg";
  private final int offset;
  private final byte[] bytes;
  private final String info;

  public TskSendMsg(int offset, byte[] bytes, String info) throws IOException {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
  }

  public TskSendMsg(long task) throws IOException {
    this.offset = 0;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(new byte[]{(byte) 0x02, (byte) 0x06, (byte) 0x0, (byte) 0x3F});
    baos.write(ByteUtils.fromUint32(task));
    this.bytes = baos.toByteArray();
    this.info = String.format("0x%X", task);
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
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s",MNEMONIC,info);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(bytes));
  }
}
