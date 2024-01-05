package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class Branch extends BranchingOpcode {

  public Branch(int offset, Destination destination) {
    super("b", new byte[] {0x01, 0x32, 0x00, 0x00}, offset, destination);
  }
}
