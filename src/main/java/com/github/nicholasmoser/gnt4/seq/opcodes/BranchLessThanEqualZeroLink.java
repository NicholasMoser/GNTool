package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchLessThanEqualZeroLink extends BranchingOpcode {

  public BranchLessThanEqualZeroLink(int offset, int destination) {
    super("blezal", new byte[] {0x01, 0x42, 0x00, 0x00}, offset, destination);
  }

}
