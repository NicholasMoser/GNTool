package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup2B {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_2B00(bs);
      case 0x01 -> op_2B01(bs);
      case 0x03 -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_2B00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    StringBuilder info = new StringBuilder(String.format(" %s", ea.getDescription()));
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" with file \"");
    info.append(fileName);
    info.append('"');
    return new UnknownOpcode(offset, baos.toByteArray(), info.toString());
  }

  public static Opcode op_2B01(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}