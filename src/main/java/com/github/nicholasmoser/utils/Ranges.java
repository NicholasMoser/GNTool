package com.github.nicholasmoser.utils;

public class Ranges {

  /**
   * Returns whether the two given ranges overlap. The start of each range is inclusive and the
   * end if exclusive. Therefore, [0, 4) and [4, 8) do not overlap, and each are of length 4.
   *
   * @param startA The start of the first range, inclusive.
   * @param endA The end of the first range, exclusive.
   * @param startB The start of the second range, inclusive.
   * @param endB The end of the second range, exclusive.
   * @return If the two ranges overlap.
   */
  public static boolean haveOverlap(int startA, int endA, int startB, int endB) {
    if (startA == endA) {
      throw new IllegalArgumentException(String.format("Cannot be equal %d and %d", startA, endA));
    } else if (startB == endB) {
      throw new IllegalArgumentException(String.format("Cannot be equal %d and %d", startB, endB));
    }
    return Math.max(startA, startB) <= Math.min(endA - 1, endB - 1);
  }
}
