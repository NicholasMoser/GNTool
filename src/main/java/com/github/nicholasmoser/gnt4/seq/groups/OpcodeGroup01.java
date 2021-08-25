package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x02:
        return UnknownOpcode.of(0x01, 0x02, 8, bs);
      case 0x04:
        return UnknownOpcode.of(0x01, 0x04, 8, bs);
      case 0x32:
        return branch(bs);
      case 0x33:
        return branchEqualToZero(bs);
      case 0x45:
        return branchLinkReturn(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  public static Opcode branch(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse two bytes after opcode at " + offset);
    }
    int destination = bs.readWord();
    return new Branch(offset, destination);
  }

  public static Opcode branchEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse two bytes after opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchEqualToZero(offset, destination);
  }

  public static Opcode branchLinkReturn(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse two bytes after opcode at " + offset);
    }
    return new BranchLinkReturn(offset);
  }
}
