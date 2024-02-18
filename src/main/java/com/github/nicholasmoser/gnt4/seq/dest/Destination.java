package com.github.nicholasmoser.gnt4.seq.dest;

/**
 * An arbitrary location that may require being resolved before it is accurate.
 */
public interface Destination {

  /**
   * @return The destination offset or -1 if it has not yet been resolved
   */
  int offset();

  /**
   * @return The destination offset bytes or -1 if it has not yet been resolved.
   */
  byte[] bytes();
}
