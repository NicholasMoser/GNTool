package com.github.nicholasmoser.gnt4.seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    Integer bits = Seq.getButtonBitfield(expected);
    assertEquals(expected, Seq.getButtonDescriptions(bits));

    expected = "Forward; Back; Down";
    bits = Seq.getButtonBitfield(expected);
    assertEquals(expected, Seq.getButtonDescriptions(bits));

    expected = "Facing_Left";
    bits = Seq.getButtonBitfield(expected);
    assertEquals(expected, Seq.getButtonDescriptions(bits));

    expected = "None";
    bits = Seq.getButtonBitfield(expected);
    assertEquals(expected, Seq.getButtonDescriptions(bits));

    String unknownDescription = "asd";
    bits = Seq.getButtonBitfield(unknownDescription);
    assertEquals(0, bits);
  }

  @Test
  public void testNoActionsWithSpaces() {
    for (String action : Seq.getActions()) {
      assertThat(action).doesNotContain(" ");
    }
  }
}
