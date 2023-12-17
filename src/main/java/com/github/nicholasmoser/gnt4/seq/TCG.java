package com.github.nicholasmoser.gnt4.seq;

/**
 * Class for reading and writing values and pointers in TCG related SEQ opcodes.
 */
public class TCG {

  public static String readValue(int value) {
    if (value < 0x7fffff00) {
      return String.format("0x%X", value);
    }
    return String.format("s_lpCTD->vars[0x%X]", (value * 4 + 0x400) / 4);
  }

  public static String readPointer(int value) {
    if (value < 0x7fffff00) {
      return String.format("s_lpCTD->vars[0x%X]", value);
    }
    return String.format("s_lpCTD->vars[0x%X]", value + -0x7fffff00);
  }

  public static int writeValue(String value) {
    if (value.startsWith("0x")) {
      return Long.decode(value).intValue();
    } if (!value.startsWith("s_lpCTD->vars[")) {
      throw new IllegalArgumentException("Missing s_lpCTD->vars[...] or 0x...");
    }
    String s = value.substring(value.indexOf("[") + 1);
    s = s.substring(0, s.indexOf("]"));
    int number = Long.decode(s).intValue();
    return 0x7fffff00 | number;
  }

  public static int writePointer(String value) {
    if (!value.startsWith("s_lpCTD->vars[")) {
      throw new IllegalArgumentException("Missing s_lpCTD->vars[...]");
    }
    String s = value.substring(value.indexOf("[") + 1);
    s = s.substring(0, s.indexOf("]"));
    int number = Long.decode(s).intValue();
    // This is currently an assumption, because 0x7FFFFFFF and 0xFF reference the same variable.
    // In most (all?) cases it will be 0x7FFFFFFF instead of 0xFF.
    return 0x7fffff00 | number;
  }
}
