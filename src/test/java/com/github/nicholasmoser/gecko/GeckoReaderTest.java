package com.github.nicholasmoser.gecko;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GeckoReaderTest {

  /**
   * Tests that parsing null returns no codes.
   */
  @Test
  public void testNullCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes(null);
    assertTrue(codes.isEmpty());
  }

  /**
   * Tests that parsing an empty String returns no codes.
   */
  @Test
  public void testEmptyCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes("");
    assertTrue(codes.isEmpty());
  }

  /**
   * Tests that parsing only whitespace returns no codes.
   */
  @Test
  public void testWhitespaceOnlyCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes("   \n  \r\n  ");
    assertTrue(codes.isEmpty());
  }

  /**
   * Tests that parsing an invalid code throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidCode() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("kdopkd21d21jfwda"));
  }

  /**
   * Tests that parsing an incorrect number of characters throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidNumberOfCharacters() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("aaa"));
  }

  /**
   * Tests that parsing incorrect insertion codes throw an IllegalArgumentException.
   */
  @Test
  public void testIncompleteInsertion() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB14"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB1400"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB140000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB14000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB1400000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB140000000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB140000000100"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB1400000001000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB140000000100000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB14000000010000000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB1400000001000000000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB140000000100000000000000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("C200CB14000000020000000000000000"));
  }

  /**
   * Tests that parsing a single insertion code returns a single insertion GeckoCode.
   */
  @Test
  public void testSingleInsertion() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes("C200CB14000000016000000000000000");
    assertEquals(1, codes.size());
    GeckoCode code = codes.get(0);
    assertTrue(code instanceof InsertAsmCode);
    assertEquals(16, code.getLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code.getBytes();
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Tests that parsing two duplicate insertion codes throws an IllegalArgumentException since they
   * insert at the same location which is invalid.
   */
  @Test
  public void testDuplicateInsertionCode() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader
        .parseCodes("C200CB14000000016000000000000000C200CB14000000016000000000000000"));
  }

  /**
   * Tests that parsing two insertion codes that insert at the same address throws an
   * IllegalArgumentException since inserting multiple codes at the same location is invalid.
   */
  @Test
  public void testDuplicateInsertionAddress() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader
        .parseCodes("C200CB14000000016000000000000000C200CB14000000013C80014500000000"));
  }

  /**
   * Tests that parsing two insertion codes returns two insertion GeckoCodes.
   */
  @Test
  public void testTwoInsertionCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader
        .parseCodes("C200CB14000000016000000000000000C200CB18000000013C80014500000000");
    assertEquals(2, codes.size());

    // Code 1
    GeckoCode code1 = codes.get(0);
    assertTrue(code1 instanceof InsertAsmCode);
    assertEquals(16, code1.getLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code1.getBytes();
    assertArrayEquals(expectedBytes, actualBytes);

    // Code 2
    GeckoCode code2 = codes.get(1);
    assertTrue(code2 instanceof InsertAsmCode);
    assertEquals(16, code2.getLength());
    byte[] expectedBytes2 = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x18, 0, 0, 0, 0x1, 0x3C,
        (byte) 0x80, 0x01, 0x45, 0, 0, 0, 0};
    byte[] actualBytes2 = code2.getBytes();
    assertArrayEquals(expectedBytes2, actualBytes2);
  }

  /**
   * Tests that parsing two insertion codes with varying types of whitespace separating them
   * and around them returns the two expected insertion codes.
   */
  @Test
  public void testWhitespaceSeparator() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> expected = reader
        .parseCodes("C200CB14000000016000000000000000C200CB18000000013C80014500000000");
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000\nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000 \nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000\n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000 \n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000   \nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000\r\nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000 \r\nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000\r\n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000 \r\n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000   \r\nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("C200CB14000000016000000000000000\r\n   C200CB18000000013C80014500000000"));
    assertEquals(expected, reader.parseCodes("\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\n"));
    assertEquals(expected, reader.parseCodes("  \nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\n  "));
    assertEquals(expected, reader.parseCodes("\n  C200CB14000000016000000000000000\nC200CB18000000013C80014500000000  \n"));
    assertEquals(expected, reader.parseCodes("  \n  C200CB14000000016000000000000000  \nC200CB18000000013C80014500000000  \n  "));
    assertEquals(expected, reader.parseCodes("\r\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\r\n"));
    assertEquals(expected, reader.parseCodes("  \r\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\r\n  "));
    assertEquals(expected, reader.parseCodes("\r\n  C200CB14000000016000000000000000\nC200CB18000000013C80014500000000  \r\n"));
    assertEquals(expected, reader.parseCodes("  \r\n  C200CB14000000016000000000000000  \nC200CB18000000013C80014500000000  \r\n  "));
  }
}
