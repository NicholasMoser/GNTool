package com.github.nicholasmoser.gecko.codes;

import com.github.nicholasmoser.utils.ByteUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Battle Mode is the Default Menu Option [Nick]
 * https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#battle-mode-is-the-default-menu-option-nick
 */
public class BattleModeDefaultMenuOption implements GeckoInjectionCode {

  public final static byte[] CODE = new byte[]{0x38, 0x00, 0x00, 0x02, (byte) 0xb0,
      0x03, 0x00, 0x00, 0x38, 0x00, 0x00, 0x00};

  @Override
  public byte[] getCode() {
    return CODE;
  }

  @Override
  public JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes) {
    JSONObject codeGroup = new JSONObject();
    codeGroup.put("name", "Battle Mode is the Default Menu Option [Nick]");
    JSONArray codes = new JSONArray();
    JSONObject code = new JSONObject();
    code.put("hijackedAddress", ByteUtils.fromLong(hijackedAddress));
    code.put("bytes", ByteUtils.bytesToHexString(CODE) + "00000000");
    code.put("targetAddress", "800D687C");
    code.put("hijackedBytes", ByteUtils.bytesToHexString(hijackedBytes));
    code.put("replacedBytes", "B0030000");
    code.put("type", "C2");
    codes.put(code);
    codeGroup.put("codes", codes);
    return codeGroup;
  }

}
