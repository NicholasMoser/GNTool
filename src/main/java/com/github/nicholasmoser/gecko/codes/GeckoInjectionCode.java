package com.github.nicholasmoser.gecko.codes;

import org.json.JSONArray;

public interface GeckoInjectionCode {

  byte[] getCode();

  void writeToJSONArray(JSONArray jsonArray);
}
