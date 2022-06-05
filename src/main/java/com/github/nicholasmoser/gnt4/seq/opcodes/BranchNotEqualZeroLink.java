package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchNotEqualZeroLink extends BranchingOpcode {

  public BranchNotEqualZeroLink(int offset, int destination) {
    super("bnezal", new byte[] {0x01, 0x3E, 0x00, 0x00}, offset, destination);
  }

}
