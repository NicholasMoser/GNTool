package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup40 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> UnknownOpcode.of(0x40, 0x01, 0x1C, bs);
      case 0x02 -> UnknownOpcode.of(0x40, 0x02, 0x20, bs);
      case 0x03 -> op_4003(bs);
      case 0x0D -> op_400D(bs);
      case 0x07 -> UnknownOpcode.of(0x40, 0x07, 0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x40, 0x0E, 0x4, bs);
      case 0x24 -> UnknownOpcode.of(0x40, 0x24, 0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_4003(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0x20);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_400D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
