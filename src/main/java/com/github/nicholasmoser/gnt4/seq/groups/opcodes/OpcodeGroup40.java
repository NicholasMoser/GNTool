package com.github.nicholasmoser.gnt4.seq.groups.opcodes;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup40 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch (opcodeByte) {
      case 0x01:
        return UnknownOpcode.of(0x40, 0x01, 0x1C, bs);
      case 0x02:
        return UnknownOpcode.of(0x40, 0x02, 0x20, bs);
      case 0x0D:
        return op_400D(bs);
      case 0x0E:
        return UnknownOpcode.of(0x40, 0x0E, 0x4, bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  private static Opcode op_400D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
