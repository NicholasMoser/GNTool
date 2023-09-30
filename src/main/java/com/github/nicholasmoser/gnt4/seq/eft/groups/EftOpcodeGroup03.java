package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class EftOpcodeGroup03 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> op_0301(bs);
      case 0x02 -> op_0302(bs);
      case 0x06 -> op_0306(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_0301(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_0302(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  public static Opcode op_0306(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}
