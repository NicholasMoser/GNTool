package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;

public class HandleRecording implements Opcode {

  private final static String MNEMONIC = "handle_recording";
  private final int offset;
  private final boolean thisChr;

  /**
   * @param offset The offset.
   * @param thisChr If this is to apply to this character, otherwise is the other character.
   */
  public HandleRecording(int offset, boolean thisChr) {
    this.offset = offset;
    this.thisChr = thisChr;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    byte option = (byte) (thisChr ? 0x00 : 0x01);
    return new byte[]{0x26, (byte) 0xE9, option, 0x00};
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    String chr = thisChr ? "chr_p" : "foe_chr_p";
    return String.format("%05X | %s (%s) %s", offset, MNEMONIC, chr, formatRawBytes(getBytes()));
  }

  @Override
  public String toAssembly() {
    return String.format("%s",MNEMONIC);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String chr = thisChr ? "chr_p" : "foe_chr_p";
    return div(attrs(id))
        .withText(String.format("%05X | %s (%s) ", offset, chr, MNEMONIC))
        .with(formatRawBytesHTML(getBytes()));
  }
}
