package com.github.nicholasmoser.gnt4.dol;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.dol.DolUtil;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.GeckoCodeJSON;
import com.github.nicholasmoser.gecko.GeckoWriter;
import com.github.nicholasmoser.gecko.InsertAsmCode;
import com.github.nicholasmoser.gecko.Write32BitsCode;
import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.gnt4.dol.CodeCaves.CodeCave;
import com.github.nicholasmoser.gnt4.ui.EyeExtender;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.HttpUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Class for hijacking code in the dol for GNT4. It is considered hijacking since code will be
 * overwritten in the dol. Ideally we only write over unused code. These unused code locations that
 * we overwrite are called {@link CodeCaves}.
 */
public class DolHijack {

  private static final Logger LOGGER = Logger.getLogger(DolHijack.class.getName());

  private final static String KNOWN_CODES_RESOURCE = "known_codes.json";
  private final static String KNOWN_CODES_URL = "https://raw.githubusercontent.com/NicholasMoser/GNTool/master/src/main/resources/com/github/nicholasmoser/gnt4/dol/known_codes.json";

  private static JSONArray CODES;

  /**
   * Returns whether or not the given Gecko codes overflow the limit of hijacked code. Logs and
   * displays a message if so.
   *
   * @param existingCodeGroups The existing code groups.
   * @param newCodes           The GeckoCode List to check.
   * @param codeCave           The code cave to write to.
   * @return If the Gecko codes overflow the limit of hijacked code.
   */
  public static boolean checkHijackOverflow(List<GeckoCodeGroup> existingCodeGroups,
      List<GeckoCode> newCodes, CodeCave codeCave) {
    long furthestEnd = getEndOfHijacking(existingCodeGroups, codeCave);
    long bytesLeft = CodeCaves.getEndAddress(codeCave) - furthestEnd;
    long totalBytes = 0;
    for (GeckoCode code : newCodes) {
      if (code instanceof InsertAsmCode insertCode) {
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
   * @param codeCave           The code cave to write codes to.
   * @return The address at the end of hijacking.
   */
  public static long getEndOfHijacking(List<GeckoCodeGroup> existingCodeGroups, CodeCave codeCave) {
    long furthestEnd = CodeCaves.getStartAddress(codeCave);
    long endRamAddress = CodeCaves.getEndAddress(codeCave);
    for (GeckoCodeGroup group : existingCodeGroups) {
      for (GeckoCode code : group.getCodes()) {
        if (code instanceof ActiveInsertAsmCode insertCode) {
          long hijackedAddress = insertCode.getHijackedAddress();
          int length = insertCode.getHijackedBytes().length;
          long end = hijackedAddress + length;
          if (end > endRamAddress) {
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
   * @param dolPath  The path to the dol.
   * @param codeCave The code cave to write codes to.
   * @param codePath The file to write the codes to.
   * @return If codes were found to be injected and a new codes JSON file was created.
   * @throws IOException if an I/O error occurs
   */
  public static boolean handleActiveCodesButNoCodeFile(Path dolPath, CodeCave codeCave, Path codePath)
      throws IOException {
    byte[] currentBytes = getCurrentBytes(dolPath, codeCave);
    byte[] originalBytes = CodeCaves.getBytes(codeCave);
    long codeCaveStartAddress = CodeCaves.getStartAddress(codeCave);
    assertSameSize(originalBytes, currentBytes);
    if (Arrays.equals(originalBytes, currentBytes)) {
      return false;
    }
    // There are active codes but no code JSON file, let's try and create one.
    JSONArray codesList = new JSONArray();
    int i = 0;
    while (true) {
      CodeMatchResult result = findCodeMatch(currentBytes, originalBytes, i, codeCaveStartAddress);
      i = result.getNewIndex();
      if (result.isCodeMatch()) {
        JSONObject codeGroup = result.getCodeGroup().get();
        // See if the code group has already been added
        if (!codeAlreadyadded(codesList, codeGroup)) {
          codesList.put(codeGroup);
        }
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
          while (originalBytes[i] == currentBytes[i]) {
            i++;
          }
          continue;
        }
        // There's a lot of different reasons this could occur, but they all will require manual
        // inspection of the user's dol. Ask them to just log an issue.
        long dolOffset = DolUtil.ram2dol(codeCaveStartAddress);
        LOGGER.log(Level.INFO, String.format("Using code cave %s at offset 0x%X", codeCave, dolOffset));
        LOGGER.log(Level.INFO, String.format("Currently at offset 0x%X (0x%X)", i, dolOffset + i));
        throw new IOException(
            "A code could not be matched when creating the code JSON, please report this on the GNTool Github.");
      }
      // Write the codes list to the codes.json file
      Files.writeString(codePath, codesList.toString(2));
      break;
    }
    return true;
  }

  /**
   * Returns if the given code group is already in the codes list.
   *
   * @param codesList The codes list to check for the code group.
   * @param codeGroup The code group to check for in the codes list.
   * @return If the code group is in the code list.
   */
  private static boolean codeAlreadyadded(JSONArray codesList, JSONObject codeGroup) {
    String newCodeName = codeGroup.getString("name");
    for (int i = 0; i < codesList.length(); i++) {
      JSONObject code = codesList.getJSONObject(i);
      String currCodeName = code.getString("name");
      if (newCodeName.equals(currCodeName)) {
        return true;
      }
    }
    return false;
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
   * @param currentBytes         The current bytes to search for a matching code.
   * @param originalBytes        The original bytes at the same location of the current bytes.
   * @param start                The index in the current and original bytes.
   * @param codeCaveStartAddress The starting address of the code cave.
   * @return The result of trying to find a code match.
   */
  private static CodeMatchResult findCodeMatch(byte[] currentBytes, byte[] originalBytes,
      final int start,
      long codeCaveStartAddress) throws IOException {
    if (CODES == null) {
      CODES = loadCodes();
    }
    int bytesLeft = currentBytes.length - start;
    for (int j = 0; j < CODES.length(); j++) {
      JSONObject codeGroup = CODES.getJSONObject(j);
      JSONArray codes = codeGroup.getJSONArray("codes");
      int numOfC2Codes = getNumberOfC2Codes(codes);
      int i = start;
      for (int k = 0; k < codes.length(); k++) {
        JSONObject code = codes.getJSONObject(k);
        if ("C2".equals(code.getString("type"))) {
          byte[] codeBytes = ByteUtils.hexStringToBytes(code.getString("bytes"));
          if (bytesLeft < codeBytes.length) {
            continue;
          }
          int endOfCode = i + codeBytes.length; // Not including branch
          byte[] subsection = Arrays.copyOfRange(currentBytes, i, endOfCode);
          if (Arrays.equals(subsection, codeBytes)) {
            // Code match
            byte[] hijackedBytes = Arrays.copyOfRange(originalBytes, i, i + codeBytes.length + 4);
            code.put("hijackedAddress", ByteUtils.fromLong(codeCaveStartAddress + i));
            code.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
            code.put("bytes", ByteUtils.bytesToHexString(codeBytes) + "00000000");
            i += codeBytes.length;
            i += 4; // Skip the ending branch
            numOfC2Codes--;
          }
          if (numOfC2Codes <= 0) {
            // All C2 codes have been processed and updated
            return new CodeMatchResult(true, i, codeGroup);
          }
        }
      }
    }
    // Hack to check for the code "Allow Additional Eye Textures"
    byte[] firstWord = Arrays.copyOfRange(currentBytes, start, start + 4);
    if (Arrays.equals(firstWord, EyeExtender.CMPWI_COSTUME_2)) {
      // Look for the code footer
      byte[] footer = ByteUtils.hexTextToBytes(EyeExtender.FOOTER_BYTES);
      for (int i = start; i < currentBytes.length - footer.length; i += 4) {
        byte[] potentialFooter = Arrays.copyOfRange(currentBytes, i, i + footer.length);
        if (Arrays.equals(potentialFooter, footer)) {
          // Found the footer, skip nops to find end of code
          int endOfFooter = i + footer.length;
          for (int j = endOfFooter; j < currentBytes.length - 4; j += 4) {
            byte[] potentialEnd = Arrays.copyOfRange(currentBytes, j, j + 4);
            if (!Arrays.equals(EyeExtender.NOP, potentialEnd)) {
              int end = j + 4;
              byte[] codeBytes = Arrays.copyOfRange(currentBytes, start, end);
              byte[] hijackedBytes = Arrays.copyOfRange(originalBytes, start, end);
              long hijackedAddress = codeCaveStartAddress + start;
              JSONObject codeGroup = getEyeTextureCodes(codeBytes, hijackedBytes, hijackedAddress);
              return new CodeMatchResult(true, end, codeGroup);
            }
          }
        }
      }
    }
    return new CodeMatchResult(false, start);
  }

  /**
   * Return a JSON object of the code "Allow Additional Eye Textures"
   *
   * @param codeBytes       The code bytes for "Allow Additional Eye Textures"
   * @param hijackedBytes   The hijacked bytes for "Allow Additional Eye Textures"
   * @param hijackedAddress The hijacked address for "Allow Additional Eye Textures"
   * @return a JSON object of the code "Allow Additional Eye Textures"
   */
  private static JSONObject getEyeTextureCodes(byte[] codeBytes, byte[] hijackedBytes,
      long hijackedAddress) {
    // Zero out last four bytes of code
    codeBytes[codeBytes.length - 1] = 0;
    codeBytes[codeBytes.length - 2] = 0;
    codeBytes[codeBytes.length - 3] = 0;
    codeBytes[codeBytes.length - 4] = 0;
    // Create the codes
    JSONObject codeGroup = new JSONObject();
    JSONArray codes = new JSONArray();
    // Add first code
    JSONObject first = new JSONObject();
    first.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    first.put("bytes", ByteUtils.bytesToHexString(codeBytes));
    first.put("targetAddress", "800AB634");
    first.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    first.put("replacedBytes", "8061000C");
    first.put("type", "C2");
    // Add second code
    JSONObject second = new JSONObject();
    second.put("bytes", "60000000");
    second.put("targetAddress", "800AB628");
    second.put("replacedBytes", "38E00001");
    second.put("type", "04");
    // Add third code
    JSONObject third = new JSONObject();
    third.put("bytes", "60000000");
    third.put("targetAddress", "800AB630");
    third.put("replacedBytes", "38E00000");
    third.put("type", "04");
    // Finish code group
    codes.put(first);
    codes.put(second);
    codes.put(third);
    codeGroup.put("codes", codes);
    codeGroup.put("name", "Allow Additional Eye Textures");
    return codeGroup;
  }

  private static int getNumberOfC2Codes(JSONArray codes) {
    int num = 0;
    for (int k = 0; k < codes.length(); k++) {
      JSONObject code = codes.getJSONObject(k);
      if ("C2".equals(code.getString("type"))) {
        num++;
      }
    }
    return num;
  }

  /**
   * Gets the current bytes of the code cave from the given dol.
   *
   * @param dolPath  The path to the dol.
   * @param codeCave The code cave.
   * @return The code bytes of the code cave.
   * @throws IOException if an I/O error occurs
   */
  private static byte[] getCurrentBytes(Path dolPath, CodeCave codeCave) throws IOException {
    int size = CodeCaves.getSize(codeCave);
    byte[] currentBytes = new byte[size];
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "r")) {
      raf.seek(CodeCaves.getStartOffset(codeCave));
      if (raf.read(currentBytes) != size) {
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

  /**
   * Load codes into a JSONArray from the known_codes.json resource.
   *
   * @return The known codes JSONArray.
   * @throws IOException If the known codes json file cannot be read.
   */
  private static JSONArray loadCodes() throws IOException {
    try {
      return HttpUtils.getJSON(KNOWN_CODES_URL);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Error downloading known codes from " + KNOWN_CODES_URL, e);
    }
    try (InputStream is = DolHijack.class.getResourceAsStream(KNOWN_CODES_RESOURCE)) {
      if (is == null) {
        throw new IllegalStateException("Unable to find resource known_codes.json");
      }
      return new JSONArray(new JSONTokener(is));
    }
  }

  /**
   * Returns whether this dol is using a specific code cave.
   *
   * @param dolPath The dol to check.
   * @param codeCave The code cave to check.
   * @return If the dol is using the old way of code hijacking.
   * @throws IOException if an I/O error occurs
   */
  public static boolean isUsingCodeCave(Path dolPath, CodeCave codeCave) throws IOException {
    int size = CodeCaves.getSize(codeCave);
    long offset = CodeCaves.getStartOffset(codeCave);
    byte[] vanillaBytes = CodeCaves.getBytes(codeCave);
    byte[] actualBytes = new byte[size];
    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "r")) {
      raf.seek(offset);
      if (raf.read(actualBytes) != size) {
        throw new IOException("Failed to read bytes for code cave: " + codeCave);
      }
      return !Arrays.equals(actualBytes, vanillaBytes);
    }
  }

  /**
   * Moves codes in the dol given a code file to a different code cave.
   *
   * @param dol The dol file with the codes.
   * @param codeFile The current code file describing codes in the dol.
   * @param to The code cave to move the codes to.
   * @throws IOException if an I/O error occurs
   */
  public static void moveCodes(Path dol, Path codeFile, CodeCave to)
      throws IOException {
    long hijackAddress = CodeCaves.getStartAddress(to);
    GeckoWriter writer = new GeckoWriter(dol);
    List<GeckoCodeGroup> oldCodeGroups = GeckoCodeJSON.parseFile(codeFile);
    List<GeckoCodeGroup> newCodeGroups = new ArrayList<>(oldCodeGroups.size());
    for (GeckoCodeGroup codeGroup : oldCodeGroups) {
      List<GeckoCode> oldCodes = codeGroup.getCodes();
      List<GeckoCode> newCodes = new ArrayList<>(oldCodes.size());
      // Convert old codes to new codes
      for (GeckoCode oldCode : oldCodes) {
        if (oldCode instanceof InsertAsmCode code) {
          newCodes.add(new InsertAsmCode(code.getBytes(), code.getTargetAddress()));
        } else if (oldCode instanceof Write32BitsCode code) {
          newCodes.add(new Write32BitsCode(code.getBytes(), code.getTargetAddress()));
        } else {
          throw new IOException("Code not yet supported");
        }
      }
      // Remove existing codes and write new codes
      writer.removeCodes(codeGroup);
      GeckoCodeGroup group = writer.writeCodes(newCodes, codeGroup.getName(), hijackAddress);
      newCodeGroups.add(group);
      hijackAddress = getEndOfHijacking(newCodeGroups, to);
    }
    GeckoCodeJSON.writeFile(newCodeGroups, codeFile);
  }
}
