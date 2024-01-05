package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchLessThanZeroLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchLessThanZeroLink(int offset, Destination destination) {
    super("bltzal", new byte[] {0x01, 0x41, 0x00, 0x00}, offset, destination);
  }
}
