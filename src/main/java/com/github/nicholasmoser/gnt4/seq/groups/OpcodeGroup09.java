package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.*;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup09 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> ptr_debug(bs);
      case 0x01 -> ptr_mov(bs);
      case 0x02 -> ptr_inc(bs); // Unused in GNT4
      case 0x03 -> ptr_dec(bs);  // Unused in GNT4
      case 0x04 -> ptr_add(bs);
      case 0x05 -> ptr_sub(bs); // Unused in GNT4
      case 0x06 -> ptr_subc(bs); // Unused in GNT4
      case 0x07 -> ptr_move(bs);
      case 0x08 -> ptr_from_offset(bs);
      case 0x09 -> ptr_to_offset(bs);
      case 0x0A -> ptr_push(bs);
      case 0x0B -> ptr_pop(bs); // Unused in GNT4
      case 0x0C -> ptr_table_lookup(bs);
      case 0x14 -> ptr_b(bs);
      case 0x15 -> ptr_beqz(bs); // Unused in GNT4
      case 0x16 -> ptr_bnez(bs); // Unused in GNT4
      case 0x17 -> ptr_bgtz(bs); // Unused in GNT4
      case 0x18, 0x1C -> ptr_bgez(bs); // Unused in GNT4
      case 0x19, 0x1B -> ptr_bltz(bs); // Unused in GNT4
      case 0x1A -> ptr_blez(bs); // Unused in GNT4
      case 0x1D -> ptr_bl(bs);
      case 0x1E -> ptr_beqzal(bs); // Unused in GNT4
      case 0x1F -> ptr_bnezal(bs);  // Unused in GNT4
      case 0x20 -> ptr_bgtzal(bs); // Unused in GNT4
      case 0x21, 0x25 -> ptr_bgezal(bs);  // Unused in GNT4
      case 0x22, 0x24 -> ptr_bltzal(bs);  // Unused in GNT4
      case 0x23 -> ptr_blezal(bs); // Unused in GNT4
      case 0x26 -> op_0926(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode ptr_debug(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new PointerMov(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_inc(ByteStream bs) throws IOException {
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
    return new PointerAdd(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_subc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_move(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new PointerMove(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_from_offset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new PointerFromOffset(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_to_offset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new PointerToOffset(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_push(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new PointerPush(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_pop(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format("pop %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode ptr_table_lookup(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    byte[] bytes = bs.readBytes(4);
    int tableOffset = ByteUtils.toInt32(bytes);
    return new PointerTableLookup(offset, Bytes.concat(ea.getBytes(), bytes), tableOffset, ea.getDescription());
  }

  private static Opcode ptr_b(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new PointerBranch(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_beqz(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bnez(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bgtz(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bgez(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bltz(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_blez(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bl(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new PointerBranchLink(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_beqzal(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bnezal(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bgtzal(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bgezal(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_bltzal(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode ptr_blezal(ByteStream bs) throws IOException {
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
