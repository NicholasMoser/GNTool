package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.*;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup09 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_0900(bs);
      case 0x01 -> movc(bs);
      case 0x02 -> op_0902(bs);
      case 0x03 -> dec(bs);
      case 0x04 -> add(bs);
      case 0x05 -> sub(bs);
      case 0x06 -> subws(bs);
      case 0x07 -> mov(bs);
      case 0x08 -> o2p(bs);
      case 0x09 -> p2o(bs);
      case 0x0A -> push(bs);
      case 0x0B -> pop(bs);
      case 0x0C -> op_090C(bs);
      case 0x14 -> branch(bs);
      case 0x15 -> branchEqualToZero(bs);
      case 0x16 -> branchNotEqualToZero(bs);
      case 0x17 -> branchGreaterThanZero(bs);
      case 0x18, 0x1C -> branchGreaterThanEqualToZero(bs);
      case 0x19, 0x1B -> branchLessThanZero(bs);
      case 0x1A -> branchLessThanOrEqualToZero(bs);
      case 0x1D -> branchLink(bs);
      case 0x1E -> branchEqualZeroLink(bs);
      case 0x1F -> branchNotEqualZeroLink(bs);
      case 0x20 -> branchGreaterThanZeroLink(bs);
      case 0x21, 0x25 -> branchGreaterThanEqualZeroLink(bs);
      case 0x22, 0x24 -> branchLessThanZeroLink(bs);
      case 0x23 -> branchLessThanEqualZeroLink(bs);
      case 0x26 -> op_0926(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0900(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode movc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Movc(offset, ea.getBytes(), info);
  }

  private static Opcode op_0902(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode dec(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Decrement(offset, ea.getBytes(), info);
  }

  private static Opcode add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Add(offset, ea.getBytes(), info);
  }

  private static Opcode sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Sub(offset, ea.getBytes(), info);
  }

  private static Opcode subws(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Subws(offset, ea.getBytes(), info);
  }

  private static Opcode mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new Mov(offset, ea.getBytes(), info);
  }

  private static Opcode o2p(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new OffsetToSeqPointer(offset, ea.getBytes(), info);
  }

  private static Opcode p2o(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new SeqPointerToOffset(offset, ea.getBytes(), info);
  }

  private static Opcode push(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("push %s", ea.getDescription());
    return new Push(offset, ea.getBytes(), info);
  }

  private static Opcode pop(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("pop %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_090C(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode branch(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("b %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("beqz %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchNotEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bnez %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchGreaterThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bgtz %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchGreaterThanEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bgez %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchLessThanZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bltz %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchLessThanOrEqualToZero(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("blez %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bl %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("beqzal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchNotEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bnezal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchGreaterThanZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bgtzal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchGreaterThanEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bgezal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchLessThanZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("bltzal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode branchLessThanEqualZeroLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("blezal %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }

  private static Opcode op_0926(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}
