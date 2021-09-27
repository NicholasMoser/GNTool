package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup4B {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4B, 0x00, 0xC, bs);
      case 0x01 -> UnknownOpcode.of(0x4B, 0x01, 0x8, bs);
      case 0x07 -> op_4B07(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_4B07(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0x8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}