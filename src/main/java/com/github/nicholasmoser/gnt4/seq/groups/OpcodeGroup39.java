package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup39 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x14, bs);
      case 0x01 -> UnknownOpcode.of(0x14, bs);
      case 0x02 -> UnknownOpcode.of(0x14, bs);
      case 0x03 -> UnknownOpcode.of(0x10, bs);
      case 0x04 -> UnknownOpcode.of(0xc, bs);
      case 0x05 -> UnknownOpcode.of(0xc, bs);
      case 0x07 -> UnknownOpcode.of(0xc, bs);
      case 0x08 -> UnknownOpcode.of(0x24, bs);
      case 0x09 -> UnknownOpcode.of(0x24, bs);
      case 0x0B -> UnknownOpcode.of(0x10, bs);
      case 0x0C -> UnknownOpcode.of(0x14, bs);
      case 0x0F -> UnknownOpcode.of(0x10, bs);
      case 0x11 -> UnknownOpcode.of(0x10, bs);
      case 0x12 -> UnknownOpcode.of(0xc, bs);
      case 0x16 -> UnknownOpcode.of(0x10, bs);
      case 0x17 -> UnknownOpcode.of(0x10, bs);
      case 0x1a -> UnknownOpcode.of(0x14, bs);
      case 0x1b -> UnknownOpcode.of(0x14, bs);
      case 0x1d -> UnknownOpcode.of(0x10, bs);
      case 0x1e -> UnknownOpcode.of(0x10, bs);
      case 0x1f -> UnknownOpcode.of(0x10, bs);
      case 0x21 -> UnknownOpcode.of(0x20, bs);
      case 0x24 -> UnknownOpcode.of(0x10, bs);
      case 0x25 -> UnknownOpcode.of(0x10, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }
}
