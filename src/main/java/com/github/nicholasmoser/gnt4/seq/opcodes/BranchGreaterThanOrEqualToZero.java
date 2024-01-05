package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class BranchGreaterThanOrEqualToZero extends BranchingOpcode {

  public BranchGreaterThanOrEqualToZero(int offset, Destination destination) {
    super("bgez", new byte[] {0x01, 0x36, 0x00, 0x00}, offset, destination);
  }

  /**
   * @param offset      The offset of the opcode.
   * @param destination The destination of the branch.
   * @param secondByte  The second byte parameter exists because this opcode can have either 0x36 or
   *                    0x3A for the second byte.
   */
  public BranchGreaterThanOrEqualToZero(int offset, Destination destination, byte secondByte) {
    super("bgez", new byte[] {0x01, secondByte, 0x00, 0x00}, offset, destination);
  }
}
