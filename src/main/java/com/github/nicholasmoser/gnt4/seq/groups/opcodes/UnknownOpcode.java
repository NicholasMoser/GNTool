package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
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
    int opcode = bytes[0] << 8;
    opcode |= bytes[1];
    return String.format("%05X | op_%04X %s%s", offset, opcode, formatRawBytes(bytes), info);
  }
}
