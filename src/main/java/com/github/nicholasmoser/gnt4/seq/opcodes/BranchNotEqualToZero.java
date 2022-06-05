package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchNotEqualToZero extends BranchingOpcode {

  public BranchNotEqualToZero(int offset, int destination) {
    super("bnez", new byte[] {0x01, 0x34, 0x00, 0x00}, offset, destination);
  }

}
