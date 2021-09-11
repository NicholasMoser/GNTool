package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup3D {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> UnknownOpcode.of(0x3D, 0x01, 0x14, bs);
      case 0x02 -> UnknownOpcode.of(0x3D, 0x02, 0x18, bs);
      case 0x0A -> UnknownOpcode.of(0x3D, 0x0A, 0x4, bs);
      case 0x0B -> UnknownOpcode.of(0x3D, 0x0B, 0x18, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}