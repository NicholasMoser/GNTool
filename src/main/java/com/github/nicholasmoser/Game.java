package com.github.nicholasmoser;

/**
 * The list of supported GNT games.
 */
public enum Game {

  GNT4("G4NJDA");

  private final String gameId;

  /**
   * @param gameId The 6-byte game ID.
   */
  Game(String gameId) {
    this.gameId = gameId;
  }

  /**
   * @return The 6-byte game ID.
   */
  public String getGameId() {
    return gameId;
  }
}
