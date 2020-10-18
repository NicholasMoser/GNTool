package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.gecko.active.ActiveWrite32BitsCode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Class to read and write the codes.json file for dol code injection.
 */
public class GeckoCodeJSON {

  public static final String CODE_FILE = "codes.json";

  /**
   * Writes a list of GeckoCodeGroup objects to a file path.
   *
   * @param groups The GeckoCodeGroup objects to write.
   * @param filePath The JSON file.
   * @throws IOException If an I/O error occurs
   */
  public static void writeFile(List<GeckoCodeGroup> groups, Path filePath) throws IOException {
    JSONArray codeGroupsJSON = new JSONArray();
    for (GeckoCodeGroup group : groups) {
      JSONObject codeGroupJSON = new JSONObject();
      JSONArray codesJSON = new JSONArray();
      codeGroupJSON.put("name", group.getName());
      for (GeckoCode code : group.getCodes()) {
        JSONObject codeJSON = getJSONFromGeckoCode(code);
        codesJSON.put(codeJSON);
      }
      codeGroupJSON.put("codes", codesJSON);
      codeGroupsJSON.put(codeGroupJSON);
    }
    Files.writeString(filePath, codeGroupsJSON.toString(2));
  }

  /**
   * Parses a JSON file for the List of GeckoCodeGroup objects.
   *
   * @param filePath The JSON file.
   * @return The List of GeckoCodeGroup objects.
   * @throws IOException If an I/O error occurs
   */
  public static List<GeckoCodeGroup> parseFile(Path filePath) throws IOException {
    List<GeckoCodeGroup> codeGroups = new ArrayList<>();
    try (InputStream is = Files.newInputStream(filePath)) {
      JSONArray array = new JSONArray(new JSONTokener(is));
      for (int i = 0; i < array.length(); i++) {
        JSONObject codeGroup = array.getJSONObject(i);
        String name = codeGroup.getString("name");
        JSONArray codes = codeGroup.getJSONArray("codes");
        List<GeckoCode> codeList = new ArrayList<>(codes.length());
        for (int j = 0; j < codes.length(); j++) {
          JSONObject code = codes.getJSONObject(j);
          GeckoCode geckoCode = getGeckoCodeFromJSON(code);
          codeList.add(geckoCode);
        }
        codeGroups.add(new GeckoCodeGroup(name, codeList));
      }
    }
    return codeGroups;
  }

  /**
   * Returns the Gecko code JSONObject from a GeckoCode.
   *
   * @param code The GeckoCode
   * @return The Gecko code JSONObject
   * @throws IOException If an invalid code type was encountered
   */
  private static JSONObject getJSONFromGeckoCode(GeckoCode code) throws IOException {
    JSONObject codeObject = new JSONObject();
    codeObject.put("targetAddress", fromLong(code.getTargetAddress()));
    if (code instanceof ActiveWrite32BitsCode) {
      ActiveWrite32BitsCode writeCode = (ActiveWrite32BitsCode) code;
      codeObject.put("type", "04");
      codeObject.put("bytes", ByteUtils.bytesToHexString(writeCode.getBytes()));
      codeObject.put("replacedBytes", ByteUtils.bytesToHexString(writeCode.getReplacedBytes()));
    } else if (code instanceof ActiveInsertAsmCode) {
      ActiveInsertAsmCode insertCode = (ActiveInsertAsmCode) code;
      codeObject.put("type", "C2");
      codeObject.put("bytes", ByteUtils.bytesToHexString(insertCode.getBytes()));
      codeObject.put("replacedBytes", ByteUtils.bytesToHexString(insertCode.getReplacedBytes()));
      codeObject.put("hijackedAddress", fromLong(insertCode.getHijackedAddress()));
      codeObject.put("hijackedBytes", ByteUtils.bytesToHexString(insertCode.getHijackedBytes()));
    } else {
      throw new IOException("Code has invalid code type: " + code);
    }
    return codeObject;
  }

  /**
   * Returns the GeckoCode from a Gecko code JSONObject.
   *
   * @param code The Gecko code JSONObject
   * @return The GeckoCode
   * @throws IOException If the code manager file has an invalid code type
   */
  private static GeckoCode getGeckoCodeFromJSON(JSONObject code) throws IOException {
    byte type = getCodeType(code.getString("type"));
    long targetAddress = getLong(code.getString("targetAddress"));
    byte[] bytes = ByteUtils.hexStringToBytes(code.getString("bytes"));
    byte[] replacedBytes = ByteUtils.hexStringToBytes(code.getString("replacedBytes"));
    switch (type) {
      case GeckoCode.THIRTY_TWO_BITS_WRITE -> {
        return new ActiveWrite32BitsCode.Builder()
            .targetAddress(targetAddress)
            .bytes(bytes)
            .replacedBytes(replacedBytes)
            .create();
      }
      case GeckoCode.INSERT_ASM -> {
        long hijackedAddress = getLong(code.getString("hijackedAddress"));
        byte[] hijackedBytes = ByteUtils.hexStringToBytes(code.getString("hijackedBytes"));
        return new ActiveInsertAsmCode.Builder()
            .targetAddress(targetAddress)
            .bytes(bytes)
            .replacedBytes(replacedBytes)
            .hijackedAddress(hijackedAddress)
            .hijackedBytes(hijackedBytes)
            .create();
      }
      default -> throw new IOException(
          "Code manager file has invalid code type: " + type);
    }
  }

  /**
   * Converts a String of hex bytes to a long.
   *
   * @param byteString The hex bytes.
   * @return The long.
   */
  private static long getLong(String byteString) {
    return ByteUtils.toUint32(ByteUtils.hexStringToBytes(byteString));
  }

  /**
   * Converts a long to a String of hex bytes.
   *
   * @param value The long to convert.
   * @return The String of hex bytes.
   */
  private static String fromLong(long value) {
    return ByteUtils.bytesToHexString(ByteUtils.fromUint32(value));
  }

  /**
   * Converts a String of Hex to the single byte code type.
   *
   * @param codeString The hex String.
   * @return The single byte code type.
   */
  private static byte getCodeType(String codeString) {
    byte[] typeInt = ByteUtils.hexStringToBytes(codeString);
    if (typeInt.length != 1) {
      throw new IllegalArgumentException("type is more than one byte: " + codeString);
    }
    return typeInt[0];
  }
}
