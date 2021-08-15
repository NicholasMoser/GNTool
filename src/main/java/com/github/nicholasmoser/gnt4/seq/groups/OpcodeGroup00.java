package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.End;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup00 {
  public static Opcode parse(ByteStream bs) throws IOException {
    int opcodeByte = bs.read();
    switch(opcodeByte) {
      case 0x00:
        return resetPC(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode resetPC(ByteStream bs) throws IOException {
    if (bs.skip(2) != 2) {
      throw new IOException("Failed to parse two bytes after opcode at " + bs.offset());
    }
    return new End(bs.offset() - 4);
  }
}
