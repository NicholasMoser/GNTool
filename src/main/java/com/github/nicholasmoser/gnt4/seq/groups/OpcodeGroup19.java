package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup19 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_1900(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_1900(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int word = bs.peekWord();
    byte flag = (byte) (word >> 8 & 0xff);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    if (flag == 1) {
      if ((word & 0xff) == 0) {
        baos.write(bs.readBytes(8));
      } else {
        baos.write(bs.readBytes(4));
      }
    } else if (flag == 0) {
      baos.write(bs.readBytes(8));
    }
    return new UnknownOpcode(offset, baos.toByteArray());
  }
}