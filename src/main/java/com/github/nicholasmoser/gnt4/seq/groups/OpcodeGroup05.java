package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.ByteMov;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup05 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> i8_mov(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode i8_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 1);
    return new ByteMov(offset, ea.getBytes(), ea.getDescription());
  }
}
