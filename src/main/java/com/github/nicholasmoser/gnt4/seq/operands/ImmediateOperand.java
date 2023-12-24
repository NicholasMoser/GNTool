package com.github.nicholasmoser.gnt4.seq.operands;

public class ImmediateOperand implements Operand {

  private final int offset;
  private final int word;
  private final StringBuilder infoBuilder;
  private int fieldOffset;
  private int wordSize;

  public ImmediateOperand(int offset, int word, int wordSize) {
    this.offset = offset;
    this.word = word;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
    this.wordSize = wordSize;
  }

  @Override
  public int get() { return getOffset(); }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
  }

  public int getOffset() {
    return offset;
  }

  public int getImmediateValue() {
    if (wordSize == 1) {
      return (word >> 0x18) & 0xFF;
    } else if (wordSize == 2) {
      return (word >> 0x10) & 0xFFFF;
    } else if (wordSize == 4) {
      return word;
    } else {
      throw new RuntimeException("Word size not supported: " + wordSize);
    }
  }

  @Override
  public String toString() {
    String field = getFieldDisplay(fieldOffset);
    String wordDisplay;
    if (wordSize == 1) {
      int shiftedWord = (word >> 0x18) & 0xFF;
      wordDisplay = String.format("byte 0x%X", shiftedWord);
    } else if (wordSize == 2) {
      int shiftedWord = (word >> 0x10) & 0xFFFF;
      wordDisplay = String.format("short 0x%X", shiftedWord);
    } else if (wordSize == 4) {
      wordDisplay = String.format("0x%X", word);
    } else {
      throw new RuntimeException("Word size not supported: " + wordSize);
    }
    return String.format("%s%s%s", wordDisplay, field, infoBuilder);
  }
}
