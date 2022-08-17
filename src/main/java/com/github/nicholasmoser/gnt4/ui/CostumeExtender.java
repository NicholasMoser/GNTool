package com.github.nicholasmoser.gnt4.ui;

import static com.github.nicholasmoser.gnt4.GNT4Characters.INTERNAL_CHAR_ORDER;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The SEQ bytes for checking costumes 3 and 4 is 0x40 bytes each.
 */
public class CostumeExtender {

  // SEQ opcodes and offsets used for adding custom costumes
  static final byte[] P1_VAR = ByteUtils.hexStringToBytes("7FFFFF20");
  static final byte[] P2_VAR = ByteUtils.hexStringToBytes("7FFFFF21");
  static final byte[] P3_VAR = ByteUtils.hexStringToBytes("7FFFFF22");
  static final byte[] P4_VAR = ByteUtils.hexStringToBytes("7FFFFF23");
  static final byte[] VAR24 = ByteUtils.hexStringToBytes("7FFFFF24");
  static final byte[] VAR27 = ByteUtils.hexStringToBytes("7FFFFF27");
  static final byte[] OPCODE1 = ByteUtils.hexStringToBytes("3C160000");
  static final byte[] OPCODE2 = ByteUtils.hexStringToBytes("3B010000");
  static final byte[] CHARSEL_UNKNOWN_OFFSET = ByteUtils.hexStringToBytes("0000F490");
  static final byte[] CHARSEL4_UNKNOWN_OFFSET = ByteUtils.hexStringToBytes("000140A0");
  static final byte[] CHARSEL_C3P1_OFFSET = ByteUtils.hexStringToBytes("0000ED50");
  static final byte[] CHARSEL_C4P1_OFFSET = ByteUtils.hexStringToBytes("0000ED80");
  static final byte[] CHARSEL_C3P2_OFFSET = ByteUtils.hexStringToBytes("0000EE98");
  static final byte[] CHARSEL_C4P2_OFFSET = ByteUtils.hexStringToBytes("0000EEC8");
  static final byte[] CHARSEL4_C3P1_OFFSET = ByteUtils.hexStringToBytes("00007A38");
  static final byte[] CHARSEL4_C4P1_OFFSET = ByteUtils.hexStringToBytes("00007AA8");
  static final byte[] CHARSEL4_C3P2_OFFSET = ByteUtils.hexStringToBytes("00007B68");
  static final byte[] CHARSEL4_C4P2_OFFSET = ByteUtils.hexStringToBytes("00007BD8");
  static final byte[] CHARSEL4_C3P3_OFFSET = ByteUtils.hexStringToBytes("00007C98");
  static final byte[] CHARSEL4_C4P3_OFFSET = ByteUtils.hexStringToBytes("00007D08");
  static final byte[] CHARSEL4_C3P4_OFFSET = ByteUtils.hexStringToBytes("00007DC8");
  static final byte[] CHARSEL4_C4P4_OFFSET = ByteUtils.hexStringToBytes("00007E38");
  static final byte[] CHARSEL_ALT_MODEL_OFFSET = ByteUtils.hexStringToBytes("00005750");
  static final byte[] CHARSEL4_ALT_MODEL_OFFSET = ByteUtils.hexStringToBytes("00004B14");

  // Existing SEQ offsets and code for costume extension
  static final String C3P1_1V1_NAME = "Extend Costume 3, Player 1 (1v1)";
  static final int C3P1_1V1_OFFSET = 0xECC8;
  static final byte[] C3P1_1V1_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 0000F490 7FFFFF20 7FFFFF24
      3B010000 0000ED50 00000007 7FFFFF24
      3B010000 0000ED50 00000002 7FFFFF24
      3B010000 0000ED50 00000009 7FFFFF24
      """);

  static final String C4P1_1V1_NAME = "Extend Costume 4, Player 1 (1v1)";
  static final int C4P1_1V1_OFFSET = 0xED0C;
  static final byte[] C4P1_1V1_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 0000F490 7FFFFF20 7FFFFF24
      3B010000 0000ED80 00000007 7FFFFF24
      3B010000 0000ED80 00000002 7FFFFF24
      3B010000 0000ED80 00000009 7FFFFF24
      """);

  static final String C3P2_1V1_NAME = "Extend Costume 3, Player 2 (1v1)";
  static final int C3P2_1V1_OFFSET = 0xEE10;
  static final byte[] C3P2_1V1_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 0000F490 7FFFFF21 7FFFFF24
      3B010000 0000EE98 00000007 7FFFFF24
      3B010000 0000EE98 00000002 7FFFFF24
      3B010000 0000EE98 00000009 7FFFFF24
      """);

  static final String C4P2_1V1_NAME = "Extend Costume 4, Player 2 (1v1)";
  static final int C4P2_1V1_OFFSET = 0xEE54;
  static final byte[] C4P2_1V1_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 0000F490 7FFFFF21 7FFFFF24
      3B010000 0000EEC8 00000007 7FFFFF24
      3B010000 0000EEC8 00000002 7FFFFF24
      3B010000 0000EEC8 00000009 7FFFFF24
      """);

  static final String C3P1_4P_NAME = "Extend Costume 3, Player 1 (4p)";
  static final int C3P1_4P_OFFSET = 0x79F0;
  static final byte[] C3P1_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF20 7FFFFF24
      3B010000 00007A38 00000007 7FFFFF24
      3B010000 00007A38 00000002 7FFFFF24
      3B010000 00007A38 00000009 7FFFFF24
      """);

  static final String C4P1_4P_NAME = "Extend Costume 4, Player 1 (4p)";
  static final int C4P1_4P_OFFSET = 0x7A60;
  static final byte[] C4P1_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF20 7FFFFF24
      3B010000 00007AA8 00000007 7FFFFF24
      3B010000 00007AA8 00000002 7FFFFF24
      3B010000 00007AA8 00000009 7FFFFF24
      """);

  static final String C3P2_4P_NAME = "Extend Costume 3, Player 2 (4p)";
  static final int C3P2_4P_OFFSET = 0x7B20;
  static final byte[] C3P2_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF21 7FFFFF24
      3B010000 00007B68 00000007 7FFFFF24
      3B010000 00007B68 00000002 7FFFFF24
      3B010000 00007B68 00000009 7FFFFF24
      """);

  static final String C4P2_4P_NAME = "Extend Costume 4, Player 2 (4p)";
  static final int C4P2_4P_OFFSET = 0x7B90;
  static final byte[] C4P2_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF21 7FFFFF24
      3B010000 00007BD8 00000007 7FFFFF24
      3B010000 00007BD8 00000002 7FFFFF24
      3B010000 00007BD8 00000009 7FFFFF24
      """);

  static final String C3P3_4P_NAME = "Extend Costume 3, Player 3 (4p)";
  static final int C3P3_4P_OFFSET = 0x7C50;
  static final byte[] C3P3_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF22 7FFFFF24
      3B010000 00007C98 00000007 7FFFFF24
      3B010000 00007C98 00000002 7FFFFF24
      3B010000 00007C98 00000009 7FFFFF24
      """);

  static final String C4P3_4P_NAME = "Extend Costume 4, Player 3 (4p)";
  static final int C4P3_4P_OFFSET = 0x7CC0;
  static final byte[] C4P3_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF22 7FFFFF24
      3B010000 00007D08 00000007 7FFFFF24
      3B010000 00007D08 00000002 7FFFFF24
      3B010000 00007D08 00000009 7FFFFF24
      """);

  static final String C3P4_4P_NAME = "Extend Costume 3, Player 4 (4p)";
  static final int C3P4_4P_OFFSET = 0x7D80;
  static final byte[] C3P4_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF23 7FFFFF24
      3B010000 00007DC8 00000007 7FFFFF24
      3B010000 00007DC8 00000002 7FFFFF24
      3B010000 00007DC8 00000009 7FFFFF24
      """);

  static final String C4P4_4P_NAME = "Extend Costume 4, Player 4 (4p)";
  static final int C4P4_4P_OFFSET = 0x7DF0;
  static final byte[] C4P4_4P_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 000140A0 7FFFFF23 7FFFFF24
      3B010000 00007E38 00000007 7FFFFF24
      3B010000 00007E38 00000002 7FFFFF24
      3B010000 00007E38 00000009 7FFFFF24
      """);

  static final String ALT_MODEL_1V1_NAME = "Use Alternate Models (1v1)";
  static final int ALT_MODEL_1V1_OFFSET = 0x572C;
  static final byte[] ALT_MODEL_1V1_BYTES = ByteUtils.hexTextToBytes("""
      3B010000 00005750 00000007 7FFFFF27
      3B010000 00005750 00000009 7FFFFF27
      """);

  static final String ALT_MODEL_4P_NAME = "Use Alternate Models (4p)";
  static final int ALT_MODEL_4P_OFFSET = 0x4AF0;
  static final byte[] ALT_MODEL_4P_BYTES = ByteUtils.hexTextToBytes("""
      3B010000 00004B14 00000007 7FFFFF27
      3B010000 00004B14 00000009 7FFFFF27
      """);

  private static final Set<String> CODE_NAMES = Set.of(C3P1_1V1_NAME, C4P1_1V1_NAME, C3P2_1V1_NAME,
      C4P2_1V1_NAME, C3P1_4P_NAME, C4P1_4P_NAME, C3P2_4P_NAME, C4P2_4P_NAME, C3P3_4P_NAME,
      C4P3_4P_NAME, C3P4_4P_NAME, C4P4_4P_NAME);

  public static byte[] getCodeBytes(List<String> characters, int costume, int player,
      boolean fourPlayerMode) throws IOException {
    // Get input bytes
    byte[] playerVar;
    byte[] costumeVar;
    switch (player) {
      case 1 -> {
        playerVar = P1_VAR;
        if (fourPlayerMode) {
          if (costume == 3) {
            costumeVar = CHARSEL4_C3P1_OFFSET;
          } else if (costume == 4) {
            costumeVar = CHARSEL4_C4P1_OFFSET;
          } else {
            throw new IOException("Unknown costume id " + costume);
          }
        } else {
          if (costume == 3) {
            costumeVar = CHARSEL_C3P1_OFFSET;
          } else if (costume == 4) {
            costumeVar = CHARSEL_C4P1_OFFSET;
          } else {
            throw new IOException("Unknown costume id " + costume);
          }
        }
      }
      case 2 -> {
        playerVar = P2_VAR;
        if (fourPlayerMode) {
          if (costume == 3) {
            costumeVar = CHARSEL4_C3P2_OFFSET;
          } else if (costume == 4) {
            costumeVar = CHARSEL4_C4P2_OFFSET;
          } else {
            throw new IOException("Unknown costume id " + costume);
          }
        } else {
          if (costume == 3) {
            costumeVar = CHARSEL_C3P2_OFFSET;
          } else if (costume == 4) {
            costumeVar = CHARSEL_C4P2_OFFSET;
          } else {
            throw new IOException("Unknown costume id " + costume);
          }
        }
      }
      case 3 -> {
        playerVar = P3_VAR;
        if (costume == 3) {
          costumeVar = CHARSEL4_C3P3_OFFSET;
        } else if (costume == 4) {
          costumeVar = CHARSEL4_C4P3_OFFSET;
        } else {
          throw new IOException("Unknown costume id " + costume);
        }
      }
      case 4 -> {
        playerVar = P4_VAR;
        if (costume == 3) {
          costumeVar = CHARSEL4_C3P4_OFFSET;
        } else if (costume == 4) {
          costumeVar = CHARSEL4_C4P4_OFFSET;
        } else {
          throw new IOException("Unknown costume id " + costume);
        }
      }
      default -> throw new IOException("Unknown player id " + player);
    }
    // Write bytes
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bytes.write(OPCODE1);
    bytes.write(fourPlayerMode ? CHARSEL4_UNKNOWN_OFFSET : CHARSEL_UNKNOWN_OFFSET);
    bytes.write(playerVar);
    bytes.write(VAR24);
    for (String character : characters) {
      Integer id = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
      if (id == null) {
        throw new IOException("Unable to find id for character " + character);
      }
      bytes.write(OPCODE2);
      bytes.write(costumeVar);
      bytes.write(ByteUtils.fromInt32(id));
      bytes.write(VAR24);
    }
    return bytes.toByteArray();
  }

  /**
   * Returns if the character select for 1v1 and 4-player mode have costume extensions. Throws an
   * IOException if there are inconsistencies between the two list of SEQ edits.
   *
   * @param charSelEdits  The SEQ edits for char_sel.seq
   * @param charSel4Edits The SEQ edits for charsel4.seq
   * @return If the character select SEQ edits have costume extensions.
   * @throws IOException If there are inconsistencies between the two list of SEQ edits.
   */
  public static boolean hasExistingEdits(List<SeqEdit> charSelEdits, List<SeqEdit> charSel4Edits)
      throws IOException {
    boolean c3Edit = charSelEdits.stream().anyMatch(edit -> edit.getName().equals(C3P1_1V1_NAME));
    boolean c4Edit = charSelEdits.stream().anyMatch(edit -> edit.getName().equals(C4P1_1V1_NAME));

    // Verify edits are consistent
    if (c3Edit) {
      // Costume 3 has extensions
      if (charSelEdits.stream().noneMatch(edit -> edit.getName().equals(C3P2_1V1_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C3P2_1V1_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C3P1_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C3P1_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C3P2_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C3P2_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C3P3_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C3P3_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C3P4_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C3P4_4P_NAME);
      }
    } else {
      // Costume 3 does not have extensions
      if (charSelEdits.stream().anyMatch(edit -> edit.getName().equals(C3P2_1V1_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C3P2_1V1_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C3P1_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C3P1_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C3P2_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C3P2_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C3P3_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C3P3_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C3P4_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C3P4_4P_NAME);
      }
    }
    if (c4Edit) {
      // Costume 4 has extensions
      if (charSelEdits.stream().noneMatch(edit -> edit.getName().equals(C4P2_1V1_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C4P2_1V1_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C4P1_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C4P1_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C4P2_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C4P2_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C4P3_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C4P3_4P_NAME);
      } else if (charSel4Edits.stream().noneMatch(edit -> edit.getName().equals(C4P4_4P_NAME))) {
        throw new IOException("Please log an issue; missing code: " + C4P4_4P_NAME);
      }
    } else {
      // Costume 4 does not have extensions
      if (charSelEdits.stream().anyMatch(edit -> edit.getName().equals(C4P2_1V1_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C4P2_1V1_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C4P1_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C4P1_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C4P2_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C4P2_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C4P3_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C4P3_4P_NAME);
      } else if (charSel4Edits.stream().anyMatch(edit -> edit.getName().equals(C4P4_4P_NAME))) {
        throw new IOException("Please log an issue; should not have code: " + C4P4_4P_NAME);
      }
    }

    return c3Edit || c4Edit;
  }

  /**
   * Given a list of char_sel.seq edits, return the characters with costume 3 extensions.
   *
   * @param charSelEdits The list of char_sel.seq edits.
   * @return The characters with costume 3 extensions.
   * @throws IOException If any I/O exception occurs.
   */
  public static List<String> getCostumeThreeCharacters(List<SeqEdit> charSelEdits)
      throws IOException {
    List<String> characters = new ArrayList<>();
    Optional<SeqEdit> edit = charSelEdits.stream()
        .filter(x -> x.getName().equals(C3P1_1V1_NAME))
        .findFirst();
    if (edit.isEmpty()) {
      throw new IOException("Unable to find SEQ edit " + C3P1_1V1_NAME);
    }
    Map<Integer, String> idToChr = GNT4Characters.INTERNAL_CHAR_ORDER.inverse();
    byte[] bytes = edit.get().getNewBytes();
    int charCount = (bytes.length - 16) / 16;
    for (int i = 0; i < charCount; i++) {
      int offset = 0x1B + (i * 0x10);
      int chrId = bytes[offset];
      String chr = idToChr.get(chrId);
      if (chr == null) {
        throw new IOException("Unable to find character with id " + chrId);
      }
      characters.add(chr);
    }
    return characters;
  }

  /**
   * Given a list of char_sel.seq edits, return the characters with costume 4 extensions.
   *
   * @param charSelEdits The list of char_sel.seq edits.
   * @return The characters with costume 4 extensions.
   * @throws IOException If any I/O exception occurs.
   */
  public static List<String> getCostumeFourCharacters(List<SeqEdit> charSelEdits)
      throws IOException {
    List<String> characters = new ArrayList<>();
    Optional<SeqEdit> edit = charSelEdits.stream()
        .filter(x -> x.getName().equals(C4P1_1V1_NAME))
        .findFirst();
    if (edit.isEmpty()) {
      throw new IOException("Unable to find SEQ edit " + C4P1_1V1_NAME);
    }
    Map<Integer, String> idToChr = GNT4Characters.INTERNAL_CHAR_ORDER.inverse();
    byte[] bytes = edit.get().getNewBytes();
    int charCount = (bytes.length - 16) / 16;
    for (int i = 0; i < charCount; i++) {
      int offset = 0x1B + (i * 0x10);
      int chrId = bytes[offset];
      String chr = idToChr.get(chrId);
      if (chr == null) {
        throw new IOException("Unable to find character with id " + chrId);
      }
      characters.add(chr);
    }
    return characters;
  }

  /**
   * Remove all costume extension codes from char_sel.seq and charsel4.seq
   *
   * @param charSel The path to char_sel.seq
   * @param charSel4 The path to charsel4.seq
   * @throws IOException If any I/O exception occurs.
   */
  public static void removeCodes(Path charSel, Path charSel4) throws IOException {
    for (SeqEdit code : SeqExt.getEdits(charSel)) {
      if (CODE_NAMES.contains(code.getName())) {
        SeqExt.removeEdit(code, charSel);
        System.out.println("Removed code " + code + " from " + charSel);
      }
    }
    for (SeqEdit code : SeqExt.getEdits(charSel4)) {
      if (CODE_NAMES.contains(code.getName())) {
        SeqExt.removeEdit(code, charSel4);
        System.out.println("Removed code " + code + " from " + charSel4);
      }
    }
  }

  /**
   * For char_sel.seq and charsel4.seq, write new costume 3 extensions code for the given list of
   * characters.
   *
   * @param charSel The path to char_sel.seq
   * @param charSel4 The path to charsel4.seq
   * @param characters The list of characters to give costume 3 to.
   * @throws IOException If any I/O exception occurs.
   */
  public static void writeCostumeThreeCodes(Path charSel, Path charSel4, List<String> characters)
      throws IOException {
    byte[] bytes = CostumeExtender.getCodeBytes(characters, 3, 1, false);
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(C3P1_1V1_NAME)
        .newBytes(bytes)
        .seqPath(charSel)
        .startOffset(C3P1_1V1_OFFSET)
        .endOffset(C3P1_1V1_OFFSET + C3P1_1V1_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel);

    bytes = CostumeExtender.getCodeBytes(characters, 3, 2, false);
    edit = SeqEditBuilder.getBuilder()
        .name(C3P2_1V1_NAME)
        .newBytes(bytes)
        .seqPath(charSel)
        .startOffset(C3P2_1V1_OFFSET)
        .endOffset(C3P2_1V1_OFFSET + C3P2_1V1_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel);

    bytes = CostumeExtender.getCodeBytes(characters, 3, 1, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C3P1_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C3P1_4P_OFFSET)
        .endOffset(C3P1_4P_OFFSET + C3P1_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 3, 2, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C3P2_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C3P2_4P_OFFSET)
        .endOffset(C3P2_4P_OFFSET + C3P2_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 3, 3, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C3P3_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C3P3_4P_OFFSET)
        .endOffset(C3P3_4P_OFFSET + C3P3_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 3, 4, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C3P4_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C3P4_4P_OFFSET)
        .endOffset(C3P4_4P_OFFSET + C3P4_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);
  }

  /**
   * For char_sel.seq and charsel4.seq, write new costume 4 extensions code for the given list of
   * characters.
   *
   * @param charSel The path to char_sel.seq
   * @param charSel4 The path to charsel4.seq
   * @param characters The list of characters to give costume 4 to.
   * @throws IOException If any I/O exception occurs.
   */
  public static void writeCostumeFourCodes(Path charSel, Path charSel4, List<String> characters)
      throws IOException {
    byte[] bytes = CostumeExtender.getCodeBytes(characters, 4, 1, false);
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(C4P1_1V1_NAME)
        .newBytes(bytes)
        .seqPath(charSel)
        .startOffset(C4P1_1V1_OFFSET)
        .endOffset(C4P1_1V1_OFFSET + C4P1_1V1_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel);

    bytes = CostumeExtender.getCodeBytes(characters, 4, 2, false);
    edit = SeqEditBuilder.getBuilder()
        .name(C4P2_1V1_NAME)
        .newBytes(bytes)
        .seqPath(charSel)
        .startOffset(C4P2_1V1_OFFSET)
        .endOffset(C4P2_1V1_OFFSET + C4P2_1V1_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel);

    bytes = CostumeExtender.getCodeBytes(characters, 4, 1, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C4P1_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C4P1_4P_OFFSET)
        .endOffset(C4P1_4P_OFFSET + C4P1_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 4, 2, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C4P2_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C4P2_4P_OFFSET)
        .endOffset(C4P2_4P_OFFSET + C4P2_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 4, 3, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C4P3_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C4P3_4P_OFFSET)
        .endOffset(C4P3_4P_OFFSET + C4P3_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);

    bytes = CostumeExtender.getCodeBytes(characters, 4, 4, true);
    edit = SeqEditBuilder.getBuilder()
        .name(C4P4_4P_NAME)
        .newBytes(bytes)
        .seqPath(charSel4)
        .startOffset(C4P4_4P_OFFSET)
        .endOffset(C4P4_4P_OFFSET + C4P4_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);
  }

  /**
   * Adds an SEQ edit to the char_sel.seq and charsel4.seq files that allows alternate models to be
   * used for all characters except Haku. Haku is an exception since his alternate costumes only
   * remove the mask model.
   *
   * @param charSel The path to the char_sel.seq file
   * @param charSel4 The path to the charsel4.seq file.
   * @throws IOException If any I/O exception occurs.
   */
  public static void allowAlternateCostumeModels(Path charSel, Path charSel4) throws IOException {
    // Get all characters but Haku since Haku's alternate costumes only remove the mask
    List<String> characters = INTERNAL_CHAR_ORDER.entrySet()
        .stream()
        .sorted(Entry.comparingByValue())
        .map(Entry::getKey)
        .filter(name -> !name.equals(GNT4Characters.HAKU))
        .collect(Collectors.toList());

    // Get the 1V1 SEQ edit
    ByteArrayOutputStream baos = new ByteArrayOutputStream(characters.size() * 0x10);
    for (String character : characters) {
      Integer chrId = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
      if (chrId == null) {
        throw new IOException("Unable to find id for character " + character);
      }
      baos.write(OPCODE2);
      baos.write(CHARSEL_ALT_MODEL_OFFSET);
      baos.write(ByteUtils.fromInt32(chrId));
      baos.write(VAR27);
    }

    // Write the 1V1 SEQ edit
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(ALT_MODEL_1V1_NAME)
        .newBytes(baos.toByteArray())
        .seqPath(charSel)
        .startOffset(ALT_MODEL_1V1_OFFSET)
        .endOffset(ALT_MODEL_1V1_OFFSET + ALT_MODEL_1V1_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel);

    // Get the 4P SEQ edit
    baos = new ByteArrayOutputStream(characters.size() * 0x10);
    for (String character : characters) {
      Integer chrId = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
      if (chrId == null) {
        throw new IOException("Unable to find id for character " + character);
      }
      baos.write(OPCODE2);
      baos.write(CHARSEL4_ALT_MODEL_OFFSET);
      baos.write(ByteUtils.fromInt32(chrId));
      baos.write(VAR27);
    }

    // Write the 4P SEQ edit
    edit = SeqEditBuilder.getBuilder()
        .name(ALT_MODEL_4P_NAME)
        .newBytes(baos.toByteArray())
        .seqPath(charSel4)
        .startOffset(ALT_MODEL_4P_OFFSET)
        .endOffset(ALT_MODEL_4P_OFFSET + ALT_MODEL_4P_BYTES.length)
        .create();
    SeqExt.addEdit(edit, charSel4);
  }
}
