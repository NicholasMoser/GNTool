package com.github.nicholasmoser.gnt4.ui;

import static com.github.nicholasmoser.gnt4.ui.EyeExtender.*;
import static com.github.nicholasmoser.utils.ByteUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class EyeExtenderTest {

  @Test
  public void canExtendNoCharacters() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
        C20AB634 00000005
        38E00000 4800001C
        38E00001 48000014
        38E00002 4800000C
        38E00003 48000004
        8061000C 00000000
        """;
    Map<String, String> costume2 = new HashMap<>();
    Map<String, String> costume3 = new HashMap<>();
    Map<String, String> costume4 = new HashMap<>();
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }

  @Test
  public void canExtendOnlyCostumeTwo() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
        C20AB634 0000000A
        2C070001 40820024
        2C050011 4182002C
        2C050009 4182002C
        2C05000A 41820024
        2C050018 4182000C
        38E00000 4800001C
        38E00001 48000014
        38E00002 4800000C
        38E00003 48000004
        8061000C 00000000
        """;
    Map<String, String> costume2 = new HashMap<>();
    Map<String, String> costume3 = new HashMap<>();
    Map<String, String> costume4 = new HashMap<>();
    costume2.put(GNT4Characters.SHIKAMARU, FOURTH);
    costume2.put(GNT4Characters.INO, FOURTH);
    costume2.put(GNT4Characters.CHOJI, SECOND);
    costume2.put(GNT4Characters.AKAMARU, THIRD);
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }

  @Test
  public void canExtendOnlyCostumeThree() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
        C20AB634 0000000A
        2C070002 40820024
        2C050013 4182002C
        2C050024 41820024
        2C05001C 4182001C
        2C050027 41820014
        38E00000 4800001C
        38E00001 48000014
        38E00002 4800000C
        38E00003 48000004
        8061000C 00000000
        """;
    Map<String, String> costume2 = new HashMap<>();
    Map<String, String> costume3 = new HashMap<>();
    Map<String, String> costume4 = new HashMap<>();
    costume3.put(GNT4Characters.ITACHI, THIRD);
    costume3.put(GNT4Characters.KISAME, THIRD);
    costume3.put(GNT4Characters.OROCHIMARU, THIRD);
    costume3.put(GNT4Characters.KABUTO, THIRD);
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }

  @Test
  public void canExtendOnlyCostumeFour() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
        C20AB634 0000000A
        2C070003 40820024
        2C050005 4182002C
        2C050006 4182002C
        2C050015 4182001C
        2C050002 4182001C
        38E00000 4800001C
        38E00001 48000014
        38E00002 4800000C
        38E00003 48000004
        8061000C 00000000
        """;
    Map<String, String> costume2 = new HashMap<>();
    Map<String, String> costume3 = new HashMap<>();
    Map<String, String> costume4 = new HashMap<>();
    costume4.put(GNT4Characters.HAKU, FOURTH);
    costume4.put(GNT4Characters.ZABUZA, FOURTH);
    costume4.put(GNT4Characters.IRUKA, THIRD);
    costume4.put(GNT4Characters.MIZUKI, THIRD);
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }

  @Test
  public void canExtendCostumesTwoThreeAndFour() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
        C20AB634 00000014
        2C070001 40820024
        2C050007 4182007C
        2C050008 4182006C
        2C050001 41820064
        2C050003 4182006C
        2C070002 40820024
        2C05000E 4182004C
        2C05001A 41820044
        2C05000F 4182004C
        2C050012 4182003C
        2C070003 40820024
        2C050021 41820034
        2C050020 4182001C
        2C050023 4182001C
        2C050022 4182000C
        38E00000 4800001C
        38E00001 48000014
        38E00002 4800000C
        38E00003 48000004
        8061000C 00000000
        """;
    Map<String, String> costume2 = new HashMap<>();
    Map<String, String> costume3 = new HashMap<>();
    Map<String, String> costume4 = new HashMap<>();
    costume2.put(GNT4Characters.NARUTO, SECOND);
    costume2.put(GNT4Characters.SASUKE, SECOND);
    costume2.put(GNT4Characters.SAKURA, THIRD);
    costume2.put(GNT4Characters.KAKASHI, FOURTH);
    costume3.put(GNT4Characters.KANKURO, SECOND);
    costume3.put(GNT4Characters.TEMARI, SECOND);
    costume3.put(GNT4Characters.GAARA, THIRD);
    costume3.put(GNT4Characters.KARASU, FOURTH);
    costume4.put(GNT4Characters.JIROBO, SECOND);
    costume4.put(GNT4Characters.SAKON, SECOND);
    costume4.put(GNT4Characters.TAYUYA, THIRD);
    costume4.put(GNT4Characters.KIDOMARU, FOURTH);
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }

  @Test
  public void canExtendAllCharactersAllCostumes() throws Exception {
    String expectedNop1GeckoCode = "040AB628 60000000";
    String expectedNop2GeckoCode = "040AB630 60000000";
    String expectedMainGeckoCode = """
           C20AB634 00000083
           2C070001 4082014C
           2C050006 418203EC
           2C05001D 418203E4
           2C050019 418203DC
           2C05001A 418203D4
           2C050029 418203CC
           2C050023 418203C4
           2C05001B 418203BC
           2C05000A 418203B4
           2C050025 418203AC
           2C050001 418203A4
           2C05001E 4182039C
           2C050007 41820394
           2C050022 4182038C
           2C050013 41820384
           2C050014 4182037C
           2C05000B 41820374
           2C050026 4182036C
           2C050008 41820364
           2C050015 4182035C
           2C05000D 41820354
           2C050004 4182034C
           2C050024 41820344
           2C05001F 4182033C
           2C050021 41820334
           2C050010 4182032C
           2C05000F 41820324
           2C05000E 4182031C
           2C050003 41820314
           2C050027 4182030C
           2C050020 41820304
           2C050017 418202FC
           2C05001C 418202F4
           2C050005 418202EC
           2C050009 418202E4
           2C050028 418202DC
           2C05000C 418202D4
           2C050002 418202CC
           2C050012 418202C4
           2C050018 418202BC
           2C050016 418202B4
           2C050011 418202AC
           2C070002 4082014C
           2C050006 4182029C
           2C05001D 41820294
           2C050019 4182028C
           2C05001A 41820284
           2C050029 4182027C
           2C050023 41820274
           2C05001B 4182026C
           2C05000A 41820264
           2C050025 4182025C
           2C050001 41820254
           2C05001E 4182024C
           2C050007 41820244
           2C050022 4182023C
           2C050013 41820234
           2C050014 4182022C
           2C05000B 41820224
           2C050026 4182021C
           2C050008 41820214
           2C050015 4182020C
           2C05000D 41820204
           2C050004 418201FC
           2C050024 418201F4
           2C05001F 418201EC
           2C050021 418201E4
           2C050010 418201DC
           2C05000F 418201D4
           2C05000E 418201CC
           2C050003 418201C4
           2C050027 418201BC
           2C050020 418201B4
           2C050017 418201AC
           2C05001C 418201A4
           2C050005 4182019C
           2C050009 41820194
           2C050028 4182018C
           2C05000C 41820184
           2C050002 4182017C
           2C050012 41820174
           2C050018 4182016C
           2C050016 41820164
           2C050011 4182015C
           2C070003 4082014C
           2C050006 4182014C
           2C05001D 41820144
           2C050019 4182013C
           2C05001A 41820134
           2C050029 4182012C
           2C050023 41820124
           2C05001B 4182011C
           2C05000A 41820114
           2C050025 4182010C
           2C050001 41820104
           2C05001E 418200FC
           2C050007 418200F4
           2C050022 418200EC
           2C050013 418200E4
           2C050014 418200DC
           2C05000B 418200D4
           2C050026 418200CC
           2C050008 418200C4
           2C050015 418200BC
           2C05000D 418200B4
           2C050004 418200AC
           2C050024 418200A4
           2C05001F 4182009C
           2C050021 41820094
           2C050010 4182008C
           2C05000F 41820084
           2C05000E 4182007C
           2C050003 41820074
           2C050027 4182006C
           2C050020 41820064
           2C050017 4182005C
           2C05001C 41820054
           2C050005 4182004C
           2C050009 41820044
           2C050028 4182003C
           2C05000C 41820034
           2C050002 4182002C
           2C050012 41820024
           2C050018 4182001C
           2C050016 41820014
           2C050011 4182000C
           38E00000 4800001C
           38E00001 48000014
           38E00002 4800000C
           38E00003 48000004
           8061000C 00000000
        """;
    Map<String, String> costume2 = new LinkedHashMap<>();
    Map<String, String> costume3 = new LinkedHashMap<>();
    Map<String, String> costume4 = new LinkedHashMap<>();
    for (String character : GNT4Characters.CHARACTERS) {
      costume2.put(character, SECOND);
      costume3.put(character, SECOND);
      costume4.put(character, SECOND);
    }
    EyeSettings eyes = new EyeSettings(costume2, costume3, costume4);
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    GeckoCode code = codes.get(0);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedMainGeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(MAIN_TARGET_ADDR);
    code = codes.get(1);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop1GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP1_TARGET_ADDR);
    code = codes.get(2);
    assertThat(code.getCodeBytes()).isEqualTo(hexTextToBytes(expectedNop2GeckoCode));
    assertThat(code.getTargetAddress()).isEqualTo(NOP2_TARGET_ADDR);

    // Convert codes back to eye settings and assert equal
    GeckoCodeGroup codeGroup = new GeckoCodeGroup(CODE_NAME, codes);
    Optional<EyeSettings> actual = EyeExtender.getEyeSettings(List.of(codeGroup));
    assertThat(actual.isPresent()).isTrue();
    EyeSettings actualEyes = actual.get();
    assertThat(actualEyes).isEqualTo(eyes);
  }
}
