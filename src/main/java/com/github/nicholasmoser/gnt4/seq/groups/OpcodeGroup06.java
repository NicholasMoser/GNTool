package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortAndWithStore;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortMove;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortNimply;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortOr;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortSubtractWithStore;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup06 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> s_mov(bs);
      case 0x03 -> s_andws(bs);
      case 0x04 -> s_nimply(bs);
      case 0x07 -> s_add(bs);
      case 0x0E -> s_or(bs);
      case 0x11 -> s_subws(bs);
      case 0x16 -> op_0616(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode s_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortMove(offset, ea.getBytes(), info);
  }

  private static Opcode s_andws(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortAndWithStore(offset, ea.getBytes(), info);
  }

  private static Opcode s_nimply(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortNimply(offset, ea.getBytes(), info);
  }

  private static Opcode s_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortAdd(offset, ea.getBytes(), info);
  }

  private static Opcode s_or(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortOr(offset, ea.getBytes(), info);
  }

  private static Opcode s_subws(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new ShortSubtractWithStore(offset, ea.getBytes(), info);
  }

  private static Opcode op_0616(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
