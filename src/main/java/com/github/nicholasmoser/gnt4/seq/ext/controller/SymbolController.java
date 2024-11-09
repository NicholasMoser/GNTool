package com.github.nicholasmoser.gnt4.seq.ext.controller;

public interface SymbolController {

  /**
   * Reads and decodes a String into an int.
   *
   * @param number The String to parse.
   * @return The int represented by the String.
   */
  default int readNumber(String number) {
    try {
      return Integer.decode(number);
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Not a valid number: " + number, e);
    }
  }

  /**
   * Verifies that the text with the given field name is only hex or whitespace. Also verifies that
   * the total amount of hex results in 4-byte words. Throws an IllegalStateException when it is
   * not.
   *
   * @param text      The text to verify is hex or whitespace.
   * @param fieldName The name of the field to use in the case of an Exception being thrown.
   */
  default void verifyIsHex(String text, String fieldName) {
    int count = 0;
    for (char c : text.toCharArray()) {
      switch (c) {
        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' ->
            count++;
        case ' ', '\r', '\n', '\t' -> {
        } // do nothing
        default -> throw new IllegalStateException(fieldName + " contain invalid character: " + c);
      }
    }
    if (count % 8 != 0) {
      throw new IllegalStateException(fieldName + " must only have four-byte words");
    }
  }

  /**
   * Read a hex String, ignoring whitespace, and return a byte array. Courtesy of Dave L. via
   * <a href="https://stackoverflow.com/a/140861">Stack Overflow</a>
   *
   * @param s The String to read.
   * @return The byte array.
   */
  default byte[] readHex(String s) {
    // Remove whitespace
    s = s.replace(" ", "");
    s = s.replace("\r", "");
    s = s.replace("\n", "");
    s = s.replace("\t", "");
    // Original code from stack overflow
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}
