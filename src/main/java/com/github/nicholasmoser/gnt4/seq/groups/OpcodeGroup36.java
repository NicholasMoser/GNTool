package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.IOException;

public class OpcodeGroup36 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x36, 0x00, 0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x36, 0x01, 0x4, bs);
      case 0x05 -> loadTexture(bs);
      case 0x06 -> UnknownOpcode.of(0x36, 0x06, 0x8, bs);
      case 0x0a -> seqInit(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode loadTexture(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] opcode = new byte[4];
    if (bs.read(opcode) != 4) {
      throw new IOException("Failed to read opcode bytes of opcode 3605");
    }
    byte[] firstWord = new byte[4];
    if (bs.read(firstWord) != 4) {
      throw new IOException("Failed to read first word of opcode 3605");
    }
    StringBuilder info = new StringBuilder(String.format(" Use index 0x%x", ByteUtils.toInt32(firstWord)));
    // Get filename
    StringBuilder fileNameBuilder = new StringBuilder();
    byte[] buffer = new byte[4];
    do {
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to parse bytes of opcode at " + offset);
      }
      fileNameBuilder.append(new String(buffer, "shift-jis"));
    } while(buffer[0] != 0 && buffer [1] != 0 && buffer[2] != 0 && buffer[3] != 0);
    String fileName = fileNameBuilder.toString().replace("\0", "");
    info.append(" for texture file \"");
    info.append(fileName);
    info.append('"');
    byte[] fullBytes = Bytes.concat(opcode, firstWord, fileNameBuilder.toString().getBytes("shift-jis"));
    return new UnknownOpcode(offset, fullBytes, info.toString());
  }

  private static Opcode seqInit(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    StringBuilder info = new StringBuilder(String.format(" %s", ea.getDescription()));
    // Get filename
    StringBuilder fileNameBuilder = new StringBuilder();
    byte[] buffer = new byte[4];
    do {
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to parse bytes of opcode at " + offset);
      }
      fileNameBuilder.append(new String(buffer, "shift-jis"));
    } while(buffer[0] != 0 && buffer [1] != 0 && buffer[2] != 0 && buffer[3] != 0);
    String fileName = fileNameBuilder.toString().replace("\0", "");
    info.append(" and init seq \"");
    info.append(fileName);
    info.append('"');
    byte[] fullBytes = Bytes.concat(ea.getBytes(), fileNameBuilder.toString().getBytes("shift-jis"));
    return new UnknownOpcode(offset, fullBytes, info.toString());
  }
}