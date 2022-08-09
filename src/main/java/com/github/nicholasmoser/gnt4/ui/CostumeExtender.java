package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The SEQ bytes for checking costumes 3 and 4 is 0x40 bytes each.
 */
public class CostumeExtender {
  // SEQ opcodes and offsets used for adding custom costumes
  static final byte[] P1_VAR = ByteUtils.hexStringToBytes("7FFFFF20");
  static final byte[] P2_VAR = ByteUtils.hexStringToBytes("7FFFFF21");
  static final byte[] P3_VAR = ByteUtils.hexStringToBytes("7FFFFF22");
  static final byte[] P4_VAR = ByteUtils.hexStringToBytes("7FFFFF23");
  static final byte[] UNKNOWN_VAR = ByteUtils.hexStringToBytes("7FFFFF24");
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

  // Existing SEQ offsets and code for costume extension
  static final String C3P1_1V1_NAME = "Extend Costume 3, Player 1 (1v1)";
  static final long C3P1_1V1_OFFSET = 0xECC8;
  static final byte[] C3P1_1V1_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 0000F490 7FFFFF20 7FFFFF24
        3B010000 0000ED50 00000007 7FFFFF24
        3B010000 0000ED50 00000002 7FFFFF24
        3B010000 0000ED50 00000009 7FFFFF24
        """);

  static final String C4P1_1V1_NAME = "Extend Costume 4, Player 1 (1v1)";
  static final long C4P1_1V1_OFFSET = 0xED0C;
  static final byte[] C4P1_1V1_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 0000F490 7FFFFF20 7FFFFF24
        3B010000 0000ED80 00000007 7FFFFF24
        3B010000 0000ED80 00000002 7FFFFF24
        3B010000 0000ED80 00000009 7FFFFF24
        """);

  static final String C3P2_1V1_NAME = "Extend Costume 3, Player 2 (1v1)";
  static final long C3P2_1V1_OFFSET = 0xEE10;
  static final byte[] C3P2_1V1_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 0000F490 7FFFFF21 7FFFFF24
        3B010000 0000EE98 00000007 7FFFFF24
        3B010000 0000EE98 00000002 7FFFFF24
        3B010000 0000EE98 00000009 7FFFFF24
        """);

  static final String C4P2_1V1_NAME = "Extend Costume 4, Player 2 (1v1)";
  static final long C4P2_1V1_OFFSET = 0xEE54;
  static final byte[] C4P2_1V1_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 0000F490 7FFFFF21 7FFFFF24
        3B010000 0000EEC8 00000007 7FFFFF24
        3B010000 0000EEC8 00000002 7FFFFF24
        3B010000 0000EEC8 00000009 7FFFFF24
        """);

  static final String C3P1_4P_NAME = "Extend Costume 3, Player 1 (4p)";
  static final long C3P1_4P_OFFSET = 0x79F0;
  static final byte[] C3P1_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF20 7FFFFF24
        3B010000 00007A38 00000007 7FFFFF24
        3B010000 00007A38 00000002 7FFFFF24
        3B010000 00007A38 00000009 7FFFFF24
        """);

  static final String C4P1_4P_NAME = "Extend Costume 4, Player 1 (4p)";
  static final long C4P1_4P_OFFSET = 0x7A60;
  static final byte[] C4P1_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF20 7FFFFF24
        3B010000 00007AA8 00000007 7FFFFF24
        3B010000 00007AA8 00000002 7FFFFF24
        3B010000 00007AA8 00000009 7FFFFF24
        """);

  static final String C3P2_4P_NAME = "Extend Costume 3, Player 2 (4p)";
  static final long C3P2_4P_OFFSET = 0x7B20;
  static final byte[] C3P2_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF21 7FFFFF24
        3B010000 00007B68 00000007 7FFFFF24
        3B010000 00007B68 00000002 7FFFFF24
        3B010000 00007B68 00000009 7FFFFF24
        """);

  static final String C4P2_4P_NAME = "Extend Costume 4, Player 2 (4p)";
  static final long C4P2_4P_OFFSET = 0x7B90;
  static final byte[] C4P2_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF21 7FFFFF24
        3B010000 00007BD8 00000007 7FFFFF24
        3B010000 00007BD8 00000002 7FFFFF24
        3B010000 00007BD8 00000009 7FFFFF24
        """);

  static final String C3P3_4P_NAME = "Extend Costume 3, Player 3 (4p)";
  static final long C3P3_4P_OFFSET = 0x7C50;
  static final byte[] C3P3_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF22 7FFFFF24
        3B010000 00007C98 00000007 7FFFFF24
        3B010000 00007C98 00000002 7FFFFF24
        3B010000 00007C98 00000009 7FFFFF24
        """);

  static final String C4P3_4P_NAME = "Extend Costume 4, Player 3 (4p)";
  static final long C4P3_4P_OFFSET = 0x7CC0;
  static final byte[] C4P3_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF22 7FFFFF24
        3B010000 00007D08 00000007 7FFFFF24
        3B010000 00007D08 00000002 7FFFFF24
        3B010000 00007D08 00000009 7FFFFF24
        """);

  static final String C3P4_4P_NAME = "Extend Costume 3, Player 4 (4p)";
  static final long C3P4_4P_OFFSET = 0x7D80;
  static final byte[] C3P4_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF23 7FFFFF24
        3B010000 00007DC8 00000007 7FFFFF24
        3B010000 00007DC8 00000002 7FFFFF24
        3B010000 00007DC8 00000009 7FFFFF24
        """);

  static final String C4P4_4P_NAME = "Extend Costume 4, Player 4 (4p)";
  static final long C4P4_4P_OFFSET = 0x7DF0;
  static final byte[] C4P4_4P_BYTES = ByteUtils.hexTextToBytes("""
        3C160000 000140A0 7FFFFF23 7FFFFF24
        3B010000 00007E38 00000007 7FFFFF24
        3B010000 00007E38 00000002 7FFFFF24
        3B010000 00007E38 00000009 7FFFFF24
        """);

  public static byte[] getCodeBytes(List<String> characters, int costume, int player, boolean fourPlayerMode) throws IOException {
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
    bytes.write(UNKNOWN_VAR);
    for (String character : characters) {
      Integer id = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
      if (id == null) {
        throw new IOException("Unable to find id for character " + character);
      }
      bytes.write(OPCODE2);
      bytes.write(costumeVar);
      bytes.write(ByteUtils.fromInt32(id));
      bytes.write(UNKNOWN_VAR);
    }
    return bytes.toByteArray();
  }
}
