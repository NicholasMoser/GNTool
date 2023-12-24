package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD1;
import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class EftOpcodeGroup0A {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_0A00(bs);
      case 0x02 -> op_0A02(bs);
      case 0x05 -> op_0A05(bs);
      case 0x06 -> op_0A06(bs);
      case 0x15 -> op_0A15(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_0A00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0A02(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0A05(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(0x14);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0A06(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(0x4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_0A15(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}
