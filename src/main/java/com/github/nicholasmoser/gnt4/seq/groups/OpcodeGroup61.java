package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup61 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x4, bs);
      case 0x02 -> op_6102(bs);
      case 0x04 -> UnknownOpcode.of(0x8, bs);
      case 0x08 -> UnknownOpcode.of(0xC, bs);
      case 0x0B -> UnknownOpcode.of(0x10, bs);
      case 0x0C -> UnknownOpcode.of(0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x10, bs);
      case 0x13 -> UnknownOpcode.of(0xC, bs);
      case 0x14 -> UnknownOpcode.of(0x8, bs);
      case 0x15 -> UnknownOpcode.of(0xC, bs);
      case 0x17 -> UnknownOpcode.of(0xC, bs);
      case 0x18 -> UnknownOpcode.of(0x14, bs);
      case 0x2C -> UnknownOpcode.of(0x8, bs);
      case 0x32 -> UnknownOpcode.of(0x8, bs);
      case 0x33 -> UnknownOpcode.of(0x8, bs);
      case 0x34 -> UnknownOpcode.of(0x8, bs);
      case 0x3C -> UnknownOpcode.of(0xC, bs);
      case 0x3D -> UnknownOpcode.of(0x10, bs);
      case 0x48 -> UnknownOpcode.of(0x8, bs);
      case 0x49 -> UnknownOpcode.of(0x8, bs);
      case 0x4C -> UnknownOpcode.of(0x8, bs);
      case 0x4E -> UnknownOpcode.of(0x8, bs);
      case 0x50 -> UnknownOpcode.of(0x4, bs);
      case 0x52 -> UnknownOpcode.of(0x14, bs);
      case 0x53 -> UnknownOpcode.of(0x10, bs);
      case 0x5A -> UnknownOpcode.of(0x10, bs);
      case 0x5B -> UnknownOpcode.of(0xC, bs);
      case 0x5C -> UnknownOpcode.of(0x10, bs);
      case 0x5D -> UnknownOpcode.of(0x14, bs);
      case 0x63 -> UnknownOpcode.of(0xc, bs);
      case 0x64 -> UnknownOpcode.of(0x10, bs);
      case 0x67 -> UnknownOpcode.of(0xC, bs);
      case 0x68 -> UnknownOpcode.of(0x8, bs);
      case 0x6B -> UnknownOpcode.of(0x1C, bs);
      case 0x6C -> UnknownOpcode.of(0x10, bs);
      case 0x6E -> UnknownOpcode.of(0x14, bs);
      case 0x70 -> UnknownOpcode.of(0xC, bs);
      case (byte) 0x85 -> UnknownOpcode.of(0x8, bs);
      case (byte) 0x86 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x8C -> UnknownOpcode.of(0x8, bs);
      case (byte) 0x8D -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x8F -> UnknownOpcode.of(0x8, bs);
      case (byte) 0x99 -> UnknownOpcode.of(0x8, bs);
      case (byte) 0x9F -> UnknownOpcode.of(0x8, bs);
      case (byte) 0xAA -> UnknownOpcode.of(0x14, bs);
      case (byte) 0xAB -> UnknownOpcode.of(0x18, bs);
      case (byte) 0xAC -> UnknownOpcode.of(0x14, bs);
      case (byte) 0xAD -> UnknownOpcode.of(0x14, bs);
      case (byte) 0xAE -> UnknownOpcode.of(0xC, bs);
      case (byte) 0xC8 -> UnknownOpcode.of(0x8, bs);
      case (byte) 0xC9 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF0 -> UnknownOpcode.of(0xC, bs);
      case (byte) 0xF3 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF5 -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_6102(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    byte[] lastWord = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), lastWord), ea.getDescription());
  }
}