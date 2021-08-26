package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup37 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x00:
        return UnknownOpcode.of(0x37, 0x00, 0x14, bs);
      case 0x02:
        return UnknownOpcode.of(0x37, 0x02, 0xc, bs);
      case 0x04:
        return UnknownOpcode.of(0x37, 0x04, 0x10, bs);
      case 0x08:
        return UnknownOpcode.of(0x37, 0x08, 0xc, bs);
      case 0x09:
        return UnknownOpcode.of(0x37, 0x09, 0x8, bs);
      case 0x0c:
        return UnknownOpcode.of(0x37, 0x0c, 0xc, bs);
      case 0x0e:
        return UnknownOpcode.of(0x37, 0x0e, 0xc, bs);
      case 0x11:
        return UnknownOpcode.of(0x37, 0x11, 0xc, bs);
      case 0x12:
        return UnknownOpcode.of(0x37, 0x12, 0xc, bs);
      case 0x13:
        return UnknownOpcode.of(0x37, 0x13, 0xc, bs);
      case 0x15:
        return UnknownOpcode.of(0x37, 0x15, 0xc, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }
}
