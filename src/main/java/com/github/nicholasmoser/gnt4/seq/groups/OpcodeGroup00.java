package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.End;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.util.Arrays;

public class OpcodeGroup00 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> end(bs);
      case 0x01 -> hardReset(bs);
      case 0x02 -> UnknownOpcode.of(0x4, bs);
      case 0x07 -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode end(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.peekWordBytes();
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      String msg = String.format("End opcode should have 0 for third and fourth byte: %s at offset 0x%X", Arrays.toString(bytes), bs.offset());
      throw new IllegalStateException(msg);
    }
    bs.skip(4);
    return new End(offset);
  }

  public static Opcode hardReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.readBytes(4);
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      String msg = String.format("Hard reset should have 0 for third and fourth byte: %s at offset 0x%X", Arrays.toString(bytes), bs.offset());
      throw new IllegalStateException(msg);
    }
    return new HardReset(offset);
  }
}
