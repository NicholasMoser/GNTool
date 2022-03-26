package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup3A {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x3A, 0x00, 0x18, bs);
      case 0x01 -> UnknownOpcode.of(0x3A, 0x01, 0x0c, bs);
      case 0x02 -> UnknownOpcode.of(0x3A, 0x02, 0x18, bs);
      case 0x05 -> UnknownOpcode.of(0x3A, 0x05, 0x1c, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
