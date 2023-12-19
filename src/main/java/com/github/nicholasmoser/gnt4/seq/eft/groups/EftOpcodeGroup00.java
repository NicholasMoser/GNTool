package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD1;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class EftOpcodeGroup00 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4, bs);
      case 0x02 -> UnknownOpcode.of(0x4, bs);
      case 0x03 -> UnknownOpcode.of(0x4, bs);
      case 0x04 -> op_0004(bs);
      case 0x05 -> UnknownOpcode.of(0x8, bs);
      case 0x09 -> UnknownOpcode.of(0x8, bs);
      case 0x0B -> UnknownOpcode.of(0x4, bs);
      case 0x0C -> UnknownOpcode.of(0x8, bs);
      case 0x0D -> UnknownOpcode.of(0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x8, bs);
      case 0x11 -> UnknownOpcode.of(0x8, bs);
      case 0x1A -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_0004(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD1 ea = EFT_REG_CMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}