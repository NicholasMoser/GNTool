package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.ppc.BranchEqual;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

public class SymbolMapTest {
  @Test
  public void testSingleSymbolInMap() {
    String name1 = "name1";
    int offset1 = 3;
    SymbolMap map = new SymbolMap();
    map.update(name1, offset1);

    Optional<String> actualName = map.getName(offset1);
    assertThat(actualName.isPresent()).isTrue();
    assertThat(actualName.get()).isEqualTo(name1);

    OptionalInt actualOffset = map.getOffset(name1);
    assertThat(actualOffset.isPresent()).isTrue();
    assertThat(actualOffset.getAsInt()).isEqualTo(offset1);

    SymbolMap map2 = new SymbolMap();
    map2.update(name1, offset1);
    assertThat(map).isEqualTo(map2);
  }

  @Test
  public void testMultiSymbolMap() {
    String name1 = "name1";
    String name2 = "name2";
    String name3 = "name3";
    int offset1 = -1;
    int offset2 = 0;
    int offset3 = 1000;
    SymbolMap map = new SymbolMap();
    map.update(name1, offset1);
    map.update(name2, offset2);
    map.update(name3, offset3);

    Optional<String> actualName = map.getName(offset1);
    assertThat(actualName.isPresent()).isTrue();
    assertThat(actualName.get()).isEqualTo(name1);

    actualName = map.getName(offset2);
    assertThat(actualName.isPresent()).isTrue();
    assertThat(actualName.get()).isEqualTo(name2);

    actualName = map.getName(offset3);
    assertThat(actualName.isPresent()).isTrue();
    assertThat(actualName.get()).isEqualTo(name3);

    actualName = map.getName(Integer.MAX_VALUE);
    assertThat(actualName.isPresent()).isFalse();

    OptionalInt actualOffset = map.getOffset(name1);
    assertThat(actualOffset.isPresent()).isTrue();
    assertThat(actualOffset.getAsInt()).isEqualTo(offset1);

    actualOffset = map.getOffset(name2);
    assertThat(actualOffset.isPresent()).isTrue();
    assertThat(actualOffset.getAsInt()).isEqualTo(offset2);

    actualOffset = map.getOffset(name3);
    assertThat(actualOffset.isPresent()).isTrue();
    assertThat(actualOffset.getAsInt()).isEqualTo(offset3);

    actualOffset = map.getOffset("unknown symbol");
    assertThat(actualOffset.isPresent()).isFalse();

    SymbolMap map2 = new SymbolMap();
    map2.update(name1, offset1);
    map2.update(name2, offset2);
    map2.update(name3, offset3);
    assertThat(map2).isEqualTo(map);

    SymbolMap map3 = new SymbolMap();
    map3.update(name1, offset1);
    map3.update(name2, offset2);
    assertThat(map3).isNotEqualTo(map);
    map3.update(name3, offset3);
    assertThat(map3).isEqualTo(map);
    map3.update("asd", 123);
    assertThat(map3).isNotEqualTo(map);
  }

  @Test
  public void testOverridingValuesFails() {
    String name1 = "name1";
    String name2 = "name2";
    int offset1 = -1;
    int offset2 = 0;

    SymbolMap map = new SymbolMap();
    map.update(name1, offset1);
    assertThrows(IllegalArgumentException.class, () -> map.update(name1, offset2));
    assertThrows(IllegalArgumentException.class, () -> map.update(name2, offset1));
  }
}
