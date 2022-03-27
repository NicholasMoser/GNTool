package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup42 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x4, bs);
      case 0x02 -> UnknownOpcode.of(0x10, bs);
      case 0x07 -> UnknownOpcode.of(0x8, bs);
      case 0x08 -> UnknownOpcode.of(0xC, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
