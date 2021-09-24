package com.github.nicholasmoser.gnt4.seq.operands;

public class SeqOperand implements Operand {

  private final byte index;

  private final StringBuilder infoBuilder;

  public SeqOperand(byte index) {
    this.index = index;
    this.infoBuilder = new StringBuilder();
  }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public String toString() {
    return String.format("EA: seq_p_sp->field_0x%02x%s", index, infoBuilder);
  }
}
