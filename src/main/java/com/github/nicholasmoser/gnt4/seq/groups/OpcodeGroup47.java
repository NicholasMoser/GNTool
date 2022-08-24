package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SpawnProjectile;
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
      case 0x03 -> UnknownOpcode.of(0x8, bs);
      case 0x04 -> UnknownOpcode.of(0x10, bs);
      case 0x05 -> UnknownOpcode.of(0x8, bs);
      case 0x06 -> op_4706(bs);
      case 0x07 -> op_4707(bs);
      case 0x08 -> UnknownOpcode.of(0xC, bs);
      case 0x09 -> UnknownOpcode.of(0xC, bs);
      case 0x0A -> op_470A(bs);
      case 0x0C -> UnknownOpcode.of(0x1C, bs);
      case 0x0D -> UnknownOpcode.of(0x8, bs);
      case 0x0E -> UnknownOpcode.of(0xC, bs);
      case 0x10 -> UnknownOpcode.of(0x10, bs);
      case 0x11 -> UnknownOpcode.of(0x14, bs);
      case 0x12 -> op_4712(bs);
      case 0x13 -> UnknownOpcode.of(0x8, bs);
      case 0x14 -> UnknownOpcode.of(0xC, bs);
      case 0x16 -> UnknownOpcode.of(0x8, bs);
      case 0x17 -> UnknownOpcode.of(0x4, bs);
      case 0x18 -> UnknownOpcode.of(0x4, bs);
      case 0x19 -> UnknownOpcode.of(0x8, bs);
      case 0x1A -> UnknownOpcode.of(0x8, bs);
      case 0x1B -> UnknownOpcode.of(0x8, bs);
      case 0x1C -> UnknownOpcode.of(0x8, bs);
      case 0x1D -> UnknownOpcode.of(0x4, bs);
      case 0x1E -> UnknownOpcode.of(0x10, bs);
      case 0x1F -> op_471F(bs);
      case 0x20 -> UnknownOpcode.of(0x18, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4700(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    baos.write(ea.getBytes());
    baos.write(bs.readBytes(0x8));
    int structOffset = bs.readWord();
    String info = String.format("%s; binary data at offset 0x%X", ea.getDescription(), structOffset);
    baos.write(ByteUtils.fromInt32(structOffset));
    baos.write(bs.readBytes(0x8));
    return new SpawnProjectile(offset, baos.toByteArray());
  }

  public static Opcode op_4706(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_4707(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_470A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_4712(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_471F(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }
}
