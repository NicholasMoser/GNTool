package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup48 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> UnknownOpcode.of(0x48, 0x02, 0x8, bs);
      case 0x04 -> UnknownOpcode.of(0x48, 0x04, 0x8, bs);
      case 0x06 -> UnknownOpcode.of(0x48, 0x06, 0x8, bs);
      case 0x07 -> UnknownOpcode.of(0x48, 0x07, 0x8, bs);
      case 0x0A -> UnknownOpcode.of(0x48, 0x0A, 0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x48, 0x0E, 0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x48, 0x10, 0xC, bs);
      case 0x11 -> UnknownOpcode.of(0x48, 0x11, 0x8, bs);
      case 0x12 -> UnknownOpcode.of(0x48, 0x12, 0x10, bs);
      case 0x15 -> UnknownOpcode.of(0x48, 0x15, 0x8, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
