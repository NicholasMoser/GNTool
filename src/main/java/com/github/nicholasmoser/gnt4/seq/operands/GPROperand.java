package com.github.nicholasmoser.gnt4.seq.operands;

public class GPROperand implements Operand {

  private final int index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;

  /**
   * Creates a new general purpose register operand.
   *
   * @param index The general purpose register index.
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public GPROperand(int index, boolean isPointer) {
    this.index = index;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
  }

  public int getIndex() {
    return index;
  }

  public boolean isPointer() {
    return pointer;
  }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public String toString() {
    String prefix = pointer ? "" : "*";
    return String.format("EA: %sgpr%02x%s", prefix, index, infoBuilder);
  }
}
