package com.github.nicholasmoser.gnt4.seq.operands;

public interface Operand {
  int get();
  void addInfo(String info);
  void withField(int fieldOffset);
  default String getFieldDisplay(int offset) {
    return offset > -1 ? String.format("->field_0x%02X", offset) : "";
  }
}
