package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup03 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x03 -> op_0303(bs);
      case 0x04 -> op_0304(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0303(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_0304(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
