package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.CountingInputStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(CountingInputStream cis) throws IOException {
    int opcodeByte = cis.read();
    switch(opcodeByte) {
      case 0x32:
        return branch(cis);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode branch(CountingInputStream cis) throws IOException {
    if (cis.skip(2) != 2) {
      throw new IOException("Failed to parse two bytes after opcode at " + cis.getCount());
    }
    byte[] offsetBytes = new byte[4];
    if (cis.read(offsetBytes) != 4) {
      throw new IOException("Failed to read jump offset at offset " + cis.getCount());
    }
    int destination = ByteUtils.toInt32(offsetBytes);
    return new Branch((int) (cis.getCount() - 8), destination);
  }
}
