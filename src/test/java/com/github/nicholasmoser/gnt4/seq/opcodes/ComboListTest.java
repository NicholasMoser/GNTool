package com.github.nicholasmoser.gnt4.seq.opcodes;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class ComboListTest {
  @Test
  public void testReadKisameCombos() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KIS_0000);
    byte[] bytes = Files.readAllBytes(seq);
    ByteStream bs = new ByteStream(bytes);
    bs.seek(0x25CE0);
    if (!SeqHelper.isComboList(bs)) {
      throw new IOException("Not at combo list");
    }
    Opcode opcode = SeqHelper.readComboList(bs);
    ComboList comboList = (ComboList) opcode;
    String expected = """
        25CE0 | combo_list (0x13 total) {00000013}
        25CE4 | combo 00BBBB {98419265 82500000 30304242 42420000}
        25CF4 | combo 00BBBA {98419265 82510000 30304242 42410000}
        25D04 | combo 00BBAAA {98419265 82520000 30304242 41414100}
        25D14 | combo 00BBAAB {98419265 82530000 30304242 41414200}
        25D24 | combo 00BABA {98419265 82540000 30304241 42410000}
        25D34 | combo 006BB {98419265 82550000 30303642 42000000}
        25D44 | combo 006BA {98419265 82560000 30303642 41000000}
        25D54 | combo 004BBB {98419265 82570000 30303442 42420000}
        25D64 | combo 004BBA {98419265 82580000 30303442 42410000}
        25D74 | combo 004BAAA {98419265 8250824F 00000000 30303442 41414100}
        25D88 | combo 004BAAB {98419265 82508250 00000000 30303442 41414200}
        25D9C | combo 002BA {98419265 82508251 00000000 30303242 41000000}
        25DB0 | combo 008BAA {98419265 82508252 00000000 30303842 41410000}
        25DC4 | combo 008BAB {98419265 82508253 00000000 30303842 41420000}
        25DD8 | combo 00AAAA {98419265 82508254 00000000 30304141 41410000}
        25DEC | combo 00AAAB {98419265 82508255 00000000 30304141 41420000}
        25E00 | combo 00ABB {98419265 82508256 00000000 30304142 42000000}
        25E14 | combo 00ABA {98419265 82508257 00000000 30304142 41000000}
        25E28 | combo 002ABA {98419265 82508258 00000000 30303241 42410000 FFFFFFFF}
        """.trim();
    String expectedBytesHex = """
        00 00 00 13 98 41 92 65 82 50 00 00 30 30 42 42
        42 42 00 00 98 41 92 65 82 51 00 00 30 30 42 42
        42 41 00 00 98 41 92 65 82 52 00 00 30 30 42 42
        41 41 41 00 98 41 92 65 82 53 00 00 30 30 42 42
        41 41 42 00 98 41 92 65 82 54 00 00 30 30 42 41
        42 41 00 00 98 41 92 65 82 55 00 00 30 30 36 42
        42 00 00 00 98 41 92 65 82 56 00 00 30 30 36 42
        41 00 00 00 98 41 92 65 82 57 00 00 30 30 34 42
        42 42 00 00 98 41 92 65 82 58 00 00 30 30 34 42
        42 41 00 00 98 41 92 65 82 50 82 4F 00 00 00 00
        30 30 34 42 41 41 41 00 98 41 92 65 82 50 82 50
        00 00 00 00 30 30 34 42 41 41 42 00 98 41 92 65
        82 50 82 51 00 00 00 00 30 30 32 42 41 00 00 00
        98 41 92 65 82 50 82 52 00 00 00 00 30 30 38 42
        41 41 00 00 98 41 92 65 82 50 82 53 00 00 00 00
        30 30 38 42 41 42 00 00 98 41 92 65 82 50 82 54
        00 00 00 00 30 30 41 41 41 41 00 00 98 41 92 65
        82 50 82 55 00 00 00 00 30 30 41 41 41 42 00 00
        98 41 92 65 82 50 82 56 00 00 00 00 30 30 41 42
        42 00 00 00 98 41 92 65 82 50 82 57 00 00 00 00
        30 30 41 42 41 00 00 00 98 41 92 65 82 50 82 58
        00 00 00 00 30 30 32 41 42 41 00 00 FF FF FF FF
        """;
    String expectedHTML = "<div><div id=\"25CE0\">25CE0 | combo_list (0x13 total) <span class=\"g\">00000013</span></div><div id=\"25CE4\">25CE4 | combo 00BBBB <span class=\"g\">98419265 82500000 30304242 42420000</span></div><div id=\"25CF4\">25CF4 | combo 00BBBA <span class=\"g\">98419265 82510000 30304242 42410000</span></div><div id=\"25D04\">25D04 | combo 00BBAAA <span class=\"g\">98419265 82520000 30304242 41414100</span></div><div id=\"25D14\">25D14 | combo 00BBAAB <span class=\"g\">98419265 82530000 30304242 41414200</span></div><div id=\"25D24\">25D24 | combo 00BABA <span class=\"g\">98419265 82540000 30304241 42410000</span></div><div id=\"25D34\">25D34 | combo 006BB <span class=\"g\">98419265 82550000 30303642 42000000</span></div><div id=\"25D44\">25D44 | combo 006BA <span class=\"g\">98419265 82560000 30303642 41000000</span></div><div id=\"25D54\">25D54 | combo 004BBB <span class=\"g\">98419265 82570000 30303442 42420000</span></div><div id=\"25D64\">25D64 | combo 004BBA <span class=\"g\">98419265 82580000 30303442 42410000</span></div><div id=\"25D74\">25D74 | combo 004BAAA <span class=\"g\">98419265 8250824F 00000000 30303442 41414100</span></div><div id=\"25D88\">25D88 | combo 004BAAB <span class=\"g\">98419265 82508250 00000000 30303442 41414200</span></div><div id=\"25D9C\">25D9C | combo 002BA <span class=\"g\">98419265 82508251 00000000 30303242 41000000</span></div><div id=\"25DB0\">25DB0 | combo 008BAA <span class=\"g\">98419265 82508252 00000000 30303842 41410000</span></div><div id=\"25DC4\">25DC4 | combo 008BAB <span class=\"g\">98419265 82508253 00000000 30303842 41420000</span></div><div id=\"25DD8\">25DD8 | combo 00AAAA <span class=\"g\">98419265 82508254 00000000 30304141 41410000</span></div><div id=\"25DEC\">25DEC | combo 00AAAB <span class=\"g\">98419265 82508255 00000000 30304141 41420000</span></div><div id=\"25E00\">25E00 | combo 00ABB <span class=\"g\">98419265 82508256 00000000 30304142 42000000</span></div><div id=\"25E14\">25E14 | combo 00ABA <span class=\"g\">98419265 82508257 00000000 30304142 41000000</span></div><div id=\"25E28\">25E28 | combo 002ABA <span class=\"g\">98419265 82508258 00000000 30303241 42410000 FFFFFFFF</span></div></div>";
    byte[] expectedBytes = ByteUtils.hexTextToBytes(expectedBytesHex);
    assertThat(comboList.toString()).isEqualTo(expected);
    assertThat(comboList.toAssembly()).isEqualTo(expected);
    assertThat(comboList.getBytes()).isEqualTo(expectedBytes);
    assertThat(comboList.toHTML().toString()).isEqualTo(expectedHTML);

    comboList.removeCombo(5);
    expected = """
        25CE0 | combo_list (0x12 total) {00000012}
        25CE4 | combo 00BBBB {98419265 82500000 30304242 42420000}
        25CF4 | combo 00BBBA {98419265 82510000 30304242 42410000}
        25D04 | combo 00BBAAA {98419265 82520000 30304242 41414100}
        25D14 | combo 00BBAAB {98419265 82530000 30304242 41414200}
        25D24 | combo 00BABA {98419265 82540000 30304241 42410000}
        25D44 | combo 006BA {98419265 82560000 30303642 41000000}
        25D54 | combo 004BBB {98419265 82570000 30303442 42420000}
        25D64 | combo 004BBA {98419265 82580000 30303442 42410000}
        25D74 | combo 004BAAA {98419265 8250824F 00000000 30303442 41414100}
        25D88 | combo 004BAAB {98419265 82508250 00000000 30303442 41414200}
        25D9C | combo 002BA {98419265 82508251 00000000 30303242 41000000}
        25DB0 | combo 008BAA {98419265 82508252 00000000 30303842 41410000}
        25DC4 | combo 008BAB {98419265 82508253 00000000 30303842 41420000}
        25DD8 | combo 00AAAA {98419265 82508254 00000000 30304141 41410000}
        25DEC | combo 00AAAB {98419265 82508255 00000000 30304141 41420000}
        25E00 | combo 00ABB {98419265 82508256 00000000 30304142 42000000}
        25E14 | combo 00ABA {98419265 82508257 00000000 30304142 41000000}
        25E28 | combo 002ABA {98419265 82508258 00000000 30303241 42410000 FFFFFFFF}
        """.trim();
    assertThat(comboList.toString()).isEqualTo(expected);
  }
}
