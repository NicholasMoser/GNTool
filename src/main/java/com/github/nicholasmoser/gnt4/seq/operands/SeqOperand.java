package com.github.nicholasmoser.gnt4.seq.operands;

public class SeqOperand implements Operand {

  private final int index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;

  /**
   * Creates a new seq_p_sp operand.
   *
   * @param index The seq_p_sp field index.
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public SeqOperand(int index, boolean isPointer) {
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
    return String.format("%sseq_p_sp->field_0x%02x%s", prefix, index, infoBuilder);
  }
}
