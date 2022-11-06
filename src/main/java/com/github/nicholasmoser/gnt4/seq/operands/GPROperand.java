package com.github.nicholasmoser.gnt4.seq.operands;

import java.nio.ByteBuffer;

public class GPROperand extends GeneralOperand implements Operand {
  /*
  private final int index;
  private final boolean pointer;
  private final StringBuilder infoBuilder;
  private int fieldOffset;
  private Object obj = null;
  private ByteBuffer bb = null;
  */
  private final StringBuilder infoBuilder;

  /**
   * Creates a new general purpose register operand.
   *
   * @param index The general purpose register index.
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public GPROperand(int index, boolean isPointer) {
    super(index, isPointer);
    this.infoBuilder = new StringBuilder();
    /*
    this.index = index;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
    this.obj = null;
    this.bb = null;
     */
  }

  public GPROperand(int index, boolean isPointer, Object obj) {
    super(index, isPointer, obj);
    this.infoBuilder = new StringBuilder();
    /*
    this.index = index;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
    this.obj = obj;
    if (obj instanceof ByteBuffer bb) {
      this.bb = bb;
    } else {
      this.bb = null;
    }

     */
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
    if (bb != null) {
      bb = bb.position(fieldOffset);
    }
  }

  public int getFieldOffset() { return fieldOffset; }

  public int getIndex() {
    return index;
  }

  public boolean isPointer() {
    return pointer;
  }
  */
  /*
  @Override
  public String toString() {
    String prefix = pointer ? "" : "*";
    String field = getFieldDisplay(fieldOffset);
    return String.format("%sgpr%d%s%s", prefix, index, field, infoBuilder);
  }
   */
  public String toString() {
    String prefix = super.isPointer() ? "" : "*";
    String field = getFieldDisplay(super.getFieldOffset());
    return String.format("%sgpr%d%s%s", prefix, super.get(), field, infoBuilder);
  }
}
