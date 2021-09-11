package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup3E {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_3E00(bs);
      case 0x01 -> op_3E01(bs);
      case 0x03 -> op_3E03(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_3E00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] lastWord = new byte[4];
    if (bs.read(lastWord) != 4) {
      throw new IOException("Failed to read last word of opcode 3E01");
    }
    byte[] fullBytes = Bytes.concat(ea.getBytes(), lastWord);
    return new UnknownOpcode(offset, fullBytes, info);
  }

  private static Opcode op_3E01(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] lastWord = new byte[4];
    if (bs.read(lastWord) != 4) {
      throw new IOException("Failed to read last word of opcode 3E01");
    }
    byte[] fullBytes = Bytes.concat(ea.getBytes(), lastWord);
    return new UnknownOpcode(offset, fullBytes, info);
  }

  private static Opcode op_3E03(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] lastWord = new byte[4];
    if (bs.read(lastWord) != 4) {
      throw new IOException("Failed to read last word of opcode 3E03");
    }
    byte[] fullBytes = Bytes.concat(ea.getBytes(), lastWord);
    return new UnknownOpcode(offset, fullBytes, info);
  }
}
