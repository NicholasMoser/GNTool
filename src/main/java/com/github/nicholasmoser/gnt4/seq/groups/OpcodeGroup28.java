package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup28 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case (byte) 0x40 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA0 -> UnknownOpcode.of(0x8, bs);
      case (byte) 0xA2 -> UnknownOpcode.of(0x10, bs);
      case (byte) 0xA4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA6 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA8 -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}