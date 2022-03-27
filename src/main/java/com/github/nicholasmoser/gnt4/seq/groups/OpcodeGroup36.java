package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup36 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x4, bs);
      case 0x04 -> UnknownOpcode.of(0x4, bs);
      case 0x05 -> loadTexture(bs);
      case 0x06 -> UnknownOpcode.of(0x8, bs);
      case 0x07 -> op_3607(bs);
      case 0x08 -> op_3608(bs);
      case 0x0A -> seqInit(bs);
      case 0x0C -> op_360C(bs);
      case 0x0D -> op_360D(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode loadTexture(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readBytes(4));
    byte[] firstWord = bs.readBytes(4);
    baos.write(firstWord);
    StringBuilder info = new StringBuilder(String.format(" Use index 0x%x", ByteUtils.toInt32(firstWord)));
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" for texture file \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }

  public static Opcode op_3607(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    baos.write(bs.readBytes(4));
    StringBuilder info = new StringBuilder(String.format(" %s", ea.getDescription()));
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" with file \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }

  public static Opcode op_3608(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode seqInit(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    StringBuilder info = new StringBuilder(String.format(" %s", ea.getDescription()));
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" and init seq \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }

  public static Opcode op_360C(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(4);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  public static Opcode op_360D(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}