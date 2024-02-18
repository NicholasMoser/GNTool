package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchDecrementNotZero extends BranchingOpcode {

  public BranchDecrementNotZero(int offset, Destination destination) {
    super("bdnz", new byte[] {0x01, 0x3B, 0x00, 0x00}, offset, destination);
  }
}
