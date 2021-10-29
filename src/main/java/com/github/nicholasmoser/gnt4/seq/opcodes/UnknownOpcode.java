package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;
import java.io.IOException;

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

  public static UnknownOpcode of(int byteOne, int byteTwo, int bytesLength, ByteStream bs) throws IOException {
    return of(byteOne, byteTwo, bytesLength, bs, "");
  }

  public static UnknownOpcode of(int byteOne, int byteTwo, int bytesLength, ByteStream bs, String info) throws IOException {
    byte[] bytes = new byte[bytesLength];
    if (bs.read(bytes) != bytesLength) {
      throw new IOException("Failed to read bytes for opcode");
    }
    bytes[0] = (byte) byteOne;
    bytes[1] = (byte) byteTwo;
    return new UnknownOpcode(bs.offset() - bytesLength, bytes, info);
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
    return String.format("%05X | op_%02X%02X %s%s", offset, bytes[0], bytes[1], formatRawBytes(bytes), info);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id)).withText(toString());
  }
}
