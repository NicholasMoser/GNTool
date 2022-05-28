package com.github.nicholasmoser.gnt4.seq.opcodes;

public interface BranchingOpcode {

  int getDestination();

  void setDestinationFunctionName(String destFuncName);
}
