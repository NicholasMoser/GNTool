package com.github.nicholasmoser.gecko.codes;

import org.json.JSONArray;

/**
 * ZTK and S. Kakashi take 1.25x damage
 *  https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/gnt4/docs/guides/gecko_codes.md#ztk-and-s-kakashi-take-125x-damage-nick
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
  public void writeToJSONArray(JSONArray jsonArray) {

  }
}
