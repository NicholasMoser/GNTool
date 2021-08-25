package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.SoftReset;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

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
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse two bytes after opcode at " + offset);
    }
    return new SoftReset(offset);
  }

  public static Opcode hardReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse two bytes after opcode at " + offset);
    }
    return new HardReset(offset);
  }
}
