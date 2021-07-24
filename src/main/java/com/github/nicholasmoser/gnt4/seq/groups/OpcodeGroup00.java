package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.End;
import com.google.common.io.CountingInputStream;
import java.io.IOException;

public class OpcodeGroup00 {
  public static Opcode parse(CountingInputStream cis) throws IOException {
    int opcodeByte = cis.read();
    switch(opcodeByte) {
      case 0x00:
        return resetPC(cis);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode resetPC(CountingInputStream cis) throws IOException {
    if (cis.skip(2) != 2) {
      throw new IOException("Failed to parse two bytes after opcode at " + cis.getCount());
    }
    return new End((int) (cis.getCount() - 4));
  }
}
