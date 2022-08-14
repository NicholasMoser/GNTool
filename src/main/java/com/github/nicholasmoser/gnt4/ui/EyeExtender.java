package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.InsertAsmCode;
import com.github.nicholasmoser.gecko.Write32BitsCode;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.ppc.BranchEqual;
import com.github.nicholasmoser.ppc.BranchNotEqual;
import com.github.nicholasmoser.ppc.CompareWithImmediate;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class to write Gecko codes for allowing additional eye textures for costumes. The main logic
 * checks if the current selected costume is costume 1, 2, 3, or 4. If it is costume 1 it will
 * always load 0 which is 1300.txg. Otherwise, it will then check if the selected character is
 * a specific character and then potentially load 1, 2, or 3 which correlates to 1301.txg, 1302.txg,
 * or 1303.txg respectively. Last it will execute the original instruction that this new code has
 * hijacked over. Portions of the original logic will also be nop'd over. An example of the new
 * Gecko code assembly can be found below:
 *
 * <code>
 * costume_2:
 *   cmpwi r7, 0x1          ; Check if this is costume 2
 *   bne- costume_3         ; If it is not costume 2, check for costume 3
 *   cmpwi r5, 0x7          ; Costume 2 logic. Check if Sakura is selected
 *   beq- load_2            ; If Sakura, load 1302.txg
 *   cmpwi r5, 0x8          ; Check if Naruto is selected
 *   beq- load_1            ; If Naruto, load 1301.txg
 *   cmpwi r5, 0x1          ; Check if Sasuke is selected
 *   beq- load_1            ; If Sasuke, load 1301.txg
 *   cmpwi r5, 0x3          ; Check if Kakashi is selected
 *   beq- load_3            ; If Kakashi, load 1303.txg
 *
 * costume_3:
 *   cmpwi r7, 0x2          ; Check if this is costume 3
 *   bne- costume_4         ; If it is not costume 3, check for costume 4
 *   cmpwi r5, 0xE          ; Check if Kankuro is selected
 *   beq- load_1            ; If Kankuro, load 1301.txg
 *   cmpwi r5, 0x1A         ; Check if Temari is selected
 *   beq- load_1            ; If Temari, load 1301.txg
 *   cmpwi r5, 0xF          ; Check if Karasu is selected
 *   beq- load_3            ; If Karasu, load 1303.txg
 *   cmpwi r5, 0x12         ; Check if Gaara is selected
 *   beq- load_2            ; If Gaara, load 1302.txg
 *
 * costume_4:
 *   cmpwi r7, 0x3          ; Check if this is costume 4
 *   bne- load_0            ; If it is not costume 4, load 1300.txg
 *   cmpwi r5, 0x21         ; Check if Kidomaru is selected
 *   beq- load_3            ; If Kidomaru, load 1303.txg
 *   cmpwi r5, 0x20         ; Check if Jirobo is selected
 *   beq- load_1            ; If Jirobo, load 1301.txg
 *   cmpwi r5, 0x23         ; Check if Tayuya is selected
 *   beq- load_2            ; If Tayuya, load 1302.txg
 *   cmpwi r5, 0x22         ; Check if Sakon is selected
 *   beq- load_1            ; If Sakon, load 1301.txg
 *
 * load_0:
 *   li r7, 0x0             ; Load 1300.txg
 *   b original_instruction
 *
 * load_1:
 *   li r7, 0x1             ; Load 1301.txg
 *   b original_instruction
 *
 * load_2:
 *   li r7, 0x2             ; Load 1302.txg
 *   b original_instruction
 *
 * load_3:
 *   li r7, 0x3             ; Load 1303.txg
 *   b original_instruction
 *
 * original_instruction:
 *   lwz r3, 12(r1)
 * </code>
 */
public class EyeExtender {
  public static final String FIRST = "1300.txg";
  public static final String SECOND = "1301.txg";
  public static final String THIRD = "1302.txg";
  public static final String FOURTH = "1303.txg";
  public static final String CODE_NAME = "Allow Additional Eye Textures";
  public static final long MAIN_TARGET_ADDR = 0x800ab634;
  public static final long NOP1_TARGET_ADDR = 0x800ab628;
  public static final long NOP2_TARGET_ADDR = 0x800ab630;
  private static final byte[] NOP = ByteUtils.hexStringToBytes("60000000");
  private static final byte[] END = ByteUtils.hexStringToBytes("00000000");
  private static final byte[] CMPWI_COSTUME_2 = ByteUtils.hexStringToBytes("2C070001");
  private static final byte[] CMPWI_COSTUME_3 = ByteUtils.hexStringToBytes("2C070002");
  private static final byte[] CMPWI_COSTUME_4 = ByteUtils.hexStringToBytes("2C070003");
  private static final int COSTUME_2_OFFSET = 0xC;
  private static final int COSTUME_3_OFFSET = 0x14;
  private static final int COSTUME_4_OFFSET = 0x1C;

  /**
   * load_0:
   *   li r7, 0x0             ; Load 1300.txg
   *   b original_instruction
   *
   * load_1:
   *   li r7, 0x1             ; Load 1301.txg
   *   b original_instruction
   *
   * load_2:
   *   li r7, 0x2             ; Load 1302.txg
   *   b original_instruction
   *
   * load_3:
   *   li r7, 0x3             ; Load 1303.txg
   *   b original_instruction
   *
   * original_instruction:
   *   lwz r3, 12(r1)
   */
  public static final String FOOTER_BYTES = """
      38E00000 4800001C
      38E00001 48000014
      38E00002 4800000C
      38E00003 48000004
      8061000C
      """;

  public static List<GeckoCode> getGeckoCodes(EyeSettings eyes) throws IOException {
    List<GeckoCode> codes = new ArrayList<>(3);
    Map<String, String> costume2 = eyes.costumeTwoSelections();
    Map<String, String> costume3 = eyes.costumeThreeSelections();
    Map<String, String> costume4 = eyes.costumeFourSelections();
    byte[] bytes = new byte[0];

    // Process costume order backwards so that correct branching offsets can be used
    // Process costume 4
    if (!costume4.isEmpty()) {
      for (Entry<String, String> entry : eyes.costumeFourSelections().entrySet()) {
        bytes = getEyeEntryBytes(entry, bytes);
      }
      byte[] cmpwi = CMPWI_COSTUME_4;
      byte[] bne = BranchNotEqual.getBytes(bytes.length + 4);
      bytes = Bytes.concat(cmpwi, bne, bytes);
    }
    // Process costume 3
    if (!costume3.isEmpty()) {
      int beforeSize = bytes.length;
      for (Entry<String, String> entry : eyes.costumeThreeSelections().entrySet()) {
        bytes = getEyeEntryBytes(entry, bytes);
      }
      int afterSize = bytes.length - beforeSize;
      byte[] cmpwi = CMPWI_COSTUME_3;
      byte[] bne = BranchNotEqual.getBytes(afterSize + 4);
      bytes = Bytes.concat(cmpwi, bne, bytes);
    }
    // Process costume 2
    if (!costume2.isEmpty()) {
      int beforeSize = bytes.length;
      for (Entry<String, String> entry : eyes.costumeTwoSelections().entrySet()) {
        bytes = getEyeEntryBytes(entry, bytes);
      }
      int afterSize = bytes.length - beforeSize;
      byte[] cmpwi = CMPWI_COSTUME_2;
      byte[] bne = BranchNotEqual.getBytes(afterSize + 4);
      bytes = Bytes.concat(cmpwi, bne, bytes);
    }

    // Get code for main logic
    ByteArrayOutputStream codeBytes = new ByteArrayOutputStream();
    codeBytes.write(bytes);
    codeBytes.write(ByteUtils.hexTextToBytes(FOOTER_BYTES));

    // Needs to be a multiple of 8 bytes and end with 00000000
    if ((codeBytes.size() % 0x8) == 0) {
      codeBytes.write(NOP);
    }
    codeBytes.write(END);

    // Return the three new Gecko codes to extend eye textures
    codes.add(new InsertAsmCode(codeBytes.toByteArray(), MAIN_TARGET_ADDR));
    codes.add(new Write32BitsCode(NOP, NOP1_TARGET_ADDR));
    codes.add(new Write32BitsCode(NOP, NOP2_TARGET_ADDR));
    return codes;
  }

  private static byte[] getEyeEntryBytes(Entry<String ,String> entry, byte[] bytes) throws IOException {
    String chr = entry.getKey();
    String txg = entry.getValue();
    int chrId = GNT4Characters.getChrId(chr);
    int branchDistance = getBranchDistance(txg, bytes.length);
    byte[] cmpwi = CompareWithImmediate.getBytes(5, chrId);
    byte[] beq = BranchEqual.getBytes(branchDistance);
    return Bytes.concat(cmpwi, beq, bytes);
  }

  private static int getBranchDistance(String texture, int offset) throws IOException {
    if (texture.equals(EyeExtender.SECOND)) {
      return COSTUME_2_OFFSET + offset;
    } else if (texture.equals(EyeExtender.THIRD)) {
      return COSTUME_3_OFFSET + offset;
    } else if (texture.equals(EyeExtender.FOURTH)) {
      return COSTUME_4_OFFSET + offset;
    } else {
      throw new IOException("Unexpected texture: " + texture);
    }
  }
}
