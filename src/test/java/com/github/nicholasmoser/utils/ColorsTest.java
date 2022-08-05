package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ColorsTest {
  @Test
  public void testColors() {
    testColor(0xFFFFFFFF, "White; Solid");
    testColor(0xFF0000FF, "Red; Solid");
    testColor(0x00FF00FF, "Green; Solid");
    testColor(0x0000FFFF, "Blue; Solid");
    testColor(0xFFFF00FF, "Yellow; Solid");
    testColor(0xFF00FFFF, "Magenta; Solid");
    testColor(0x00FFFFFF, "Cyan; Solid");
    testColor(0x000000FF, "Black; Solid");
    testColor(0xFFFFFFFF, "White; Solid");
    testColor(0x123456FF, "#123456; Solid");
    testColor(0x01FFBAFF, "#01FFBA; Solid");
    testColor(0xFFFFFF00, "White; Alpha:0x00");
    testColor(0xFF000001, "Red; Alpha:0x01");
    testColor(0x00FF0011, "Green; Alpha:0x11");
    testColor(0x0000FF22, "Blue; Alpha:0x22");
    testColor(0xFFFF0033, "Yellow; Alpha:0x33");
    testColor(0xFF00FF44, "Magenta; Alpha:0x44");
    testColor(0x00FFFF7F, "Cyan; Alpha:0x7F");
    testColor(0x00000080, "Black; Alpha:0x80");
    testColor(0xFFFFFF81, "White; Alpha:0x81");
    testColor(0x123456FE, "#123456; Alpha:0xFE");
    testColor(0x01FFBA13, "#01FFBA; Alpha:0x13");
  }

  private void testColor(int hex, String color) {
    assertEquals(color, Colors.hexToColor(hex));
    assertEquals(hex, Colors.colorToHex(color));
  }
}
