package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup47 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_4700(bs);
      case 0x03 -> UnknownOpcode.of(0x47, 0x03, 0x8, bs);
      case 0x04 -> UnknownOpcode.of(0x47, 0x04, 0x10, bs);
      case 0x05 -> UnknownOpcode.of(0x47, 0x05, 0x8, bs);
      case 0x06 -> op_4706(bs);
      case 0x07 -> op_4707(bs);
      case 0x08 -> UnknownOpcode.of(0x47, 0x08, 0xC, bs);
      case 0x0A -> op_470A(bs);
      case 0x0C -> UnknownOpcode.of(0x47, 0x0C, 0x1C, bs);
      case 0x0D -> UnknownOpcode.of(0x47, 0x0D, 0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x47, 0x0E, 0xC, bs);
      case 0x12 -> op_4712(bs);
      case 0x14 -> UnknownOpcode.of(0x47, 0x14, 0xC, bs);
      case 0x16 -> UnknownOpcode.of(0x47, 0x16, 0x8, bs);
      case 0x17 -> UnknownOpcode.of(0x47, 0x17, 0x4, bs);
      case 0x18 -> UnknownOpcode.of(0x47, 0x18, 0x4, bs);
      case 0x19 -> UnknownOpcode.of(0x47, 0x19, 0x8, bs);
      case 0x1A -> UnknownOpcode.of(0x47, 0x1A, 0x8, bs);
      case 0x1B -> UnknownOpcode.of(0x47, 0x1B, 0x8, bs);
      case 0x1C -> UnknownOpcode.of(0x47, 0x1C, 0x8, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4700(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    baos.write(ea.getBytes());
    baos.write(bs.readBytes(0x8));
    int structOffset = bs.readWord();
    String info = String.format(" %s; binary data at offset 0x%X", ea.getDescription(), structOffset);
    baos.write(ByteUtils.fromInt32(structOffset));
    baos.write(bs.readBytes(0x8));
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }

  public static Opcode op_4706(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  public static Opcode op_4707(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  public static Opcode op_470A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  public static Opcode op_4712(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}
