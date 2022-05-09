package com.github.nicholasmoser.gnt4.seq.operands;

public class SeqOperand implements Operand {

  private final int index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;
  private int fieldOffset;

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
    this.fieldOffset = -1;
  }

  @Override
  public int get() { return getIndex(); }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
  }

  public int getIndex() {
    return index;
  }

  public boolean isPointer() {
    return pointer;
  }

  @Override
  public String toString() {
    String prefix = pointer ? "" : "*";
    String field = getFieldDisplay(fieldOffset);
    return String.format("%sseq_p_sp->field_0x%02X%s%s", prefix, index * 4, field, infoBuilder);
  }
}
