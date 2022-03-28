package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup26 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> UnknownOpcode.of(0x10, bs);
      case 0x02 -> op_2602(bs);
      case 0x03 -> op_2603(bs);
      case 0x05 -> op_2605(bs);
      case 0x08 -> UnknownOpcode.of(0xC, bs);
      case 0x0A -> op_260A(bs);
      case 0x0B -> op_260B(bs);
      case 0x0E -> UnknownOpcode.of(0x4, bs);
      case 0x10 -> UnknownOpcode.of(0x8, bs);
      case 0x11 -> UnknownOpcode.of(0xC, bs);
      case 0x12 -> UnknownOpcode.of(0x4, bs);
      case 0x13 -> UnknownOpcode.of(0x8, bs);
      case 0x19 -> op_2619(bs);
      case 0x1A -> UnknownOpcode.of(0x4, bs);
      case 0x20 -> UnknownOpcode.of(0xC, bs);
      case 0x21 -> UnknownOpcode.of(0x8, bs);
      case 0x23 -> UnknownOpcode.of(0x4, bs);
      case 0x24 -> UnknownOpcode.of(0x4, bs);
      case 0x25 -> UnknownOpcode.of(0x4, bs);
      case 0x27 -> UnknownOpcode.of(0x4, bs);
      case 0x30 -> UnknownOpcode.of(0x4, bs);
      case 0x31 -> UnknownOpcode.of(0x4, bs);
      case 0x32 -> UnknownOpcode.of(0x4, bs);
      case 0x34 -> op_2634(bs);
      case 0x36 -> UnknownOpcode.of(0x4, bs);
      case 0x37 -> UnknownOpcode.of(0x4, bs);
      case 0x40 -> UnknownOpcode.of(0x4, bs);
      case 0x41 -> UnknownOpcode.of(0x4, bs);
      case 0x42 -> UnknownOpcode.of(0x4, bs);
      case 0x43 -> UnknownOpcode.of(0x4, bs);
      case 0x46 -> UnknownOpcode.of(0x4, bs);
      case 0x47 -> UnknownOpcode.of(0x4, bs);
      case 0x56 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x80 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x81 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x82 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x84 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x85 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x86 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x88 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x8A -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x8B -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x8E -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x91 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x92 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x93 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x94 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x96 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0x98 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA0 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA1 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA2 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA5 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA6 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA7 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xA9 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xAA -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xAC -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xAE -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB1 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB2 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB3 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB6 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xB7 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xBD -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xBE -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xBF -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xC0 -> op_26C0(bs);
      case (byte) 0xC1 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xC2 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xC4 -> op_26C4(bs);
      case (byte) 0xC6 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xC8 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xC9 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xCC -> op_26CC(bs);
      case (byte) 0xCE -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xD1 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xD3 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xD4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xD6 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xD9 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xDA -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xDB -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xDC -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xDD -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xDE -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xE0 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xE1 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xE2 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xE5 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xE6 -> UnknownOpcode.of(0x4, bs); //noop?
      case (byte) 0xE7 -> UnknownOpcode.of(0x4, bs); //noop?
      case (byte) 0xE8 -> op_26E8(bs);
      case (byte) 0xE9 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xEA -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xEB -> op_26EB(bs);
      case (byte) 0xEC -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF0 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF1 -> UnknownOpcode.of(0x8, bs);
      case (byte) 0xF2 -> op_26F2(bs);
      case (byte) 0xF3 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF4 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF5 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF6 -> op_26F6(bs);
      case (byte) 0xF7 -> UnknownOpcode.of(0x4, bs);
      case (byte) 0xF9 -> op_26F9(bs);
      case (byte) 0xFA -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_2602(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    baos.write(bytes);
    byte flag = bytes[2]; // Third byte of opcode
    baos.write(bs.readBytes(flag * 2 * 4));
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode op_2603(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    baos.write(bytes);
    byte flag = bytes[2]; // Third byte of opcode
    baos.write(bs.readBytes(flag * 2 * 4));
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode op_2605(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    baos.write(bytes);
    byte flag = bytes[2]; // Third byte of opcode
    baos.write(bs.readBytes(flag * 2 * 4));
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode op_260A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    baos.write(bytes);
    byte flag = bytes[2]; // Third byte of opcode
    int iVar20 = 0;
    int uVar12 = flag - 1 >> 3;
    if (flag > 8) {
      iVar20 = 8 * uVar12;
      baos.write(bs.readBytes(iVar20 * 2 * 4));
    }
    int iVar16 = flag - iVar20;
    if (iVar20 < (int)flag) {
      do {
        baos.write(bs.readBytes(8));
        iVar16--;
      } while (iVar16 != 0);
    }
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode op_260B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    baos.write(bytes);
    byte flag = bytes[2]; // Third byte of opcode
    baos.write(bs.readBytes(flag * 2 * 4));
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode op_2619(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  private static Opcode op_2634(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26C0(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26C4(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26CC(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26E8(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26EB(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26F2(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26F6(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_26F9(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}