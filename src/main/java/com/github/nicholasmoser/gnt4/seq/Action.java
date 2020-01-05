package com.github.nicholasmoser.gnt4.seq;

/**
 * An action in a sequence file.
 */
public class Action {
  private int id;

  private String description;

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
