package com.github.nicholasmoser.ppc;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BranchTest {

  /**
   * Test branching a distance of 0.
   */
  @Test
  public void testBranchInPlaceDistance() {
    byte[] branch = Branch.getBranchInstruction(0x00);
    assertArrayEquals(new byte[]{0x48, 0x00, 0x00, 0x00}, branch);
  }

  /**
   * Tests branching a variety of distances forwards.
   */
  @Test
  public void testBranchPositiveDistance() {
    byte[] branch = Branch.getBranchInstruction(0x10);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x00, (byte) 0x10}, branch);
    branch = Branch.getBranchInstruction(0x100);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x00}, branch);
    branch = Branch.getBranchInstruction(0x104);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x04}, branch);
    branch = Branch.getBranchInstruction(0x108);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x08}, branch);
    branch = Branch.getBranchInstruction(0x10C);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x0C}, branch);
    branch = Branch.getBranchInstruction(0x110);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x10}, branch);
    branch = Branch.getBranchInstruction(0xA7C);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x0A, (byte) 0x7C}, branch);
    branch = Branch.getBranchInstruction(0xA7CC);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0xA7, (byte) 0xCC}, branch);
    branch = Branch.getBranchInstruction(0x77CC0);
    assertArrayEquals(new byte[]{0x48, (byte) 0x07, (byte) 0x7C, (byte) 0xC0}, branch);
    branch = Branch.getBranchInstruction(0x77CC04);
    assertArrayEquals(new byte[]{0x48, (byte) 0x77, (byte) 0xCC, (byte) 0x04}, branch);
    branch = Branch.getBranchInstruction(0x1CCCCCC);
    assertArrayEquals(new byte[]{0x49, (byte) 0xCC, (byte) 0xCC, (byte) 0xCC}, branch);
    branch = Branch.getBranchInstruction(0x1FFFFF8);
    assertArrayEquals(new byte[]{0x49, (byte) 0xFF, (byte) 0xFF, (byte) 0xF8}, branch);
  }

  /**
   * Tests branching a variety of distances backwards.
   */
  @Test
  public void testBranchNegativeDistance() {
    byte[] branch = Branch.getBranchInstruction(-0x10);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xF0}, branch);
    branch = Branch.getBranchInstruction(-0x100);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0x00}, branch);
    branch = Branch.getBranchInstruction(-0x104);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xFC}, branch);
    branch = Branch.getBranchInstruction(-0x108);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF8}, branch);
    branch = Branch.getBranchInstruction(-0x10C);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF4}, branch);
    branch = Branch.getBranchInstruction(-0x110);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF0}, branch);
    branch = Branch.getBranchInstruction(-0xA7C);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xF5, (byte) 0x84}, branch);
    branch = Branch.getBranchInstruction(-0xA7CC);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0x58, (byte) 0x34}, branch);
    branch = Branch.getBranchInstruction(-0x77CC0);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xF8, (byte) 0x83, (byte) 0x40}, branch);
    branch = Branch.getBranchInstruction(-0x77CC04);
    assertArrayEquals(new byte[]{0x4B, (byte) 0x88, (byte) 0x33, (byte) 0xFC}, branch);
    branch = Branch.getBranchInstruction(-0x1CCCCCC);
    assertArrayEquals(new byte[]{0x4A, (byte) 0x33, (byte) 0x33, (byte) 0x34}, branch);
    branch = Branch.getBranchInstruction(-0x1FFFFFC);
    assertArrayEquals(new byte[]{0x4A, (byte) 0x00, (byte) 0x00, (byte) 0x04}, branch);
  }

  /**
   * Tests branching a single instruction of distance backwards.
   */
  @Test
  public void testNegativeFourDistance() {
    byte[] branch = Branch.getBranchInstruction(-4);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC}, branch);
  }

  /**
   * Tests branching to the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranchDistance() {
    byte[] branch = Branch.getBranchInstruction(0x1FFFFFC);
    assertArrayEquals(new byte[]{0x49, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC}, branch);
  }

  /**
   * Tests branching to the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranchDistance() {
    byte[] branch = Branch.getBranchInstruction(-0x2000000);
    assertArrayEquals(new byte[]{0x4A, 0x00, 0x00, 0x00}, branch);
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x1FFFFFD));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x2000000));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x20000000));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x7FFFFFFF));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x2000001));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x3000000));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x20000000));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x7FFFFFFF));
  }

  /**
   * Tests that branching with a distance that isn't a multiple of 4 throws an
   * IllegalArgumentException.
   */
  @Test
  public void testNonMultipleFourDistance() {
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x1));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x2));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x3));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x5));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x6));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x7));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x9));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x171));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0xABCD));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0xABCDE));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x9));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x1));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x2));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x3));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x5));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x6));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x7));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x9));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0x171));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0xABCD));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(-0xABCDE));
  }

  /**
   * Test branching in place using addresses.
   */
  @Test
  public void testBranchInPlace() {
    byte[] branchInPlace = new byte[]{0x48, 0x00, 0x00, 0x00};
    byte[] branch = Branch.getBranchInstruction(0x80000000L, 0x80000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80000004L, 0x80000004L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80000008L, 0x80000008L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x8000000CL, 0x8000000CL);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80000010L, 0x80000010L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80000100L, 0x80000100L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80000104L, 0x80000104L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80001008L, 0x80001008L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x80100000L, 0x80100000L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x81000000L, 0x81000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x81FF0000L, 0x81FF0000L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x81FFFF00L, 0x81FFFF00L);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x81FFFFFCL, 0x81FFFFFCL);
    assertArrayEquals(branchInPlace, branch);
    branch = Branch.getBranchInstruction(0x82000000L, 0x82000000L);
    assertArrayEquals(branchInPlace, branch);
  }

  /**
   * Tests branching forward between two addresses.
   */
  @Test
  public void testBranchPositive() {
    byte[] branch = Branch.getBranchInstruction(0x80000000L, 0x80000010L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x00, (byte) 0x10}, branch);
    branch = Branch.getBranchInstruction(0x80000100L, 0x80000200L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x00}, branch);
    branch = Branch.getBranchInstruction(0x80000200L, 0x80000304L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x04}, branch);
    branch = Branch.getBranchInstruction(0x80000200L, 0x80000308L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x08}, branch);
    branch = Branch.getBranchInstruction(0x80000200L, 0x8000030CL);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x0C}, branch);
    branch = Branch.getBranchInstruction(0x80000200L, 0x80000310L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x01, (byte) 0x10}, branch);
    branch = Branch.getBranchInstruction(0x80000600L, 0x8000107CL);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0x0A, (byte) 0x7C}, branch);
    branch = Branch.getBranchInstruction(0x80002348L, 0x8000CB14L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0xA7, (byte) 0xCC}, branch);
    branch = Branch.getBranchInstruction(0x80010000L, 0x8001A7CCL);
    assertArrayEquals(new byte[]{0x48, (byte) 0x00, (byte) 0xA7, (byte) 0xCC}, branch);
    branch = Branch.getBranchInstruction(0x80100000L, 0x80177CC0L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x07, (byte) 0x7C, (byte) 0xC0}, branch);
    branch = Branch.getBranchInstruction(0x80000000L, 0x8077CC04L);
    assertArrayEquals(new byte[]{0x48, (byte) 0x77, (byte) 0xCC, (byte) 0x04}, branch);
    branch = Branch.getBranchInstruction(0x80000000L, 0x81CCCCCCL);
    assertArrayEquals(new byte[]{0x49, (byte) 0xCC, (byte) 0xCC, (byte) 0xCC}, branch);
    branch = Branch.getBranchInstruction(0x90000000L, 0x91FFFFF8L);
    assertArrayEquals(new byte[]{0x49, (byte) 0xFF, (byte) 0xFF, (byte) 0xF8}, branch);
  }

  /**
   * Tests branching backwards between two addresses.
   */
  @Test
  public void testBranchNegative() {
    byte[] branch = Branch.getBranchInstruction(0x80000010L, 0x80000000L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xF0}, branch);
    branch = Branch.getBranchInstruction(0x80000200L, 0x80000100L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0x00}, branch);
    branch = Branch.getBranchInstruction(0x80000304L, 0x80000200L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xFC}, branch);
    branch = Branch.getBranchInstruction(0x80000308L, 0x80000200L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF8}, branch);
    branch = Branch.getBranchInstruction(0x8000030CL, 0x80000200L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF4}, branch);
    branch = Branch.getBranchInstruction(0x80000310L, 0x80000200L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFE, (byte) 0xF0}, branch);
    branch = Branch.getBranchInstruction(0x8000107CL, 0x80000600L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xF5, (byte) 0x84}, branch);
    branch = Branch.getBranchInstruction(0x8000CB14L, 0x80002348L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0x58, (byte) 0x34}, branch);
    branch = Branch.getBranchInstruction(0x80177CC0L, 0x80100000L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xF8, (byte) 0x83, (byte) 0x40}, branch);
    branch = Branch.getBranchInstruction(0x8077CC04L, 0x80000000L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0x88, (byte) 0x33, (byte) 0xFC}, branch);
    branch = Branch.getBranchInstruction(0x81CCCCCCL, 0x80000000L);
    assertArrayEquals(new byte[]{0x4A, (byte) 0x33, (byte) 0x33, (byte) 0x34}, branch);
    branch = Branch.getBranchInstruction(0x81FFFFFCL, 0x80000000L);
    assertArrayEquals(new byte[]{0x4A, (byte) 0x00, (byte) 0x00, (byte) 0x04}, branch);
  }

  /**
   * Test branching from the beginning of GNT4's init section to the end of the text section.
   */
  @Test
  public void testBranchAcrossGNT4Code() {
    byte[] branch = Branch.getBranchInstruction(0x80003100L, 0x801FD7FCL);
    assertArrayEquals(new byte[]{0x48, (byte) 0x1F, (byte) 0xA6, (byte) 0xFC}, branch);
  }

  /**
   * Test branching from the end of GNT4's text section to the beginning of the init section.
   */
  @Test
  public void testBranchAcrossGNT4CodeBackwards() {
    byte[] branch = Branch.getBranchInstruction(0x801FD7FCL, 0x80003100L);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xE0, (byte) 0x59, (byte) 0x04}, branch);
  }

  /**
   * Tests branching a single instruction backwards.
   */
  @Test
  public void testNegativeFour() {
    byte[] branch = Branch.getBranchInstruction(0x80000004L, 0x80000000);
    assertArrayEquals(new byte[]{0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC}, branch);
  }

  /**
   * Tests branching using the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranch() {
    byte[] branch = Branch.getBranchInstruction(0x80000000L, 0x81FFFFFCL);
    assertArrayEquals(new byte[]{0x49, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC}, branch);
  }

  /**
   * Tests branching using the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranch() {
    byte[] branch = Branch.getBranchInstruction(0x82000000L, 0x80000000L);
    assertArrayEquals(new byte[]{0x4A, 0x00, 0x00, 0x00}, branch);
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranch() {
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x80000000L, 0x81FFFFFDL));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x80000000L, 0x82000000L));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x80000000L, 0xA0000000L));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x80000000L, 0xFFFFFFFFL));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranch() {
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x82000001L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0x83000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0xA0000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> Branch.getBranchInstruction(0xFFFFFFFFL, 0x80000000L));
  }
}
