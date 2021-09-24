package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup2A {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_2A00(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_2A00(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    byte[] flags = bs.readBytes(4);
    baos.write(flags);
    int param3 = flags[2] << 8 | flags[3];
    switch(param3) {
      case 0:
      case 4:
      case 16:
      case 28:
      case 31:
      case 33:
      case 34:
      case 36:
      case 39:
      case 41:
      case 43:
      case 48:
        break;
      case 8:
      case 10:
      case 14:
      case 27:
      case 29:
      case 30:
      case 35:
      case 37:
      case 38:
      case 44:
      case 49:
      case 50:
        baos.write(bs.readBytes(0x4));
        break;
      case 2:
      case 6:
      case 18:
      case 24:
        baos.write(bs.readBytes(0x8));
        break;
      case 3:
      case 19:
      case 32:
      case 45:
        baos.write(bs.readBytes(0xc));
        break;
      case 12:
      case 13:
      case 15:
      case 20:
      case 42:
      case 46:
      case 47:
        baos.write(bs.readBytes(0x10));
        break;
      case 7:
      case 11:
      case 21:
      case 22:
      case 40:
        baos.write(bs.readBytes(0x14));
        break;
      case 9:
        baos.write(bs.readBytes(0x18));
        break;
      case 1:
      case 5:
      case 17:
      case 26:
        baos.write(bs.readBytes(0x1C));
        break;
      case 25:
        baos.write(bs.readBytes(0x20));
        break;
      case 23:
        baos.write(bs.readBytes(0x24));
        break;
      default:
        throw new IllegalStateException("This flag is not yet supported for op_2A00: " + param3 + "\nLook at offset from 80212868");
    }
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }
}
