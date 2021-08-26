package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;

public class OpcodeGroup36 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    switch(opcodeByte) {
      case 0x00:
        return UnknownOpcode.of(0x36, 0x00, 0x4, bs);
      case 0x01:
        return UnknownOpcode.of(0x36, 0x01, 0x4, bs);
      case 0x0a:
        return seqInit(bs);
      default:
        throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    }
  }

  private static Opcode seqInit(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    StringBuilder info = new StringBuilder(String.format(" %s", ea.getDescription()));
    // Get filename
    StringBuffer fileNameBuilder = new StringBuffer();
    byte[] buffer = new byte[4];
    do {
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to parse bytes of opcode at " + offset);
      }
      fileNameBuilder.append(new String(buffer, "shift-jis"));
    } while(buffer[0] != 0 && buffer [1] != 0 && buffer[2] != 0 && buffer[3] != 0);
    String fileName = fileNameBuilder.toString().replace("\0", "");
    info.append(" and init seq ");
    info.append(fileName);
    return new UnknownOpcode(offset, ea.getBytes(), info.toString());
  }
}