package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup41 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x03 -> UnknownOpcode.of(0xc, bs);
      case 0x04 -> UnknownOpcode.of(0xc, bs);
      case 0x06 -> UnknownOpcode.of(0x1c, bs);
      case 0x08 -> UnknownOpcode.of(0x4, bs);
      case 0x09 -> UnknownOpcode.of(0x4, bs);
      case 0x0A -> UnknownOpcode.of(0x8, bs);
      case 0x0B -> UnknownOpcode.of(0x8, bs);
      case 0x0C -> UnknownOpcode.of(0x4, bs);
      case 0x0D -> UnknownOpcode.of(0x10, bs);
      case 0x0E -> UnknownOpcode.of(0x8, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}