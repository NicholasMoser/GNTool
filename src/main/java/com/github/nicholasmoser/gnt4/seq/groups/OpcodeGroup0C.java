package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.MatrixScale;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup0C {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x09 -> mtx_scale(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode mtx_scale(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new MatrixScale(offset, ea.getBytes(), info);
  }
}