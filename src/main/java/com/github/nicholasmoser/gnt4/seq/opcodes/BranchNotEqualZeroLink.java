package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchNotEqualZeroLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchNotEqualZeroLink(int offset, Destination destination) {
    super("bnezal", new byte[] {0x01, 0x3E, 0x00, 0x00}, offset, destination);
  }
}
