package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup2B {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_2B00(bs);
      case 0x01 -> op_2B01(bs);
      case 0x03 -> UnknownOpcode.of(0x2B, 0x03, 0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_2B00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    return new UnknownOpcode(offset, ea.getBytes(), info);
  }
}