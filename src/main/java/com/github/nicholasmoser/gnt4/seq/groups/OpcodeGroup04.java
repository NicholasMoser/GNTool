package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntAnd;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntAndCompare;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntDecrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntDivide;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntIncrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntModulo;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntMov;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntMultiply;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntNimply;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntOr;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntRandom;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntSubtract;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntSubtractCompare;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntXor;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.util.Optional;

public class OpcodeGroup04 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> i32_mov(bs);
      case 0x03 -> i32_andc(bs);
      case 0x04 -> i32_nimply(bs);
      case 0x05 -> i32_inc(bs);
      case 0x06 -> i32_dec(bs);
      case 0x07 -> i32_add(bs);
      case 0x08 -> i32_sub(bs);
      case 0x09 -> i32_mul(bs);
      case 0x0A -> i32_div(bs);
      case 0x0D -> i32_and(bs);
      case 0x0E -> i32_or(bs);
      case 0x0F -> i32_xor(bs);
      case 0x11 -> i32_subc(bs);
      case 0x15 -> i32_rand(bs);
      case 0x17 -> i32_mod(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode i32_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    Optional<String> result = SeqHelper.getChrFieldDescription(ea, 4);
    if (result.isPresent()) {
      return new IntMov(offset, ea.getBytes(), result.get());
    }
    return new IntMov(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_andc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    Optional<String> result = SeqHelper.getChrFieldDescription(ea, 4);
    if (result.isPresent()) {
      return new IntAndCompare(offset, ea.getBytes(), result.get());
    }
    return new IntAndCompare(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_nimply(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntNimply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_inc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntIncrement(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_dec(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntDecrement(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntAdd(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntSubtract(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_mul(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntMultiply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_div(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntDivide(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_and(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntAnd(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_or(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntOr(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_xor(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntXor(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_subc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    Optional<String> result = SeqHelper.getChrFieldDescription(ea, 4);
    if (result.isPresent()) {
      return new IntSubtractCompare(offset, ea.getBytes(), result.get());
    }
    return new IntSubtractCompare(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_rand(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntRandom(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_mod(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new IntModulo(offset, ea.getBytes(), ea.getDescription());
  }
}
