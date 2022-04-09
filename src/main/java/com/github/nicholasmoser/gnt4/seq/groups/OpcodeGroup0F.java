package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup0F {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x0A -> op_0F0A(bs);
      case 0x0D -> op_0F0D(bs);
      case 0x0E -> op_0F0E(bs);
      case 0x0F -> op_0F0F(bs);
      case 0x10 -> op_0F10(bs);
      case 0x11 -> op_0F11(bs);
      case 0x12 -> UnknownOpcode.of(0x4, bs);
      case 0x13 -> UnknownOpcode.of(0x4, bs);
      case 0x14 -> op_0F14(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_0F0A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0F0D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0F0E(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    StringBuilder info = new StringBuilder(ea.getDescription());
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" with file name \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }

  private static Opcode op_0F0F(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0F10(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0F11(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0F14(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    StringBuilder info = new StringBuilder(ea.getDescription());
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" with file name \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }
}