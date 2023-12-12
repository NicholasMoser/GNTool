package com.github.nicholasmoser.gnt4.seq;

public class TCG {

  public static String read1(int value) {
    if (value < 0x7fffff00) {
      return String.format("0x%X", value);
    }
    return String.format("s_lpCTD->vars[0x%X]", value * 4 + 0x400);
  }

  public static String read2(int value) {
    if (value < 0x7fffff00) {
      return String.format("0x%X", value);
    }
    return String.format("0x%X", value + -0x7fffff00);
  }

  public static int write1(String value) {
    if (value.startsWith("0x")) {
      return Long.decode(value).intValue();
    }
    String s = value.substring(value.indexOf("[") + 1);
    s = s.substring(0, s.indexOf("]"));
    int number = Long.decode(s).intValue();
    return ((number - 0x400) / 4) - Integer.MIN_VALUE; // thanks java overflow
  }

  public static int write2(String value) {
    return 0;
  }
}
