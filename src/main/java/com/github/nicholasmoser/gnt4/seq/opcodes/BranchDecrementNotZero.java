package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchDecrementNotZero extends BranchingOpcode {

  public BranchDecrementNotZero(int offset, int destination) {
    super("bdnz", new byte[] {0x01, 0x3B, 0x00, 0x00}, offset, destination);
  }

  public BranchDecrementNotZero(int offset, String destFuncLabel) {
    super("bdnz", new byte[] {0x01, 0x3B, 0x00, 0x00}, offset, destFuncLabel);
  }

}
