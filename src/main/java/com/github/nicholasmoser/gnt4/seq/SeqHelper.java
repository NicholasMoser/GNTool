package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Combo;
import com.github.nicholasmoser.gnt4.seq.opcodes.ComboList;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeqHelper {
  /**
   * Reads binary data until a branch and link opcode and returns the binary data.
   *
   * @param bs The ByteStream to read from.
   * @return The binary data.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode getBinaryUntilBranchAndLink(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while(bs.peekWord() != 0x013C0000) {
      baos.write(bs.readBytes(4));
    }
    return new BinaryData(offset, baos.toByteArray());
  }

  public static List<Opcode> getBinaries(ByteStream bs) throws IOException {
    int offset = bs.offset();
    List<Opcode> opcodes = new ArrayList<>();
    if (SeqHelper.isOp04700Binary(bs)) {
      byte[] bytes = bs.readBytes(0x10);
      opcodes.add(new BinaryData(offset, bytes, "; Binary data referenced by op_4700"));
    } else if (SeqHelper.isComboList(bs)) {
      opcodes.add(SeqHelper.readComboList(bs));
    } else if (SeqHelper.isUnknownBinary1(bs)) {
      opcodes.add(SeqHelper.readUnknownBinary1(bs));
    }
    return Collections.unmodifiableList(opcodes);
  }

  /**
   * Reads a currently unknown 4-byte struct. The first three bytes are read at instructions
   * 0x8010704c, 0x80107050, and 0x80107054, which is used in opcodes such as op_4700.
   *
   * @param bs
   * @return
   * @throws IOException
   */
  public static boolean isOp04700Binary(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x10) {
      return false;
    }
    byte[] op_4700 = new byte[]{0x0, 0x0, 0x0, 0x4, 0x0, 0x0, 0x0, 0xA, 0x0, 0x0, 0x0, 0xA, 0x0,
        0x0, 0x0, 0x0};
    byte[] bytes = bs.peekBytes(0x10);
    return Arrays.equals(op_4700, bytes);
  }

  public static boolean isUnknownBinary1(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x8) {
      return false;
    }
    byte[] expected = new byte[] { 0x00, 0x20, 0x56, (byte) 0x80, 0x00, (byte) 0xF0, 0x00, 0x04 };
    byte[] bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  public static Opcode readUnknownBinary1(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (bs.peekWord() != 0x04026B00) {
      baos.write(bs.readBytes(4));
    }
    return new BinaryData(offset, baos.toByteArray());
  }

  public static boolean isComboList(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x10) {
      return false;
    }
    byte[] combo = "Combo".getBytes(StandardCharsets.UTF_8);
    byte[] bytes = bs.peekBytes(0x10);
    return Arrays.equals(combo, Arrays.copyOfRange(bytes, 4, 9));
  }

  public static Opcode readComboList(ByteStream bs) throws IOException {
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
}
