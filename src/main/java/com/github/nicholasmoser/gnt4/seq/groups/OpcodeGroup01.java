package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchAndLink;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchNotEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x01, 0x00, 4, bs);
      case 0x02 -> UnknownOpcode.of(0x01, 0x02, 8, bs);
      case 0x04 -> UnknownOpcode.of(0x01, 0x04, 4, bs);
      case 0x08 -> UnknownOpcode.of(0x01, 0x08, 4, bs);
      case 0x32 -> branch(bs);
      case 0x33 -> branchEqualToZero(bs);
      case 0x34 -> branchNotEqualToZero(bs);
      case 0x3c -> branchAndLink(bs);
      case 0x45 -> branchLinkReturn(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode branch(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new Branch(offset, destination);
  }

  public static Opcode branchEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchEqualToZero(offset, destination);
  }

  public static Opcode branchNotEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchNotEqualToZero(offset, destination);
  }

  private static Opcode branchAndLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchAndLink(offset, destination);
  }

  public static Opcode branchLinkReturn(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturn(offset);
  }
}
