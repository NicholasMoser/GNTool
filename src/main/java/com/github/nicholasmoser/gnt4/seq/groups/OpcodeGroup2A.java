package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddress;
import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class OpcodeGroup2A {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> op_2A00(bs);
      case 0x01 -> op_2A01(bs);
      case 0x19 -> op_2A19(bs);
      case 0x1A -> op_2A1A(bs);
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
    ByteBuffer bb = ByteBuffer.allocate(2);
    bb.put(flags[2]);
    bb.put(flags[3]);
    short pointerIndex = bb.getShort(0);
    switch (pointerIndex) {
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
      case 52:
      case 70:
      case 79:
      case 200:
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
      case 201:
      case 324:
        baos.write(bs.readBytes(0x4));
        break;
      case 2:
      case 6:
      case 18:
      case 24:
      case 126:
      case 127:
      case 128:
      case 137:
        baos.write(bs.readBytes(0x8));
        break;
      case 3:
      case 19:
      case 32:
      case 45:
      case 132:
      case 134:
      case 136:
      case 138:
        baos.write(bs.readBytes(0xc));
        break;
      case 12:
      case 13:
      case 15:
      case 20:
      case 42:
      case 46:
      case 47:
      case 96:
      case 121:
      case 133:
      case 155:
      case 204:
        baos.write(bs.readBytes(0x10));
        break;
      case 7:
      case 11:
      case 21:
      case 22:
      case 40:
      case 95:
      case 206:
        baos.write(bs.readBytes(0x14));
        break;
      case 9:
        baos.write(bs.readBytes(0x18));
        break;
      case 1:
      case 5:
      case 17:
      case 26:
      case 130:
      case 145:
        baos.write(bs.readBytes(0x1C));
        break;
      case 25:
      case 129:
        baos.write(bs.readBytes(0x20));
        break;
      case 23:
      case 131:
      case 135:
        baos.write(bs.readBytes(0x24));
        break;
      default:
        int flagOffset = pointerIndex * 4;
        String message = "This flag is not yet supported for op_2A00: " + pointerIndex;
        message = String.format("%s\nLook at address 0x80212868 + 0x%X = 0x%X", message, flagOffset,
            0x80212868 + flagOffset);
        throw new IllegalStateException(message);
    }
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }

  private static Opcode op_2A01(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    String info = String.format(" %s", ea.getDescription());
    byte[] flagBytes = bs.readBytes(4);
    baos.write(flagBytes);
    int flags = ByteUtils.toInt32(flagBytes);
    int pointerOffset = flags >> 0xe & 0x3fffc;
    int flag = flags & 0xFFFF;
    switch (pointerOffset) {
      case 0x4:
      case 0x1C:
        if (flag == 1) {
          baos.write(bs.readBytes(0x14));
        } else if (flag == 0) {
          baos.write(bs.readBytes(0x8));
        }
        break;
      case 0x8:
        if (flag == 1) {
          baos.write(bs.readBytes(0x4));
        } else if (flag == 0) {
          baos.write(bs.readBytes(0x4));
        } else if (flag < 3) {
          baos.write(bs.readBytes(0x4));
        }
        break;
      case 0xC:
        if (flag == 0) {
          baos.write(bs.readBytes(0x14));
        } else if (flag == 1) {
          baos.write(bs.readBytes(0x18));
        } else if (flag == 3) {
          baos.write(bs.readBytes(0x1C));
        } else if (flag == 4) {
          baos.write(bs.readBytes(0x20));
        }
        break;
      case 0x14:
        if (flag == 1) {
          baos.write(bs.readBytes(0x28));
        } else if (flag == 0) {
          baos.write(bs.readBytes(0x1C));
        }
        break;
      case 0x18:
        if (flag == 1) {
          baos.write(bs.readBytes(0x10));
        } else if (flag == 0) {
          baos.write(bs.readBytes(0x4));
        }
        break;
      case 0x20:
        if (flag == 1) {
          baos.write(bs.readBytes(0x10));
        } else {
          baos.write(bs.readBytes(0x4));
        }
        break;
      case 0x24:
        baos.write(bs.readBytes(0x14));
        break;
      case 0x34:
        baos.write(bs.readBytes(0x18));
        break;
      default:
        String message = "This flag is not yet supported for op_2A01: " + pointerOffset;
        message += String.format("\nLook at offset from 0x80212BB0 + 0x%X = 0x%X", pointerOffset,
            0x80212bb0 + pointerOffset);
        throw new IllegalStateException(message);
    }
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }

  private static Opcode op_2A19(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(8);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }

  private static Opcode op_2A1A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0xC);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}
