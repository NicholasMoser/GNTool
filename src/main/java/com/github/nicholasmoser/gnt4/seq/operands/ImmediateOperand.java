package com.github.nicholasmoser.gnt4.seq.operands;

public class ImmediateOperand implements Operand {

  private final int offset;
  private final int word;
  private final StringBuilder infoBuilder;
  private int fieldOffset;

  public ImmediateOperand(int offset, int word) {
    this.offset = offset;
    this.word = word;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
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
    return word;
  }

  @Override
  public String toString() {
    String field = getFieldDisplay(fieldOffset);
    return String.format("0x%X%s%s", word, field, infoBuilder);
  }
}
