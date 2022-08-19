package com.github.nicholasmoser.ppc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.github.nicholasmoser.utils.ByteUtils.*;
import org.junit.jupiter.api.Test;

public class BranchEqualTest {


  /**
   * Test branching a distance of 0.
   */
  @Test
  public void testBranchInPlaceDistance() {
    byte[] branch = BranchEqual.getBytes(0x00);
    assertThat(hexStringToBytes("41820000")).isEqualTo(branch);
  }

  /**
   * Tests branching a variety of distances forwards.
   */
  @Test
  public void testBranchPositiveDistance() {
    assertThat(hexStringToBytes("41820010")).isEqualTo(BranchEqual.getBytes(0x10));
    assertThat(hexStringToBytes("41820100")).isEqualTo(BranchEqual.getBytes(0x100));
    assertThat(hexStringToBytes("41820104")).isEqualTo(BranchEqual.getBytes(0x104));
    assertThat(hexStringToBytes("41820108")).isEqualTo(BranchEqual.getBytes(0x108));
    assertThat(hexStringToBytes("4182010C")).isEqualTo(BranchEqual.getBytes(0x10C));
    assertThat(hexStringToBytes("41820110")).isEqualTo(BranchEqual.getBytes(0x110));
    assertThat(hexStringToBytes("41820A7C")).isEqualTo(BranchEqual.getBytes(0xA7C));
    assertThat(hexStringToBytes("41827000")).isEqualTo(BranchEqual.getBytes(0x7000));
  }

  /**
   * Tests branching a variety of distances backwards.
   */
  @Test
  public void testBranchNegativeDistance() {
    assertThat(hexStringToBytes("4182FFF0")).isEqualTo(BranchEqual.getBytes(-0x10));
    assertThat(hexStringToBytes("4182FF00")).isEqualTo(BranchEqual.getBytes(-0x100));
    assertThat(hexStringToBytes("4182FEFC")).isEqualTo(BranchEqual.getBytes(-0x104));
    assertThat(hexStringToBytes("4182FEF8")).isEqualTo(BranchEqual.getBytes(-0x108));
    assertThat(hexStringToBytes("4182FEF4")).isEqualTo(BranchEqual.getBytes(-0x10C));
    assertThat(hexStringToBytes("4182FEF0")).isEqualTo(BranchEqual.getBytes(-0x110));
    assertThat(hexStringToBytes("4182F584")).isEqualTo(BranchEqual.getBytes(-0xA7C));
    assertThat(hexStringToBytes("41829000")).isEqualTo(BranchEqual.getBytes(-0x7000));
  }

  /**
   * Tests branching a single instruction of distance backwards.
   */
  @Test
  public void testNegativeFourDistance() {
    assertThat(hexStringToBytes("4182FFFC")).isEqualTo(BranchEqual.getBytes(-4));
  }

  /**
   * Tests branching to the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranchDistance() {
    assertThat(hexStringToBytes("41827FFC")).isEqualTo(BranchEqual.getBytes(0x7FFC));
  }

  /**
   * Tests branching to the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranchDistance() {
    assertThat(hexStringToBytes("41828000")).isEqualTo(BranchEqual.getBytes(-0x8000));
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x8000));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x8001));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x10000));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x7FFFFFFF));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranchDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x8001));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x8002));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x10000));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x7FFFFFFF));
  }

  /**
   * Tests that branching with a distance that isn't a multiple of 4 throws an
   * IllegalArgumentException.
   */
  @Test
  public void testNonMultipleFourDistance() {
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x1));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x2));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x3));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x5));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x6));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x7));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x171));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0xABCD));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0xABCDE));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x1));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x2));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x3));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x5));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x6));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x7));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x9));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0x171));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0xABCD));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(-0xABCDE));
  }

  /**
   * Test branching in place using addresses.
   */
  @Test
  public void testBranchInPlace() {
    byte[] branchInPlace = new byte[]{0x41, (byte) 0x82, 0x00, 0x00};
    byte[] branch = BranchEqual.getBytes(0x80000000L, 0x80000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80000004L, 0x80000004L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80000008L, 0x80000008L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x8000000CL, 0x8000000CL);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80000010L, 0x80000010L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80000100L, 0x80000100L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80000104L, 0x80000104L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80001008L, 0x80001008L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x80100000L, 0x80100000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x81000000L, 0x81000000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x81FF0000L, 0x81FF0000L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x81FFFF00L, 0x81FFFF00L);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x81FFFFFCL, 0x81FFFFFCL);
    assertArrayEquals(branchInPlace, branch);
    branch = BranchEqual.getBytes(0x82000000L, 0x82000000L);
    assertArrayEquals(branchInPlace, branch);
  }

  /**
   * Tests branching forward between two addresses.
   */
  @Test
  public void testBranchPositive() {
    assertThat(hexStringToBytes("41820010")).isEqualTo(BranchEqual.getBytes(0x80000000L, 0x80000010L));
    assertThat(hexStringToBytes("41820100")).isEqualTo(BranchEqual.getBytes(0x80000100L, 0x80000200L));
    assertThat(hexStringToBytes("41820104")).isEqualTo(BranchEqual.getBytes(0x80000200L, 0x80000304L));
    assertThat(hexStringToBytes("41820108")).isEqualTo(BranchEqual.getBytes(0x80000200L, 0x80000308L));
    assertThat(hexStringToBytes("4182010C")).isEqualTo(BranchEqual.getBytes(0x80000200L, 0x8000030CL));
    assertThat(hexStringToBytes("41820110")).isEqualTo(BranchEqual.getBytes(0x80000200L, 0x80000310L));
    assertThat(hexStringToBytes("41820A7C")).isEqualTo(BranchEqual.getBytes(0x80000600L, 0x8000107CL));
    assertThat(hexStringToBytes("418267CC")).isEqualTo(BranchEqual.getBytes(0x80002348L, 0x80008B14L));
  }

  /**
   * Tests branching backwards between two addresses.
   */
  @Test
  public void testBranchNegative() {
    assertThat(hexStringToBytes("4182FFF0")).isEqualTo(BranchEqual.getBytes(0x80000010L, 0x80000000L));
    assertThat(hexStringToBytes("4182FF00")).isEqualTo(BranchEqual.getBytes(0x80000200L, 0x80000100L));
    assertThat(hexStringToBytes("4182FEFC")).isEqualTo(BranchEqual.getBytes(0x80000304L, 0x80000200L));
    assertThat(hexStringToBytes("4182FEF8")).isEqualTo(BranchEqual.getBytes(0x80000308L, 0x80000200L));
    assertThat(hexStringToBytes("4182FEF4")).isEqualTo(BranchEqual.getBytes(0x8000030CL, 0x80000200L));
    assertThat(hexStringToBytes("4182FEF0")).isEqualTo(BranchEqual.getBytes(0x80000310L, 0x80000200L));
    assertThat(hexStringToBytes("4182F584")).isEqualTo(BranchEqual.getBytes(0x8000107CL, 0x80000600L));
    assertThat(hexStringToBytes("41829834")).isEqualTo(BranchEqual.getBytes(0x80008B14L, 0x80002348L));
  }

  /**
   * Tests branching a single instruction backwards.
   */
  @Test
  public void testNegativeFour() {
    assertThat(hexStringToBytes("4182FFFC")).isEqualTo(BranchEqual.getBytes(0x80000004L, 0x80000000));
  }

  /**
   * Tests branching using the furthest possible distance forward.
   */
  @Test
  public void testFurthestPositiveBranch() {
    assertThat(hexStringToBytes("41827FFC")).isEqualTo(BranchEqual.getBytes(0x80000000L, 0x80007FFCL));
  }

  /**
   * Tests branching using the furthest possible distance backwards.
   */
  @Test
  public void testFurthestNegativeBranch() {
    assertThat(hexStringToBytes("41828000")).isEqualTo(BranchEqual.getBytes(0x80008000L, 0x80000000L));
  }

  /**
   * Tests that branching a distance over 0x1FFFFFC throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargePositiveBranch() {
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80000000L, 0x80008000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80000000L, 0x81FFFFFDL));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80000000L, 0x82000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80000000L, 0xA0000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80000000L, 0xFFFFFFFFL));
  }

  /**
   * Tests that branching a distance under -0x2000000 throws an IllegalArgumentException.
   */
  @Test
  public void testErrorTooLargeNegativeBranch() {
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x80008001L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x82000001L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0x83000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0xA0000000L, 0x80000000L));
    assertThrows(IllegalArgumentException.class, () -> BranchEqual.getBytes(0xFFFFFFFFL, 0x80000000L));
  }
}
