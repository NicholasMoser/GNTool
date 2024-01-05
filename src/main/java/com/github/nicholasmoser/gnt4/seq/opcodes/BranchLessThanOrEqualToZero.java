package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchLessThanOrEqualToZero extends BranchingOpcode {

  public BranchLessThanOrEqualToZero(int offset, Destination destination) {
    super("blez", new byte[] {0x01, 0x38, 0x00, 0x00}, offset, destination);
  }
}
