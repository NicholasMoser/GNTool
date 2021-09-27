package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup3C {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x3C, 0x00, 0xc, bs);
      case 0x01 -> UnknownOpcode.of(0x3C, 0x01, 0xc, bs);
      case 0x02 -> UnknownOpcode.of(0x3C, 0x02, 0xc, bs);
      case 0x04 -> UnknownOpcode.of(0x3C, 0x04, 0xc, bs);
      case 0x05 -> UnknownOpcode.of(0x3C, 0x05, 0xc, bs);
      case 0x08 -> UnknownOpcode.of(0x3C, 0x08, 0x10, bs);
      case 0x09 -> UnknownOpcode.of(0x3C, 0x09, 0x10, bs);
      case 0x0a -> UnknownOpcode.of(0x3C, 0x0a, 0xc, bs);
      case 0x0b -> UnknownOpcode.of(0x3C, 0x0b, 0xc, bs);
      case 0x0c -> UnknownOpcode.of(0x3C, 0x0c, 0x14, bs);
      case 0x0d -> UnknownOpcode.of(0x3C, 0x0d, 0x14, bs);
      case 0x11 -> UnknownOpcode.of(0x3C, 0x11, 0x10, bs);
      case 0x14 -> UnknownOpcode.of(0x3C, 0x14, 0x10, bs);
      case 0x15 -> UnknownOpcode.of(0x3C, 0x15, 0x10, bs);
      case 0x16 -> UnknownOpcode.of(0x3C, 0x16, 0x10, bs);
      case 0x17 -> UnknownOpcode.of(0x3C, 0x17, 0x10, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
