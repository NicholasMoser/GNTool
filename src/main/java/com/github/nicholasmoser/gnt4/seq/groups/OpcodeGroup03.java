package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Movr;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Pop;
import com.github.nicholasmoser.gnt4.seq.opcodes.Push;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup03 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> movr(bs);
      case 0x03 -> push(bs);
      case 0x04 -> pop(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  // TODO: Is it a move or copy? Looks like copy...
  private static Opcode movr(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Movr(offset, ea.getBytes(), info);
  }

  private static Opcode push(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Push(offset, ea.getBytes(), info);
  }

  private static Opcode pop(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Pop(offset, ea.getBytes(), info);
  }
}
