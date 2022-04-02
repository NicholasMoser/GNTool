package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SeqPointerToOffset;
import com.github.nicholasmoser.gnt4.seq.opcodes.OffsetToSeqPointer;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup09 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_0900(bs);
      case 0x01 -> ptr_movc(bs);
      case 0x02 -> op_0902(bs); // Unused in GNT4
      case 0x03 -> ptr_dec(bs);  // Unused in GNT4
      case 0x04 -> ptr_add(bs);
      case 0x05 -> ptr_sub(bs); // Unused in GNT4
      case 0x06 -> ptr_subws(bs); // Unused in GNT4
      case 0x07 -> ptr_mov(bs);
      case 0x08 -> offsetToPointer(bs);
      case 0x09 -> pointerToOffset(bs);
      case 0x0A -> ptr_push(bs);
      case 0x0B -> ptr_pop(bs); // Unused in GNT4
      case 0x0C -> op_090C(bs);
      case 0x14 -> ptr_branch(bs);
      case 0x15 -> branchEqualToZero(bs); // Unused in GNT4
      case 0x16 -> branchNotEqualToZero(bs); // Unused in GNT4
      case 0x17 -> branchGreaterThanZero(bs); // Unused in GNT4
      case 0x18, 0x1C -> branchGreaterThanEqualToZero(bs); // Unused in GNT4
      case 0x19, 0x1B -> branchLessThanZero(bs); // Unused in GNT4
      case 0x1A -> branchLessThanOrEqualToZero(bs); // Unused in GNT4
      case 0x1D -> ptr_branchLink(bs);
      case 0x1E -> branchEqualZeroLink(bs); // Unused in GNT4
      case 0x1F -> branchNotEqualZeroLink(bs);  // Unused in GNT4
      case 0x20 -> branchGreaterThanZeroLink(bs); // Unused in GNT4
      case 0x21, 0x25 -> branchGreaterThanEqualZeroLink(bs);  // Unused in GNT4
      case 0x22, 0x24 -> branchLessThanZeroLink(bs);  // Unused in GNT4
      case 0x23 -> branchLessThanEqualZeroLink(bs); // Unused in GNT4
      case 0x26 -> op_0926(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0900(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_movc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0902(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_dec(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_subws(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode offsetToPointer(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new OffsetToSeqPointer(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode pointerToOffset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new SeqPointerToOffset(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_push(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_pop(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("pop %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_090C(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  private static Opcode ptr_branch(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchNotEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchGreaterThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchGreaterThanEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchLessThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchLessThanOrEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_branchLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchNotEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchGreaterThanZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchGreaterThanEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchLessThanZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode branchLessThanEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0926(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}
