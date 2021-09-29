package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.opcodes.ActionID;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SectionTitle;
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

  /**
   * Returns if the next 4-byte word is the start of a new seq section.
   *
   * @param bs The seq ByteStream.
   * @return If the next 4-byte word is the start of a new seq section.
   * @throws IOException
   */
  public static boolean isSeqSectionTitle(ByteStream bs) throws IOException {
    byte[] word = bs.peekWordBytes();
    String startOfTitle = new String(word, Charsets.US_ASCII);
    return "chr_".equals(startOfTitle);
  }

  /**
   * Handles the current SEQ section. First reads the section title, which ends with 0x0A and is
   * 16-byte aligned.
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
      default:
        throw new IOException("Unknown seq section: " + title);
    }
  }

  /**
   * Parse and return the opcodes associated with the chr_tbl section. This section is composed of
   * action IDs and their associated offsets. It ends with 0xFFFFFFFF.
   *
   * @param bs The seq ByteStream.
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
      section.add(new ActionID(bs.offset() - 4, ByteUtils.fromInt32(word), id++));
      word = bs.readWord();
    }
    return section;
  }

  /**
   * Parse and return the opcodes associated with the chr_data section. This section is composed of
   * binary data, that ends with the definition of the next section.
   *
   * @param bs The seq ByteStream.
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
   * @param bs The seq ByteStream.
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
