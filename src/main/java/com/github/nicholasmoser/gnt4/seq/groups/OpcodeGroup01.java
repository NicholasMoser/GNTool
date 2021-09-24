package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLink;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchDecrementNotZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchGreaterThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchGreaterThanZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLessThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLessThanZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturnEqualZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchLinkReturnNotEqualZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchNotEqualToZero;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BranchNotEqualZeroLink;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x01, 0x00, 4, bs);
      case 0x01 -> op_0101(bs);
      case 0x02 -> UnknownOpcode.of(0x01, 0x02, 8, bs);
      case 0x04 -> UnknownOpcode.of(0x01, 0x04, 4, bs);
      case 0x08 -> UnknownOpcode.of(0x01, 0x08, 4, bs);
      case 0x32 -> branch(bs);
      case 0x33 -> branchEqualToZero(bs);
      case 0x34 -> branchNotEqualToZero(bs);
      case 0x35 -> branchGreaterThanZero(bs);
      case 0x36, 0x3A -> branchGreaterThanOrEqualToZero(bs, opcodeByte);
      case 0x37, 0x39 -> branchLessThanZero(bs, opcodeByte);
      case 0x38 -> branchLessThanOrEqualToZero(bs);
      case 0x3B -> branchDecrementNotZero(bs);
      case 0x3C -> branchLink(bs);
      case 0x3E -> branchNotEqualZeroLink(bs);
      case 0x45 -> branchLinkReturn(bs);
      case 0x46 -> branchLinkReturnEqualZero(bs);
      case 0x47 -> branchLinkReturnNotEqualZero(bs);
      case 0x50 -> op_0150(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0101(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
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

  public static Opcode branchGreaterThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchGreaterThanZero(offset, destination);
  }

  public static Opcode branchGreaterThanOrEqualToZero(ByteStream bs, byte secondByte) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchGreaterThanOrEqualToZero(offset, destination, secondByte);
  }

  public static Opcode branchLessThanZero(ByteStream bs, byte secondByte) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchLessThanZero(offset, destination, secondByte);
  }

  public static Opcode branchLessThanOrEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchLessThanOrEqualToZero(offset, destination);
  }

  private static Opcode branchDecrementNotZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchDecrementNotZero(offset, destination);
  }

  private static Opcode branchLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchLink(offset, destination);
  }

  private static Opcode branchNotEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchNotEqualZeroLink(offset, destination);
  }

  public static Opcode branchLinkReturn(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturn(offset);
  }

  public static Opcode branchLinkReturnEqualZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnEqualZero(offset);
  }

  public static Opcode branchLinkReturnNotEqualZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnNotEqualZero(offset);
  }

  private static Opcode op_0150(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
