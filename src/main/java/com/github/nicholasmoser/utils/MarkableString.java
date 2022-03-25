package com.github.nicholasmoser.utils;

/**
 * A {@link String} that can be marked. It is up to the consumer of MarkableString what being
 * marked means.
 */
public class MarkableString {

  private final String text;
  private boolean marked;

  /**
   * Create a new unmarked String.
   *
   * @param text The text of the String.
   */
  public MarkableString(String text) {
    this.text = text;
    this.marked = false;
  }

  /**
   * Toggle whether this String is marked.
   */
  public void toggleMark() {
    marked = !marked;
  }

  /**
   * @return If this String is marked.
   */
  public boolean isMarked() {
    return marked;
  }

  /**
   * @return The String.
   */
  public String toString() {
    return text;
  }
}
