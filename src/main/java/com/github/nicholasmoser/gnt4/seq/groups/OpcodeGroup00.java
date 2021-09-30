package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Combo;
import com.github.nicholasmoser.gnt4.seq.opcodes.ComboList;
import com.github.nicholasmoser.gnt4.seq.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SoftReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OpcodeGroup00 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> softReset(bs);
      case 0x01 -> hardReset(bs);
      case 0x02 -> UnknownOpcode.of(0x00, 0x02, 0x4, bs);
      case 0x07 -> UnknownOpcode.of(0x00, 0x07, 0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode softReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.peekWordBytes();
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      Optional<Opcode> binaryData = getBinaryData(bs);
      if (binaryData.isPresent()) {
        return binaryData.get();
      }
      throw new IllegalStateException(
          "Soft reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    bs.skip(4);
    return new SoftReset(offset);
  }

  /**
   * Reads a currently unknown 4-byte struct. The first three bytes are read at instructions
   * 0x8010704c, 0x80107050, and 0x80107054, which is used in opcodes such as op_4700.
   */
  public static Optional<Opcode> getBinaryData(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] op_4700 = new byte[]{0x0, 0x0, 0x0, 0x4, 0x0, 0x0, 0x0, 0xA, 0x0, 0x0, 0x0, 0xA, 0x0,
        0x0, 0x0, 0x0};
    byte[] combo = "Combo".getBytes(StandardCharsets.UTF_8);
    byte[] bytes = bs.peekBytes(0x10);
    if (Arrays.equals(op_4700, bytes)) {
      bs.skip(0x10);
      return Optional.of(new BinaryData(offset, bytes, "; Binary data referenced by op_4700"));
    }
    if (Arrays.equals(combo, Arrays.copyOfRange(bytes, 4, 9))) {
      return Optional.of(getComboList(bs));
    }
    return Optional.empty();
  }

  private static Opcode getComboList(ByteStream bs) throws IOException {
    int numberOfCombos = bs.readWord();
    List<Combo> combos = new ArrayList<>(numberOfCombos);
    byte[] end = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    byte[] bytes = bs.readBytes(4);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    String info = "";
    int offset = bs.offset();
    boolean parsingName = true;
    while (!Arrays.equals(end, bytes)) {
      baos.write(bytes);
      if (bytes[3] == 0) {
        if (parsingName) {
          parsingName = false;
        } else {
          info = baos.toString(StandardCharsets.US_ASCII).replace("\0\0\0\0", " ");
          info = info.replace("\0\0\0", " ").replace("\0\0", " ").replace('\0', ' ');
          if (bs.peekWord() == -1) {
            baos.write(end);
          }
          combos.add(new Combo(offset, baos.toByteArray(), info));
          baos = new ByteArrayOutputStream();
          offset = bs.offset();
          parsingName = true;
        }
      }
      bytes = bs.readBytes(4);
    }
    return new ComboList(combos);
  }

  public static Opcode hardReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.readBytes(4);
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      throw new IllegalStateException(
          "Hard reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    return new HardReset(offset);
  }
}
