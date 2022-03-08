package com.github.nicholasmoser.utils;

import com.google.common.io.BaseEncoding;
import com.google.common.io.CountingInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
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
   * Converts a uint32 (as a long) to a 4-byte little-endian byte array. Values over 4,294,967,295 will
   * wrap back to zero.
   *
   * @param value The uint32 (as a long) as a 4-byte little-endian array.
   * @return The output bytes.
   */
  public static byte[] fromUint32LE(long value) {
    return new byte[]{
        (byte) (value),
        (byte) (value >> 8),
        (byte) (value >> 16),
        (byte) (value >> 24)
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
    ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(bytes)
        .put(new byte[]{0, 0, 0, 0});
    buffer.position(0);

    return buffer.getLong();
  }

  /**
   * Converts a 2-byte big-endian byte array to a int16 (as an int).
   *
   * @param bytes The bytes to use.
   * @return The output int16 (as an int).
   */
  public static short toInt16(byte[] bytes) {
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
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
    if (raf.read(bytes) != 4) {
      throw new IOException("Failed to read uint32 from file at offset " + raf.getFilePointer());
    }
    return toUint32(bytes);
  }

  /**
   * Reads a number of big-endian uint32 values (as longs) from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The uint32 values (as longs).
   * @throws IOException If an I/O error occurs.
   */
  public static long[] readUint32s(RandomAccessFile raf, int num) throws IOException {
    long[] values = new long[num];
    for (int i = 0; i < num; i++) {
      long value = ByteUtils.readUint32(raf);
      values[i] = value;
    }
    return values;
  }

  /**
   * Reads a big-endian int32 (as an int) from a CountingInputStream.
   *
   * @param cis The CountingInputStream to read from.
   * @return The int32 (as an int).
   * @throws IOException If an I/O error occurs.
   */
  public static int readInt32(CountingInputStream cis) throws IOException {
    byte[] bytes = new byte[4];
    if (cis.read(bytes) != 4) {
      throw new IOException("Failed to read int at offset " + cis.getCount());
    }
    return toInt32(bytes);
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
    if (raf.read(buffer) != 4) {
      throw new IOException("Failed to read 4 bytes from file");
    }
    return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }

  /**
   * Reads a big-endian int16 (as an int) from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The int16 (as an int).
   * @throws IOException If an I/O error occurs.
   */
  public static short readInt16(RandomAccessFile raf) throws IOException {
    byte[] bytes = new byte[2];
    if (raf.read(bytes) != 2) {
      long offset = raf.getFilePointer();
      throw new IOException("Failed to read 2 bytes from file at offset " + offset);
    }
    return toInt16(bytes);
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
    if (raf.read(bytes) != 4) {
      throw new IOException("Failed to read 4 bytes from file");
    }
    return toInt32(bytes);
  }

  /**
   * Reads a big-endian 32-bit float from a RandomAccessFile.
   *
   * @param raf The RandomAccessFile to read from.
   * @return The 32-bit float.
   * @throws IOException If an I/O error occurs.
   */
  public static float readFloat(RandomAccessFile raf) throws IOException {
    byte[] bytes = new byte[4];
    if (raf.read(bytes) != 4) {
      throw new IOException("Failed to read 4 bytes from file");
    }
    return toFloat(bytes);
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

  /**
   * Encodes the given String of text into shift-jis. This is necessary for GNT4 paths since the ISO
   * expects them to be in shift-jis encoding.
   *
   * @param text The text to encode to shift-jis.
   * @return The shift-jis encoded text.
   * @throws CharacterCodingException If the text cannot be encoded/decoded as shift-jis.
   */
  public static String encodeShiftJis(String text) throws CharacterCodingException {
    Charset charset = Charset.forName("shift-jis");
    CharsetDecoder decoder = charset.newDecoder();
    CharsetEncoder encoder = charset.newEncoder();
    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
    CharBuffer cbuf = decoder.decode(bbuf);
    return cbuf.toString();
  }

  /**
   * Converts a float to a four-byte array.
   *
   * @param value The float to convert.
   * @return The four byte array representing the float.
   */
  public static byte[] floatToBytes(float value) {
    return ByteBuffer.allocate(4).putFloat(value).array();
  }

  /**
   * Converts a four-byte array to a float.
   *
   * @param bytes The bytes to convert.
   * @return The float representing the bytes.
   */
  public static float bytesToFloat(byte[] bytes) {
    int intBits =
        bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    return Float.intBitsToFloat(intBits);
  }

  /**
   * Converts a float String to a four-byte array. A float String in this case is a String like
   * "1.5", "2", or "-0.0000056".
   *
   * @param value The float String to convert.
   * @return The bytes representing the float String.
   */
  public static byte[] floatStringToBytes(String value) {
    return floatToBytes(Float.valueOf(value));
  }

  /**
   * Converts a four-byte array to a float String. A float String in this case is a String like
   * "1.5", "2", or "-0.0000056".
   *
   * @param bytes The four-byte array to convert.
   * @return The float String representing the bytes.
   */
  public static String bytesToStringFloat(byte[] bytes) {
    return Float.toString(bytesToFloat(bytes));
  }

  /**
   * Converts a byte array to a hex String. The hex returned will be in uppercase.
   *
   * @param bytes The bytes to convert.
   * @return The bytes in hex.
   */
  public static String bytesToHexString(byte[] bytes) {
    return BaseEncoding.base16().upperCase().encode(bytes);
  }

  /**
   * Converts a byte array to a hex String. The hex returned will be in uppercase. The bytes will
   * be returned as 32-bit words.
   *
   * @param bytes The bytes to convert.
   * @return The bytes in hex.
   */
  public static String bytesToHexStringWords(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(String.format("%02X", bytes[i]));
      if (i % 4 == 3 && i != bytes.length - 1) {
        sb.append(' ');
      }
    }
    return sb.toString();
  }

  /**
   * Converts a hex String to a byte array.
   *
   * @param hex The hex String.
   * @return The bytes.
   */
  public static byte[] hexStringToBytes(String hex) {
    return BaseEncoding.base16().decode(hex);
  }

  /**
   * Gets the next byte aligned position using a given modulo.
   *
   * @param currentPosition The current position to check against.
   * @param modulo          The modulo to use.
   * @return The next byte aligned position.
   */
  public static int nextAlignedPos(int currentPosition, int modulo) {
    int remainder = currentPosition % modulo;
    if (remainder == 0) {
      return currentPosition;
    }
    return currentPosition + (modulo - remainder);
  }

  /**
   * Gets the previous byte aligned position using a given modulo.
   *
   * @param currentPosition The current position to check against.
   * @param modulo          The modulo to use.
   * @return The previous byte aligned position.
   */
  public static int previousAlignedPos(int currentPosition, int modulo) {
    return currentPosition - (currentPosition % modulo);
  }

  /**
   * Converts a long to a String of hex bytes.
   *
   * @param value The long to convert.
   * @return The String of hex bytes.
   */
  public static String fromLong(long value) {
    return ByteUtils.bytesToHexString(ByteUtils.fromUint32(value));
  }

  /**
   * Align a RandomAccessFile to a provided alignment byte-boundary.
   *
   * @param raf The RandomAccessFile to align.
   * @param alignment The number of bytes to align on.
   * @throws IOException If an I/O error occurs.
   */
  public static void byteAlign(RandomAccessFile raf, int alignment) throws IOException {
    long offset = raf.getFilePointer();
    if (offset % alignment != 0) {
      raf.skipBytes((int) (alignment - (offset % alignment)));
    }
  }
}
