package com.github.nicholasmoser.gnt4.seq.operands;

import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import java.util.Optional;

public class ChrOperand implements Operand {

  private final boolean isFoe;
  private final boolean pointer;
  private final StringBuilder infoBuilder;
  private int fieldOffset;

  /**
   * Creates a new chr_p operand.
   *
   * @param isPointer If this operand is a pointer; otherwise it is the value of a pointer.
   */
  public ChrOperand(boolean isFoe, boolean isPointer) {
    this.isFoe = isFoe;
    this.pointer = isPointer;
    this.infoBuilder = new StringBuilder();
    this.fieldOffset = -1;
  }

  @Override
  public int get() { return fieldOffset; }

  @Override
  public void addInfo(String info) {
    infoBuilder.append(info);
  }

  @Override
  public void withField(int fieldOffset) {
    this.fieldOffset = fieldOffset;
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
