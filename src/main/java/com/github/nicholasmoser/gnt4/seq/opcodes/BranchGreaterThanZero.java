package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchGreaterThanZero extends BranchingOpcode {

  public BranchGreaterThanZero(int offset, int destination) {
    super("bgtz", new byte[] {0x01, 0x35, 0x00, 0x00}, offset, destination);
  }

}
