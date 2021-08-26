package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.SoftReset;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.util.Arrays;

public class OpcodeGroup00 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x00:
        return softReset(bs);
      case 0x01:
        return hardReset(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode softReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = new byte[4];
    if (bs.read(bytes) != 4) {
      throw new IllegalStateException("failed to read opcode at " + offset);
    }
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      throw new IllegalStateException("Soft reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    return new SoftReset(offset);
  }

  public static Opcode hardReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = new byte[4];
    if (bs.read(bytes) != 4) {
      throw new IllegalStateException("failed to read opcode at " + offset);
    }
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      throw new IllegalStateException("Hard reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    return new HardReset(offset);
  }
}
