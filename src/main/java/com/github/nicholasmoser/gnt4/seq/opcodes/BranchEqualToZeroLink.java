package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchEqualToZeroLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchEqualToZeroLink(int offset, int destination) {
    super("beqzal", new byte[] {0x01, 0x3D, 0x00, 0x00}, offset, destination);
  }

  public BranchEqualToZeroLink(int offset, String destFuncName) {
    super("beqzal", new byte[] {0x01, 0x3D, 0x00, 0x00}, offset, destFuncName);
  }

}
