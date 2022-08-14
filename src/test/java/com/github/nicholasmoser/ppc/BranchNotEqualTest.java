package com.github.nicholasmoser.ppc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.github.nicholasmoser.utils.ByteUtils.*;
import org.junit.jupiter.api.Test;

public class BranchNotEqualTest {


  /**
   * Test branching a distance of 0.
   */
  @Test
  public void testBranchInPlaceDistance() {
    byte[] branch = BranchNotEqual.getBytes(0x00);
    assertThat(hexStringToBytes("40820000")).isEqualTo(branch);
  }

  /**
   * Tests branching a variety of distances forwards.
   */
  @Test
  public void testBranchPositiveDistance() {
    assertThat(hexStringToBytes("40820010")).isEqualTo(BranchNotEqual.getBytes(0x10));
    assertThat(hexStringToBytes("40820100")).isEqualTo(BranchNotEqual.getBytes(0x100));
    assertThat(hexStringToBytes("40820104")).isEqualTo(BranchNotEqual.getBytes(0x104));
    assertThat(hexStringToBytes("40820108")).isEqualTo(BranchNotEqual.getBytes(0x108));
    assertThat(hexStringToBytes("4082010C")).isEqualTo(BranchNotEqual.getBytes(0x10C));
    assertThat(hexStringToBytes("40820110")).isEqualTo(BranchNotEqual.getBytes(0x110));
    assertThat(hexStringToBytes("40820A7C")).isEqualTo(BranchNotEqual.getBytes(0xA7C));
    assertThat(hexStringToBytes("40827000")).isEqualTo(BranchNotEqual.getBytes(0x7000));
  }

  /**
   * Tests branching a variety of distances backwards.
   */
  @Test
  public void testBranchNegativeDistance() {
    assertThat(BranchNotEqual.getBytes(-0x10)).asHexString().isEqualTo("40A2FFF0");
    assertThat(hexStringToBytes("40A2FFF0")).isEqualTo(BranchNotEqual.getBytes(-0x10));
    assertThat(hexStringToBytes("40A2FF00")).isEqualTo(BranchNotEqual.getBytes(-0x100));
    assertThat(hexStringToBytes("40A2FEFC")).isEqualTo(BranchNotEqual.getBytes(-0x104));
    assertThat(hexStringToBytes("40A2FEF8")).isEqualTo(BranchNotEqual.getBytes(-0x108));
    assertThat(hexStringToBytes("40A2FEF4")).isEqualTo(BranchNotEqual.getBytes(-0x10C));
    assertThat(hexStringToBytes("40A2FEF0")).isEqualTo(BranchNotEqual.getBytes(-0x110));
    assertThat(hexStringToBytes("40A2F584")).isEqualTo(BranchNotEqual.getBytes(-0xA7C));
    assertThat(hexStringToBytes("40A29000")).isEqualTo(BranchNotEqual.getBytes(-0x7000));
  }

  /**
   * Tests branching a single instruction of distance backwards.
   */
  @Test
  public void testNegativeFourDistance() {
    assertThat(hexStringToBytes("40A2FFFC")).isEqualTo(BranchNotEqual.getBytes(-4));
  }

  /**
   * Tests branching to the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranchDistance() {
    assertThat(hexStringToBytes("40827FFC")).isEqualTo(BranchNotEqual.getBytes(0x7FFC));
  }

  /**
   * Tests branching to the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranchDistance() {
    assertThat(hexStringToBytes("40A28000")).isEqualTo(BranchNotEqual.getBytes(-0x8000));
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x8000));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x8001));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x10000));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x7FFFFFFF));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x8001));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x8002));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x10000));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x7FFFFFFF));
  }

  /**
   * Tests that branching with a distance that isn't a multiple of 4 throws an
   * IllegalArgumentException.
   */
  @Test
  public void testNonMultipleFourDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x1));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x2));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x3));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x5));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x6));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x7));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x171));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0xABCD));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0xABCDE));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x1));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x2));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x3));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x5));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x6));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x7));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0x171));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0xABCD));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(-0xABCDE));
  }

  /**
   * Test branching in place using addresses.
   */
  @Test
  public void testBranchInPlace() {
    byte[] branchInPlace = new byte[]{0x40, (byte) 0x82, 0x00, 0x00};
    byte[] branch = BranchNotEqual.getBytes(0x80000000L, 0x80000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80000004L, 0x80000004L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80000008L, 0x80000008L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x8000000CL, 0x8000000CL);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80000010L, 0x80000010L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80000100L, 0x80000100L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80000104L, 0x80000104L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80001008L, 0x80001008L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x80100000L, 0x80100000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x81000000L, 0x81000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x81FF0000L, 0x81FF0000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x81FFFF00L, 0x81FFFF00L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x81FFFFFCL, 0x81FFFFFCL);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchNotEqual.getBytes(0x82000000L, 0x82000000L);
    assertArrayEquals(branchInPlace, branch);
  }

  /**
   * Tests branching forward between two addresses.
   */
  @Test
  public void testBranchPositive() {
    assertThat(hexStringToBytes("40820010")).isEqualTo(BranchNotEqual.getBytes(0x80000000L, 0x80000010L));
    assertThat(hexStringToBytes("40820100")).isEqualTo(BranchNotEqual.getBytes(0x80000100L, 0x80000200L));
    assertThat(hexStringToBytes("40820104")).isEqualTo(BranchNotEqual.getBytes(0x80000200L, 0x80000304L));
    assertThat(hexStringToBytes("40820108")).isEqualTo(BranchNotEqual.getBytes(0x80000200L, 0x80000308L));
    assertThat(hexStringToBytes("4082010C")).isEqualTo(BranchNotEqual.getBytes(0x80000200L, 0x8000030CL));
    assertThat(hexStringToBytes("40820110")).isEqualTo(BranchNotEqual.getBytes(0x80000200L, 0x80000310L));
    assertThat(hexStringToBytes("40820A7C")).isEqualTo(BranchNotEqual.getBytes(0x80000600L, 0x8000107CL));
    assertThat(hexStringToBytes("408267CC")).isEqualTo(BranchNotEqual.getBytes(0x80002348L, 0x80008B14L));
  }

  /**
   * Tests branching backwards between two addresses.
   */
  @Test
  public void testBranchNegative() {
    assertThat(hexStringToBytes("40A2FFF0")).isEqualTo(BranchNotEqual.getBytes(0x80000010L, 0x80000000L));
    assertThat(hexStringToBytes("40A2FF00")).isEqualTo(BranchNotEqual.getBytes(0x80000200L, 0x80000100L));
    assertThat(hexStringToBytes("40A2FEFC")).isEqualTo(BranchNotEqual.getBytes(0x80000304L, 0x80000200L));
    assertThat(hexStringToBytes("40A2FEF8")).isEqualTo(BranchNotEqual.getBytes(0x80000308L, 0x80000200L));
    assertThat(hexStringToBytes("40A2FEF4")).isEqualTo(BranchNotEqual.getBytes(0x8000030CL, 0x80000200L));
    assertThat(hexStringToBytes("40A2FEF0")).isEqualTo(BranchNotEqual.getBytes(0x80000310L, 0x80000200L));
    assertThat(hexStringToBytes("40A2F584")).isEqualTo(BranchNotEqual.getBytes(0x8000107CL, 0x80000600L));
    assertThat(hexStringToBytes("40A29834")).isEqualTo(BranchNotEqual.getBytes(0x80008B14L, 0x80002348L));
  }

  /**
   * Tests branching a single instruction backwards.
   */
  @Test
  public void testNegativeFour() {
    assertThat(hexStringToBytes("40A2FFFC")).isEqualTo(BranchNotEqual.getBytes(0x80000004L, 0x80000000));
  }

  /**
   * Tests branching using the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranch() {
    assertThat(hexStringToBytes("40827FFC")).isEqualTo(BranchNotEqual.getBytes(0x80000000L, 0x80007FFCL));
  }

  /**
   * Tests branching using the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranch() {
    assertThat(hexStringToBytes("40A28000")).isEqualTo(BranchNotEqual.getBytes(0x80008000L, 0x80000000L));
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranch() {
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80000000L, 0x80008000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80000000L, 0x81FFFFFDL));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80000000L, 0x82000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80000000L, 0xA0000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80000000L, 0xFFFFFFFFL));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranch() {
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x80008001L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x82000001L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0x83000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0xA0000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchNotEqual.getBytes(0xFFFFFFFFL, 0x80000000L));
  }
}
