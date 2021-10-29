package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqKingTest {

  private static final boolean COMPARE_MODE = false;
  private static final boolean VERBOSE = false;

  @Test
  public void parseCharSel() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/maki/char_sel.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAnko() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/ank/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJirobo() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/bou/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseChoji() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/cho/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAkamaru() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/dog/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGai() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/gai/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGaara() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/gar/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHaku() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/hak/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseWokeHinata() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/hi2/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHinata() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/hin/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIno() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/ino/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIruka() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/iru/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseItachi() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/ita/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJiraiya() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/jir/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKabuto() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kab/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKakashi() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kak/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKankuro() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kan/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKarasu() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kar/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKiba() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kib/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKidomaru() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kid/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKimimaro() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kim/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKisame() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/kis/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseLee() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/loc/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMizuki() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/miz/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKyuubiNaruto() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/na9/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNaruto() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/nar/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNeji() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/nej/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOboro() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/obo/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOrochimaru() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/oro/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCS2Sasuke() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sa2/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakura() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sak/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSarutobi() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sar/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSasuke() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sas/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShikamaru() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sik/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShino() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sin/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakon() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sko/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseDokiDemon() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/ta2/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTayuya() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/tay/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTemari() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/tem/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTenten() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/ten/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTsunade() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/tsu/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseZabuza() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/zab/0000.seq";
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  private void compare(String seq) throws Exception {
    String output = seq.replace(".seq", "_test.html");
    Path seqPath = Paths.get(seq);
    Path outputPath = Paths.get(output);
    SeqKing.generateHTML(seqPath, outputPath, VERBOSE);
    String expected = seq.replace(".seq", ".html");
    Path expectedPath = Paths.get(expected);
    byte[] expectedBytes = Files.readAllBytes(expectedPath);
    byte[] actualBytes = Files.readAllBytes(outputPath);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  private void generate(String seq) throws Exception {
    String output = seq.replace(".seq", ".html");
    Path seqPath = Paths.get(seq);
    Path outputPath = Paths.get(output);
    SeqKing.generateHTML(seqPath, outputPath, VERBOSE);
  }
}
