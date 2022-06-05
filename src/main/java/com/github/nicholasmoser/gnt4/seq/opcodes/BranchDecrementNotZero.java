package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchDecrementNotZero extends BranchingOpcode {

  public BranchDecrementNotZero(int offset, int destination) {
    super("bdnz", new byte[] {0x01, 0x3B, 0x00, 0x00}, offset, destination);
  }

}
