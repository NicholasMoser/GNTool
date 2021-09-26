package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup24 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x06 -> UnknownOpcode.of(0x24, 0x06, 0x8, bs);
      case 0x07 -> UnknownOpcode.of(0x24, 0x07, 0x4, bs);
      case 0x08 -> UnknownOpcode.of(0x24, 0x08, 0x4, bs);
      case 0x09 -> UnknownOpcode.of(0x24, 0x09, 0x8, bs);
      case 0x0A -> op_240A(bs);
      case 0x0B -> UnknownOpcode.of(0x24, 0x0B, 0x4, bs);
      case 0x0C -> UnknownOpcode.of(0x24, 0x0C, 0x8, bs);
      case 0x0D -> UnknownOpcode.of(0x24, 0x0D, 0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x24, 0x0E, 0x8, bs);
      case 0x0F -> UnknownOpcode.of(0x24, 0x0F, 0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x24, 0x10, 0x4, bs);
      case 0x14 -> UnknownOpcode.of(0x24, 0x14, 0x8, bs);
      case 0x15 -> UnknownOpcode.of(0x24, 0x15, 0x8, bs);
      case 0x17 -> UnknownOpcode.of(0x24, 0x17, 0x8, bs);
      case 0x18 -> UnknownOpcode.of(0x24, 0x18, 0x8, bs);
      case 0x19 -> UnknownOpcode.of(0x24, 0x19, 0x8, bs);
      case 0x1A -> UnknownOpcode.of(0x24, 0x1A, 0x8, bs);
      case 0x1B -> UnknownOpcode.of(0x24, 0x1B, 0xC, bs);
      case 0x1C -> UnknownOpcode.of(0x24, 0x1C, 0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_240A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int word = bs.peekWord();
    byte flag = (byte) (word >> 8 & 0xff);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    if (flag == 0x11) {
      byte[] bytes = bs.readBytes(8);
      baos.write(bytes);
    }
    byte[] bytes = bs.readBytes(8);
    baos.write(bytes);
    return new UnknownOpcode(offset, baos.toByteArray());
  }
}