package com.github.nicholasmoser.gnt4.dol;

public class CodeMatchResult {

  private final boolean codeMatch;
  private final int newIndex;

  public CodeMatchResult(boolean codeMatch, int newIndex) {
    this.codeMatch = codeMatch;
    this.newIndex = newIndex;
  }

  public boolean isCodeMatch() {
    return codeMatch;
  }

  public int getNewIndex() {
    return newIndex;
  }
}
