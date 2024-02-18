package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchLessThanZero extends BranchingOpcode {

  public BranchLessThanZero(int offset, Destination destination) {
    super("bltz", new byte[] {0x01, 0x37, 0x00, 0x00}, offset, destination);
  }

  /**
   * @param offset      The offset of the opcode.
   * @param destination The destination of the branch.
   * @param secondByte  The second byte parameter exists because this opcode can have either 0x37 or
   *                    0x39 for the second byte.
   */
  public BranchLessThanZero(int offset, Destination destination, byte secondByte) {
    super("bltz", new byte[] {0x01, secondByte, 0x00, 0x00}, offset, destination);
  }
}
