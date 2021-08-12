package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the ByteStream class.
 */
public class ByteStreamTest {

  @Test
  public void testNull() {
    assertThrows(NullPointerException.class, () -> new ByteStream(null));
  }

  @Test
  public void testZeroByteArray() throws Exception {
    byte[] bytes = new byte[0];

    // Test that you can't do anything with a zero byte array
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    assertThrows(IOException.class, bs::readWord);
    assertThrows(IOException.class, bs::peekWord);
    assertEquals(-1, bs.read());
    assertEquals(-1, bs.read(new byte[4]));
    assertThrows(IOException.class, bs::skipWord);
  }

  @Test
  public void testOneByteArray() {
    byte[] bytes = new byte[] { 0x56 };

    // Test that you can't peekWord with only one byte
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    assertThrows(IOException.class, bs::peekWord);
    assertEquals(1, bs.offset()); // It will read but fail to reset

    // Test that you can't readWord with only one byte
    ByteStream bs2 = new ByteStream(bytes);
    assertThrows(IOException.class, bs2::readWord);
    assertEquals(1, bs2.offset());

    // Test that you can still mark, read, and reset with a one byte array
    ByteStream bs3 = new ByteStream(bytes);
    bs3.mark(1);
    assertEquals(0, bs3.offset());
    assertEquals(0x56, bs3.read());
    assertEquals(1, bs3.offset());
    bs3.reset();
    assertEquals(0, bs3.offset());

    // Test skipWord
    ByteStream bs4 = new ByteStream(bytes);
    assertThrows(IOException.class, bs4::skipWord);
  }

  @Test
  public void testFourByteArray() throws Exception {
    byte[] bytes = new byte[] {(byte) 0x80, 0x00, 0x52, 0x24 };

    // Test that you can peekWord and readWord with a four-byte array
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    assertEquals(0x80005224, bs.peekWord());
    assertEquals(0, bs.offset());
    assertEquals(0x80005224, bs.readWord());
    assertEquals(4, bs.offset());
    assertThrows(IOException.class, bs::peekWord);
    assertThrows(IOException.class, bs::readWord);

    // Test that having less than 4 bytes left prevents you from calling readWord or peekWord
    ByteStream bs2 = new ByteStream(bytes);
    assertEquals(0x80, bs2.read());
    assertEquals(1, bs2.offset());
    assertThrows(IOException.class, bs2::peekWord);

    ByteStream bs3 = new ByteStream(bytes);
    assertEquals(0x80, bs3.read());
    assertEquals(1, bs3.offset());
    assertThrows(IOException.class, bs3::readWord);

    // Test skipWord
    ByteStream bs4 = new ByteStream(bytes);
    assertEquals(0, bs4.offset());
    bs4.skipWord();
    assertEquals(4, bs4.offset());
    assertThrows(IOException.class, bs4::skipWord);
  }

  @Test
  public void testEightByteArray() throws Exception {
    byte[] bytes = new byte[] { 0x11, 0x22, 0x33, 0x44, 0x01, 0x02, 0x03, 0x04 };

    // Test that you can peekWord and readWord twice with a four-byte array
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    assertEquals(0x11223344, bs.peekWord());
    assertEquals(0, bs.offset());
    assertEquals(0x11223344, bs.readWord());
    assertEquals(4, bs.offset());
    assertEquals(0x01020304, bs.peekWord());
    assertEquals(4, bs.offset());
    assertEquals(0x01020304, bs.readWord());
    assertEquals(8, bs.offset());
    assertThrows(IOException.class, bs::peekWord);
    assertThrows(IOException.class, bs::readWord);

    // Test skipWord
    ByteStream bs2 = new ByteStream(bytes);
    assertEquals(0, bs2.offset());
    bs2.skipWord();
    assertEquals(4, bs2.offset());
    bs2.skipWord();
    assertEquals(8, bs2.offset());
    assertThrows(IOException.class, bs2::skipWord);
  }
}
