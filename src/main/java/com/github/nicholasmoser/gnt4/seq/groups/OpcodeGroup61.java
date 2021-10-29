package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup61 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x08:
        return UnknownOpcode.of(0x61, 0x08, 0xC, bs);
      case 0x0C:
        return UnknownOpcode.of(0x61, 0x0c, 0x8, bs);
      case 0x15:
        return UnknownOpcode.of(0x61, 0x15, 0xC, bs);
      case 0x17:
        return UnknownOpcode.of(0x61, 0x17, 0xC, bs);
      case 0x52:
        return UnknownOpcode.of(0x61, 0x52, 0x14, bs);
      case 0x53:
        return UnknownOpcode.of(0x61, 0x53, 0x10, bs);
      case 0x5A:
        return UnknownOpcode.of(0x61, 0x5A, 0x10, bs);
      case 0x5B:
        return UnknownOpcode.of(0x61, 0x5B, 0xC, bs);
      case 0x5C:
        return UnknownOpcode.of(0x61, 0x5C, 0x10, bs);
      case 0x5D:
        return UnknownOpcode.of(0x61, 0x5D, 0x14, bs);
      case 0x64:
        return UnknownOpcode.of(0x61, 0x64, 0x10, bs);
      case 0x67:
        return UnknownOpcode.of(0x61, 0x67, 0xc, bs);
      case 0x68:
        return UnknownOpcode.of(0x61, 0x68, 0x8, bs);
      case 0x6B:
        return UnknownOpcode.of(0x61, 0x6B, 0x1C, bs);
      case 0x6C:
        return UnknownOpcode.of(0x61, 0x6C, 0x10, bs);
      case 0x6E:
        return UnknownOpcode.of(0x61, 0x6E, 0x14, bs);
      case (byte) 0x8C:
        return UnknownOpcode.of(0x61, 0x8C, 0x8, bs);
      case (byte) 0x99:
        return UnknownOpcode.of(0x61, 0x99, 0x8, bs);
      case (byte) 0xAA:
        return UnknownOpcode.of(0x61, 0xAA, 0x14, bs);
      case (byte) 0xAB:
        return UnknownOpcode.of(0x61, 0xAB, 0x18, bs);
      case (byte) 0xAC:
        return UnknownOpcode.of(0x61, 0xAC, 0x14, bs);
      case (byte) 0xAD:
        return UnknownOpcode.of(0x61, 0xAD, 0x14, bs);
      case (byte) 0xAE:
        return UnknownOpcode.of(0x61, 0xAE, 0xC, bs);
      case (byte) 0xF3:
        return UnknownOpcode.of(0x61, 0xF3, 0x4, bs);
      case (byte) 0xF4:
        return UnknownOpcode.of(0x61, 0xF4, 0x4, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }
}