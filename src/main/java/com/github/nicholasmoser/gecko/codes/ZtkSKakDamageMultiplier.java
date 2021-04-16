package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ZTK and S. Kakashi take 1.25x damage
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#ztk-and-s-kakashi-take-125x-damage-nick
 */
public class ZtkSKakDamageMultiplier implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{(byte) 0x80, 0x03, 0x00, 0x1c, 0x28,
      0x00, 0x00, 0x03, 0x40, (byte) 0x82, 0x00, 0x08, 0x38, 0x00, 0x00, 0x08, 0x60, 0x00, 0x00,
      0x00};

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "ZTK and S. Kakashi take 1.25x damage [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code1 = new JSONObject();
    code1.put("bytes", "3FA00000");
    code1.put("targetAddress", "80278890");
    code1.put("replacedBytes", "3FC00000");
    code1.put("type", "04");
    JSONObject code2 = new JSONObject();
    code2.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code2.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code2.put("targetAddress", "8003E2B0");
    code2.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code2.put("replacedBytes", "8003001C");
    code2.put("type", "C2");
    codes.put(code1);
    codes.put(code2);
    codeGroup.put("codes", codes);
    return codeGroup;
  }
}
