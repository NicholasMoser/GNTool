package com.github.nicholasmoser.dol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.dol.DolUtil.Section;
import org.junit.jupiter.api.Test;

public class DolUtilTest {

  /**
   * Test that the init section is correctly identified and returned for addresses in the init
   * section.
   */
  @Test
  public void testGetInitSection() {
    assertEquals(Section.INIT, DolUtil.getSection(0x80003100L));
    assertEquals(Section.INIT, DolUtil.getSection(0x80003140L));
    assertEquals(Section.INIT, DolUtil.getSection(0x800040D8L));
    assertEquals(Section.INIT, DolUtil.getSection(0x80004B94L));
    assertEquals(Section.INIT, DolUtil.getSection(0x800056BCL));
  }

  /**
   * Test that the text section is correctly identified and returned for addresses in the text
   * section.
   */
  @Test
  public void testGetTextSection() {
    assertEquals(Section.TEXT, DolUtil.getSection(0x800056C0L));
    assertEquals(Section.TEXT, DolUtil.getSection(0x80009718L));
    assertEquals(Section.TEXT, DolUtil.getSection(0x801CC2E8L));
    assertEquals(Section.TEXT, DolUtil.getSection(0x801CCE8CL));
    assertEquals(Section.TEXT, DolUtil.getSection(0x801FD7FCL));
  }

  /**
   * Test that the ctors section is correctly identified and returned for addresses in the ctors
   * section.
   */
  @Test
  public void testGetCtorsSection() {
    assertEquals(Section.CTORS, DolUtil.getSection(0x801FD800L));
    assertEquals(Section.CTORS, DolUtil.getSection(0x801FD810L));
  }

  /**
   * Test that the dtors section is correctly identified and returned for addresses in the dtors
   * section.
   */
  @Test
  public void testGetDtorsSection() {
    assertEquals(Section.DTORS, DolUtil.getSection(0x801FD820L));
    assertEquals(Section.DTORS, DolUtil.getSection(0x801FD830L));
  }

  /**
   * Test that the rodata section is correctly identified and returned for addresses in the rodata
   * section.
   */
  @Test
  public void testGetRodataSection() {
    assertEquals(Section.RODATA, DolUtil.getSection(0x801FD840L));
    assertEquals(Section.RODATA, DolUtil.getSection(0x80201DD8L));
    assertEquals(Section.RODATA, DolUtil.getSection(0x802057E8L));
    assertEquals(Section.RODATA, DolUtil.getSection(0x80205B38L));
  }

  /**
   * Test that the data section is correctly identified and returned for addresses in the data
   * section.
   */
  @Test
  public void testGetDataSection() {
    assertEquals(Section.DATA, DolUtil.getSection(0x80205C40L));
    assertEquals(Section.DATA, DolUtil.getSection(0x8020C368L));
    assertEquals(Section.DATA, DolUtil.getSection(0x80219594L));
    assertEquals(Section.DATA, DolUtil.getSection(0x802229C0L));
  }

  /**
   * Test that the bss section is correctly identified and returned for addresses in the bss
   * section.
   */
  @Test
  public void testGetBssSection() {
    assertEquals(Section.BSS, DolUtil.getSection(0x802229E0L));
    assertEquals(Section.BSS, DolUtil.getSection(0x8024361CL));
    assertEquals(Section.BSS, DolUtil.getSection(0x8024CD28L));
    assertEquals(Section.BSS, DolUtil.getSection(0x80274920L));
  }

  /**
   * Test that the sdata section is correctly identified and returned for addresses in the sdata
   * section.
   */
  @Test
  public void testGetSdataSection() {
    assertEquals(Section.SDATA, DolUtil.getSection(0x80276920L));
    assertEquals(Section.SDATA, DolUtil.getSection(0x80276B40L));
    assertEquals(Section.SDATA, DolUtil.getSection(0x80276E48L));
    assertEquals(Section.SDATA, DolUtil.getSection(0x80276FD0L));
  }

  /**
   * Test that the sbss section is correctly identified and returned for addresses in the sbss
   * section.
   */
  @Test
  public void testGetSbssSection() {
    assertEquals(Section.SBSS, DolUtil.getSection(0x80276FE0L));
    assertEquals(Section.SBSS, DolUtil.getSection(0x802775C0L));
    assertEquals(Section.SBSS, DolUtil.getSection(0x802779B8L));
    assertEquals(Section.SBSS, DolUtil.getSection(0x80277C94L));
  }

  /**
   * Test that the sdata2 section is correctly identified and returned for addresses in the sdata2
   * section.
   */
  @Test
  public void testGetSdata2Section() {
    assertEquals(Section.SDATA2, DolUtil.getSection(0x80277CA0L));
    assertEquals(Section.SDATA2, DolUtil.getSection(0x8027B670L));
    assertEquals(Section.SDATA2, DolUtil.getSection(0x8027C2A8L));
    assertEquals(Section.SDATA2, DolUtil.getSection(0x8027C558L));
  }

  /**
   * Test that the sbss2 section is correctly identified and returned for addresses in the sbss2
   * section.
   */
  @Test
  public void testGetSbss2Section() {
    assertEquals(Section.SBSS2, DolUtil.getSection(0x8027C560L));
    assertEquals(Section.SBSS2, DolUtil.getSection(0x8027C568L));
    assertEquals(Section.SBSS2, DolUtil.getSection(0x8027C570L));
    assertEquals(Section.SBSS2, DolUtil.getSection(0x8027C574L));
  }

  /**
   * Test that the attempting to get a section at an address before the dol starting address throws
   * an IllegalArgumentException.
   */
  @Test
  public void testGetSectionBeforeDol() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x800030FFL));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x80003000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x80003000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x5));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x1));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x0));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(-1));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(-5));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Integer.MIN_VALUE));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Long.MIN_VALUE));
  }

  /**
   * Test that the attempting to get a section at an address after the dol ending address throws an
   * IllegalArgumentException.
   */
  @Test
  public void testGetSectionAfterDol() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x8027C578L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x8027D000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x90000000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0xFFFFFFFFL));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x100000000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Integer.MAX_VALUE));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Long.MAX_VALUE));
  }

  /**
   * Test that converting a ram address to a dol offset in the init section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolInitSection() {
    assertEquals(0x100, DolUtil.ram2dol(0x80003100L));
    assertEquals(0x140, DolUtil.ram2dol(0x80003140L));
    assertEquals(0x10D8, DolUtil.ram2dol(0x800040D8L));
    assertEquals(0x1B94, DolUtil.ram2dol(0x80004B94L));
    assertEquals(0x26BC, DolUtil.ram2dol(0x800056BCL));
  }

  /**
   * Test that converting a ram address to a dol offset in the text section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolTextSection() {
    assertEquals(0x26C0, DolUtil.ram2dol(0x800056C0L));
    assertEquals(0x6718, DolUtil.ram2dol(0x80009718L));
    assertEquals(0x1C92E8, DolUtil.ram2dol(0x801CC2E8L));
    assertEquals(0x1C9E8C, DolUtil.ram2dol(0x801CCE8CL));
    assertEquals(0x1FA7FC, DolUtil.ram2dol(0x801FD7FCL));
  }

  /**
   * Test that converting a ram address to a dol offset in the ctors section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolCtorsSection() {
    assertEquals(0x1FA800, DolUtil.ram2dol(0x801FD800L));
    assertEquals(0x1FA810, DolUtil.ram2dol(0x801FD810L));
  }

  /**
   * Test that converting a ram address to a dol offset in the dtors section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolDtorsSection() {
    assertEquals(0x1FA820, DolUtil.ram2dol(0x801FD820L));
    assertEquals(0x1FA830, DolUtil.ram2dol(0x801FD830L));
  }

  /**
   * Test that converting a ram address to a dol offset in the rodata section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolRodataSection() {
    assertEquals(0x1FA840, DolUtil.ram2dol(0x801FD840L));
    assertEquals(0x1FEDD8, DolUtil.ram2dol(0x80201DD8L));
    assertEquals(0x2027E8, DolUtil.ram2dol(0x802057E8L));
    assertEquals(0x202B38, DolUtil.ram2dol(0x80205B38L));
  }

  /**
   * Test that converting a ram address to a dol offset in the data section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolDataSection() {
    assertEquals(0x202C40, DolUtil.ram2dol(0x80205C40L));
    assertEquals(0x209368, DolUtil.ram2dol(0x8020C368L));
    assertEquals(0x216594, DolUtil.ram2dol(0x80219594L));
    assertEquals(0x21F9C0, DolUtil.ram2dol(0x802229C0L));
  }

  /**
   * Test that converting a ram address to a dol offset in the bss section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolBssSection() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x802229E0L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8024361CL));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8024CD28L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x80274920L));
  }

  /**
   * Test that converting a ram address to a dol offset in the sdata section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolSdataSection() {
    assertEquals(0x21F9E0, DolUtil.ram2dol(0x80276920L));
    assertEquals(0x21FC00, DolUtil.ram2dol(0x80276B40L));
    assertEquals(0x21FF08, DolUtil.ram2dol(0x80276E48L));
    assertEquals(0x220090, DolUtil.ram2dol(0x80276FD0L));
  }

  /**
   * Test that converting a ram address to a dol offset in the sbss section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolSbssSection() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x80276FE0L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x802775C0L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x802779B8L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x80277C94L));
  }

  /**
   * Test that converting a ram address to a dol offset in the sdata2 section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolSdata2Section() {
    assertEquals(0x2200A0, DolUtil.ram2dol(0x80277CA0L));
    assertEquals(0x223A70, DolUtil.ram2dol(0x8027B670L));
    assertEquals(0x2246A8, DolUtil.ram2dol(0x8027C2A8L));
    assertEquals(0x224958, DolUtil.ram2dol(0x8027C558L));
  }

  /**
   * Test that converting a ram address to a dol offset in the sbss2 section is correctly
   * calculated.
   */
  @Test
  public void testRam2DolSbss2Section() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8027C560L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8027C568L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8027C570L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.ram2dol(0x8027C574L));
  }

  /**
   * Test that converting a ram address to a dol offset before the start address in the dol throws
   * an IllegalArgumentException.
   */
  @Test
  public void testRam2DolSectionBeforeDol() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x800030FFL));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x80003000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x80003000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x5));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x1));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x0));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(-1));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(-5));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Integer.MIN_VALUE));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Long.MIN_VALUE));
  }

  /**
   * Test that converting a ram address to a dol offset after the end address in the dol throws an
   * IllegalArgumentException.
   */
  @Test
  public void testRam2DolSectionAfterDol() {
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x8027C578L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x8027D000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x90000000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0xFFFFFFFFL));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(0x100000000L));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Integer.MAX_VALUE));
    assertThrows(IllegalArgumentException.class, () -> DolUtil.getSection(Long.MAX_VALUE));
  }
}
