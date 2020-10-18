package com.github.nicholasmoser.gecko;

import java.util.Collections;
import java.util.List;

/**
 * A group of Gecko Codes with a name associated with it. Many codes are actually comprised of
 * multiple Gecko Codes combined to achieve the desired effect. This class represents the grouped
 * together unit of desired functionality.
 */
public class GeckoCodeGroup {

  private final String name;
  private final List<GeckoCode> codes;

  public GeckoCodeGroup(String name, List<GeckoCode> codes) {
    this.name = name;
    this.codes = codes;
  }

  /**
   * @return The name of the GeckoCodeGroup.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The GeckoCodes associated with this GeckoCodeGroup.
   */
  public List<GeckoCode> getCodes() {
    return Collections.unmodifiableList(codes);
  }

  @Override
  public String toString() {
    return "GeckoCodeGroup{" +
        "name='" + name + '\'' +
        ", codes=" + codes +
        '}';
  }
}
