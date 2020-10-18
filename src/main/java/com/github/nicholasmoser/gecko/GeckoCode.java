package com.github.nicholasmoser.gecko;

public interface GeckoCode {

  // For all code types see:
  // https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/general/docs/guides/gecko_codetype_documentation.md
  byte THIRTY_TWO_BITS_WRITE = 0x04;
  byte INSERT_ASM = (byte) 0xC2;

  /**
   * @return The length in bytes of the full code.
   */
  int getCodeLength();

  /**
   * @return The original bytes of the full code.
   */
  byte[] getCodeBytes();

  /**
   * @return The target address of the code.
   */
  long getTargetAddress();

  /**
   * @return The original String of the full Gecko code.
   */
  String toGeckoString();
}
