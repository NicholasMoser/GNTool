package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup61 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x0c:
        return UnknownOpcode.of(0x61, 0x0c, 0x8, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }
}