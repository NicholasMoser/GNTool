package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchEqualToZero extends BranchingOpcode {

  public BranchEqualToZero(int offset, Destination destination) {
    super("beqz", new byte[] {0x01, 0x33, 0x00, 0x00}, offset, destination);
  }
}
