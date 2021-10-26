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
      case 0x1B -> op_2A1B(bs);
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
    info += String.format("; with flag 0x%X", pointerIndex);
    switch (pointerIndex) {
      case 0x0:
      case 0x4:
      case 0x10:
      case 0x1C:
      case 0x1F:
      case 0x21:
      case 0x22:
      case 0x24:
      case 0x27:
      case 0x29:
      case 0x2B:
      case 0x30:
      case 0x34:
      case 0x3E:
      case 0x44:
      case 0x45:
      case 0x46:
      case 0x47:
      case 0x4F:
      case 0x55:
      case 0x56:
      case 0x5A:
      case 0x70:
      case 0x77:
      case 0x9A:
      case 0x9C:
      case 0xC8:
        break;
      case 0x8:
      case 0xA:
      case 0xE:
      case 0x1B:
      case 0x1D:
      case 0x1E:
      case 0x23:
      case 0x25:
      case 0x26:
      case 0x2C:
      case 0x31:
      case 0x32:
      case 0x35:
      case 0x3B:
      case 0x4C:
      case 0x4E:
      case 0x54:
      case 0X68:
      case 0x6A:
      case 0xC9:
      case 0x144:
        baos.write(bs.readBytes(0x4));
        break;
      case 0x2:
      case 0x6:
      case 0x12:
      case 0x18:
      case 0x3A:
      case 0x41:
      case 0x6F:
      case 0x7E:
      case 0x7F:
      case 0x80:
      case 0x89:
      case 0xCA:
        baos.write(bs.readBytes(0x8));
        break;
      case 0x3:
      case 0x13:
      case 0x20:
      case 0x2D:
      case 0x48:
      case 0x84:
      case 0x86:
      case 0x88:
      case 0x8A:
        baos.write(bs.readBytes(0xc));
        break;
      case 0xC:
      case 0xD:
      case 0xF:
      case 0x14:
      case 0x2A:
      case 0x2E:
      case 0x2F:
      case 0x39:
      case 0x60:
      case 0x79:
      case 0x85:
      case 0x90:
      case 0x9B:
      case 0xCC:
        baos.write(bs.readBytes(0x10));
        break;
      case 0x7:
      case 0xB:
      case 0x15:
      case 0x16:
      case 0x28:
      case 0x3D:
      case 0x49:
      case 0x5F:
      case 0x61:
      case 0x64:
      case 0xCD:
      case 0xCE:
        baos.write(bs.readBytes(0x14));
        break;
      case 0x9:
      case 0xD0:
        baos.write(bs.readBytes(0x18));
        break;
      case 0x1:
      case 0x5:
      case 0x11:
      case 0x1A:
      case 0x38:
      case 0x7D:
      case 0x82:
      case 0x8C:
      case 0x8E:
      case 0x8F:
      case 0x91:
      case 0x92:
        baos.write(bs.readBytes(0x1C));
        break;
      case 0x19:
      case 0x62:
      case 0x6B:
      case 0x75:
      case 0x81:
        baos.write(bs.readBytes(0x20));
        break;
      case 0x17:
      case 0x65:
      case 0x6C:
      case 0x83:
      case 0x87:
        baos.write(bs.readBytes(0x24));
        break;
      default:
        int flagOffset = pointerIndex * 4;
        String message = String.format("This flag is not yet supported for op_2A00: 0x%X", pointerIndex);
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

  private static Opcode op_2A1B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddress ea = EffectiveAddress.get(bs);
    String info = String.format(" %s", ea.getDescription());
    byte[] bytes = bs.readBytes(0x10);
    return new UnknownOpcode(offset, Bytes.concat(ea.getBytes(), bytes), info);
  }
}
