package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.ActionID;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.SectionTitle;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.Charsets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeqSection {

  public static boolean isSeqSectionTitle(ByteStream bs) throws IOException {
    byte[] word = bs.peekWordBytes();
    String startOfTitle = new String(word, Charsets.US_ASCII);
    return "chr_".equals(startOfTitle);
  }

  /**
   * Handles the current SEQ section. First reads the section title, which ends with 0x0A and is
   * 16-byte aligned.
   *
   * @param bs
   * @throws IOException
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
      case (Seq.CHR_ACT):
      case (Seq.CHR_CAM):
      case (Seq.CHR_SUB02):
      case (Seq.CHR_MODEL):
      case (Seq.CHR_MOT):
      case (Seq.CHR_HIRA):
      case (Seq.CHR_SEL):
      case (Seq.CHR_SHOT):
      case (Seq.CHR_FACE):
      case (Seq.CHR_DATA):
      case (Seq.CHR_VISUAL2D):
        return Collections.singletonList(sectionTitle);
      default:
        throw new IOException("Unknown seq section: " + title);
    }
  }

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

}
