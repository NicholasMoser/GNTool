package com.github.nicholasmoser.gnt4.seq.opcodes.tcg;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class TcgBgt extends TcgBranchingOpcode {

  private final static String MNEMONIC = "tcg_bgt";

  public TcgBgt(int offset, byte[] bytes, String info, Destination destination) {
    super(MNEMONIC, bytes, offset, info, destination);
  }
}
