package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup50 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> UnknownOpcode.of(0x50, 0x02, 0x4, bs);
      case 0x03 -> UnknownOpcode.of(0x50, 0x03, 0x4, bs);
      case 0x04 -> UnknownOpcode.of(0x50, 0x04, 0x4, bs);
      case 0x06 -> UnknownOpcode.of(0x50, 0x06, 0x8, bs);
      case 0x0B -> UnknownOpcode.of(0x50, 0x0B, 0x4, bs);
      case 0x0D -> op_500D(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_500D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
