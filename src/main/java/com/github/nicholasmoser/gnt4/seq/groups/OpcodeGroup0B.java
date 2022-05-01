package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorMove;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorMoveStack;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorMultiplyMatrix;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorMultiplyScalar;
import com.github.nicholasmoser.gnt4.seq.opcodes.FloatVectorSubtract;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup0B {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> fvec_mov(bs);
      case 0x03 -> fvec_add(bs);
      case 0x04 -> fvec_sub(bs);
      case 0x07 -> fvec_muls(bs);
      case 0x0A -> fvec_movs(bs);
      case 0x13 -> fvec_mulm(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode fvec_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatVectorMove(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode fvec_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatVectorAdd(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode fvec_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatVectorSubtract(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode fvec_muls(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatVectorMultiplyScalar(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode fvec_movs(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new FloatVectorMoveStack(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode fvec_mulm(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new FloatVectorMultiplyMatrix(offset, ea.getBytes(), ea.getDescription());
  }
}