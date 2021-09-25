package com.github.nicholasmoser.gnt4.seq.operands;

public class GPROperand implements Operand {

  private final byte index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;

  /**
   * Creates a new general purpose register operand.
   *
   * @param index The general purpose register index.
   * @param pointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public GPROperand(byte index, boolean pointer) {
    this.index = index;
    this.pointer = pointer;
    this.infoBuilder = new StringBuilder();
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
