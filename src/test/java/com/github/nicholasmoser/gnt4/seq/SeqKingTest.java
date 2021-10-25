package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqKingTest {

  private static final boolean COMPARE_MODE = false;

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
  public void parseIruka() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/iru/0000.seq";
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
  public void parseMizuki() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/miz/0000.seq";
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
  public void parseSasuke() throws Exception {
    String seq = "D:/GNT/aaa/uncompressed/files/chr/sas/0000.seq";
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
    SeqKing.generate(seqPath, outputPath);
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
    SeqKing.generate(seqPath, outputPath);
  }
}
