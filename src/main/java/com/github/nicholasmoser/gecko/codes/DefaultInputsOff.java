package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Training Mode Default Show Inputs: OFF
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#training-mode-default-show-inputs-off-nick
 */
public class DefaultInputsOff implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{0x3F, (byte) 0xE0, (byte) 0x80, 0x22,
      0x3B, (byte) 0xFF, 0x61, (byte) 0xD8, (byte) 0x83, (byte) 0xFF, 0x22, 0x1C, 0x64, 0x00, 0x00,
      (byte) 0x80, (byte) 0x90, 0x1F, 0x00, 0x00, (byte) 0x80, 0x01, 0x00, 0x24, 0x60, 0x00, 0x00,
      0x00};

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    org.json.JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "Training Mode Default Show Inputs: OFF [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code1 = new JSONObject();
    code1.put("bytes", "B0A30032");
    code1.put("targetAddress", "80045364");
    code1.put("replacedBytes", "B0030032");
    code1.put("type", "04");
    JSONObject code2 = new JSONObject();
    code2.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code2.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code2.put("targetAddress", "8004CF8C");
    code2.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code2.put("replacedBytes", "80010024");
    code2.put("type", "C2");
    codes.put(code1);
    codes.put(code2);
    codeGroup.put("codes", codes);
    return codeGroup;
  }
}
