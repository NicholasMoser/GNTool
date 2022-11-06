package com.github.nicholasmoser.gnt4.seq.operands;

import com.github.nicholasmoser.gnt4.seq.structs.Chr;

import java.nio.ByteBuffer;
import java.util.Optional;

public class ChrOperand extends GeneralOperand implements Operand{

  private final boolean isFoe;
  private final boolean pointer;
  private final StringBuilder infoBuilder;
  private int fieldOffset;
  private ByteBuffer chr_p;

  /**
   * Creates a new chr_p operand.
   *
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public ChrOperand(boolean isFoe, boolean isPointer) {
    super(isFoe ? 0x27 : 0x26, isPointer);
    this.isFoe = isFoe;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
    this.chr_p = null;
  }

  public ChrOperand(boolean isFoe, boolean isPointer, ByteBuffer chr_p) {
    super(isFoe ? 0x27 : 0x26, isPointer, chr_p);
    this.isFoe = isFoe;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
    this.chr_p = chr_p;
  }

  @Override
  public int get() { return fieldOffset; }

  public boolean isFoe() { return isFoe; }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
    if (chr_p != null)
      chr_p = chr_p.position(fieldOffset);
  }

  public boolean isPointer() {
    return pointer;
  }

  @Override
  public String toString() {
    String chr = isFoe ? "foe_chr_p" : "chr_p";
    String prefix = pointer ? "" : "*";
    Optional<String> knownField = Chr.getField(fieldOffset);
    String field = knownField.isPresent() ? "->" + knownField.get() : getFieldDisplay(fieldOffset);
    return String.format("%s%s%s%s", prefix, chr, field, infoBuilder);
  }
}
