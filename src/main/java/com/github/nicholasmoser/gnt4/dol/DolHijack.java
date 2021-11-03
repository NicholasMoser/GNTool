package com.github.nicholasmoser.gnt4.dol;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.InsertAsmCode;
import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.gecko.codes.BattleModeDefaultMenuOption;
import com.github.nicholasmoser.gecko.codes.CounterHitPlaysSound;
import com.github.nicholasmoser.gecko.codes.DebugTraining;
import com.github.nicholasmoser.gecko.codes.Default2PControl;
import com.github.nicholasmoser.gecko.codes.DefaultInputsOff;
import com.github.nicholasmoser.gecko.codes.GeckoInjectionCode;
import com.github.nicholasmoser.gecko.codes.UnlockEverything;
import com.github.nicholasmoser.gecko.codes.ZtkSKakDamageMultiplier;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for hijacking code in the dol for GNT4. The code currently hijacked is a space of 297
 * instructions (1188 bytes) relating to the unused recording function in training mode.
 *
 * <ul>
 *   <li>80086f58 - Every frame of replay loop</li>
 *   <li>80087060 - Increase record number</li>
 *   <li>800870c0 - Saves recorded number?</li>
 *   <li>800871a0 - Does something every frame of recording</li>
 *   <li>80087364 - Helper function for 800b3790</li>
 *   <li>80087374 - Stop replay</li>
 * </ul>
 */
public class DolHijack {

  private static final Logger LOGGER = Logger.getLogger(DolHijack.class.getName());

  // Inclusive
  public final static long START_RAM_ADDRESS = 0x80086F58L;
  public final static long START_DOL_OFFSET = 0x83F58L;

  // Exclusive
  public final static long END_RAM_ADDRESS = 0x800873FCL;
  public final static long END_DOL_OFFSET = 0x843FC;

  public final static int SIZE = (int) (END_DOL_OFFSET - START_DOL_OFFSET);

  private final static List<GeckoInjectionCode> CODES = List
      .of(new Default2PControl(), new DefaultInputsOff(), new ZtkSKakDamageMultiplier(),
          new UnlockEverything(), new DebugTraining(), new BattleModeDefaultMenuOption(),
          new CounterHitPlaysSound());

  /**
   * Returns whether or not the given Gecko codes overflow the limit of hijacked code. Logs and
   * displays a message if so.
   *
   * @param newCodes The GeckoCode List to check.
   * @return If the Gecko codes overflow the limit of hijacked code.
   */
  public static boolean checkHijackOverflow(List<GeckoCodeGroup> existingCodeGroups,
      List<GeckoCode> newCodes) {
    long furthestEnd = getEndOfHijacking(existingCodeGroups);
    long bytesLeft = DolHijack.END_RAM_ADDRESS - furthestEnd;
    long totalBytes = 0;
    for (GeckoCode code : newCodes) {
      if (code instanceof InsertAsmCode) {
        InsertAsmCode insertCode = (InsertAsmCode) code;
        totalBytes += insertCode.getBytes().length;
      }
    }
    if (totalBytes > bytesLeft) {
      String msg = String
          .format("Codes hijack %d bytes but only %d bytes are left.", totalBytes, bytesLeft);
      LOGGER.log(Level.SEVERE, msg);
      Message.error("Not Enough Space to Insert Codes", msg);
      return true;
    }
    return false;
  }

  /**
   * Returns the end of hijacked codes in the given list of code groups.
   *
   * @param existingCodeGroups The existing code groups to find the furthest end of hijacking.
   * @return The address at the end of hijacking.
   */
  public static long getEndOfHijacking(List<GeckoCodeGroup> existingCodeGroups) {
    long furthestEnd = DolHijack.START_RAM_ADDRESS;
    for (GeckoCodeGroup group : existingCodeGroups) {
      for (GeckoCode code : group.getCodes()) {
        if (code instanceof ActiveInsertAsmCode) {
          ActiveInsertAsmCode insertCode = (ActiveInsertAsmCode) code;
          long hijackedAddress = insertCode.getHijackedAddress();
          int length = insertCode.getHijackedBytes().length;
          long end = hijackedAddress + length;
          if (end > DolHijack.END_RAM_ADDRESS) {
            throw new IllegalStateException(
                "codeGroups extends past max hijack: " + existingCodeGroups);
          } else if (end > furthestEnd) {
            furthestEnd = end;
          }
        }
      }
    }
    return furthestEnd;
  }

  /**
   * Check the dol hijack area in the code to see if there are any injected codes. If there aren't,
   * return false. If there are, create a new codes JSON file and return true. This will only work
   * with known codes.
   *
   * @param dolPath The path to the dol.
   * @param originalBytes The original bytes in the dol that can be hijacked.
   * @return If codes were found to be injected and a new codes JSON file was created.
   * @throws IOException if an I/O error occurs
   */
  public static boolean handleActiveCodesButNoCodeFile(Path dolPath, byte[] originalBytes) throws IOException {
    byte[] currentBytes = getCurrentBytes(dolPath);
    assertSameSize(originalBytes, currentBytes);
    if (Arrays.equals(originalBytes, currentBytes)) {
      return false;
    }
    // There are active codes but no code JSON file, let's try and create one.
    JSONArray codesList = new JSONArray();
    int i = 0;
    while (true) {
      CodeMatchResult result = findCodeMatch(currentBytes, originalBytes, i);
      i = result.getNewIndex();
      if (result.isCodeMatch()) {
        codesList.put(result.getCodeGroup().get());
        // Continue finding code matches until we can't find any more
        continue;
      }
      // No more code matches, validate the rest of the bytes match the original bytes.
      byte[] subsection1 = Arrays.copyOfRange(originalBytes, i, originalBytes.length);
      byte[] subsection2 = Arrays.copyOfRange(currentBytes, i, originalBytes.length);
      if (!Arrays.equals(subsection1, subsection2)) {
        if (isAtMatchingWord(subsection1, subsection2)) {
          // Check for empty space between codes
          // TODO: Be smart and remove empty space
          while(originalBytes[i] == currentBytes[i]) {
            i++;
          }
          continue;
        }
        // There's a lot of different reasons this could occur, but they all will require manual
        // inspection of the user's dol. Ask them to just log an issue.
        LOGGER.log(Level.SEVERE, "A code could not be matched when creating the code JSON, please report this on the GNTool Github.");
        return false;
      }
      // Write the codes list to the codes.json file
      Path codesJson = dolPath.resolve("../../../codes.json");
      Files.writeString(codesJson, codesList.toString(2));
      break;
    }
    return true;
  }

  private static boolean isAtMatchingWord(byte[] bytes1, byte[] bytes2) {
    if (bytes1.length < 4) {
      // Less than a word less in the byte arrays
      return false;
    }
    int word1 = ByteUtils.toInt32(Arrays.copyOfRange(bytes1, 0, 4));
    int word2 = ByteUtils.toInt32(Arrays.copyOfRange(bytes2, 0, 4));
    return word1 == word2;
  }

  /**
   * Attempts to find and return a matching code in the current set of bytes.
   *
   * @param currentBytes The current bytes to search for a matching code.
   * @param originalBytes The original bytes at the same location of the current bytes.
   * @param i The index in the current and original bytes.
   * @return The result of trying to find a code match.
   */
  private static CodeMatchResult findCodeMatch(byte[] currentBytes, byte[] originalBytes, int i) {
    int bytesLeft = currentBytes.length - i;
    for (GeckoInjectionCode code : CODES) {
      byte[] codeBytes = code.getCode();
      if (bytesLeft >= codeBytes.length) {
        byte[] subsection = Arrays.copyOfRange(currentBytes, i, i + codeBytes.length);
        if (Arrays.equals(subsection, codeBytes)) {
          // Code match
          byte[] hijackedBytes = Arrays.copyOfRange(originalBytes, i, i + codeBytes.length + 4);
          JSONObject codeGroup = code.getJSONObject(START_RAM_ADDRESS + i, hijackedBytes);
          i += codeBytes.length;
          i += 4; // Skip the ending branch
          return new CodeMatchResult(true, i, codeGroup);
        }
      }
    }
    return new CodeMatchResult(false, i);
  }

  /**
   * Gets the current bytes of the dol injection area of the given dol path.
   *
   * @param dolPath The path to the dol.
   * @return The code bytes of the injection area.
   * @throws IOException if an I/O error occurs
   */
  private static byte[] getCurrentBytes(Path dolPath) throws IOException {
    byte[] currentBytes = new byte[SIZE];
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "r")) {
      raf.seek(START_DOL_OFFSET);
      if (raf.read(currentBytes) != SIZE) {
        throw new IOException("Unable to check for active codes with no code file.");
      }
    }
    return currentBytes;
  }

  /**
   * Asserts that the original bytes and current bytes are the same size and throws an IOException
   * if not.
   *
   * @param originalBytes The original bytes.
   * @param currentBytes  The current bytes.
   * @throws IOException If the original bytes and current bytes are not the same size.
   */
  private static void assertSameSize(byte[] originalBytes, byte[] currentBytes) throws IOException {
    if (originalBytes.length != currentBytes.length) {
      String msg = "Length of original bytes not equal to current bytes";
      throw new IOException(
          String.format("%s: %d vs %d", msg, originalBytes.length, currentBytes.length));
    }
  }
}
