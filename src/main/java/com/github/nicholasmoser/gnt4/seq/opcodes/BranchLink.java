package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchLink(int offset, Destination destination) {
    super("bl", new byte[] {0x01, 0x3C, 0x00, 0x00}, offset, destination);
  }
}
