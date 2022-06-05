package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchLessThanZeroLink extends BranchingOpcode {

  public BranchLessThanZeroLink(int offset, int destination) {
    super("bltzal", new byte[] {0x01, 0x41, 0x00, 0x00}, offset, destination);
  }

}
