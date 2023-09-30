package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD1;
import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class EftOpcodeGroup0B {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_0B00(bs);
      case 0x04 -> op_0B04(bs);
      case 0x07 -> op_0B07(bs);
      case 0x29 -> op_0B29(bs);
      case 0x2B -> op_0B2B(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_0B00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(0xC);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0B04(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(0x8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0B07(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(0x14);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0B29(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(0x4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0B2B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(0x8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }
}
