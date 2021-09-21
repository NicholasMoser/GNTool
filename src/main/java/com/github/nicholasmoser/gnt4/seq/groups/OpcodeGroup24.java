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
      case 0x09 -> UnknownOpcode.of(0x24, 0x09, 0x8, bs);
      case 0x0A -> op_240A(bs);
      case 0x0E -> UnknownOpcode.of(0x24, 0x0E, 0x8, bs);
      case 0x14 -> UnknownOpcode.of(0x24, 0x14, 0x8, bs);
      case 0x1A -> UnknownOpcode.of(0x24, 0x1A, 0x8, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  //  >> 8 & 0xff

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