package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqKingTest {

  // For unit testing: compare mode is off, verbose is off, and output files are deleted
  private static final boolean COMPARE_MODE = true;
  private static final boolean VERBOSE = false;
  private static final boolean DELETE_FILE = false;

  @Test
  public void parseCharSel() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/maki/char_sel.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAnko() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/ank/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJirobo() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/bou/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseChoji() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/cho/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAkamaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/dog/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGai() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/gai/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGaara() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/gar/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHaku() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/hak/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseWokeHinata() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/hi2/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHinata() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/hin/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIno() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/ino/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIruka() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/iru/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseItachi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/ita/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJiraiya() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/jir/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKabuto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kab/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKakashi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kak/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKankuro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kan/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKarasu() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kar/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKiba() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kib/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKidomaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kid/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKimimaro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kim/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKisame() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/kis/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseLee() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/loc/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMizuki() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/miz/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKyuubiNaruto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/na9/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNaruto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/nar/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNeji() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/nej/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOboro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/obo/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOrochimaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/oro/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCS2Sasuke() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sa2/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakura() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sak/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSarutobi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sar/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSasuke() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sas/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShikamaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sik/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShino() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sin/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakon() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/sko/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseDokiDemon() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/ta2/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTayuya() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/tay/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTemari() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/tem/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTenten() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/ten/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTsunade() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/tsu/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseZabuza() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/zab/0000.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIruka0010() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve("files/chr/iru/0010.seq");
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  private void compare(Path seq) throws Exception {
    Path outputPath = null;
    try {
      String output = seq.toString().replace(".seq", "_test.html");
      outputPath = Paths.get(output);
      SeqKing.generateHTML(seq, outputPath, VERBOSE);
      String expected = seq.toString().replace(".seq", ".html");
      Path expectedPath = Paths.get(expected);
      byte[] expectedBytes = Files.readAllBytes(expectedPath);
      byte[] actualBytes = Files.readAllBytes(outputPath);
      assertArrayEquals(expectedBytes, actualBytes);
    } finally {
      if (DELETE_FILE && outputPath != null) {
        Files.deleteIfExists(outputPath);
      }
    }
  }

  private void generate(Path seq) throws Exception {
    Path outputPath = null;
    try {
      String output = seq.toString().replace(".seq", ".html");
      outputPath = Paths.get(output);
      SeqKing.generateHTML(seq, outputPath, VERBOSE);
    } finally {
      if (DELETE_FILE && outputPath != null) {
        Files.deleteIfExists(outputPath);
      }
    }
  }
}
