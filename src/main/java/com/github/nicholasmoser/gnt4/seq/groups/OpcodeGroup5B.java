package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup5B {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
