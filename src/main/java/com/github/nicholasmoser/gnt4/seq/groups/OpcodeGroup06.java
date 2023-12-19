package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortAndCompare;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortDecrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortIncrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortMove;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortNimply;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortOr;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortRandom;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortSubtract;
import com.github.nicholasmoser.gnt4.seq.opcodes.ShortSubtractCompare;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.util.Optional;

public class OpcodeGroup06 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x02 -> i16_mov(bs);
      case 0x03 -> i16_andc(bs);
      case 0x04 -> i16_nimply(bs);
      case 0x05 -> i16_inc(bs);
      case 0x06 -> i16_dec(bs);
      case 0x07 -> i16_add(bs);
      case 0x08 -> i16_sub(bs);
      case 0x0E -> i16_or(bs);
      case 0x11 -> i16_subc(bs);
      case 0x15 -> i16_rand(bs);
      case 0x16 -> i16_mov(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode i16_mov(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortMove(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_andc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    Optional<String> result = SeqHelper.getChrFieldDescription(ea);
    if (result.isPresent()) {
      return new ShortAndCompare(offset, ea.getBytes(), result.get());
    }
    return new ShortAndCompare(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_nimply(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortNimply(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_inc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs, 2);
    return new ShortIncrement(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_dec(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs, 2);
    return new ShortDecrement(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_add(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortAdd(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_sub(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortSubtract(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_or(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortOr(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_subc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    Optional<String> result = SeqHelper.getChrFieldDescription(ea);
    if (result.isPresent()) {
      return new ShortSubtractCompare(offset, ea.getBytes(), result.get());
    }
    return new ShortSubtractCompare(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode i16_rand(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs, 2);
    return new ShortRandom(offset, ea.getBytes(), ea.getDescription());
  }
}
