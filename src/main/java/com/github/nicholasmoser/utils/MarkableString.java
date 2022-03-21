package com.github.nicholasmoser.utils;

public class MarkableString {

  private final String text;
  private boolean marked;

  public MarkableString(String text) {
    this.text = text;
    this.marked = false;
  }

  public void toggleMark() {
    marked = !marked;
  }

  public boolean isMarked() {
    return marked;
  }

  public String toString() {
    return text;
  }
}
