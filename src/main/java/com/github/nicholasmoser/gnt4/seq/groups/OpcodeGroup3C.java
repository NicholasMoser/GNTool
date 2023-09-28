package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.TCG;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgAnd;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgDiv;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgMod;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgMov;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgMul;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgOr;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgRand;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgSub;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup3C {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> tcg_mov(bs);
      case 0x01 -> tcg_add(bs);
      case 0x02 -> tcg_sub(bs);
      case 0x03 -> tcg_and(bs); // Not in GNT4
      case 0x04 -> tcg_or(bs);
      case 0x05 -> tcg_mul(bs);
      case 0x06 -> tcg_div(bs);
      case 0x07 -> tcg_mod(bs);
      case 0x08 -> UnknownOpcode.of(0x10, bs);
      case 0x09 -> UnknownOpcode.of(0x10, bs);
      case 0x0a -> tcg_rand(bs);
      case 0x0b -> UnknownOpcode.of(0xc, bs);
      case 0x0c -> UnknownOpcode.of(0x14, bs);
      case 0x0d -> UnknownOpcode.of(0x14, bs);
      case 0x11 -> UnknownOpcode.of(0x10, bs);
      case 0x12 -> UnknownOpcode.of(0x10, bs);
      case 0x14 -> UnknownOpcode.of(0x10, bs);
      case 0x15 -> UnknownOpcode.of(0x10, bs);
      case 0x16 -> UnknownOpcode.of(0x10, bs);
      case 0x17 -> UnknownOpcode.of(0x10, bs);
      case 0x18 -> UnknownOpcode.of(0x14, bs);
      case 0x19 -> UnknownOpcode.of(0x10, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode tcg_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgMov(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgAdd(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgSub(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_and(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgAnd(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_or(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgOr(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_mul(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgMul(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_div(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgDiv(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_mod(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgMod(offset, baos.toByteArray(), description);
  }

  private static Opcode tcg_rand(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    String description = String.format("%s, s_lpCTD->vars[%s]", TCG.read1(op1), TCG.read2(op2));
    return new TcgRand(offset, baos.toByteArray(), description);
  }
}
