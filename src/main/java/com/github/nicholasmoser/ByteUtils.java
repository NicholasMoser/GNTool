package com.github.nicholasmoser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Utility methods for working with bytes.
 */
public class ByteUtils {

  /**
   * Converts a Java integer to a 4 byte big-endian byte array.
   * 
   * @param integer The integer to use.
   * @return The 4 byte big-endian array.
   */
  public static byte[] intToBytes(int integer) {
    return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(integer).array();
  }

  /**
   * Converts a Java String to a 4 byte big-endian byte array.
   * 
   * @param string The String to use.
   * @return The 4 byte big-endian byte array.
   */
  public static byte[] stringToBytes(String string) {
    return string.getBytes(StandardCharsets.ISO_8859_1);
  }
}
