package com.github.nicholasmoser.gecko.codes;

import org.json.JSONArray;

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
  public void writeToJSONArray(JSONArray jsonArray) {

  }
}
