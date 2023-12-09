package com.github.nicholasmoser.gnt4.seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class SeqTest {
  @Test
  public void testActionMapping() {
    String expected = "stand";
    int id = Seq.getActionId(expected);
    assertEquals(expected, Seq.getActionDescription(id));

    expected = "running";
    id = Seq.getActionId(expected);
    assertEquals(expected, Seq.getActionDescription(id));

    expected = "hit_medium";
    id = Seq.getActionId(expected);
    assertEquals(expected, Seq.getActionDescription(id));

    String unknownDescription = "unknown_0xFFF";
    int unknownId = 0xFFF;
    id = Seq.getActionId(unknownDescription);
    assertEquals(unknownId, id);
    String description = Seq.getActionDescription(unknownId);
    assertEquals(unknownDescription, description);

    String unusedDescription = "unknown_0xFF1 (unused)";
    int unusedId = 0xFF1;
    id = Seq.getActionId(unusedDescription);
    assertEquals(unusedId, id);
    description = Seq.getActionDescription(unusedId);
    assertEquals("unknown_0xFF1", description);
  }

  @Test
  public void testButtonMapping() {
    String expected = "Up";
    OptionalInt bits = Seq.getButtonBitfield(expected);
    assertThat(bits.isPresent()).isTrue();
    assertEquals(expected, Seq.getButtonDescriptions(bits.getAsInt()));

    expected = "Forward; Back; Down";
    bits = Seq.getButtonBitfield(expected);
    assertThat(bits.isPresent()).isTrue();
    assertEquals(expected, Seq.getButtonDescriptions(bits.getAsInt()));

    expected = "Facing_Left";
    bits = Seq.getButtonBitfield(expected);
    assertThat(bits.isPresent()).isTrue();
    assertEquals(expected, Seq.getButtonDescriptions(bits.getAsInt()));

    bits = Seq.getButtonBitfield("asdasd");
    assertThat(bits.isPresent()).isFalse();
    assertNull(Seq.getButtonDescriptions(0));
    assertNull(Seq.getButtonDescriptions(0x100000));
  }

  @Test
  public void testNoActionsWithSpaces() {
    for (String action : Seq.getActions()) {
      assertThat(action).doesNotContain(" ");
    }
    for (String action : Seq.getButtonDescriptions()) {
      assertThat(action).doesNotContain(" ");
    }
    for (String action : GNT4Characters.CHARACTERS) {
      assertThat(action).doesNotContain(" ");
    }
  }

  @Test
  public void testOperandSymbolsUnique() {
    Set<String> found = new HashSet<>();
    for (String symbol : Seq.getActions()) {
      if (found.contains(symbol)) {
        fail("Duplicates of symbol: " + symbol);
      }
      found.add(symbol);
    }
    for (String symbol : Seq.getButtonDescriptions()) {
      if (found.contains(symbol)) {
        fail("Duplicates of symbol: " + symbol);
      }
      found.add(symbol);
    }
    for (String symbol : GNT4Characters.CHARACTERS) {
      if (found.contains(symbol)) {
        fail("Duplicates of symbol: " + symbol);
      }
      found.add(symbol);
    }
  }

  @Test
  public void testOperandSymbolsNotContainSeparator() {
    for (String action : Seq.getActions()) {
      assertThat(action).doesNotContain(";");
    }
    for (String action : Seq.getButtonDescriptions()) {
      assertThat(action).doesNotContain(";");
    }
    for (String action : GNT4Characters.CHARACTERS) {
      assertThat(action).doesNotContain(";");
    }
  }
}
