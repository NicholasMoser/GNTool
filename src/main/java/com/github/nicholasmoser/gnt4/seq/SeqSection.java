package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActionID;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActionID.Type;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SectionTitle;
import com.github.nicholasmoser.gnt4.seq.opcodes.SeqEditOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.Charsets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeqSection {

  // The below three byte arrays are for various types of chr reset functions that are used
  private final static byte[] CHR_RESET_1 = new byte[] { 0x24, 0x1A, 0x00, 0x00, 0x00, 0x00, 0x00,
      0x01, 0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x02, 0x3C, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00,
      0x00, 0x01};
  private final static byte[] CHR_RESET_2 = new byte[] { 0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x02,
      0x3C, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0B, 0x01, 0x3C, 0x00, 0x00, 0x00, 0x00,
      0x0A, 0x1C };
  private final static byte[] CHR_RESET_3 = new byte[] { 0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x02,
      0x3C, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x3C, 0x00, 0x00, 0x00, 0x00,
      0x0A, 0x1C };

  /**
   * Returns if the next 4-byte word is the start of a new seq section.
   *
   * @param bs The seq ByteStream.
   * @return If the next 4-byte word is the start of a new seq section.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isSeqSectionTitle(ByteStream bs) throws IOException {
    byte[] word = bs.peekWordBytes();
    String startOfTitle = new String(word, Charsets.US_ASCII);
    return "chr_".equals(startOfTitle);
  }

  /**
   * Returns if the next 4-byte word is the start of the seq extension section.
   *
   * @param bs The seq ByteStream.
   * @return If the next 4-byte word is the start of the seq extension section.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isSeqExtension(ByteStream bs) throws IOException {
    byte[] word = bs.peekWordBytes();
    String startOfTitle = new String(word, Charsets.US_ASCII);
    return "seq_".equals(startOfTitle);
  }

  /**
   * Handles the current SEQ section. First reads the section title, which ends with 0x0A and is
   * 16-byte aligned. {@link #isSeqSectionTitle(ByteStream)} must be called first.
   *
   * @param bs The seq ByteStream.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> handleSeqSection(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    boolean newLine = false;
    int chr = bs.read();
    // Get seq header title
    while (!newLine) {
      if (chr == -1) {
        throw new IOException("Unexpected EOF for seq header title at offset " + bs.offset());
      } else if (chr == 0) {
        // The title ends with a null byte
        newLine = true;
      } else {
        // Add the character
        baos.write(chr);
        chr = bs.read();
      }
    }
    // The title ends with a newline, strip it off
    String title = baos.toString(StandardCharsets.US_ASCII).strip();
    // Go to next 16-byte alignment
    while (bs.offset() % 16 != 0) {
      chr = bs.read();
      if (chr != 0) {
        throw new IOException("Unexpected bytes after seq header title at offset " + bs.offset());
      }
      baos.write(chr);
    }
    SectionTitle sectionTitle = new SectionTitle(offset, baos.toByteArray(), title);
    switch (title) {
      case (Seq.CHR_TBL):
        return parseChrTbl(bs, sectionTitle);
      case (Seq.CHR_DATA):
        return parseChrData(bs, sectionTitle);
      case (Seq.CHR_SHOT):
        return parseChrShot(bs, sectionTitle);
      case (Seq.CHR_ACT):
      case (Seq.CHR_CAM):
      case (Seq.CHR_SUB02):
      case (Seq.CHR_MODEL):
      case (Seq.CHR_MOT):
      case (Seq.CHR_HIRA):
      case (Seq.CHR_SEL):
      case (Seq.CHR_FACE):
      case (Seq.CHR_VISUAL2D):
        return Collections.singletonList(sectionTitle);
      case (Seq.SEQ_EXT):
        throw new RuntimeException("Did not call isSeqSectionTitle first");
      default:
        throw new IOException("Unknown seq section: " + title);
    }
  }


  public static List<Opcode> handleSeqExtension(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] titleBytes = bs.readBytes(8);
    String title = new String(titleBytes, StandardCharsets.US_ASCII);
    if (!"seq_ext\n".equals(title)) {
      throw new IllegalStateException("expected seq_ext\\n but is actually: " + title);
    }
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new SectionTitle(offset, titleBytes, title.strip()));
    byte[] seqEnd = "seq_end\n".getBytes(StandardCharsets.US_ASCII);
    while (!Arrays.equals(seqEnd, bs.peekBytes(8))) {
      offset = bs.offset();
      String name = new String(SeqHelper.readString(bs), StandardCharsets.UTF_8);
      name = name.replace("\0", "");
      int hijackOffset = bs.readWord();
      byte[] oldBytes = readEditBytes(bs);
      byte[] newBytes = readEditBytes(bs);
      byte[] newBytesWithoutBranchBack = Arrays.copyOfRange(newBytes, 0, newBytes.length - 8);
      SeqEdit edit = new SeqEdit(name, hijackOffset, oldBytes, newBytesWithoutBranchBack);
      opcodes.add(new SeqEditOpcode(offset, edit));
    }
    offset = bs.offset();
    byte[] endBytes = bs.readBytes(8);
    opcodes.add(new SectionTitle(offset, endBytes,
        new String(endBytes, StandardCharsets.US_ASCII).strip()));
    return opcodes;
  }

  private static byte[] readEditBytes(ByteStream bs) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[4];
    if (bs.read(buffer) != 4) {
      throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
    }
    while (!Arrays.equals(SeqEdit.STOP, buffer)) {
      baos.write(buffer);
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
      }
    }
    return baos.toByteArray();
  }

  /**
   * Parse and return the opcodes associated with the chr_tbl section. This section is composed of
   * action IDs and their associated offsets. It ends with 0xFFFFFFFF.
   *
   * @param bs    The seq ByteStream.
   * @param title The chr_tbl title.
   * @return The list of opcodes associated with chr_tbl.
   * @throws IOException If an I/O error occurs.
   */
  private static List<Opcode> parseChrTbl(ByteStream bs, SectionTitle title) throws IOException {
    List<Opcode> section = new ArrayList<>();
    section.add(title);
    int id = 0;
    int word = bs.readWord();
    while (word != -1) {
      Type type = getActionType(bs, word);
      section.add(new ActionID(bs.offset() - 4, ByteUtils.fromInt32(word), id++, type));
      word = bs.readWord();
    }
    return section;
  }

  private static Type getActionType(ByteStream bs, int offset) throws IOException {
    int savedOffset = bs.offset();
    try {
      bs.seek(offset);
      int actionCode = bs.peekWord();
      if (actionCode == 0) {
        return Type.UNUSED;
      } else {
        byte[] actual = bs.readNBytes(0x18);
        if (Arrays.equals(CHR_RESET_1, actual) ||
            Arrays.equals(CHR_RESET_2, actual) ||
            Arrays.equals(CHR_RESET_3, actual)) {
          return Type.RESET;
        }
      }
      return Type.NORMAL;
    } finally {
      bs.seek(savedOffset);
    }
  }

  /**
   * Parse and return the opcodes associated with the chr_data section. This section is composed of
   * binary data, that ends with the definition of the next section.
   *
   * @param bs    The seq ByteStream.
   * @param title The chr_data title.
   * @return The list of opcodes associated with chr_data.
   * @throws IOException If an I/O error occurs.
   */
  private static List<Opcode> parseChrData(ByteStream bs, SectionTitle title) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (!isSeqSectionTitle(bs)) {
      baos.write(bs.readBytes(4));
    }
    return List.of(title, new BinaryData(offset, baos.toByteArray()));
  }

  /**
   * Parse and return the opcodes associated with the chr_shot section. This section is composed of
   * 4 bytes of binary data followed by seq opcodes. The binary will be 0x00000004 0x0000000A
   * 0x0000000A and 0x00000000.
   *
   * @param bs    The seq ByteStream.
   * @param title The chr_shot title.
   * @return The chr_shot title and 4-bytes of binary data.
   * @throws IOException If an I/O error occurs.
   */
  private static List<Opcode> parseChrShot(ByteStream bs, SectionTitle title) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = bs.readBytes(4);
    if (!Arrays.equals(new byte[]{0, 0, 0, 0x4}, bytes)) {
      throw new IOException("chr_shot binary not 00000004: " + Arrays.toString(bytes));
    }
    baos.write(bytes);
    bytes = bs.readBytes(4);
    if (!Arrays.equals(new byte[]{0, 0, 0, 0xA}, bytes)) {
      throw new IOException("chr_shot binary not 0000000A: " + Arrays.toString(bytes));
    }
    baos.write(bytes);
    bytes = bs.readBytes(4);
    if (!Arrays.equals(new byte[]{0, 0, 0, 0xA}, bytes)) {
      throw new IOException("chr_shot binary not 0000000A: " + Arrays.toString(bytes));
    }
    baos.write(bytes);
    bytes = bs.readBytes(4);
    if (!Arrays.equals(new byte[]{0, 0, 0, 0}, bytes)) {
      throw new IOException("chr_shot binary not 00000000: " + Arrays.toString(bytes));
    }
    baos.write(bytes);
    return List.of(title, new BinaryData(offset, baos.toByteArray()));
  }
}
