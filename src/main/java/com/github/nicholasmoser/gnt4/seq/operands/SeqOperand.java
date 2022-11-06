package com.github.nicholasmoser.gnt4.seq.operands;

import java.nio.ByteBuffer;

public class SeqOperand extends GeneralOperand implements Operand {

  private final StringBuilder infoBuilder;
  //private int fieldOffset;
  //private Object obj = null;
  //private ByteBuffer bb = null;

  /**
   * Creates a new seq_p_sp operand.
   *
   * @param index The seq_p_sp field index.
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public SeqOperand(int index, boolean isPointer) {
    super(index + 0x18, isPointer);
    this.infoBuilder = new StringBuilder();
    //this.fieldOffset = -1;
  }

  public SeqOperand(int index, boolean isPointer, Object obj) {
    super(index + 0x18, isPointer, obj);
    this.infoBuilder = new StringBuilder();
    //this.fieldOffset = -1;
  }
/*
  @Override
  public int get() { return getIndex(); }
*/
  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }
/*
  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
  }

  public int getFieldOffset() { return fieldOffset; }

  public int getIndex() {
    return super.getIndex();
  }*/
/*
  public boolean isPointer() {
    return pointer;
  }
*/
  /*
  @Override
  public String toString() {
    String prefix = pointer ? "" : "*";
    String field = getFieldDisplay(fieldOffset);
    return String.format("%sseq_p_sp%d%s%s", prefix, index, field, infoBuilder);
  }
  */
  public String toString() {
  String prefix = super.isPointer() ? "" : "*";
  String field = getFieldDisplay(super.getFieldOffset());
  return String.format("%sseq_p_sp%d%s%s", prefix, super.get(), field, infoBuilder);
}
}
