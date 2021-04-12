package com.github.nicholasmoser.gecko.codes;

import org.json.JSONArray;

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
  public void writeToJSONArray(JSONArray jsonArray) {

  }
}
