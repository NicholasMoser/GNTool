package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchGreaterThanOrEqualToZero extends BranchingOpcode {

  public BranchGreaterThanOrEqualToZero(int offset, int destination) {
    super("bgez", new byte[] {0x01, 0x36, 0x00, 0x00}, offset, destination);
  }

  /**
   * @param offset      The offset of the opcode.
   * @param destination The destination of the branch.
   * @param secondByte  The second byte parameter exists because this opcode can have either 0x36 or
   *                    0x3A for the second byte.
   */
  public BranchGreaterThanOrEqualToZero(int offset, int destination, byte secondByte) {
    super("bgez", new byte[] {0x01, secondByte, 0x00, 0x00}, offset, destination);
  }

  public BranchGreaterThanOrEqualToZero(int offset, String destFuncName) {
    super("bgez", new byte[] {0x01, 0x36, 0x00, 0x00}, offset, destFuncName);
  }

}
