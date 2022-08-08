package com.github.nicholasmoser.gnt4.ui;

import static com.github.nicholasmoser.gnt4.GNT4Characters.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class CostumeExtenderTest {

  @Test
  public void matchesVanillaCostumeExtensions() throws Exception {
    List<String> characters = List.of(SAKURA, HAKU, INO);
    String expectedHex = """
        3C160000 0000F490 7FFFFF20 7FFFFF24
        3B010000 0000ED50 00000007 7FFFFF24
        3B010000 0000ED50 00000002 7FFFFF24
        3B010000 0000ED50 00000009 7FFFFF24
        """;
    byte[] expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    byte[] bytes = CostumeExtender.getCodeBytes(characters, 3, 1, false);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 0000F490 7FFFFF20 7FFFFF24
        3B010000 0000ED80 00000007 7FFFFF24
        3B010000 0000ED80 00000002 7FFFFF24
        3B010000 0000ED80 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 1, false);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 0000F490 7FFFFF21 7FFFFF24
        3B010000 0000EE98 00000007 7FFFFF24
        3B010000 0000EE98 00000002 7FFFFF24
        3B010000 0000EE98 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 3, 2, false);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 0000F490 7FFFFF21 7FFFFF24
        3B010000 0000EEC8 00000007 7FFFFF24
        3B010000 0000EEC8 00000002 7FFFFF24
        3B010000 0000EEC8 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 2, false);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF20 7FFFFF24
        3B010000 00007A38 00000007 7FFFFF24
        3B010000 00007A38 00000002 7FFFFF24
        3B010000 00007A38 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 3, 1, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF20 7FFFFF24
        3B010000 00007AA8 00000007 7FFFFF24
        3B010000 00007AA8 00000002 7FFFFF24
        3B010000 00007AA8 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 1, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF21 7FFFFF24
        3B010000 00007B68 00000007 7FFFFF24
        3B010000 00007B68 00000002 7FFFFF24
        3B010000 00007B68 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 3, 2, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF21 7FFFFF24
        3B010000 00007BD8 00000007 7FFFFF24
        3B010000 00007BD8 00000002 7FFFFF24
        3B010000 00007BD8 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 2, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF22 7FFFFF24
        3B010000 00007C98 00000007 7FFFFF24
        3B010000 00007C98 00000002 7FFFFF24
        3B010000 00007C98 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 3, 3, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF22 7FFFFF24
        3B010000 00007D08 00000007 7FFFFF24
        3B010000 00007D08 00000002 7FFFFF24
        3B010000 00007D08 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 3, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF23 7FFFFF24
        3B010000 00007DC8 00000007 7FFFFF24
        3B010000 00007DC8 00000002 7FFFFF24
        3B010000 00007DC8 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 3, 4, true);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF23 7FFFFF24
        3B010000 00007E38 00000007 7FFFFF24
        3B010000 00007E38 00000002 7FFFFF24
        3B010000 00007E38 00000009 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 4, true);
    assertThat(bytes).isEqualTo(expectedBytes);
  }

  @Test
  public void extendAllCharacterCostumes() throws Exception {
    List<String> characters = INTERNAL_CHAR_ORDER.entrySet()
        .stream()
        .sorted(Entry.comparingByValue())
        .map(Entry::getKey)
        .collect(Collectors.toList());

    String expectedHex = """
        3C160000 0000F490 7FFFFF20 7FFFFF24
        3B010000 0000ED50 00000001 7FFFFF24
        3B010000 0000ED50 00000002 7FFFFF24
        3B010000 0000ED50 00000003 7FFFFF24
        3B010000 0000ED50 00000004 7FFFFF24
        3B010000 0000ED50 00000005 7FFFFF24
        3B010000 0000ED50 00000006 7FFFFF24
        3B010000 0000ED50 00000007 7FFFFF24
        3B010000 0000ED50 00000008 7FFFFF24
        3B010000 0000ED50 00000009 7FFFFF24
        3B010000 0000ED50 0000000A 7FFFFF24
        3B010000 0000ED50 0000000B 7FFFFF24
        3B010000 0000ED50 0000000C 7FFFFF24
        3B010000 0000ED50 0000000D 7FFFFF24
        3B010000 0000ED50 0000000E 7FFFFF24
        3B010000 0000ED50 0000000F 7FFFFF24
        3B010000 0000ED50 00000010 7FFFFF24
        3B010000 0000ED50 00000011 7FFFFF24
        3B010000 0000ED50 00000012 7FFFFF24
        3B010000 0000ED50 00000013 7FFFFF24
        3B010000 0000ED50 00000014 7FFFFF24
        3B010000 0000ED50 00000015 7FFFFF24
        3B010000 0000ED50 00000016 7FFFFF24
        3B010000 0000ED50 00000017 7FFFFF24
        3B010000 0000ED50 00000018 7FFFFF24
        3B010000 0000ED50 00000019 7FFFFF24
        3B010000 0000ED50 0000001A 7FFFFF24
        3B010000 0000ED50 0000001B 7FFFFF24
        3B010000 0000ED50 0000001C 7FFFFF24
        3B010000 0000ED50 0000001D 7FFFFF24
        3B010000 0000ED50 0000001E 7FFFFF24
        3B010000 0000ED50 0000001F 7FFFFF24
        3B010000 0000ED50 00000020 7FFFFF24
        3B010000 0000ED50 00000021 7FFFFF24
        3B010000 0000ED50 00000022 7FFFFF24
        3B010000 0000ED50 00000023 7FFFFF24
        3B010000 0000ED50 00000024 7FFFFF24
        3B010000 0000ED50 00000025 7FFFFF24
        3B010000 0000ED50 00000026 7FFFFF24
        3B010000 0000ED50 00000027 7FFFFF24
        3B010000 0000ED50 00000028 7FFFFF24
        3B010000 0000ED50 00000029 7FFFFF24
        """;
    byte[] expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    byte[] bytes = CostumeExtender.getCodeBytes(characters, 3, 1, false);
    assertThat(bytes).isEqualTo(expectedBytes);

    expectedHex = """
        3C160000 000140A0 7FFFFF23 7FFFFF24
        3B010000 00007E38 00000001 7FFFFF24
        3B010000 00007E38 00000002 7FFFFF24
        3B010000 00007E38 00000003 7FFFFF24
        3B010000 00007E38 00000004 7FFFFF24
        3B010000 00007E38 00000005 7FFFFF24
        3B010000 00007E38 00000006 7FFFFF24
        3B010000 00007E38 00000007 7FFFFF24
        3B010000 00007E38 00000008 7FFFFF24
        3B010000 00007E38 00000009 7FFFFF24
        3B010000 00007E38 0000000A 7FFFFF24
        3B010000 00007E38 0000000B 7FFFFF24
        3B010000 00007E38 0000000C 7FFFFF24
        3B010000 00007E38 0000000D 7FFFFF24
        3B010000 00007E38 0000000E 7FFFFF24
        3B010000 00007E38 0000000F 7FFFFF24
        3B010000 00007E38 00000010 7FFFFF24
        3B010000 00007E38 00000011 7FFFFF24
        3B010000 00007E38 00000012 7FFFFF24
        3B010000 00007E38 00000013 7FFFFF24
        3B010000 00007E38 00000014 7FFFFF24
        3B010000 00007E38 00000015 7FFFFF24
        3B010000 00007E38 00000016 7FFFFF24
        3B010000 00007E38 00000017 7FFFFF24
        3B010000 00007E38 00000018 7FFFFF24
        3B010000 00007E38 00000019 7FFFFF24
        3B010000 00007E38 0000001A 7FFFFF24
        3B010000 00007E38 0000001B 7FFFFF24
        3B010000 00007E38 0000001C 7FFFFF24
        3B010000 00007E38 0000001D 7FFFFF24
        3B010000 00007E38 0000001E 7FFFFF24
        3B010000 00007E38 0000001F 7FFFFF24
        3B010000 00007E38 00000020 7FFFFF24
        3B010000 00007E38 00000021 7FFFFF24
        3B010000 00007E38 00000022 7FFFFF24
        3B010000 00007E38 00000023 7FFFFF24
        3B010000 00007E38 00000024 7FFFFF24
        3B010000 00007E38 00000025 7FFFFF24
        3B010000 00007E38 00000026 7FFFFF24
        3B010000 00007E38 00000027 7FFFFF24
        3B010000 00007E38 00000028 7FFFFF24
        3B010000 00007E38 00000029 7FFFFF24
        """;
    expectedBytes = ByteUtils.hexTextToBytes(expectedHex);
    bytes = CostumeExtender.getCodeBytes(characters, 4, 4, true);
    assertThat(bytes).isEqualTo(expectedBytes);
  }

  @Test
  public void onlyValidCostumeAndPlayersAllowed() {
    List<String> characters = List.of(SAKURA, HAKU, INO);

    // Test invalid costumes
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 1, false));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 1, false));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 2, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 2, false));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 2, false));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 1, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 1, true));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 2, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 2, true));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 3, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 3, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 3, true));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, -1, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 0, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 2, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 5, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 6, 4, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 1000, 4, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MAX_VALUE, 4, true));

    // Test invalid players
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 3, Integer.MIN_VALUE, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, -1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 0, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 5, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 6, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 1000, false));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 3, Integer.MAX_VALUE, false));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 4, Integer.MIN_VALUE, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, -1, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 0, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 5, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 6, false));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 1000, false));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 4, Integer.MAX_VALUE, false));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, -1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 0, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 5, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 6, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 3, 1000, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 3, Integer.MAX_VALUE, true));

    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, Integer.MIN_VALUE, 2, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, -1, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 0, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 5, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 6, true));
    assertThrows(IOException.class, () -> CostumeExtender.getCodeBytes(characters, 4, 1000, true));
    assertThrows(IOException.class,
        () -> CostumeExtender.getCodeBytes(characters, 4, Integer.MAX_VALUE, true));
  }
}
