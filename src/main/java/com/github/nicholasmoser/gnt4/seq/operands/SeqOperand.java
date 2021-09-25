package com.github.nicholasmoser.gnt4.seq.operands;

public class SeqOperand implements Operand {

  private final byte index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;

  /**
   * Creates a new seq_p_sp operand.
   *
   * @param index The seq_p_sp field index.
   * @param pointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public SeqOperand(byte index, boolean pointer) {
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
    return String.format("EA: %sseq_p_sp->field_0x%02x%s", prefix, index, infoBuilder);
  }
}
