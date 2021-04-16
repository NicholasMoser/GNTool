package com.github.nicholasmoser.gecko.codes;

import org.json.JSONObject;

/**
 * A code group that includes a Gecko injection code (C2 code).
 */
public interface GeckoInjectionCode {

  /**
   * @return The raw bytes of the code group.
   */
  byte[] getCode();

  /**
   * Returns the JSON Object of the code group for the codes.json file.
   *
   * @param hijackedAddress The location of the bytes that have been replaced by the injected code.
   * @param hijackedBytes The bytes that have been replaced by the injected code.
   * @return The JSON Object of the code group for the codes.json file.
   */
  JSONObject getJSONObject(long hijackedAddress, byte[] hijackedBytes);
}
