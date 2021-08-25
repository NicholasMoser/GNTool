package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup39 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x12:
        return UnknownOpcode.of(0x39, 0x12, 0xc, bs);
      case 0x17:
        return UnknownOpcode.of(0x39, 0x17, 0x10, bs);
      case 0x21:
        return UnknownOpcode.of(0x39, 0x21, 0x20, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }
}
