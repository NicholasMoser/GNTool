package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchLessThanOrEqualToZero extends BranchingOpcode {

  public BranchLessThanOrEqualToZero(int offset, int destination) {
    super("blez", new byte[] {0x01, 0x38, 0x00, 0x00}, offset, destination);
  }

}
