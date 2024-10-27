package com.github.nicholasmoser.gnt4.seq.opcodes.tcg;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;

public class TcgBmeq extends TcgBranchingOpcode {

  private final static String MNEMONIC = "tcg_bmeq";

  public TcgBmeq(int offset, byte[] bytes, String info, Destination destination) {
    super(MNEMONIC, bytes, offset, info, destination);
  }
}
