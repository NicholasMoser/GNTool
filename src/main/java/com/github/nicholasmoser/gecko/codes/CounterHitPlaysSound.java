package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Unlock Everything
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#counter-hit-plays-sound
 */
public class CounterHitPlaysSound implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{0x2c, 0x15, 0x00, (byte) 0x80, 0x41, (byte) 0x82,
      0x00, 0x30, 0x7c, 0x77, 0x1b, 0x78, 0x38, 0x60, 0x03, 0x00, 0x38, (byte) 0x80, 0x02, 0x1c,
      0x38, (byte) 0xa0, 0x7f, 0x00, 0x38, (byte) 0xc0, 0x00, 0x00, 0x38, (byte) 0xe0, 0x00, 0x00,
      0x3c, 0x00, (byte) 0x80, 0x0c, 0x60, 0x00, (byte) 0xe4, (byte) 0xa8, 0x7c, 0x09, 0x03,
      (byte) 0xa6, 0x4e, (byte) 0x80, 0x04, 0x21, 0x7e, (byte) 0xe3, (byte) 0xbb, 0x78, 0x3a, 0x70,
      0x01, 0x66, 0x60, 0x00, 0x00, 0x00 };

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "Counter Hit Plays Sound [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code = new JSONObject();
    code.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code.put("targetAddress", "8003A378");
    code.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code.put("replacedBytes", "3A700166");
    code.put("type", "C2");
    codes.put(code);
    codeGroup.put("codes", codes);
    return codeGroup;
  }
}
