package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchEqualToZeroLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchEqualToZeroLink(int offset, Destination destination) {
    super("beqzal", new byte[] {0x01, 0x3D, 0x00, 0x00}, offset, destination);
  }
}
