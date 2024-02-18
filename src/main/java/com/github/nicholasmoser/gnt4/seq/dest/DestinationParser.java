package com.github.nicholasmoser.gnt4.seq.dest;

public class DestinationParser {

  /**
   * Parse a destination string and return a destination type.
   *
   * @param destination The destination string to parse.
   * @return The destination.
   */
  public static Destination get(String destination) {
    if (destination.startsWith("+") || destination.startsWith("-")) {
      Long longValue = Long.decode(destination);
      if (longValue > Integer.MAX_VALUE) {
        throw new IllegalStateException("Cannot have absolute relative offset over 0x7FFFFFFF: " + destination);
      } else if (longValue < Integer.MIN_VALUE) {
        throw new IllegalStateException("Cannot have absolute relative offset under -0x80000000: " + destination);
      }
      return new RelativeDestination(longValue.intValue());
    }
    try {
      int offset = Long.decode(destination).intValue();
      return new AbsoluteDestination(offset);
    } catch (Exception e) {
      return new LabelDestination(destination);
    }
  }
}
