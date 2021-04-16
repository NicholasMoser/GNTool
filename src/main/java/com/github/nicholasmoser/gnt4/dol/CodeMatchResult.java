package com.github.nicholasmoser.gnt4.dol;

import java.util.Optional;
import org.json.JSONObject;

/**
 * A POJO for a code match result.
 */
public class CodeMatchResult {

  private final boolean codeMatch;
  private final int newIndex;
  private final JSONObject codeGroup;

  /**
   * Constructor for CodeMatchResult.
   *
   * @param codeMatch If there was a code match.
   * @param newIndex The new index in the hijacked bytes after searching for a code match.
   */
  public CodeMatchResult(boolean codeMatch, int newIndex) {
    this.codeMatch = codeMatch;
    this.newIndex = newIndex;
    this.codeGroup = null;
  }

  /**
   * Constructor for CodeMatchResult.
   *
   * @param codeMatch If there was a code match.
   * @param newIndex The new index in the hijacked bytes after searching for a code match.
   * @param codeGroup The JSON object code group of the code match.
   */
  public CodeMatchResult(boolean codeMatch, int newIndex, JSONObject codeGroup) {
    this.codeMatch = codeMatch;
    this.newIndex = newIndex;
    this.codeGroup = codeGroup;
  }

  /**
   * @return If there was a code match.
   */
  public boolean isCodeMatch() {
    return codeMatch;
  }

  /**
   * @return The new index in the hijacked bytes after searching for a code match.
   */
  public int getNewIndex() {
    return newIndex;
  }

  /**
   * @return An optional JSON object code group of the code match.
   */
  public Optional<JSONObject> getCodeGroup() {
    return Optional.ofNullable(codeGroup);
  }
}
