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
}
