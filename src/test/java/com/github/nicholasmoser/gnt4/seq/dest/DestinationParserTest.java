package com.github.nicholasmoser.gnt4.seq.dest;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DestinationParserTest {
  @Test
  public void testAbsoluteDestination() {
    Destination dest = DestinationParser.get("0x10");
    assertThat(dest).isInstanceOf(AbsoluteDestination.class);
    assertThat(dest.offset()).isEqualTo(0x10);
    assertThat(dest.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x10 });
    assertThat(dest.toString()).isEqualTo("0x10");
  }

  @Test
  public void testNegativeAbsoluteDestination() {
    Destination dest = DestinationParser.get("0xFFFFFFFF");
    assertThat(dest).isInstanceOf(AbsoluteDestination.class);
    assertThat(dest.offset()).isEqualTo(-1);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("0xFFFFFFFF");
  }

  @Test
  public void testRelativeDestination() {
    Destination dest = DestinationParser.get("+0x10");
    assertThat(dest).isInstanceOf(RelativeDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("+0x10");

    // Now resolve it
    RelativeDestination relativeDest = (RelativeDestination) dest;
    relativeDest.resolve(0x100);
    assertThat(dest.offset()).isEqualTo(0x110);
    assertThat(dest.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x01, 0x10 });
    assertThat(dest.toString()).isEqualTo("0x110");
  }

  @Test
  public void testLargestRelativeDestination() {
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("+0x80000000"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("+0x80000001"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("+0x90000000"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("+0x100000000"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("+" + Long.MAX_VALUE));
    Destination dest = DestinationParser.get("+0x7FFFFFFF");
    assertThat(dest).isInstanceOf(RelativeDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("+0x7FFFFFFF");

    // Now resolve it
    RelativeDestination relativeDest = (RelativeDestination) dest;
    relativeDest.resolve(0);
    assertThat(dest.offset()).isEqualTo(0x7FFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] { 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("0x7FFFFFFF");
  }

  @Test
  public void testNegativeRelativeDestination() {
    Destination dest = DestinationParser.get("-0x10000");
    assertThat(dest).isInstanceOf(RelativeDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("-0x10000");

    // Now resolve it
    RelativeDestination relativeDest = (RelativeDestination) dest;
    relativeDest.resolve(0x100000);
    assertThat(dest.offset()).isEqualTo(0xF0000);
    assertThat(dest.bytes()).isEqualTo(new byte[] { 0x00, 0x0F, 0x00, 0x00 });
    assertThat(dest.toString()).isEqualTo("0xF0000");
  }

  @Test
  public void testSmallestNegativeRelativeDestination() {
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("-0x80000001"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("-0x80000002"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("-0x90000000"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("-0x100000000"));
    assertThrows(IllegalStateException.class, () -> DestinationParser.get("-" + Long.MAX_VALUE));
    Destination dest = DestinationParser.get("-0x80000000");
    assertThat(dest).isInstanceOf(RelativeDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF});
    assertThat(dest.toString()).isEqualTo("-0x80000000");

    // Now resolve it
    RelativeDestination relativeDest = (RelativeDestination) dest;
    relativeDest.resolve(0x7FFFFFFF);
    assertThat(dest.offset()).isEqualTo(-0x1);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
    assertThat(dest.toString()).isEqualTo("0xFFFFFFFF");
  }

  @Test
  public void testLabelDestination() {
    Destination dest = DestinationParser.get("lol");
    assertThat(dest).isInstanceOf(LabelDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
    assertThat(dest.toString()).isEqualTo("lol");

    // Now resolve it
    Map<String, Integer> labelMap = Map.ofEntries(
        entry("asd", 0x10),
        entry("lol", 0x20),
        entry("lmao", 0x30)
    );
    LabelDestination labelDest = (LabelDestination) dest;
    labelDest.resolve(labelMap);
    assertThat(labelDest.offset()).isEqualTo(0x20);
    assertThat(labelDest.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x20 });
    assertThat(labelDest.toString()).isEqualTo("0x20");
  }

  @Test
  public void testMissingLabelDestination() {
    Destination dest = DestinationParser.get("ok");
    assertThat(dest).isInstanceOf(LabelDestination.class);
    assertThat(dest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(dest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
    assertThat(dest.toString()).isEqualTo("ok");

    // Now resolve it
    Map<String, Integer> labelMap = Map.ofEntries(
        entry("asd", 0x10),
        entry("lol", 0x20),
        entry("lmao", 0x30)
    );
    LabelDestination labelDest = (LabelDestination) dest;
    assertThrows(IllegalStateException.class, () -> labelDest.resolve(labelMap));
    assertThrows(IllegalStateException.class, () -> labelDest.resolve(Collections.emptyMap()));
    assertThat(labelDest.offset()).isEqualTo(0xFFFFFFFF);
    assertThat(labelDest.bytes()).isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
    assertThat(labelDest.toString()).isEqualTo("ok");
  }
}
