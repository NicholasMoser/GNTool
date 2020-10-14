package com.github.nicholasmoser.gecko;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C200CB140000000100000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C200CB14000000010000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C200CB1400000001000000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C200CB140000000100000000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C200CB14000000020000000000000000"));
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
    assertEquals(16, code.getCodeLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code.getCodeBytes();
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

    GeckoCode code1 = codes.get(0);
    assertTrue(code1 instanceof InsertAsmCode);
    assertEquals(16, code1.getCodeLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code1.getCodeBytes();
    assertArrayEquals(expectedBytes, actualBytes);

    GeckoCode code2 = codes.get(1);
    assertTrue(code2 instanceof InsertAsmCode);
    assertEquals(16, code2.getCodeLength());
    byte[] expectedBytes2 = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x18, 0, 0, 0, 0x1, 0x3C,
        (byte) 0x80, 0x01, 0x45, 0, 0, 0, 0};
    byte[] actualBytes2 = code2.getCodeBytes();
    assertArrayEquals(expectedBytes2, actualBytes2);
  }

  /**
   * Tests that inserting a code before where the dol begins throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidInsertBeforeDol() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C2003000000000016000000000000000"));
  }

  /**
   * Tests that inserting a code into the init section is valid.
   */
  @Test
  public void testValidInsertIntoInit() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C20031E8000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the text section is valid.
   */
  @Test
  public void testValidInsertIntoText() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C20062A8000000016000000000000000");
    reader.parseCodes("C21CCE8C000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the ctors section is valid.
   */
  @Test
  public void testValidInsertIntoCtors() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C21FD800000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the dtors section is valid.
   */
  @Test
  public void testValidInsertIntoDtors() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C21FD820000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the rodata section is valid.
   */
  @Test
  public void testValidInsertIntoRodata() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C2200FE0000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the data section is valid.
   */
  @Test
  public void testValidInsertIntoData() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C22062C8000000016000000000000000");
    reader.parseCodes("C2220020000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the bss throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidInsertIntoBss() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C2222A70000000016000000000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C22744B0000000016000000000000000"));
  }

  /**
   * Tests that inserting a code into the sdata section is valid.
   */
  @Test
  public void testValidInsertIntoSdata() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C2276928000000016000000000000000");
    reader.parseCodes("C2276FB0000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the sbss throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidInsertIntoSbss() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C2277028000000016000000000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C2277C8C000000016000000000000000"));
  }

  /**
   * Tests that inserting a code into the sdata2 section is valid.
   */
  @Test
  public void testValidInsertIntoSdata2() {
    GeckoReader reader = new GeckoReader();
    reader.parseCodes("C2277CE0000000016000000000000000");
    reader.parseCodes("C227C548000000016000000000000000");
  }

  /**
   * Tests that inserting a code into the sbss2 throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidInsertIntoSbss2() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C227C560000000016000000000000000"));
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C227C574000000016000000000000000"));
  }

  /**
   * Tests that inserting a code after where the dol ends throws an IllegalArgumentException.
   */
  @Test
  public void testInvalidInsertAfterDol() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("C22DB4E3000000016000000000000000"));
  }

  /**
   * Tests that parsing a single insertion code with lowercase letters returns a single insertion
   * GeckoCode.
   */
  @Test
  public void testSingleInsertionLowercase() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes("c200cb14000000016000000000000000");
    assertEquals(1, codes.size());
    GeckoCode code = codes.get(0);
    assertTrue(code instanceof InsertAsmCode);
    assertEquals(16, code.getCodeLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code.getCodeBytes();
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Tests that parsing a single 32-bit write code returns a single 32-bit write GeckoCode.
   */
  @Test
  public void testSingle32BitWrite() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader.parseCodes("040374B860000000");
    assertEquals(1, codes.size());
    GeckoCode code = codes.get(0);
    assertTrue(code instanceof Write32BitsCode);
    assertEquals(8, code.getCodeLength());
    byte[] expectedBytes = new byte[]{0x04, 0x03, 0x74, (byte) 0xB8, 0x60, 0, 0, 0};
    byte[] actualBytes = code.getCodeBytes();
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Tests that parsing two 32-bit write codes that insert at the same address throws an
   * IllegalArgumentException since inserting multiple codes at the same location is invalid.
   */
  @Test
  public void testDuplicateSingle32BitWrites() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class,
        () -> reader.parseCodes("040374B860000000040374B860000000"));
  }

  /**
   * Tests that parsing two 32-bit write codes returns two 32-bit write GeckoCodes.
   */
  @Test
  public void testTwo32BitWriteCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader
        .parseCodes("0416E15CC3A2A02404279CC43FE38E39");
    assertEquals(2, codes.size());

    GeckoCode code1 = codes.get(0);
    assertTrue(code1 instanceof Write32BitsCode);
    assertEquals(8, code1.getCodeLength());
    byte[] expectedBytes = new byte[]{0x04, 0x16, (byte) 0xE1, 0x5C, (byte) 0xC3, (byte) 0xA2,
        (byte) 0xA0, 0x24};
    byte[] actualBytes = code1.getCodeBytes();
    assertArrayEquals(expectedBytes, actualBytes);

    GeckoCode code2 = codes.get(1);
    assertTrue(code2 instanceof Write32BitsCode);
    assertEquals(8, code2.getCodeLength());
    byte[] expectedBytes2 = new byte[]{0x04, 0x27, (byte) 0x9C, (byte) 0xC4, 0x3F, (byte) 0xE3,
        (byte) 0x8E, 0x39};
    byte[] actualBytes2 = code2.getCodeBytes();
    assertArrayEquals(expectedBytes2, actualBytes2);
  }

  /**
   * Tests that parsing incorrect 32-bit write codes throw an IllegalArgumentException.
   */
  @Test
  public void testIncomplete32BitWrite() {
    GeckoReader reader = new GeckoReader();
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("04"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("0403"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("04037"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B8"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B86"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B860"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B86000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B860000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B8600000"));
    assertThrows(IllegalArgumentException.class, () -> reader.parseCodes("040374B86000000"));
  }

  /**
   * Tests that parsing one Asm Insert code and one 32-bit write code returns a insert asm GeckoCode
   * and a 32-bit write GeckoCode.
   */
  @Test
  public void testOneInsertionCodeOne32BitWriteCodes() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> codes = reader
        .parseCodes("C200CB1400000001600000000000000004279CC43FE38E39");
    assertEquals(2, codes.size());

    GeckoCode code1 = codes.get(0);
    assertTrue(code1 instanceof InsertAsmCode);
    assertEquals(16, code1.getCodeLength());
    byte[] expectedBytes = new byte[]{(byte) 0xC2, 0, (byte) 0xCB, 0x14, 0, 0, 0, 0x1, 0x60, 0, 0,
        0, 0, 0, 0, 0};
    byte[] actualBytes = code1.getCodeBytes();
    assertArrayEquals(expectedBytes, actualBytes);

    GeckoCode code2 = codes.get(1);
    assertTrue(code2 instanceof Write32BitsCode);
    assertEquals(8, code2.getCodeLength());
    byte[] expectedBytes2 = new byte[]{0x04, 0x27, (byte) 0x9C, (byte) 0xC4, 0x3F, (byte) 0xE3,
        (byte) 0x8E, 0x39};
    byte[] actualBytes2 = code2.getCodeBytes();
    assertArrayEquals(expectedBytes2, actualBytes2);

    // Validate that the codes are the same even when in a different order
    List<GeckoCode> codesReversed = reader
        .parseCodes("04279CC43FE38E39C200CB14000000016000000000000000");
    assertTrue(codesReversed.containsAll(codes));
    assertTrue(codes.containsAll(codesReversed));
  }

  /**
   * Tests that parsing two insertion codes with varying types of whitespace separating them and
   * around them returns the two expected insertion codes.
   */
  @Test
  public void testWhitespaceSeparator() {
    GeckoReader reader = new GeckoReader();
    List<GeckoCode> expected = reader
        .parseCodes("C200CB14000000016000000000000000C200CB18000000013C80014500000000");
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000\nC200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000 \nC200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000\n C200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000 \n C200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000   \nC200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000\r\nC200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000 \r\nC200CB18000000013C80014500000000"));
    assertEquals(expected,
        reader.parseCodes("C200CB14000000016000000000000000\r\n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader
        .parseCodes("C200CB14000000016000000000000000 \r\n C200CB18000000013C80014500000000"));
    assertEquals(expected, reader
        .parseCodes("C200CB14000000016000000000000000   \r\nC200CB18000000013C80014500000000"));
    assertEquals(expected, reader
        .parseCodes("C200CB14000000016000000000000000\r\n   C200CB18000000013C80014500000000"));
    assertEquals(expected, reader
        .parseCodes("\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\n"));
    assertEquals(expected, reader
        .parseCodes("  \nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\n  "));
    assertEquals(expected, reader
        .parseCodes("\n  C200CB14000000016000000000000000\nC200CB18000000013C80014500000000  \n"));
    assertEquals(expected, reader.parseCodes(
        "  \n  C200CB14000000016000000000000000  \nC200CB18000000013C80014500000000  \n  "));
    assertEquals(expected, reader
        .parseCodes("\r\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\r\n"));
    assertEquals(expected, reader.parseCodes(
        "  \r\nC200CB14000000016000000000000000\nC200CB18000000013C80014500000000\r\n  "));
    assertEquals(expected, reader.parseCodes(
        "\r\n  C200CB14000000016000000000000000\nC200CB18000000013C80014500000000  \r\n"));
    assertEquals(expected, reader.parseCodes(
        "  \r\n  C200CB14000000016000000000000000  \nC200CB18000000013C80014500000000  \r\n  "));
  }
}
