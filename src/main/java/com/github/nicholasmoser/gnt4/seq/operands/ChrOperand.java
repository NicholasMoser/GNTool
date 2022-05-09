package com.github.nicholasmoser.gnt4.seq.operands;

public class ChrOperand implements Operand {

  private final boolean pointer;
  private final StringBuilder infoBuilder;
  private int fieldOffset;

  /**
   * Creates a new chr_p operand.
   *
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public ChrOperand(boolean isPointer) {
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
  }

  @Override
  public int get() { return fieldOffset; }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
  }

  public boolean isPointer() {
    return pointer;
  }

  @Override
  public String toString() {
    String prefix = pointer ? "" : "*";
    String field = getFieldDisplay(fieldOffset);
    return String.format("%schr_p%s%s", prefix, field, infoBuilder);
  }
}