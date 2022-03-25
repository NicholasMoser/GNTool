package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RangesTest {
  @Test
  void testRanges() {
    // Test cases of overlap
    assertTrue(Ranges.haveOverlap(0, 1, 0, 1));
    assertTrue(Ranges.haveOverlap(1, 2, 1, 2));
    assertTrue(Ranges.haveOverlap(0, 10, 5, 6));
    assertTrue(Ranges.haveOverlap(0, 10, 1, 9));
    assertTrue(Ranges.haveOverlap(0, 10, 0, 10));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 5555, 7777));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 5555, 10_000));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 5555, 10_001));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 5555, Integer.MAX_VALUE));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 1337, 7777));
    assertTrue(Ranges.haveOverlap(1337, 10_000, 1336, 7777));
    assertTrue(Ranges.haveOverlap(-5, 5, 0, 1));
    assertTrue(Ranges.haveOverlap(-0, 1, -5, 5));
    assertTrue(Ranges.haveOverlap(-10, -5, -10, -5));
    assertTrue(Ranges.haveOverlap(-10, -5, -10, -9));
    assertTrue(Ranges.haveOverlap(-10, -9, -10, -5));
    assertTrue(Ranges.haveOverlap(1337, 10_000, Integer.MIN_VALUE, 7777));
    assertTrue(Ranges.haveOverlap(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE));

    // Test cases of non-overlap
    assertFalse(Ranges.haveOverlap(0, 4, 4, 8));
    assertFalse(Ranges.haveOverlap(4, 8, 0, 4));
    assertFalse(Ranges.haveOverlap(0, 32, 32, 64));
    assertFalse(Ranges.haveOverlap(32, 64, 0, 32));
    assertFalse(Ranges.haveOverlap(-5, 0, 0, 5));
    assertFalse(Ranges.haveOverlap(0, 5, -5, 0));
    assertFalse(Ranges.haveOverlap(Integer.MIN_VALUE, 0, 0, Integer.MAX_VALUE));
    assertFalse(Ranges.haveOverlap(0, Integer.MAX_VALUE, Integer.MIN_VALUE, 0));

    // Known failure case for PR-84
    assertFalse(Ranges.haveOverlap(125220, 125228, 123592, 123600));
    assertFalse(Ranges.haveOverlap(123592, 123600, 125220, 125228));
  }
}
