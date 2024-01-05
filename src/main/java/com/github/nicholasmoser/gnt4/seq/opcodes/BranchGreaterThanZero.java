package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchGreaterThanZero extends BranchingOpcode {

  public BranchGreaterThanZero(int offset, Destination destination) {
    super("bgtz", new byte[] {0x01, 0x35, 0x00, 0x00}, offset, destination);
  }
}
