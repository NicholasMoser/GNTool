package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup27 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_2700(bs);
      case 0x03 -> op_2703(bs);
      case 0x04 -> op_2704(bs);
      case 0x0A -> op_270A(bs);
      case 0x0C -> op_270C(bs);
      case 0x0E -> op_270E(bs);
      case 0x14 -> op_2714(bs);
      case 0x16 -> op_2716(bs);
      case (byte) 0x80 -> op_2780(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_2700(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_2703(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_2704(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_270A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int opcode = bs.peekWord();
    byte flag = (byte) (opcode >> 8 & 0xff);
    String info = "";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    if (flag > 2) {
      EffectiveAddress ea = EffectiveAddress.get(bs);
      info += String.format(" %s", ea.getDescription());
      baos.write(ea.getBytes());
    }
    byte[] bytes = new byte[8];
    if (bs.read(bytes) != 8) {
      throw new IOException("Failed to read 8 bytes for opcode 0x270A at offset " + bs.offset());
    }
    baos.write(bytes);
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }

  private static Opcode op_270C(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = new byte[4];
    if (bs.read(bytes) != 4) {
      throw new IOException("Failed to read 4 bytes for opcode 0x270C at offset " + bs.offset());
    }
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_270E(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_2714(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_2716(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_2780(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}