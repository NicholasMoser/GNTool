package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup03;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup06;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup07;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup08;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup0A;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup0B;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup0C;
import com.github.nicholasmoser.gnt4.seq.eft.groups.EftOpcodeGroup10;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup01;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup02;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup03;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup04;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup05;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup06;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup08;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0F;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup10;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup11;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup12;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup13;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup14;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup15;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup16;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1F;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup20;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup21;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup22;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup23;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup24;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup26;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup27;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup28;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup2A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup2B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup31;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup34;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup36;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup37;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup38;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup39;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3F;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup40;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup41;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup42;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup43;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup44;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup46;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup47;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup48;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup49;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup50;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup55;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup56;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup5B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup61;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActiveAttack;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActiveString;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActiveStrings;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.CharacterStats;
import com.github.nicholasmoser.gnt4.seq.opcodes.Combo;
import com.github.nicholasmoser.gnt4.seq.opcodes.ComboList;
import com.github.nicholasmoser.gnt4.seq.opcodes.ExtraData;
import com.github.nicholasmoser.gnt4.seq.opcodes.FileName;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Pop;
import com.github.nicholasmoser.gnt4.seq.opcodes.Push;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SeqHelper {

  // Vanilla Combo translation for Oboro: コンボその
  private static final byte[] COMBO_JAPANESE = new byte[]{(byte) 0x83, 0x52, (byte) 0x83, (byte) 0x93, (byte) 0x83};
  // Vanilla Combo text for all other characters: 連打
  private static final byte[] REPEATED_HITS = new byte[]{(byte) 0x98, 0x41, (byte) 0x92, 0x65, (byte) 0x82};
  // Kosheh Combo translation for most characters
  private static final byte[] COMBO = "Combo".getBytes(StandardCharsets.UTF_8);
  // Kosheh Combo translation for Tayuyu doki demon
  private static final byte[] CHORD = "Chord".getBytes(StandardCharsets.UTF_8);
  // Kosheh Combo translation for Karasu
  private static final byte[] ROUTINE = "Routi".getBytes(StandardCharsets.UTF_8);

  public static Opcode getEftOpcode(ByteStream bs, byte opcodeGroup, byte opcode) throws IOException {
    return switch (opcodeGroup) {
      case 0x00 -> EftOpcodeGroup00.parse(bs, opcode);
      case 0x03 -> EftOpcodeGroup03.parse(bs, opcode);
      case 0x06 -> EftOpcodeGroup06.parse(bs, opcode);
      case 0x07 -> EftOpcodeGroup07.parse(bs, opcode);
      case 0x08 -> EftOpcodeGroup08.parse(bs, opcode);
      case 0x09 -> EftOpcodeGroup09.parse(bs, opcode);
      case 0x0A -> EftOpcodeGroup0A.parse(bs, opcode);
      case 0x0B -> EftOpcodeGroup0B.parse(bs, opcode);
      case 0x0C -> EftOpcodeGroup0C.parse(bs, opcode);
      case 0x10 -> EftOpcodeGroup10.parse(bs, opcode);
      case (byte) 0xCC -> SeqHelper.getNullBytes(bs); // Modding specific no-op
      default -> throw new IllegalStateException(
          String.format("Unknown opcode group: %02X at offset 0x%X", opcodeGroup, bs.offset()));
    };
  }

  public static Opcode getSeqOpcode(ByteStream bs, byte opcodeGroup, byte opcode) throws IOException {
    return switch (opcodeGroup) {
      case 0x00 -> OpcodeGroup00.parse(bs, opcode);
      case 0x01 -> OpcodeGroup01.parse(bs, opcode);
      case 0x02 -> OpcodeGroup02.parse(bs, opcode);
      case 0x03 -> OpcodeGroup03.parse(bs, opcode);
      case 0x04 -> OpcodeGroup04.parse(bs, opcode);
      case 0x05 -> OpcodeGroup05.parse(bs, opcode);
      case 0x06 -> OpcodeGroup06.parse(bs, opcode);
      case 0x08 -> OpcodeGroup08.parse(bs, opcode);
      case 0x09 -> OpcodeGroup09.parse(bs, opcode);
      case 0x0B -> OpcodeGroup0B.parse(bs, opcode);
      case 0x0C -> OpcodeGroup0C.parse(bs, opcode);
      case 0x0E -> OpcodeGroup0E.parse(bs, opcode);
      case 0x0F -> OpcodeGroup0F.parse(bs, opcode);
      case 0x10 -> OpcodeGroup10.parse(bs, opcode);
      case 0x11 -> OpcodeGroup11.parse(bs, opcode);
      case 0x12 -> OpcodeGroup12.parse(bs, opcode);
      case 0x13 -> OpcodeGroup13.parse(bs, opcode);
      case 0x14 -> OpcodeGroup14.parse(bs, opcode);
      case 0x15 -> OpcodeGroup15.parse(bs, opcode);
      case 0x16 -> OpcodeGroup16.parse(bs, opcode);
      case 0x1A -> OpcodeGroup1A.parse(bs, opcode);
      case 0x1B -> OpcodeGroup1B.parse(bs, opcode);
      case 0x1C -> OpcodeGroup1C.parse(bs, opcode);
      case 0x1D -> OpcodeGroup1D.parse(bs, opcode);
      case 0x1E -> OpcodeGroup1E.parse(bs, opcode);
      case 0x1F -> OpcodeGroup1F.parse(bs, opcode);
      case 0x20 -> OpcodeGroup20.parse(bs, opcode);
      case 0x21 -> OpcodeGroup21.parse(bs, opcode);
      case 0x22 -> OpcodeGroup22.parse(bs, opcode);
      case 0x23 -> OpcodeGroup23.parse(bs, opcode);
      case 0x24 -> OpcodeGroup24.parse(bs, opcode);
      case 0x26 -> OpcodeGroup26.parse(bs, opcode);
      case 0x27 -> OpcodeGroup27.parse(bs, opcode);
      case 0x28 -> OpcodeGroup28.parse(bs, opcode);
      case 0x2A -> OpcodeGroup2A.parse(bs, opcode);
      case 0x2B -> OpcodeGroup2B.parse(bs, opcode);
      case 0x31 -> OpcodeGroup31.parse(bs, opcode);
      case 0x34 -> OpcodeGroup34.parse(bs, opcode);
      case 0x36 -> OpcodeGroup36.parse(bs, opcode);
      case 0x37 -> OpcodeGroup37.parse(bs, opcode);
      case 0x38 -> OpcodeGroup38.parse(bs, opcode);
      case 0x39 -> OpcodeGroup39.parse(bs, opcode);
      case 0x3A -> OpcodeGroup3A.parse(bs, opcode);
      case 0x3B -> OpcodeGroup3B.parse(bs, opcode);
      case 0x3C -> OpcodeGroup3C.parse(bs, opcode);
      case 0x3D -> OpcodeGroup3D.parse(bs, opcode);
      case 0x3E -> OpcodeGroup3E.parse(bs, opcode);
      case 0x3F -> OpcodeGroup3F.parse(bs, opcode);
      case 0x40 -> OpcodeGroup40.parse(bs, opcode);
      case 0x41 -> OpcodeGroup41.parse(bs, opcode);
      case 0x42 -> OpcodeGroup42.parse(bs, opcode);
      case 0x43 -> OpcodeGroup43.parse(bs, opcode);
      case 0x44 -> OpcodeGroup44.parse(bs, opcode);
      case 0x46 -> OpcodeGroup46.parse(bs, opcode);
      case 0x47 -> OpcodeGroup47.parse(bs, opcode);
      case 0x48 -> OpcodeGroup48.parse(bs, opcode);
      case 0x49 -> OpcodeGroup49.parse(bs, opcode);
      case 0x4A -> OpcodeGroup4A.parse(bs, opcode);
      case 0x4B -> OpcodeGroup4B.parse(bs, opcode);
      case 0x4C -> OpcodeGroup4C.parse(bs, opcode);
      case 0x4D -> OpcodeGroup4D.parse(bs, opcode);
      case 0x50 -> OpcodeGroup50.parse(bs, opcode);
      case 0x55 -> OpcodeGroup55.parse(bs, opcode);
      case 0x56 -> OpcodeGroup56.parse(bs, opcode);
      case 0x5B -> OpcodeGroup5B.parse(bs, opcode);
      case 0x61 -> OpcodeGroup61.parse(bs, opcode);
      case (byte) 0xCC -> SeqHelper.getNullBytes(bs); // Modding specific no-op
      default -> throw new IllegalStateException(
          String.format("Unknown opcode group: %02X at offset 0x%X", opcodeGroup, bs.offset()));
    };
  }

  /**
   * Returns the type of seq file this is.
   *
   * @param fileName   The name of the seq file from {@link Seqs}
   * @return The type of seq this file is.
   */
  public static SeqType getSeqType(String fileName) {
    if (fileName.startsWith("files/chr/")) {
      return SeqType.CHR_0000;
    }
    return SeqType.OTHER;
  }

  /**
   * Read bytes to see if the next group of bytes are binary data. If they are, return a list of
   * opcodes for them. Otherwise, return an empty list.
   *
   * @param bs The ByteStream to read from.
   * @param opcodes The list of current opcodes.
   * @param seqType The type of seq file this is.
   * @param uniqueBinaries The unique binaries encountered during parsing so far.
   * @return The list of binaries found, if any.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> getBinaries(ByteStream bs, List<Opcode> opcodes, SeqType seqType,
      Set<String> uniqueBinaries) throws IOException {
    // There must be at least one opcode. Grab the last one for comparison.
    if (opcodes.isEmpty()) {
      return Collections.emptyList();
    }
    int offset = bs.offset();
    Opcode lastOpcode = opcodes.get(opcodes.size() - 1);

    if (seqType == SeqType.CHR_0000) {
      // These binaries will always be after a specific opcode
      if (lastOpcode instanceof ComboList) {
        // Make sure this is the last combo list
        if (SeqHelper.isComboList(bs)) {
          return Collections.singletonList(SeqHelper.readComboList(bs));
        }
        // There is binary data after the combo list that leads to the last set of opcodes
        return Collections.singletonList(SeqHelper.getBinaryUntilBranchAndLink(bs));
      }

      if (lastOpcode instanceof BranchLinkReturn) {
        if (SeqHelper.isChrLongBinary(opcodes)) {
          if (uniqueBinaries.contains("foundChrLongBinary")) {
            throw new IllegalStateException("There should only be one chr long binary.");
          }
          uniqueBinaries.add("foundChrLongBinary");
          return Collections.singletonList(SeqHelper.getChrLongBinary(bs));
        } else if (SeqHelper.isComboList(bs)) {
          return Collections.singletonList(SeqHelper.readComboList(bs));
        } else if (SeqHelper.isUnknownBinary3(bs)) {
          if (uniqueBinaries.contains("foundUnknownBinary3")) {
            throw new IllegalStateException("There should only be one unknown binary 3.");
          }
          uniqueBinaries.add("foundUnknownBinary3");
          return Collections.singletonList(SeqHelper.readUnknownBinary3(bs));
        } else if (SeqHelper.isUnknownBinary1(bs)) {
          if (uniqueBinaries.contains("foundUnknownBinary1")) {
            throw new IllegalStateException("There should only be one unknown binary 1.");
          }
          uniqueBinaries.add("foundUnknownBinary1");
          //return Collections.singletonList(SeqHelper.readUnknownBinary1(bs));
          return SeqHelper.readUnknownBinary1(bs);
        } else if (SeqHelper.isUnknownBinary4(bs)) {
          if (uniqueBinaries.contains("foundUnknownBinary4")) {
            throw new IllegalStateException("There should only be one unknown binary 4.");
          }
          uniqueBinaries.add("foundUnknownBinary4");
          return Collections.singletonList(SeqHelper.readUnknownBinary4(bs));
        } else if (SeqHelper.isUnknownBinary5(bs)) {
          if (uniqueBinaries.contains("foundUnknownBinary5")) {
            throw new IllegalStateException("There should only be one unknown binary 5.");
          }
          uniqueBinaries.add("foundUnknownBinary5");
          //return Collections.singletonList(SeqHelper.readUnknownBinary5(bs));
          return SeqHelper.readUnknownBinary5(bs);
        } else if (SeqHelper.isUnknownBinary6(bs)) {
          if (uniqueBinaries.contains("foundUnknownBinary6")) {
            throw new IllegalStateException("There should only be one unknown binary 6.");
          }
          uniqueBinaries.add("foundUnknownBinary6");
          return Collections.singletonList(SeqHelper.readUnknownBinary6(bs));
        }
      }

      // These binaries can be after multiple different opcodes
      if (SeqHelper.isOp04700Binary(bs)) {
        byte[] bytes = bs.readBytes(0x10);
        return Collections.singletonList(new BinaryData(offset, bytes, " (Binary data referenced by op_4700)"));
      } else if (SeqHelper.isUnknownBinary2(bs)) {
        byte[] bytes = bs.readBytes(0x10);
        return Collections.singletonList(new BinaryData(offset, bytes));
      }
    }
    return Collections.emptyList();
  }

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
    int word = bs.peekWord();
    // Need to also check for 0x01320000 since that is used by seq editing
    while (word != 0x013C0000 && word != 0x01320000) {
      baos.write(bs.readBytes(4));
      word = bs.peekWord();
    }
    return new BinaryData(offset, baos.toByteArray());
  }

  /**
   * Reads a currently unknown 16-byte struct. The first three bytes are read at instructions
   * 0x8010704c, 0x80107050, and 0x80107054, which is used in opcodes such as op_4700.
   *
   * @param bs The ByteStream to read from.
   * @return If this is the binary for op_4700.
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
    // Check SCON4 expected first
    byte[] expected = new byte[]{0x00, 0x20, 0x46, (byte) 0x80, 0x00, (byte) 0xF0, 0x00, 0x04};
    byte[] bytes = bs.peekBytes(0x8);
    if (Arrays.equals(expected, bytes)) {
      return true;
    }
    // Then check GNT4 expected
    expected = new byte[]{0x00, 0x20, 0x56, (byte) 0x80, 0x00, (byte) 0xF0, 0x00, 0x04};
    bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Reads the binary data for unknown binary 1.
   *
   * @param bs The ByteStream to read from.
   * @return The binary data for unknown binary 1.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> readUnknownBinary1(ByteStream bs) throws IOException {
    List<Opcode> data = new LinkedList<>();
    while (!Arrays.equals(bs.peekBytes(4), new byte[]{0x00, 0x00, 0x00, 0x00})) {
      data.add(new ActiveAttack(bs.offset(), bs.readBytes(20)));
    }
    data.add(new ActiveAttack(bs.offset(), bs.readBytes(20)));
    List<Integer> edl = new LinkedList<>();
    for (Opcode op : data) {
      ActiveAttack aa = (ActiveAttack) op;
      if (!edl.contains(aa.getExtra_data_offset())) {
        int offset = aa.getExtra_data_offset();
        if (offset < bs.length() && offset > 0) {
          // Ignore weird offsets, such as for the Attack condition terminator at the end of Attack
          // conditions.
          edl.add(aa.getExtra_data_offset());
        }
      }
    }
    Collections.sort(edl);
    for (int i : edl) {
      bs.seek(i);
      data.add(new ExtraData(bs));
    }
    while (Arrays.equals(bs.peekBytes(4), new byte[]{0x00, 0x01, 0x00, 0x0A})) {
      ExtraData ed = new ExtraData(bs);
      data.add(ed);
    }
    if (bs.offset()%4 != 0) {
      bs.skip(4 - (bs.offset() % 4));
    }
    ActiveStrings as = new ActiveStrings(bs);
    data.add(as);
    List<Integer> stringOffsets = new LinkedList<>();
    for (int i : as.getStringOffsets()) {
      stringOffsets.add(i);
    }
    Collections.sort(stringOffsets);
    for (Integer i : stringOffsets) {
      if (i <= 0 || i > bs.length()) {
        continue;
      }
      bs.seek(i);
      ActiveString activeString = new ActiveString(bs);
      data.add(activeString);
    }
    while (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x01})) {
      data.add(new ActiveString(bs));
    }
    if (bs.offset()%4 != 0) {
      bs.skip(4 - (bs.offset() % 4));
    }
    data.add(new CharacterStats(bs));
    return data;
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 2.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 2.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary2(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x10) {
      return false;
    }
    byte[] expected = new byte[]{0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00,
        0x10, 0x00, 0x00, 0x00, 0x00};
    byte[] bytes = bs.peekBytes(0x10);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 3.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 3.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary3(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x7B0) {
      return false;
    }
    byte[] expected = new byte[]{0x00, 0x5A, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02};
    byte[] bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 3.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 3.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode readUnknownBinary3(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = new byte[0x7B0];
    if (bs.read(bytes) != 0x7B0) {
      throw new IllegalStateException("Failed to read 0x7B0 bytes");
    } else if (bytes[0x7AD] != 0x7E) {
      throw new IllegalStateException("Third last byte should be 0x7E at offset " + bs.offset());
    }
    return new BinaryData(offset, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 4.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 4.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary4(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x3C0) {
      return false;
    }
    byte[] expected = new byte[]{0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    byte[] bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 4.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 4.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode readUnknownBinary4(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = new byte[0x3C0];
    if (bs.read(bytes) != 0x3C0) {
      throw new IllegalStateException("Failed to read 0x3C0 bytes");
    } else if (bytes[0x3A8] != 0x43) {
      throw new IllegalStateException("Byte at size minus 0x18 should be 0x43");
    }
    return new BinaryData(offset, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 5.
   * Should probably change name to something more fitting
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 5.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary5(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x1E4) {
      return false;
    }
    byte[] expected = new byte[]{0x00, 0x01, 0x00, 0x00, 0x63, 0x68, 0x72, 0x2F};
    byte[] bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 5.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 5.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> readUnknownBinary5(ByteStream bs) throws IOException {
    List<Opcode> filenames = new LinkedList<>();
    filenames.add(UnknownOpcode.of(4, bs));
    int offset = bs.offset();
    byte[] bytes = new byte[0x1E0];
    if (bs.read(bytes) != 0x1E0) {
      throw new IllegalStateException("Failed to read 0x1E0 bytes");
    } else if (bytes[0x1DB] != 0x66) {
      throw new IllegalStateException("Byte at size minus 0x5 should be 0x66");
    }
    ByteStream localBs = new ByteStream(bytes);
    StringBuilder sb = new StringBuilder();
    int off = 0;
    while (localBs.bytesAreLeft()) {
      byte b = localBs.peekBytes(1)[0];
      if (b == 0) {
        filenames.add(new FileName(offset + off, sb.toString()));
        sb = new StringBuilder();
        while (b == 0) {
          localBs.skip(1);
          try {
            b = localBs.peekBytes(1)[0];
          } catch (Exception e) {
            break;
          }
        }
        off = localBs.offset();
        continue;
      }
      sb.append((char) b);
      localBs.skip(1);
    }
    return filenames;
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 6.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 6.
   * @throws IOException If an I/O error occurs.
   */
  public static boolean isUnknownBinary6(ByteStream bs) throws IOException {
    if (bs.length() - bs.offset() < 0x2C) {
      return false;
    }
    byte[] expected = new byte[]{0x00, 0x00, 0x0B, 0x0D, 0x00, 0x00, 0x0B, 0x0F};
    byte[] bytes = bs.peekBytes(0x8);
    return Arrays.equals(expected, bytes);
  }

  /**
   * Returns if the ByteStream is currently at unknown binary 6.
   *
   * @param bs The ByteStream to read from.
   * @return If the ByteStream is at unknown binary 6.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode readUnknownBinary6(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (bs.peekWord() != 0x0908023F) {
      baos.write(bs.readBytes(4));
    }
    byte[] bytes = baos.toByteArray();
    int len = bytes.length;
    if (len != 0x2C) {
      throw new IllegalStateException("Unknown binary 6 must be of size 0x2C but is size " + len);
    }
    return new BinaryData(offset, bytes);
  }

  /**
   * The chr long binary is the longest binary data block in a chr file. It will come after a
   * function that has the following opcodes in the following order: push, push, bl, pop, pop, blr.
   *
   * @param opcodes The list of opcodes parsed so far.
   * @return If the next data is the unknown long binary in a chr 0000.seq file.
   */
  public static boolean isChrLongBinary(List<Opcode> opcodes) {
    if (opcodes.size() < 6) {
      return false;
    }
    int startPos = opcodes.size() - 1;
    return opcodes.get(startPos--) instanceof BranchLinkReturn
        && opcodes.get(startPos--) instanceof Pop
        && opcodes.get(startPos--) instanceof Pop
        && opcodes.get(startPos--) instanceof BranchLink
        && opcodes.get(startPos--) instanceof Push
        && opcodes.get(startPos) instanceof Push;
  }

  /**
   * Reads the the longest chr binary data block in a chr file. It is terminated with a movc
   * (0x04021366).
   *
   * @param bs The ByteStream to read from.
   * @return The binary data for the long chr 0000.seq data.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode getChrLongBinary(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (bs.peekWord() != 0x04021366) {
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
    return Arrays.equals(bytes, COMBO) || Arrays.equals(bytes, CHORD) ||
        Arrays.equals(bytes, ROUTINE) || Arrays.equals(bytes, COMBO_JAPANESE) ||
        Arrays.equals(bytes, REPEATED_HITS);
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
    int comboNum = 1;
    while (!Arrays.equals(end, bytes)) {
      baos.write(bytes);
      if (bytes[3] == 0) {
        if (parsingName) {
          parsingName = false;
        } else {
          String combo = getComboText(baos.toByteArray());
          String info = String.format("Combo %d %s ", comboNum++, combo);
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
   * Gets the combo text for the given bytes. It will usually start with zeroes, followed by bytes
   * that correlate to the text for "Combo", followed by one or more zeroes, followed by the combo
   * itself. The combo can be represented in ASCII for the most part.
   *
   * @param bytes The bytes to parse the combo text for.
   * @return The combo in ASCII text, for the most party.
   */
  private static String getComboText(byte[] bytes) {
    boolean firstZeroes = true;
    boolean name = false;
    boolean theRest = false;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (byte chr : bytes) {
      if (firstZeroes && chr != 0) {
        firstZeroes = false;
        name = true;
      } else if (name && chr == 0) {
        name = false;
        theRest = true;
      } else if (theRest && chr != 0) {
        baos.write(chr);
      }
    }
    return baos.toString(StandardCharsets.US_ASCII);
  }

  public static List<ContainerTag> getOpcodesString(byte[] bytes, int newBytesOffset) throws IOException {
    // This is a pretty nasty hack to get the offsets in the HTML correct, basically add null bytes
    // in the byte stream before the start of the new bytes to ensure that they are correct.
    byte[] fullBytes = new byte[newBytesOffset + bytes.length];
    System.arraycopy(bytes, 0, fullBytes, newBytesOffset, bytes.length);
    ByteStream bs = new ByteStream(fullBytes);
    bs.skip(newBytesOffset);

    List<ContainerTag> tags = new ArrayList<>();
    while (bs.bytesAreLeft()) {
      bs.mark();
      byte opcodeGroup = (byte) bs.read();
      byte opcode = (byte) bs.read();
      bs.reset();
      tags.add(getSeqOpcode(bs, opcodeGroup, opcode).toHTML());
    }
    return tags;
  }

  /**
   * Returns null bytes as binary data, where the null bytes are 0xCCCCCCCC. This is currently
   * only used in mods in Super Clash of Ninja 4 (SCON4).
   *
   * @param bs The ByteStream to read from.
   * @return The null bytes as an opcode.
   * @throws IOException If an I/O error occurs.
   */
  public static Opcode getNullBytes(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int word = bs.peekWord();
    if (word != 0xCCCCCCCC) {
      throw new IllegalStateException("Null word is not 0xCCCCCCCC at offset " + offset);
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (bs.peekWord() == 0xCCCCCCCC) {
      baos.write(bs.readBytes(4));
    }
    return new BinaryData(offset, baos.toByteArray(), " null bytes");
  }

  /**
   * Read the string bytes from the byte stream. Reads a word at a time and terminates when any byte
   * in the word is 0.
   *
   * @param bs The byte stream to read from.
   * @return The string bytes.
   * @throws IOException If there was a failure parsing the string bytes.
   */
  public static byte[] readString(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[4];
    do {
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to parse bytes at offset " + offset);
      }
      baos.write(buffer);
    } while(buffer[0] != 0 && buffer [1] != 0 && buffer[2] != 0 && buffer[3] != 0);
    return baos.toByteArray();
  }

  /**
   * Given bytes from {@link #readString(ByteStream)}, return the shift-jis string.
   *
   * @param bytes The bytes read from an seq byte stream.
   * @return The shift-jis string.
   * @throws UnsupportedEncodingException If shift-jis is not supported.
   */
  public static String getString(byte[] bytes) throws UnsupportedEncodingException {
    String text = new String(bytes, "shift-jis");
    return text.replace("\0", "").replace("\n", "\\n");
  }

  /**
   * Checks the operands for a chr operand and immediate operand. If so, checks the chr field to
   * see if it has known fields. If so, returns a better description including the name of the
   * field values. Otherwise returns an empty optional.
   *
   * @param ea The two operands
   * @return given known chr_p fields, optional more descriptive description
   */
  public static Optional<String> getChrFieldDescription(SEQ_RegCMD2 ea) throws IOException {
    if (ea.getFirstOperand() instanceof ChrOperand chr && ea.getSecondOperand() instanceof ImmediateOperand immediate) {
      int value = immediate.getImmediateValue();
      if (chr.get() == 0x1C) { // chr_id
        String chrName = GNT4Characters.INTERNAL_CHAR_ORDER.inverse().get(value);
        if (chrName == null) {
          throw new IOException("Unknown character for chr_id " + value);
        }
        return Optional.of(String.format("%s, %s (0x%X)", chr, chrName, value));
      } else if (chr.get() == 0x23C) { // act_id
        String action = Seq.getActionDescription(value);
        return Optional.of(String.format("%s, %s", chr, action));
      } else if (chr.get() == 0x3BE) { // current_buttons_held
        String buttons = Seq.getButtonDescriptions(value);
        return Optional.of(String.format("%s, %s", chr, buttons));
      }
    }
    return Optional.empty();
  }
}
