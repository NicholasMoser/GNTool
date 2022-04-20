package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Add;
import com.github.nicholasmoser.gnt4.seq.opcodes.And;
import com.github.nicholasmoser.gnt4.seq.opcodes.Andws;
import com.github.nicholasmoser.gnt4.seq.opcodes.Decrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.Divide;
import com.github.nicholasmoser.gnt4.seq.opcodes.Increment;
import com.github.nicholasmoser.gnt4.seq.opcodes.Movc;
import com.github.nicholasmoser.gnt4.seq.opcodes.Multiply;
import com.github.nicholasmoser.gnt4.seq.opcodes.Nimply;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Or;
import com.github.nicholasmoser.gnt4.seq.opcodes.Sub;
import com.github.nicholasmoser.gnt4.seq.opcodes.Subws;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Xor;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

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
    return new Movc(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_andc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Andws(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_nimply(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Nimply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_inc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Increment(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_dec(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Decrement(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Add(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Sub(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_mul(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Multiply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_div(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Divide(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_and(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new And(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_or(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Or(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_xor(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Xor(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_subc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new Subws(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_rand(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i32_mod(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}
