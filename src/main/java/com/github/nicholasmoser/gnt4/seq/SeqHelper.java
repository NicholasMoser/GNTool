package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Combo;
import com.github.nicholasmoser.gnt4.seq.opcodes.ComboList;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
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
    while (bs.peekWord() != 0x013C0000) {
      baos.write(bs.readBytes(4));
    }
    return new BinaryData(offset, baos.toByteArray());
  }

  /**
   * Read bytes to see if the next group of bytes are binary data. If they are, return a list of
   * opcodes for them. Otherwise, return an empty list.
   *
   * @param bs The ByteStream to read from.
   * @return The binary data.
   * @throws IOException If an I/O error occurs.
   */
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
   * Reads a currently unknown 16-byte struct. The first three bytes are read at instructions
   * 0x8010704c, 0x80107050, and 0x80107054, which is used in opcodes such as op_4700.
   *
   * @param bs
   * @return
   * @throws IOException If an I/O error occurs.
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

  /**
   * Returns if the ByteStream is currently at unknown binary 1.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 1.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary1(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x8) {
      return false;
    }
    byte[] expected = new byte[]{0x00, 0x20, 0x56, (byte) 0x80, 0x00, (byte) 0xF0, 0x00, 0x04};
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

  /**
   * Return if the ByteStream is currently at a combo list.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is currently at a combo list.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isComboList(ByteStream bs) throws IOException {
    // Skip null bytes until at non-null bytes
    bs.mark();
    int word;
    do {
      if (bs.offset() >= bs.length()) {
        bs.reset();
        return false; // EOF
      }
      word = bs.readWord();
    } while (word == 0);
    // Read the next 5 bytes
    if (bs.offset() + 5 > bs.length()) {
      bs.reset();
      return false; // EOF
    }
    byte[] bytes = bs.readBytes(5);
    bs.reset();
    return isCombo(bytes);
  }

  /**
   * Returns whether or not the given bytes are a combo definition. Most characters use "Combo" for
   * combo definitions, but a few characters have different text. The bytes passed into this method
   * must be 5 bytes or it will never return true.
   *
   * @param bytes The 5 bytes to check.
   * @return If the 5 bytes are a combo definition.
   */
  public static boolean isCombo(byte[] bytes) {
    byte[] combo = "Combo".getBytes(StandardCharsets.UTF_8);
    // Tayuyu doki demon
    byte[] chord = "Chord".getBytes(StandardCharsets.UTF_8);
    // Karasu
    byte[] routine = "Routi".getBytes(StandardCharsets.UTF_8);
    // Oboro
    byte[] comboJapanese = new byte[]{(byte) 0x83, 0x52, (byte) 0x83, (byte) 0x93, (byte) 0x83};
    if (Arrays.equals(bytes, combo) || Arrays.equals(bytes, chord) ||
        Arrays.equals(bytes, routine) || Arrays.equals(bytes, comboJapanese)) {
      return true;
    }
    return false;
  }

  /**
   * Read the combo list from the ByteStream and return a ComboList opcode.
   *
   * @param bs The ByteStream to read from.
   * @return The ComboList opcode.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode readComboList(ByteStream bs) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int word;
    do {
      word = bs.readWord();
      baos.write(ByteUtils.fromInt32(word));
    } while (word == 0);
    int numberOfCombos = word;
    List<Combo> combos = new ArrayList<>(numberOfCombos);
    byte[] end = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    byte[] bytes = bs.readBytes(4);
    int offset = bs.offset();
    boolean parsingName = true;
    while (!Arrays.equals(end, bytes)) {
      baos.write(bytes);
      if (bytes[3] == 0) {
        if (parsingName) {
          parsingName = false;
        } else {
          String info = replaceNulls(baos.toString(StandardCharsets.US_ASCII));
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

  /**
   * Replaces combinations of 1-4 null bytes with a single space. This will make sure that
   *
   * @param text The text to replace nulls in.
   * @return The text without nulls.
   */
  private static String replaceNulls(String text) {
    String newText = text.replace("\0\0\0\0", " ");
    newText = newText.replace("\0\0\0", " ");
    newText = newText.replace("\0\0", " ");
    return newText.replace('\0', ' ');
  }
}
