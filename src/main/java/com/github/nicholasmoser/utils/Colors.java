package com.github.nicholasmoser.utils;

import java.nio.ByteBuffer;

public class Colors {

  /**
   * Converts a four byte RGBA word to color and transparency such as "Black; Solid". RGBA in this
   * case stands for Red, Green, Blue, Alpha.
   *
   * @param word The RGBA word.
   * @return The output color.
   */
  public static String hexToColor(int word) {
    byte[] bytes = ByteUtils.fromInt32(word);
    int r = Byte.toUnsignedInt(bytes[0]);
    int g = Byte.toUnsignedInt(bytes[1]);
    int b = Byte.toUnsignedInt(bytes[2]);
    int a = Byte.toUnsignedInt(bytes[3]);

    StringBuilder color = new StringBuilder();
    if (r == 0x00 && g == 0x00 && b == 0x00) {
      color.append("Black; ");
    } else if (r == 0xFF && g == 0x00 && b == 0x00) {
      color.append("Red; ");
    } else if (r == 0x00 && g == 0xFF && b == 0x00) {
      color.append("Green; ");
    } else if (r == 0x00 && g == 0x00 && b == 0xFF) {
      color.append("Blue; ");
    } else if (r == 0xFF && g == 0xFF && b == 0x00) {
      color.append("Yellow; ");
    } else if (r == 0xFF && g == 0x00 && b == 0xFF) {
      color.append("Magenta; ");
    } else if (r == 0x00 && g == 0xFF && b == 0xFF) {
      color.append("Cyan; ");
    } else if (r == 0xFF && g == 0xFF && b == 0xFF) {
      color.append("White; ");
    } else {
      color.append(String.format("#%02X%02X%02X; ", r, g, b));
    }
    if (a == 0xFF) {
      color.append("Solid");
    } else {
      color.append(String.format("Alpha:0x%02X", a));
    }
    return color.toString();
  }

  /**
   * Converts a color and transparency such as "Black; Solid" to a four byte RGBA word. RGBA in this
   * case stands for Red, Green, Blue, Alpha.
   *
   * @param color The color.
   * @return The output RGBA word.
   */
  public static int colorToHex(String color) {
    String[] parts = color.split("; ");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Color must separate rgb and a by \"; \": " + color);
    }
    ByteBuffer bb = ByteBuffer.allocate(4);
    String rgb = parts[0];
    switch (rgb) {
      case "White" -> bb.put(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
      case "Red" -> bb.put(new byte[]{(byte) 0xFF, 0x00, 0x00});
      case "Green" -> bb.put(new byte[]{0x00, (byte) 0xFF, 0x00});
      case "Blue" -> bb.put(new byte[]{0x00, 0x00, (byte) 0xFF});
      case "Yellow" -> bb.put(new byte[]{(byte) 0xFF, (byte) 0xFF, 0x00});
      case "Magenta" -> bb.put(new byte[]{(byte) 0xFF, 0x00, (byte) 0xFF});
      case "Cyan" -> bb.put(new byte[]{0x00, (byte) 0xFF, (byte) 0xFF});
      case "Black" -> bb.put(new byte[]{0x00, 0x00, 0x00});
      default -> {
        if (!rgb.startsWith("#")) {
          throw new IllegalArgumentException("rgb color not recognized: " + rgb);
        }
        bb.put((byte) Integer.parseInt(rgb.substring(1, 3), 16));
        bb.put((byte) Integer.parseInt(rgb.substring(3, 5), 16));
        bb.put((byte) Integer.parseInt(rgb.substring(5, 7), 16));
      }
    }
    String alpha = parts[1];
    switch (alpha) {
      case "Solid":
        bb.put((byte) 0xFF);
        break;
      default:
        bb.put((byte) Integer.parseInt(alpha.substring(8, 10), 16));
        break;
    }
    bb.rewind();
    return bb.getInt();
  }
}
