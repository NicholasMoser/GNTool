package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;
import java.io.IOException;
import java.nio.ByteBuffer;

public class UnknownOpcode implements Opcode {

  private final int offset;
  private final byte[] bytes;
  private final String info;

  public UnknownOpcode(int offset, byte[] bytes) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = "";
  }

  public UnknownOpcode(int offset, byte[] bytes, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
  }

  public static UnknownOpcode of(int bytesLength, ByteStream bs) throws IOException {
    return of(bytesLength, bs, "");
  }

  public static UnknownOpcode of(int bytesLength, ByteStream bs, String info) throws IOException {
    byte[] bytes = new byte[bytesLength];
    if (bs.read(bytes) != bytesLength) {
      throw new IOException("Failed to read bytes for opcode");
    }
    return new UnknownOpcode(bs.offset() - bytesLength, bytes, info);
  }

  public static byte[] of(String opcode, String operands) {
    String[] op = operands.split(",");
    int len = (op.length-1)*4;
    ByteBuffer bytes = ByteBuffer.allocate(len);
    bytes.putShort(Short.parseShort(opcode,16));
    bytes.put(Short.decode(op[0]).byteValue());
    bytes.put(Short.decode(op[1]).byteValue());

    for (int i = 2; i < op.length; i++){
        bytes.putInt(Long.decode(op[i]).intValue());
    }
    return bytes.array();
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
    return String.format("%05X | op_%02X%02X %s %s", offset, bytes[0], bytes[1], info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("op_%02X%02X 0x%02X, 0x%02X", bytes[0], bytes[1], bytes[2], bytes[3]));
    for (int i = 4; i < bytes.length; i = i+4) {
      builder.append(String.format(", 0x%02X%02X%02X%02X", bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]));
    }
    return builder.toString();
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | op_%02X%02X %s ", offset, bytes[0], bytes[1], info))
        .with(formatRawBytesHTML(bytes));
  }
}
