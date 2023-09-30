package com.github.nicholasmoser.gnt4.seq.eft.groups;

import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD1;
import com.github.nicholasmoser.gnt4.seq.EFT_REG_CMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EftOpcodeGroup10 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> op_1002(bs);
      case 0x03 -> op_1003(bs);
      case 0x07 -> op_1007(bs);
      case 0x18 -> op_1018(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_1002(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_1003(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    byte[] bytes = bs.readNBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  public static Opcode op_1007(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    EFT_REG_CMD1 ea2 = EFT_REG_CMD1.get(bs);
    String description = ea.getDescription() + "; " + ea2.getDescription();
    byte[] extraBytes = bs.readNBytes(0xC);
    byte[] bytes = Bytes.concat(ea.getBytes(), ea2.getBytes(), extraBytes);
    return new UnknownOpcode(offset, bytes, description);
  }

  public static Opcode op_1018(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    EFT_REG_CMD1 ea2 = EFT_REG_CMD1.get(bs);
    EFT_REG_CMD1 ea3 = EFT_REG_CMD1.get(bs);
    EFT_REG_CMD1 ea4 = EFT_REG_CMD1.get(bs);
    String description = ea.getDescription() + "; " + ea2.getDescription() + "; "
        + ea3.getDescription() + "; " + ea4.getDescription();
    byte[] bytes = Bytes.concat(ea.getBytes(), ea2.getBytes(), ea3.getBytes(), ea4.getBytes());
    return new UnknownOpcode(offset, bytes, description);
  }
}
