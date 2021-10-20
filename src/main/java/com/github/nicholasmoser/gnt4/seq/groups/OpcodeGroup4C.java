package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup4C {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_4C00(bs);
      case 0x01 -> op_4C01(bs);
      case 0x02 -> op_4C02(bs);
      case 0x03 -> op_4C03(bs);
      case 0x04 -> op_4C04(bs);
      case 0x05 -> op_4C05(bs);
      case 0x08 -> op_4C08(bs);
      case 0x0B -> op_4C0B(bs);
      case 0x0E -> op_4C0E(bs);
      case 0x14 -> op_4C14(bs);
      case 0x1F -> UnknownOpcode.of(0x4C, 0x1F, 0xC, bs);
      case 0x20 -> UnknownOpcode.of(0x4C, 0x20, 0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4C00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C01(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C02(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C03(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C04(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C05(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C08(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C0B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0xC);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C0E(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0xC);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_4C14(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}