package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Training Mode Default 2P Action: 2P Control
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#training-mode-default-2p-action-2p-control-nick
 */
public class Default2PControl implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{0x38, 0x00, 0x00, 0x02, (byte) 0xb0,
      0x03, 0x00, 0x2c, 0x60, 0x00, 0x00, 0x00};

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "Training Mode Default 2P Action: 2P Control [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code1 = new JSONObject();
    code1.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code1.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code1.put("targetAddress", "80045350");
    code1.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code1.put("replacedBytes", "B0A3002C");
    code1.put("type", "C2");
    JSONObject code2 = new JSONObject();
    code2.put("bytes", "3BC00012");
    code2.put("targetAddress", "8002E718");
    code2.put("replacedBytes", "3BC00011");
    code2.put("type", "04");
    JSONObject code3 = new JSONObject();
    code3.put("bytes", "3BC00012");
    code3.put("targetAddress", "8002E728");
    code3.put("replacedBytes", "3BC00011");
    code3.put("type", "04");
    codes.put(code1);
    codes.put(code2);
    codes.put(code3);
    codeGroup.put("codes", codes);
    return codeGroup;
  }
}
