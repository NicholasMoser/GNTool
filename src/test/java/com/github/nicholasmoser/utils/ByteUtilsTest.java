package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.primitives.Bytes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteUtilsTest {

  /**
   * Edge case testing for {@link ByteUtils#fromUint32(long)}
   */
  @Test
  public void testFromUint32() {
    byte[] bytes = ByteUtils.fromUint32(-1L);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromUint32(0L);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromUint32(11L);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x0B, bytes[3]);
    bytes = ByteUtils.fromUint32(0x3C070002L);
    assertEquals((byte) 0x3C, bytes[0]);
    assertEquals((byte) 0x07, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x02, bytes[3]);
    bytes = ByteUtils.fromUint32(0x81030002L);
    assertEquals((byte) 0x81, bytes[0]);
    assertEquals((byte) 0x03, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x02, bytes[3]);
    bytes = ByteUtils.fromUint32(Integer.MAX_VALUE - 1L);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFE, bytes[3]);
    bytes = ByteUtils.fromUint32(Integer.MAX_VALUE);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromUint32(Integer.MAX_VALUE + 1L);
    assertEquals((byte) 0x80, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromUint32(0xFFFFFFFFL - 1L);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFE, bytes[3]);
    bytes = ByteUtils.fromUint32(0xFFFFFFFFL);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromUint32(0xFFFFFFFFL + 1L);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromUint32(Long.MAX_VALUE - 1L);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFE, bytes[3]);
    bytes = ByteUtils.fromUint32(Long.MAX_VALUE);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
  }

  /**
   * Edge case testing for {@link ByteUtils#fromInt32(int)}
   */
  @Test
  public void testFromInt32() {
    byte[] bytes = ByteUtils.fromInt32(-1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromInt32(0);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromInt32(11);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x0B, bytes[3]);
    bytes = ByteUtils.fromInt32(0x3C070002);
    assertEquals((byte) 0x3C, bytes[0]);
    assertEquals((byte) 0x07, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x02, bytes[3]);
    bytes = ByteUtils.fromInt32(0x81030002);
    assertEquals((byte) 0x81, bytes[0]);
    assertEquals((byte) 0x03, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x02, bytes[3]);
    bytes = ByteUtils.fromInt32(Integer.MAX_VALUE - 1);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFE, bytes[3]);
    bytes = ByteUtils.fromInt32(Integer.MAX_VALUE);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromInt32(Integer.MAX_VALUE + 1);
    assertEquals((byte) 0x80, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromInt32(0xFFFFFFFF - 1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFE, bytes[3]);
    bytes = ByteUtils.fromInt32(0xFFFFFFFF);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromInt32(0xFFFFFFFF + 1);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
  }

  /**
   * Edge case testing for {@link ByteUtils#fromUint24(int)}
   */
  @Test
  public void testFromUint24() {
    byte[] bytes = ByteUtils.fromUint24(-1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    bytes = ByteUtils.fromUint24(0);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    bytes = ByteUtils.fromUint24(11);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x0B, bytes[2]);
    bytes = ByteUtils.fromUint24(0x3C070002);
    assertEquals((byte) 0x07, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x02, bytes[2]);
    bytes = ByteUtils.fromUint24(Integer.MAX_VALUE - 1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFE, bytes[2]);
    bytes = ByteUtils.fromUint24(Integer.MAX_VALUE);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    bytes = ByteUtils.fromUint24(Integer.MAX_VALUE + 1);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    bytes = ByteUtils.fromUint24(0xFFFFFFFF - 1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFE, bytes[2]);
    bytes = ByteUtils.fromUint24(0xFFFFFFFF);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    bytes = ByteUtils.fromUint24(0xFFFFFFFF + 1);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
  }

  /**
   * Edge case testing for {@link ByteUtils#fromUint16(int)}
   */
  @Test
  public void testFromUint16() {
    byte[] bytes = ByteUtils.fromUint16(-1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    bytes = ByteUtils.fromUint16(0);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    bytes = ByteUtils.fromUint16(11);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x0B, bytes[1]);
    bytes = ByteUtils.fromUint16(Short.MAX_VALUE - 1);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFE, bytes[1]);
    bytes = ByteUtils.fromUint16(Short.MAX_VALUE);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    bytes = ByteUtils.fromUint16(Short.MAX_VALUE + 1);
    assertEquals((byte) 0x80, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    bytes = ByteUtils.fromUint16(Integer.MAX_VALUE - 1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFE, bytes[1]);
    bytes = ByteUtils.fromUint16(Integer.MAX_VALUE);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    bytes = ByteUtils.fromUint16(Integer.MAX_VALUE + 1);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    bytes = ByteUtils.fromUint16(0xFFFFFFFF - 1);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFE, bytes[1]);
    bytes = ByteUtils.fromUint16(0xFFFFFFFF);
    assertEquals((byte) 0xFF, bytes[0]);
    assertEquals((byte) 0xFF, bytes[1]);
    bytes = ByteUtils.fromUint16(0xFFFFFFFF + 1);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
  }

  /**
   * Edge case testing for {@link ByteUtils#fromFloat(float)}
   */
  @Test
  public void testFromFloat() {
    byte[] bytes = ByteUtils.fromFloat(-1.0f);
    assertEquals((byte) 0xBF, bytes[0]);
    assertEquals((byte) 0x80, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromFloat(0.0f);
    assertEquals((byte) 0x00, bytes[0]);
    assertEquals((byte) 0x00, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromFloat(11.0f);
    assertEquals((byte) 0x41, bytes[0]);
    assertEquals((byte) 0x30, bytes[1]);
    assertEquals((byte) 0x00, bytes[2]);
    assertEquals((byte) 0x00, bytes[3]);
    bytes = ByteUtils.fromFloat(425.924f);
    assertEquals((byte) 0x43, bytes[0]);
    assertEquals((byte) 0xD4, bytes[1]);
    assertEquals((byte) 0xF6, bytes[2]);
    assertEquals((byte) 0x46, bytes[3]);
    bytes = ByteUtils.fromFloat(Float.MAX_VALUE - 1.0f);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0x7F, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromFloat(Float.MAX_VALUE);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0x7F, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
    bytes = ByteUtils.fromFloat(Float.MAX_VALUE + 1.0f);
    assertEquals((byte) 0x7F, bytes[0]);
    assertEquals((byte) 0x7F, bytes[1]);
    assertEquals((byte) 0xFF, bytes[2]);
    assertEquals((byte) 0xFF, bytes[3]);
  }

  /**
   * Edge case testing for {@link ByteUtils#toUint32(byte[])}
   */
  @Test
  public void testToUint32() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toUint32(bytes);
    assertEquals(0L, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B };
    value = ByteUtils.toUint32(bytes);
    assertEquals(11L, value);
    bytes = new byte[] { (byte) 0x3C, (byte) 0x07, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toUint32(bytes);
    assertEquals(0x3C070002L, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x81, (byte) 0x03, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toUint32(bytes);
    assertEquals(0x81030002L, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
    value = ByteUtils.toUint32(bytes);
    assertEquals(0xFFFFFFFEL, value);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toUint32(bytes);
    assertEquals(0xFFFFFFFFL, value);
  }

  /**
   * Edge case testing for {@link ByteUtils#toInt32(byte[])}
   */
  @Test
  public void testToInt32() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toInt32(bytes);
    assertEquals(0, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B };
    value = ByteUtils.toInt32(bytes);
    assertEquals(11, value);
    bytes = new byte[] { (byte) 0x3C, (byte) 0x07, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toInt32(bytes);
    assertEquals(0x3C070002, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x81, (byte) 0x03, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toInt32(bytes);
    assertEquals(0x81030002, value);
    assertTrue(value < 0);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
    value = ByteUtils.toInt32(bytes);
    assertEquals(0xFFFFFFFE, value);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toInt32(bytes);
    assertEquals(0xFFFFFFFF, value);
  }

  /**
   * Edge case testing for {@link ByteUtils#toUint16(byte[])}
   */
  @Test
  public void testToUint16() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toUint16(bytes);
    assertEquals(0, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x0B };
    value = ByteUtils.toUint16(bytes);
    assertEquals(11, value);
    bytes = new byte[] { (byte) 0x3C, (byte) 0x07 };
    value = ByteUtils.toUint16(bytes);
    assertEquals(0x3C07, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x81, (byte) 0x03 };
    value = ByteUtils.toUint16(bytes);
    assertEquals(0x8103, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFE };
    value = ByteUtils.toUint16(bytes);
    assertEquals(0xFFFE, value);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toUint16(bytes);
    assertEquals(0xFFFF, value);
  }

  /**
   * Edge case testing for {@link ByteUtils#toFloat(byte[])}
   */
  @Test
  public void testToFloat() {
    byte[] bytes = new byte[] { (byte) 0xBF, (byte) 0x80, (byte) 0x00, (byte) 0x00 };
    float value = ByteUtils.toFloat(bytes);
    assertEquals(-1.0f, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    value = ByteUtils.toFloat(bytes);
    assertEquals(0.0f, value);
    bytes = new byte[] { (byte) 0x41, (byte) 0x30, (byte) 0x00, (byte) 0x00 };
    value = ByteUtils.toFloat(bytes);
    assertEquals(11.0f, value);
    bytes = new byte[] { (byte) 0x43, (byte) 0xD4, (byte) 0xF6, (byte) 0x46 };
    value = ByteUtils.toFloat(bytes);
    assertEquals(425.924f, value);
    bytes = new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toFloat(bytes);
    assertEquals(Float.MAX_VALUE - 1.0f, value);
    bytes = new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toFloat(bytes);
    assertEquals(Float.MAX_VALUE, value);
    bytes = new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toFloat(bytes);
    assertEquals(Float.MAX_VALUE + 1.0f, value);
  }

  /**
   * Edge case testing for {@link ByteUtils#toUint32(byte[], int)} 
   */
  @Test
  public void testToUint32WithOffset() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toUint32(bytes, 0);
    assertEquals(0L, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0x00 };
    value = ByteUtils.toUint32(bytes, 0);
    assertEquals(11L, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x3C, (byte) 0x07, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toUint32(bytes, 1);
    assertEquals(0x3C070002L, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x00, (byte) 0x81, (byte) 0x03, (byte) 0x00, (byte) 0x02, (byte) 0x00 };
    value = ByteUtils.toUint32(bytes, 1);
    assertEquals(0x81030002L, value);
    assertTrue(value > 0);
    byte[] zeroes = new byte[512];
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
    value = ByteUtils.toUint32(Bytes.concat(zeroes, bytes), 512);
    assertEquals(0xFFFFFFFEL, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toUint32(Bytes.concat(bytes, zeroes), 1);
    assertEquals(0xFFFFFFFFL, value);
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      byte[] errorBytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
      ByteUtils.toUint32(errorBytes, 5);
    });
  }

  /**
   * Edge case testing for {@link ByteUtils#toInt32(byte[], int)} 
   */
  @Test
  public void testToInt32WithOffset() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toInt32(bytes, 0);
    assertEquals(0, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0x00 };
    value = ByteUtils.toInt32(bytes, 0);
    assertEquals(11, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0x3C, (byte) 0x07, (byte) 0x00, (byte) 0x02 };
    value = ByteUtils.toInt32(bytes, 1);
    assertEquals(0x3C070002, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x00, (byte) 0x81, (byte) 0x03, (byte) 0x00, (byte) 0x02, (byte) 0x00 };
    value = ByteUtils.toInt32(bytes, 1);
    assertEquals(0x81030002, value);
    assertTrue(value < 0);
    byte[] zeroes = new byte[512];
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE };
    value = ByteUtils.toInt32(Bytes.concat(zeroes, bytes), 512);
    assertEquals(0xFFFFFFFE, value);
    bytes = new byte[] { (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toInt32(Bytes.concat(bytes, zeroes), 1);
    assertEquals(0xFFFFFFFF, value);
    Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      byte[] errorBytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
      ByteUtils.toInt32(errorBytes, 5);
    });
  }

  /**
   * Edge case testing for {@link ByteUtils#toUint32LE(byte[])}
   */
  @Test
  public void testToUint32LE() {
    byte[] bytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    long value = ByteUtils.toUint32LE(bytes);
    assertEquals(0L, value);
    bytes = new byte[] { (byte) 0x0B, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    value = ByteUtils.toUint32LE(bytes);
    assertEquals(11L, value);
    bytes = new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x07, (byte) 0x3C };
    value = ByteUtils.toUint32LE(bytes);
    assertEquals(0x3C070002L, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x81 };
    value = ByteUtils.toUint32LE(bytes);
    assertEquals(0x81030002L, value);
    assertTrue(value > 0);
    bytes = new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toUint32LE(bytes);
    assertEquals(0xFFFFFFFEL, value);
    bytes = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    value = ByteUtils.toUint32LE(bytes);
    assertEquals(0xFFFFFFFFL, value);
  }
}
