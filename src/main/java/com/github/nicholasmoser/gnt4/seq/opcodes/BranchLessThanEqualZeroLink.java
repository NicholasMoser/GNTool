package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchLessThanEqualZeroLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchLessThanEqualZeroLink(int offset, Destination destination) {
    super("blezal", new byte[] {0x01, 0x42, 0x00, 0x00}, offset, destination);
  }
}
