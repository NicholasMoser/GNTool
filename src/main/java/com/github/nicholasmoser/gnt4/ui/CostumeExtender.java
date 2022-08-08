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
  private static final byte[] P1_VAR = ByteUtils.hexStringToBytes("7FFFFF20");
  private static final byte[] P2_VAR = ByteUtils.hexStringToBytes("7FFFFF21");
  private static final byte[] P3_VAR = ByteUtils.hexStringToBytes("7FFFFF22");
  private static final byte[] P4_VAR = ByteUtils.hexStringToBytes("7FFFFF23");
  private static final byte[] UNKNOWN_VAR = ByteUtils.hexStringToBytes("7FFFFF24");
  private static final byte[] OPCODE1 = ByteUtils.hexStringToBytes("3C160000");
  private static final byte[] OPCODE2 = ByteUtils.hexStringToBytes("3B010000");
  private static final byte[] CHARSEL_UNKNOWN_OFFSET = ByteUtils.hexStringToBytes("0000F490");
  private static final byte[] CHARSEL4_UNKNOWN_OFFSET = ByteUtils.hexStringToBytes("000140A0");
  private static final byte[] CHARSEL_C3P1_OFFSET = ByteUtils.hexStringToBytes("0000ED50");
  private static final byte[] CHARSEL_C4P1_OFFSET = ByteUtils.hexStringToBytes("0000ED80");
  private static final byte[] CHARSEL_C3P2_OFFSET = ByteUtils.hexStringToBytes("0000EE98");
  private static final byte[] CHARSEL_C4P2_OFFSET = ByteUtils.hexStringToBytes("0000EEC8");
  private static final byte[] CHARSEL4_C3P1_OFFSET = ByteUtils.hexStringToBytes("00007A38");
  private static final byte[] CHARSEL4_C4P1_OFFSET = ByteUtils.hexStringToBytes("00007AA8");
  private static final byte[] CHARSEL4_C3P2_OFFSET = ByteUtils.hexStringToBytes("00007B68");
  private static final byte[] CHARSEL4_C4P2_OFFSET = ByteUtils.hexStringToBytes("00007BD8");
  private static final byte[] CHARSEL4_C3P3_OFFSET = ByteUtils.hexStringToBytes("00007C98");
  private static final byte[] CHARSEL4_C4P3_OFFSET = ByteUtils.hexStringToBytes("00007D08");
  private static final byte[] CHARSEL4_C3P4_OFFSET = ByteUtils.hexStringToBytes("00007DC8");
  private static final byte[] CHARSEL4_C4P4_OFFSET = ByteUtils.hexStringToBytes("00007E38");

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
