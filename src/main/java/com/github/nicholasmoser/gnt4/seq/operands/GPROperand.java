package com.github.nicholasmoser.gnt4.seq.operands;

public class GPROperand implements Operand {

  private final byte index;
  private final StringBuilder infoBuilder;

  public GPROperand(byte index) {
    this.index = index;
    this.infoBuilder = new StringBuilder();
  }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public String toString() {
    return String.format("EA: *gpr%02x%s", index, infoBuilder);
  }
}
