package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD1;
import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class EftOpcodeGroup07 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> op_0701(bs);
      case 0x03 -> op_0703(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_0701(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_0703(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    byte[] bytes = bs.readNBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }
}
