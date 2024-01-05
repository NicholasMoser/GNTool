package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchNotEqualToZero extends BranchingOpcode {

  public BranchNotEqualToZero(int offset, Destination destination) {
    super("bnez", new byte[] {0x01, 0x34, 0x00, 0x00}, offset, destination);
  }
}
