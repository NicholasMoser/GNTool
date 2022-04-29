package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatCompare;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatDivide;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatMoveEphemeral;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatMultiply;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatSubtract;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup08 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> f32_move(bs);
      case 0x04 -> f32_sub(bs);
      case 0x05 -> f32_mul(bs);
      case 0x06 -> f32_div(bs);
      case 0x07 -> f32_cmp(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode f32_move(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatMoveEphemeral(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode f32_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatSubtract(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode f32_mul(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatMultiply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode f32_div(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatDivide(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode f32_cmp(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatCompare(offset, ea.getBytes(), ea.getDescription());
  }
}