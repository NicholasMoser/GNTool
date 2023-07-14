package com.github.nicholasmoser.gnt4.seq.opcodes;

public class BranchLink extends BranchingOpcode implements BranchingLinkingOpcode {

  public BranchLink(int offset, int destination) {
    super("bl", new byte[] {0x01, 0x3C, 0x00, 0x00}, offset, destination);
  }

  public BranchLink(int offset, String destFuncName) {
    super("bl", new byte[] {0x01, 0x3C, 0x00, 0x00}, offset, destFuncName);
  }

}
