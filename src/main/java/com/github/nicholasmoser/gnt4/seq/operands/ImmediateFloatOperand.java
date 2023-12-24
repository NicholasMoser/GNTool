package com.github.nicholasmoser.gnt4.seq.operands;

public class ImmediateFloatOperand implements Operand {

  private final int offset;
  private final float value;
  private final StringBuilder infoBuilder;
  private int fieldOffset;

  public ImmediateFloatOperand(int offset, float value) {
    this.offset = offset;
    this.value = value;
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

  public float getImmediateValue() {
    return value;
  }

  @Override
  public String toString() {
    String field = getFieldDisplay(fieldOffset);
    return String.format("0x%.4f%s%s", value, field, infoBuilder);
  }
}
