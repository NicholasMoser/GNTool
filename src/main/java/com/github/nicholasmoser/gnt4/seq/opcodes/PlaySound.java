package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class PlaySound implements Opcode {

  private final static String MNEMONIC = "play_sound";
  private final int offset;
  private final byte[] bytes;
  private final String info;

  public PlaySound(int offset, byte[] bytes, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    StringBuilder builder = new StringBuilder();
    String type = switch(bytes[2]) {
      case 0x0 -> "general";
      case 0x1 -> "hit";
      case 0x2 -> "stored_1";
      case 0x3 -> "stored_2";
      case 0x5 -> "run";
      case 0x6 -> "unused";
      case 0x8 -> "land_on_feet";
      case 0x9 -> "land_on_body";
      case 0xB -> "grunt";
      case 0xC -> "hiki";
      case 0xD -> "walk";
      case 0xE -> "3MC";
      default -> throw new IllegalArgumentException("Unknown type: " + bytes[2]);
    };
    builder.append(String.format("%s %s", MNEMONIC, type));
    builder.append(String.format(", 0x%02X%02X, 0x%02X%02X", bytes[4], bytes[5], bytes[6], bytes[7]));
    return builder.toString();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
        .with(formatRawBytesHTML(bytes));
  }
}
