package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup1E {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch (opcodeByte) {
      case 0x02:
        return op_1E02(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  private static Opcode op_1E02(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}