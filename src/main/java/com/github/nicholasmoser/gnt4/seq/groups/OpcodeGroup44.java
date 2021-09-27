package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup44 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_4400(bs);
      case 0x01 -> op_4401(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4400(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  public static Opcode op_4401(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}
