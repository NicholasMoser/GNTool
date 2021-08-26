package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup38 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x00:
        return UnknownOpcode.of(0x38, 0x00, 0x14, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }
}