package com.github.nicholasmoser.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Utility methods for working with bytes.
 */
public class ByteUtils {

  /**
   * Converts a Java integer to a 16-bit big-endian byte array.
   *
   * @param integer The integer to use.
   * @return The 16-bit big-endian array.
   */
  public static byte[] fromUint16(int integer) {
    return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort((short) integer).array();
  }

  /**
   * Converts a Java integer to a 24-bit big-endian byte array.
   *
   * @param integer The integer to use.
   * @return The 24-bit big-endian array.
   */
  public static byte[] fromUint24(int integer) {
    return new byte[] {
        (byte)((integer >> 16) & 0xff),
        (byte)((integer >> 8) & 0xff),
        (byte)((integer >> 0) & 0xff),
    };
  }

  /**
   * Converts a Java integer to a 32-bit big-endian byte array.
   *
   * @param integer The integer to use.
   * @return The 32-bit big-endian array.
   */
  public static byte[] fromUint32(int integer) {
    return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(integer).array();
  }

  /**
   * Converts a Java float to a 32-bit big-endian byte array.
   *
   * @param floatValue The float to use.
   * @return The 32-bit big-endian array.
   */
  public static byte[] fromFloat(float floatValue) {
    return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putFloat(floatValue).array();
  }

  /**
   * Converts a 16-bit big-endian byte array to an integer.
   *
   * @param bytes The bytes to use.
   * @return The integer.
   */
  public static int toUint16(byte[] bytes) {
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
  }

  /**
   * Converts a 32-bit big-endian byte array to an integer.
   *
   * @param bytes The bytes to use.
   * @return The integer.
   */
  public static int toUint32(byte[] bytes) {
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  /**
   * Converts a 32-bit big-endian byte array to a float.
   *
   * @param bytes The bytes to use.
   * @return The float.
   */
  public static float toFloat(byte[] bytes) {
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getFloat();
  }

  /**
   * Retrieves a uint16 from an array of bytes at the given offset.
   *
   * @param bytes  The bytes to retrieve the int from.
   * @param offset The offset to retrieve the int from.
   * @return The uint16.
   */
  public static int toUint16(byte[] bytes, int offset) {
    return toUint16(Arrays.copyOfRange(bytes, offset, offset + 2));
  }

  /**
   * Retrieves a uint32 from an array of bytes at the given offset.
   *
   * @param bytes  The bytes to retrieve the int from.
   * @param offset The offset to retrieve the int from.
   * @return The uint32.
   */
  public static int toUint32(byte[] bytes, int offset) {
    return toUint32(Arrays.copyOfRange(bytes, offset, offset + 4));
  }

  /**
   * Retrieves a float from an array of bytes at the given offset.
   *
   * @param bytes  The bytes to retrieve the float from.
   * @param offset The offset to retrieve the float from.
   * @return The float.
   */
  public static float toFloat(byte[] bytes, int offset) {
    return toFloat(Arrays.copyOfRange(bytes, offset, offset + 4));
  }

  /**
   * Converts a Java String to a 4 byte big-endian byte array.
   *
   * @param string The String to use.
   * @return The 4 byte big-endian byte array.
   */
  public static byte[] fromString(String string) {
    return string.getBytes(Charset.forName("shift-jis"));
  }

  /**
   * Reads a 32-bit big-endian integer from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The integer.
   * @throws IOException If an I/O error occurs.
   */
  public static int readUint32(RandomAccessFile raf) throws IOException {
    byte[] bytes = new byte[4];
    raf.read(bytes);
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  /**
   * Read an ASCII String from a RandomAccessFile terminated by a null byte (0).
   *
   * @param raf The RandomAccessFile to read from.
   * @return The ASCII String.
   * @throws IOException If an I/O error occurs.
   */
  public static String readString(RandomAccessFile raf) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte current = raf.readByte();
    while (current != 0) {
      baos.write(current);
      current = raf.readByte();
    }
    return baos.toString(StandardCharsets.US_ASCII);
  }

  /**
   * Returns an ASCII String as bytes terminated by a null byte (0).
   *
   * @param text The text to return as null terminated bytes.
   * @return The null terminated bytes.
   * @throws IOException If an I/O error occurs.
   */
  public static byte[] toNullTerminatedBytes(String text) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length() + 1);
    baos.write(text.getBytes(StandardCharsets.US_ASCII));
    baos.write(0);
    return baos.toByteArray();
  }

  /**
   * Reads a little-endian 4-byte uint from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The little-endian 4-byte uint.
   * @throws IOException If an I/O error occurs.
   */
  public static int readUint32LE(RandomAccessFile raf) throws IOException {
    byte[] buffer = new byte[4];
    raf.read(buffer);
    return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }
}
