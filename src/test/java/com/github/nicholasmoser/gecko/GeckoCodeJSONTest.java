package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import com.github.nicholasmoser.gecko.active.ActiveWrite32BitsCode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeckoCodeJSONTest {

  /**
   * Tests parsing no codes being applied.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testParseEmptyCodes() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/empty.json");
    List<GeckoCodeGroup> groups = GeckoCodeJSON.parseFile(testFile);
    assertEquals(0, groups.size());
  }

  /**
   * Tests parsing a single Write 32 bits code being applied, Skip Three Intro Videos.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testParseSkipThreeIntroVideos() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/skip_intro.json");
    List<GeckoCodeGroup> groups = GeckoCodeJSON.parseFile(testFile);
    assertEquals(1, groups.size());
    validateSkipIntro(groups.get(0));
  }

  /**
   * Tests parsing a single Insert ASM code being applied, Enable Dpad.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testParseEnableDpad() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/enable_dpad.json");
    List<GeckoCodeGroup> groups = GeckoCodeJSON.parseFile(testFile);
    assertEquals(1, groups.size());
    validateEnableDpad(groups.get(0));
  }

  /**
   * Tests parsing two codes, an Insert ASM code (Enable Dpad) and a Write 32 bits code (Skip
   * Intro).
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testParseEnableDpadAndSkipIntro() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/enable_dpad_and_skip_intro.json");
    List<GeckoCodeGroup> groups = GeckoCodeJSON.parseFile(testFile);
    assertEquals(2, groups.size());
    validateEnableDpad(groups.get(0));
    validateSkipIntro(groups.get(1));
  }

  /**
   * Tests parsing two codes, a Write 32 bits code (Skip Intro) and an Insert ASM code (Enable
   * Dpad).
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testParseSkipIntroAndEnableDpad() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/skip_intro_and_enable_dpad.json");
    List<GeckoCodeGroup> groups = GeckoCodeJSON.parseFile(testFile);
    assertEquals(2, groups.size());
    validateSkipIntro(groups.get(0));
    validateEnableDpad(groups.get(1));
  }

  /**
   * Writes an empty list of code groups and asserts that it matches the expected empty code JSON
   * file.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testWriteEmptyCodes() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/temp.json");
    Path expectedFile = Paths.get("src/test/resources/CodeJson/empty.json");
    try {
      List<GeckoCodeGroup> codeGroups = new ArrayList<>();
      GeckoCodeJSON.writeFile(codeGroups, testFile);
      byte[] actualBytes = Files.readAllBytes(testFile);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  /**
   * Tests writing a single Write 32 bits code being applied, Skip Three Intro Videos.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testWriteSkipThreeIntroVideos() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/temp.json");
    Path expectedFile = Paths.get("src/test/resources/CodeJson/skip_intro.json");
    try {
      List<GeckoCodeGroup> codeGroups = new ArrayList<>();
      codeGroups.add(getSkipIntroCodeGroup());
      GeckoCodeJSON.writeFile(codeGroups, testFile);
      byte[] actualBytes = Files.readAllBytes(testFile);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  /**
   * Tests writing a single Insert ASM code being applied, Enable Dpad.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testWriteEnableDpad() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/temp.json");
    Path expectedFile = Paths.get("src/test/resources/CodeJson/enable_dpad.json");
    try {
      List<GeckoCodeGroup> codeGroups = new ArrayList<>();
      codeGroups.add(getEnableDpadCodeGroup());
      GeckoCodeJSON.writeFile(codeGroups, testFile);
      byte[] actualBytes = Files.readAllBytes(testFile);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  /**
   * Tests writing two codes, an Insert ASM code (Enable Dpad) and a Write 32 bits code (Skip
   * Intro).
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testWriteEnableDpadAndSkipIntro() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/temp.json");
    Path expectedFile = Paths.get("src/test/resources/CodeJson/enable_dpad_and_skip_intro.json");
    try {
      List<GeckoCodeGroup> codeGroups = new ArrayList<>();
      codeGroups.add(getEnableDpadCodeGroup());
      codeGroups.add(getSkipIntroCodeGroup());
      GeckoCodeJSON.writeFile(codeGroups, testFile);
      byte[] actualBytes = Files.readAllBytes(testFile);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  /**
   * Tests writing two codes, a Write 32 bits code (Skip Intro) and an Insert ASM code (Enable
   * Dpad).
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testWriteSkipIntroAndEnableDpad() throws Exception {
    Path testFile = Paths.get("src/test/resources/CodeJson/temp.json");
    Path expectedFile = Paths.get("src/test/resources/CodeJson/skip_intro_and_enable_dpad.json");
    try {
      List<GeckoCodeGroup> codeGroups = new ArrayList<>();
      codeGroups.add(getSkipIntroCodeGroup());
      codeGroups.add(getEnableDpadCodeGroup());
      GeckoCodeJSON.writeFile(codeGroups, testFile);
      byte[] actualBytes = Files.readAllBytes(testFile);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  private GeckoCodeGroup getEnableDpadCodeGroup() {
    List<GeckoCode> codes = new ArrayList<>();
    ActiveInsertAsmCode code = new ActiveInsertAsmCode.Builder()
        .targetAddress(0x80042C40L)
        .bytes(new byte[]{0x54, (byte) 0xA0, 0x07, (byte) 0xBD, 0x41, (byte) 0x82, 0x00, 0x08, 0x64,
            (byte) 0xA5, 0x00, 0x08, 0x54, (byte) 0xA0, 0x07, (byte) 0xFF, 0x41, (byte) 0x82, 0x00,
            0x08, 0x64, (byte) 0xA5, 0x00, 0x04, 0x54, (byte) 0xA0, 0x07, 0x7B, 0x41, (byte) 0x82,
            0x00, 0x08, 0x64, (byte) 0xA5, 0x00, 0x02, 0x54, (byte) 0xA0, 0x07, 0x39, 0x41,
            (byte) 0x82, 0x00, 0x08, 0x64, (byte) 0xA5, 0x00, 0x01, (byte) 0x80, 0x03, 0x01, 0x54})
        .replacedBytes(new byte[]{0x12, 0x34, 0x56, 0x78})
        .hijackedAddress(0x80060000L)
        .hijackedBytes(
            new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
                0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12,
                0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34,
                0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56,
                0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78,
                (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78})
        .create();
    codes.add(code);
    return new GeckoCodeGroup("Enable DPad During Fights", codes);
  }

  private GeckoCodeGroup getSkipIntroCodeGroup() {
    List<GeckoCode> codes = new ArrayList<>();
    ActiveWrite32BitsCode code1 = new ActiveWrite32BitsCode.Builder()
        .targetAddress(0x8000CB14L)
        .bytes(new byte[]{0x60, 0x00, 0x00, 0x00})
        .replacedBytes(new byte[]{0x12, 0x34, 0x56, 0x78})
        .create();
    codes.add(code1);
    ActiveWrite32BitsCode code2 = new ActiveWrite32BitsCode.Builder()
        .targetAddress(0x8000CB28L)
        .bytes(new byte[]{0x60, 0x00, 0x00, 0x00})
        .replacedBytes(new byte[]{0x22, 0x22, 0x11, 0x11})
        .create();
    codes.add(code2);
    ActiveWrite32BitsCode code3 = new ActiveWrite32BitsCode.Builder()
        .targetAddress(0x8000CB3CL)
        .bytes(new byte[]{0x60, 0x00, 0x00, 0x00})
        .replacedBytes(new byte[]{0x44, 0x44, 0x33, 0x33})
        .create();
    codes.add(code3);
    return new GeckoCodeGroup("Skip Three Intro Videos", codes);
  }

  /**
   * Helper function to validate that the Enable Dpad code is correct.
   *
   * @param group The Enable Dpad code group.
   */
  private void validateEnableDpad(GeckoCodeGroup group) {
    assertEquals("Enable DPad During Fights", group.getName());
    List<GeckoCode> codeList = group.getCodes();
    assertEquals(1, codeList.size());
    GeckoCode code = codeList.get(0);
    assertTrue(code instanceof ActiveInsertAsmCode);
    ActiveInsertAsmCode writeCode1 = (ActiveInsertAsmCode) code;
    assertEquals(0x80042C40L, writeCode1.getTargetAddress());
    assertArrayEquals(
        new byte[]{0x54, (byte) 0xA0, 0x07, (byte) 0xBD, 0x41, (byte) 0x82, 0x00, 0x08, 0x64,
            (byte) 0xA5, 0x00, 0x08, 0x54, (byte) 0xA0, 0x07, (byte) 0xFF, 0x41, (byte) 0x82, 0x00,
            0x08, 0x64, (byte) 0xA5, 0x00, 0x04, 0x54, (byte) 0xA0, 0x07, 0x7B, 0x41, (byte) 0x82,
            0x00, 0x08, 0x64, (byte) 0xA5, 0x00, 0x02, 0x54, (byte) 0xA0, 0x07, 0x39, 0x41,
            (byte) 0x82, 0x00, 0x08, 0x64, (byte) 0xA5, 0x00, 0x01, (byte) 0x80, 0x03, 0x01, 0x54},
        writeCode1.getBytes());
    assertArrayEquals(new byte[]{0x12, 0x34, 0x56, 0x78}, writeCode1.getReplacedBytes());
    assertEquals(0x80060000L, writeCode1.getHijackedAddress());
    assertArrayEquals(
        new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12,
            0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56,
            0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78,
            (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78, (byte) 0x90,
            (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56,
            0x78},
        writeCode1.getHijackedBytes());
  }

  /**
   * Helper function to validate that the Skip Intro code is correct.
   *
   * @param group The Skip Intro code group.
   */
  private void validateSkipIntro(GeckoCodeGroup group) {
    assertEquals("Skip Three Intro Videos", group.getName());
    List<GeckoCode> codeList = group.getCodes();
    assertEquals(3, codeList.size());

    GeckoCode code1 = codeList.get(0);
    assertTrue(code1 instanceof ActiveWrite32BitsCode);
    ActiveWrite32BitsCode writeCode1 = (ActiveWrite32BitsCode) code1;
    assertEquals(0x8000CB14L, writeCode1.getTargetAddress());
    assertArrayEquals(new byte[]{0x60, 0, 0, 0}, writeCode1.getBytes());
    assertArrayEquals(new byte[]{0x12, 0x34, 0x56, 0x78}, writeCode1.getReplacedBytes());

    GeckoCode code2 = codeList.get(1);
    assertTrue(code2 instanceof ActiveWrite32BitsCode);
    ActiveWrite32BitsCode writeCode2 = (ActiveWrite32BitsCode) code2;
    assertEquals(0x8000CB28L, writeCode2.getTargetAddress());
    assertArrayEquals(new byte[]{0x60, 0, 0, 0}, writeCode2.getBytes());
    assertArrayEquals(new byte[]{0x22, 0x22, 0x11, 0x11}, writeCode2.getReplacedBytes());

    GeckoCode code3 = codeList.get(2);
    assertTrue(code3 instanceof ActiveWrite32BitsCode);
    ActiveWrite32BitsCode writeCode3 = (ActiveWrite32BitsCode) code3;
    assertEquals(0x8000CB3CL, writeCode3.getTargetAddress());
    assertArrayEquals(new byte[]{0x60, 0, 0, 0}, writeCode3.getBytes());
    assertArrayEquals(new byte[]{0x44, 0x44, 0x33, 0x33}, writeCode3.getReplacedBytes());
  }
}
