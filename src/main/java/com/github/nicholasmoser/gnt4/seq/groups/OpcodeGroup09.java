package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup09 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> op_0901(bs);
      case 0x07 -> op_0907(bs);
      case 0x08 -> op_0908(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0901(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_0907(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_0908(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
