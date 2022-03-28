package com.github.nicholasmoser.gnt4.seq.operands;

public class ImmediateOperand implements Operand {

  private final int offset;
  private final int word;
  private final StringBuilder infoBuilder;

  public ImmediateOperand(int offset, int word) {
    this.offset = offset;
    this.word = word;
    this.infoBuilder = new StringBuilder();
  }

  public int getOffset() {
    return offset;
  }

  public int getImmediateValue() {
    return word;
  }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public String toString() {
    return String.format("Immediate value offset 0x%x (0x%08x)%s", offset, word, infoBuilder);
  }
}
