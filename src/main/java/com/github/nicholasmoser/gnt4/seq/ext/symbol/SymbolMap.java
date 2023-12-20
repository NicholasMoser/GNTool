package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public class SymbolMap {
  private final BiMap<String, Integer> nameToOffset;
  private final BiMap<Integer, String> offsetToName;
  public SymbolMap() {
    this.nameToOffset = HashBiMap.create();
    this.offsetToName = nameToOffset.inverse();
  }

  public void update(String name, int offset) {
    if (nameToOffset.containsKey(name)) {
      throw new IllegalArgumentException("key already present: " + name);
    }
    nameToOffset.put(name, offset);
  }

  public OptionalInt getOffset(String name) {
    Integer offset = nameToOffset.get(name);
    if (offset == null) {
      return OptionalInt.empty();
    }
    return OptionalInt.of(offset);
  }

  public Optional<String> getName(int offset) {
    return Optional.ofNullable(offsetToName.get(offset));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SymbolMap symbolMap = (SymbolMap) o;
    return Objects.equals(nameToOffset, symbolMap.nameToOffset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameToOffset);
  }

  @Override
  public String toString() {
    return "SymbolMap{" +
        "nameToOffset=" + nameToOffset +
        '}';
  }
}
