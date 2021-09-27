package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup34 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch (opcodeByte) {
      case 0x0C:
        return UnknownOpcode.of(0x34, 0x0C, 0x4, bs);
      case 0x0D:
        return op_340D(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  private static Opcode op_340D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
