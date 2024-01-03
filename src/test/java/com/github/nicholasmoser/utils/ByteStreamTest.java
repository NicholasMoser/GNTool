package com.github.nicholasmoser.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the ByteStream class.
 */
public class ByteStreamTest {

  /**
   * Tests a ByteStream with a null byte array.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testNull() {
    assertThrows(NullPointerException.class, () -> new ByteStream(null));
  }

  /**
   * Tests a ByteStream with an empty byte array.
   *
   * @throws Exception If any Exception occurs.
   */
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

  /**
   * Tests a ByteStream with a one byte array.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testOneByteArray() {
    byte[] bytes = new byte[]{0x56};

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

  /**
   * Tests a ByteStream with a four byte array.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testFourByteArray() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x80, 0x00, 0x52, 0x24};

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

  /**
   * Tests a ByteStream with an eight byte array.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testEightByteArray() throws Exception {
    byte[] bytes = new byte[]{0x11, 0x22, 0x33, 0x44, 0x01, 0x02, 0x03, 0x04};

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

  /**
   * Tests that reading an arbitrary number of bytes via readBytes works as expected.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testReadBytes() throws Exception {
    byte[] bytes = new byte[]{0x11, 0x22, 0x33, 0x44, 0x01, 0x02, 0x03, 0x04};
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    assertArrayEquals(new byte[]{0x11, 0x22, 0x33, 0x44}, bs.readBytes(4));
    assertEquals(4, bs.offset());
    assertArrayEquals(new byte[]{0x01, 0x02}, bs.readBytes(2));
    assertEquals(6, bs.offset());
    assertArrayEquals(new byte[]{0x03}, bs.readBytes(1));
    assertEquals(7, bs.offset());
    assertArrayEquals(new byte[]{}, bs.readBytes(0));
    assertEquals(7, bs.offset());
  }

  /**
   * Test the length function of ByteStream.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testLength() throws Exception {
    byte[] bytes = new byte[]{0x11, 0x22, 0x33, 0x44, 0x01, 0x02, 0x03, 0x04};
    ByteStream bs = new ByteStream(bytes);
    assertEquals(8, bs.length());
    bs.readBytes(4);
    assertEquals(8, bs.length());
    bs = new ByteStream(new byte[0]);
    assertEquals(0, bs.length());
    bs = new ByteStream(new byte[]{0x11});
    assertEquals(1, bs.length());
  }

  /**
   * Test the seek function of ByteStream.
   */
  @Test
  public void testSeek() {
    byte[] bytes = new byte[]{0x11, 0x22, 0x33, 0x44, 0x01, 0x02, 0x03, 0x04};
    ByteStream bs = new ByteStream(bytes);
    assertEquals(0, bs.offset());
    bs.seek(0);
    assertEquals(0, bs.offset());
    bs.seek(1);
    assertEquals(1, bs.offset());
    bs.seek(2);
    assertEquals(2, bs.offset());
    bs.seek(3);
    assertEquals(3, bs.offset());
    bs.seek(4);
    assertEquals(4, bs.offset());
    bs.seek(5);
    assertEquals(5, bs.offset());
    bs.seek(6);
    assertEquals(6, bs.offset());
    bs.seek(7);
    assertEquals(7, bs.offset());
    Assertions.assertThrows(IllegalArgumentException.class, () -> bs.seek(8));
    Assertions.assertThrows(IllegalArgumentException.class, () -> bs.seek(44444));
    bs.seek(4);
    assertEquals(4, bs.offset());
    bs.seek(0);
    assertEquals(0, bs.offset());
    bs.seek(3);
    assertEquals(3, bs.offset());
    Assertions.assertThrows(IllegalArgumentException.class, () -> bs.seek(-1));
    Assertions.assertThrows(IllegalArgumentException.class, () -> bs.seek(-55555));
  }

  @Test
  public void testBytesAreLeft() throws Exception {
    ByteStream bs = new ByteStream(new byte[] {1, 2, 3, 4});
    assertThat(bs.bytesAreLeft()).isTrue();
    assertThat(bs.bytesAreLeft(1)).isTrue();
    assertThat(bs.bytesAreLeft(2)).isTrue();
    assertThat(bs.bytesAreLeft(3)).isTrue();
    assertThat(bs.bytesAreLeft(4)).isTrue();
    assertThat(bs.bytesAreLeft(5)).isFalse();
    assertThat(bs.bytesAreLeft(6)).isFalse();
    assertThat(bs.bytesAreLeft(8)).isFalse();
    assertThat(bs.bytesAreLeft(16)).isFalse();

    bs.seek(2); // halfway through bytes
    assertThat(bs.bytesAreLeft()).isTrue();
    assertThat(bs.bytesAreLeft(1)).isTrue();
    assertThat(bs.bytesAreLeft(2)).isTrue();
    assertThat(bs.bytesAreLeft(3)).isFalse();
    assertThat(bs.bytesAreLeft(4)).isFalse();
    assertThat(bs.bytesAreLeft(5)).isFalse();
    assertThat(bs.bytesAreLeft(6)).isFalse();
    assertThat(bs.bytesAreLeft(8)).isFalse();
    assertThat(bs.bytesAreLeft(16)).isFalse();

    bs.skipNBytes(2); // end of bytes
    assertThat(bs.bytesAreLeft()).isFalse();
    assertThat(bs.bytesAreLeft(1)).isFalse();
    assertThat(bs.bytesAreLeft(2)).isFalse();
    assertThat(bs.bytesAreLeft(3)).isFalse();
    assertThat(bs.bytesAreLeft(4)).isFalse();
    assertThat(bs.bytesAreLeft(5)).isFalse();
    assertThat(bs.bytesAreLeft(6)).isFalse();
    assertThat(bs.bytesAreLeft(8)).isFalse();
    assertThat(bs.bytesAreLeft(16)).isFalse();
  }
}
