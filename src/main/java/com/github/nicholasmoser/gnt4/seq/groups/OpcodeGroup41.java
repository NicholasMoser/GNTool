package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup41 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x03 -> UnknownOpcode.of(0x41, 0x03, 0xc, bs);
      case 0x04 -> UnknownOpcode.of(0x41, 0x04, 0xc, bs);
      case 0x06 -> UnknownOpcode.of(0x41, 0x06, 0x1c, bs);
      case 0x0A -> UnknownOpcode.of(0x41, 0x0A, 0x8, bs);
      case 0x0B -> UnknownOpcode.of(0x41, 0x0B, 0x8, bs);
      case 0x0D -> UnknownOpcode.of(0x41, 0x0D, 0x10, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}