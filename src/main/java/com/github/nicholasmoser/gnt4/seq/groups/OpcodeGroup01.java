package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchEqualToZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanEqualZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchDecrementNotZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchGreaterThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchGreaterThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnGreaterThanEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnGreaterThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnLessThanEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnLessThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnNotEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchNotEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchNotEqualZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTable;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTableLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup01 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(4, bs);
      case 0x01 -> op_0101(bs);
      case 0x02 -> UnknownOpcode.of(8, bs);
      case 0x04 -> UnknownOpcode.of(4, bs);
      case 0x05 -> op_0105(bs);
      case 0x08 -> UnknownOpcode.of(4, bs);
      case 0x32 -> branch(bs);
      case 0x33 -> branchEqualToZero(bs);
      case 0x34 -> branchNotEqualToZero(bs);
      case 0x35 -> branchGreaterThanZero(bs);
      case 0x36, 0x3A -> branchGreaterThanOrEqualToZero(bs, opcodeByte);
      case 0x37, 0x39 -> branchLessThanZero(bs, opcodeByte);
      case 0x38 -> branchLessThanOrEqualToZero(bs);
      case 0x3B -> branchDecrementNotZero(bs);
      case 0x3C -> branchLink(bs);
      case 0x3D -> branchEqualToZeroLink(bs);
      case 0x3E -> branchNotEqualZeroLink(bs);
      case 0x41 -> branchLessThanZeroLink(bs);
      case 0x42 -> branchLessThanEqualZeroLink(bs);
      case 0x45 -> branchLinkReturn(bs);
      case 0x46 -> branchLinkReturnEqualZero(bs);
      case 0x47 -> branchLinkReturnNotEqualZero(bs);
      case 0x48 -> branchLinkReturnGreaterThanZero(bs);
      case 0x49 -> branchLinkReturnGreaterThanEqualZero(bs);
      case 0x4A -> branchLinkReturnLessThanZero(bs);
      case 0x4B -> branchLinkReturnLessThanEqualZero(bs);
      case 0x50 -> branch_table(bs);
      case 0x51 -> branch_table_link(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0101(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0105(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
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

  private static Opcode branchEqualToZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchEqualToZeroLink(offset, destination);
  }

  private static Opcode branchNotEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchNotEqualZeroLink(offset, destination);
  }

  private static Opcode branchLessThanZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchLessThanZeroLink(offset, destination);
  }

  private static Opcode branchLessThanEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    int destination = bs.readWord();
    return new BranchLessThanEqualZeroLink(offset, destination);
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

  public static Opcode branchLinkReturnGreaterThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnGreaterThanZero(offset);
  }

  public static Opcode branchLinkReturnGreaterThanEqualZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnGreaterThanEqualZero(offset);
  }

  public static Opcode branchLinkReturnLessThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnLessThanZero(offset);
  }

  public static Opcode branchLinkReturnLessThanEqualZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (bs.skip(4) != 4) {
      throw new IOException("Failed to parse bytes of opcode at " + offset);
    }
    return new BranchLinkReturnLessThanEqualZero(offset);
  }

  private static Opcode branch_table(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    byte[] numOfBranchesBytes = bs.readBytes(4);
    baos.write(numOfBranchesBytes);
    int numOfBranches = ByteUtils.toInt32(numOfBranchesBytes);
    int[] offsets = new int[numOfBranches];
    for (int i = 0; i < numOfBranches; i++) {
      byte[] offsetBytes = bs.readBytes(4);
      baos.write(offsetBytes);
      offsets[i] = ByteUtils.toInt32(offsetBytes);
    }
    return new BranchTable(offset, baos.toByteArray(), ea.getDescription(), offsets);
  }

  private static Opcode branch_table_link(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    byte[] numOfBranchesBytes = bs.readBytes(4);
    baos.write(numOfBranchesBytes);
    int numOfBranches = ByteUtils.toInt32(numOfBranchesBytes);
    int[] offsets = new int[numOfBranches];
    for (int i = 0; i < numOfBranches; i++) {
      byte[] offsetBytes = bs.readBytes(4);
      baos.write(offsetBytes);
      offsets[i] = ByteUtils.toInt32(offsetBytes);
    }
    return new BranchTableLink(offset, baos.toByteArray(), ea.getDescription(), offsets);
  }
}
