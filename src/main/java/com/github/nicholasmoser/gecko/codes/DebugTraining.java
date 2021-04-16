package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 3MC Training Mode is 1v1 Training Mode and 1v1 Training Mode Uses Fight Debug Menu [Nick]
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#3mc-training-mode-is-1v1-training-mode-and-1v1-training-mode-uses-fight-debug-menu-nick
 */
public class DebugTraining implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{(byte) 0x90, 0x03, 0x00, 0x1c, 0x3c,
      (byte) 0xa0, (byte) 0x80, 0x22, (byte) 0x80, 0x05, 0x61, (byte) 0xec, 0x28, 0x00, 0x00, 0x07,
      0x40, (byte) 0x82, 0x00, 0x10, 0x38, 0x00, 0x00, 0x06, (byte) 0x90, 0x05, 0x61, (byte) 0xec,
      0x48, 0x00, 0x00, 0x10, 0x3c, (byte) 0xa0, (byte) 0x80, 0x05, 0x38, (byte) 0xa5, (byte) 0xcb,
      0x64, (byte) 0x90, (byte) 0xa4, 0x00, 0x08};

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "3MC Training Mode is 1v1 Training Mode and 1v1 Training Mode Uses Fight Debug Menu [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code = new JSONObject();
    code.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code.put("targetAddress", "80045328");
    code.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code.put("replacedBytes", "9003001C");
    code.put("type", "C2");
    codes.put(code);
    codeGroup.put("codes", codes);
    return codeGroup;
  }
}
