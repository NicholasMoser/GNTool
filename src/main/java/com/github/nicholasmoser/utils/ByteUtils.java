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
   * Converts a uint16 (as an int) to a 2-byte big-endian byte array. Values over 65,535 will wrap
   * back to zero.
   *
   * @param value the uint16 (as an int).
   * @return The output bytes.
   */
  public static byte[] fromUint16(int value) {
    return new byte[]{
        (byte) (value >> 8),
        (byte) (value),
    };
  }

  /**
   * Converts a uint24 (as an int) to a 3-byte big-endian byte array. Values over 16,777,215 will
   * wrap back to zero.
   *
   * @param value the uint24 (as an int).
   * @return The output bytes.
   */
  public static byte[] fromUint24(int value) {
    return new byte[]{
        (byte) (value >> 16),
        (byte) (value >> 8),
        (byte) (value),
    };
  }

  /**
   * Converts a uint32 (as a long) to a 4-byte big-endian byte array. Values over 4,294,967,295 will
   * wrap back to zero.
   *
   * @param value The uint32 (as a long) as a 4-byte big-endian array.
   * @return The output bytes.
   */
  public static byte[] fromUint32(long value) {
    return new byte[]{
        (byte) (value >> 24),
        (byte) (value >> 16),
        (byte) (value >> 8),
        (byte) (value),
    };
  }

  /**
   * Converts an int32 (as a long) to a 4-byte big-endian byte array.
   *
   * @param value The int32 (as an int) as a 4-byte big-endian array.
   * @return The output bytes.
   */
  public static byte[] fromInt32(int value) {
    return new byte[]{
        (byte) (value >> 24),
        (byte) (value >> 16),
        (byte) (value >> 8),
        (byte) (value),
    };
  }

  /**
   * Converts a float to a 4-byte big-endian byte array.
   *
   * @param value The float.
   * @return The output bytes.
   */
  public static byte[] fromFloat(float value) {
    return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putFloat(value).array();
  }

  /**
   * Converts a 2-byte big-endian byte array to a uint16 (as an int).
   *
   * @param bytes The bytes to use.
   * @return The output uint16 (as an int).
   */
  public static int toUint16(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(4).put(new byte[]{0, 0}).put(bytes);
    buffer.position(0);

    return buffer.getInt();
  }

  /**
   * Converts a 4-byte big-endian byte array to a uint32 (as a long).
   *
   * @param bytes The bytes to use.
   * @return The output uint32 (as a long).
   */
  public static long toUint32(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(8).put(new byte[]{0, 0, 0, 0}).put(bytes);
    buffer.position(0);

    return buffer.getLong();
  }

  /**
   * Converts a 4-byte little-endian byte array to a uint32 (as a long).
   *
   * @param bytes The bytes to use.
   * @return The output uint32 (as a long).
   */
  public static long toUint32LE(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(bytes).put(new byte[]{0, 0, 0, 0});
    buffer.position(0);

    return buffer.getLong();
  }

  /**
   * Converts a 4-byte big-endian byte array to a int32 (as an int).
   *
   * @param bytes The bytes to use.
   * @return The output int32 (as an int).
   */
  public static int toInt32(byte[] bytes) {
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  /**
   * Converts a 4-byte big-endian byte array to a float.
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
   * Retrieves an int32 (as an int) from an array of bytes at the given offset.
   *
   * @param bytes  The bytes to retrieve the int from.
   * @param offset The offset to retrieve the int from.
   * @return The int32 (as an int).
   */
  public static int toInt32(byte[] bytes, int offset) {
    return toInt32(Arrays.copyOfRange(bytes, offset, offset + 4));
  }

  /**
   * Retrieves a uint32 (as a long) from an array of bytes at the given offset.
   *
   * @param bytes  The bytes to retrieve the long from.
   * @param offset The offset to retrieve the long from.
   * @return The uint32 (as a long).
   */
  public static long toUint32(byte[] bytes, int offset) {
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
   * Converts a String to a 4 byte big-endian byte array.
   *
   * @param string The String to use.
   * @return The output bytes.
   */
  public static byte[] fromString(String string) {
    return string.getBytes(Charset.forName("shift-jis"));
  }

  /**
   * Reads a big-endian uint32 (as a long) from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The uint32 (as a long).
   * @throws IOException If an I/O error occurs.
   */
  public static long readUint32(RandomAccessFile raf) throws IOException {
    byte[] bytes = new byte[4];
    raf.read(bytes);
    return toUint32(bytes);
  }

  /**
   * Reads a little-endian uint32 (as a long) from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The long.
   * @throws IOException If an I/O error occurs.
   */
  public static int readUint32LE(RandomAccessFile raf) throws IOException {
    byte[] buffer = new byte[4];
    raf.read(buffer);
    return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }

  /**
   * Reads a big-endian int32 (as an int) from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The int32 (as an int).
   * @throws IOException If an I/O error occurs.
   */
  public static int readInt32(RandomAccessFile raf) throws IOException {
    byte[] bytes = new byte[4];
    raf.read(bytes);
    return toInt32(bytes);
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
   * @return The null terminated output bytes.
   * @throws IOException If an I/O error occurs.
   */
  public static byte[] toNullTerminatedBytes(String text) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length() + 1);
    baos.write(text.getBytes(StandardCharsets.US_ASCII));
    baos.write(0);
    return baos.toByteArray();
  }
}
