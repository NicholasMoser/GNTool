package com.github.nicholasmoser.gnt4.seq.opcodes;

public class Branch extends BranchingOpcode {

  public Branch(int offset, int destination) {
    super("b", new byte[] {0x01, 0x32, 0x00, 0x00}, offset, destination);
  }

  public Branch(int offset, String destFuncName) {
    super("b", new byte[] {0x01, 0x32, 0x00, 0x00}, offset, destFuncName);
  }

  public Branch(int offset, int destination, String destFuncName) {
    super("b", new byte[] {0x01, 0x32, 0x00, 0x00}, offset, destination, destFuncName);
  }

}
