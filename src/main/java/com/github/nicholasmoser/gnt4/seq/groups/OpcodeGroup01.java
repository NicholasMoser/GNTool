package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Op_0102;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.CountingInputStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(ByteStream bs) throws IOException {
    int opcodeByte = bs.read();
    switch(opcodeByte) {
      case 0x02:
        return op_0102(bs);
      case 0x32:
        return branch(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode op_0102(ByteStream bs) throws IOException {
    byte[] bytes = new byte[8];
    if (bs.read(bytes, 2, 6) != 6) {
      throw new IOException("Failed to read bytes for opcode 0102");
    }
    bytes[0] = 0x01;
    bytes[1] = 0x02;
    return new Op_0102(bs.offset() - 8, bytes);
  }

  public static Opcode branch(ByteStream bs) throws IOException {
    if (bs.skip(2) != 2) {
      throw new IOException("Failed to parse two bytes after opcode at " + bs.offset());
    }
    int destination = bs.readWord();
    return new Branch(bs.offset() - 8, destination);
  }
}
