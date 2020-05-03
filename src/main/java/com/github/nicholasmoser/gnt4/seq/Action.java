package com.github.nicholasmoser.gnt4.seq;

/**
 * An action in a sequence file.
 */
public class Action {
  private final int id;

  private final String description;

  public Action(int id, String description) {
    this.id = id;
    this.description = description;
  }

  /**
   * @return The id of the action.
   */
  public int getId() {
    return id;
  }

  /**
   * @return The description of the action.
   */
  public String getDescription() {
    return description;
  }
}
