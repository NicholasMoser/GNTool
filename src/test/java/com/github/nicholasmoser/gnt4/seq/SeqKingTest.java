package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.github.nicholasmoser.gnt4.seq.opcodes.BranchingOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.ByteUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SeqKingTest {

  // For unit testing: compare mode is off, verbose is off, and output files are deleted
  private static final boolean COMPARE_MODE = false;
  private static final boolean VERBOSE = false;
  private static final boolean PERMISSIVE = false;
  private static final boolean DELETE_FILE = false;

  @Test
  public void parseTitle() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_TITLE);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGame00() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.GAME_00);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseChrCmn() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CMN_1000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseLoading() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.LOADING);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseFCamera() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.F_CAMERA);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCamera00() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CAMERA_00);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCamera01() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CAMERA_01);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parsePlayer00() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.PLAYER_00);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCharSel() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CHARSEL);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAnko() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.ANK_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJirobo() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.BOU_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseChoji() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CHO_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseAkamaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.DOG_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGai() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.GAI_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseGaara() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.GAR_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHaku() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.HAK_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseWokeHinata() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.HI2_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseHinata() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.HIN_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIno() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.INO_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIruka() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.IRU_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseItachi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.ITA_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseJiraiya() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.JIR_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKabuto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KAB_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKakashi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KAK_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKankuro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KAN_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKarasu() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KAR_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKiba() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KIB_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKidomaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KID_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKimimaro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KIM_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKisame() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.KIS_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseLee() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.LOC_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMizuki() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.MIZ_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseKyuubiNaruto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.NA9_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNaruto() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.NAR_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseNeji() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.NEJ_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOboro() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.OBO_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseOrochimaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.ORO_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCS2Sasuke() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SA2_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakura() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SAK_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSarutobi() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SAR_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSasuke() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SAS_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShikamaru() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SIK_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseShino() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SIN_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSakon() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SKO_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseDokiDemon() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.TA2_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTayuya() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.TAY_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTemari() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.TEM_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTenten() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.TEN_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseTsunade() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.TSU_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseZabuza() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.ZAB_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseIruka0010() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.IRU_0010);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseCharSel4() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.CHARSEL_4);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseStg001_0000() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.STG_001_0000);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseStg001_0100() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.STG_001_0100);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMGal() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_GAL);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMNFile() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_NFILE);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMNSiki() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_NSIKI);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMSndplr() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_SNDPLR);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseMViewer() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_VIEWER);
    if (COMPARE_MODE) {
      compare(seq);
    } else {
      generate(seq);
    }
  }

  @Test
  public void parseSak1000() throws Exception {
    Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.SAK_1000);
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
      Optional<String> fileName = Seqs.getFileName(seq);
      SeqKing.generateHTML(seq, fileName.get(), outputPath, VERBOSE, PERMISSIVE);
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
      Optional<String> fileName = Seqs.getFileName(seq);
      SeqKing.generateHTML(seq, fileName.get(), outputPath, VERBOSE, PERMISSIVE);
    } finally {
      if (DELETE_FILE && outputPath != null) {
        Files.deleteIfExists(outputPath);
      }
    }
  }

  /**
   * Try and find permutations of bytes of an opcode.
   */
  @Test
  @Disabled("Enable only when you actually need to run this")
  public void findOpcodePermutations() throws Exception {
    byte group = 0x24;
    byte code = 0x17;
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> seqPaths = Files.walk(uncompressed)
        .filter(path -> path.toString().endsWith(".seq"))
        .collect(Collectors.toList());
    Set<String> permutations = new HashSet<>();
    for (Path path : seqPaths) {
      System.out.println(path);
      try {
        List<Opcode> opcodes = SeqKing.getOpcodes(path, Seqs.getFileName(path).get(), false, true);
        for (Opcode opcode : opcodes) {
          byte[] bytes = opcode.getBytes();
          if (bytes[0] == group && bytes[1] == code) {
            permutations.add(ByteUtils.bytesToHexStringWords(bytes));
          }
        }
      } catch (Exception e) {
        System.out.println("Failed to process...");
      }
    }
    System.out.println("\nPermutations:\n");
    permutations.stream()
        .sorted()
        .forEachOrdered(System.out::println);
  }

  /**
   * Helper function used to find commonality between character SEQ files using opcode 4700
   *
   * @param seq The seq to search.
   * @throws Exception If any Exception occurs.
   */
  private void findSpawnProjectileInfo(Path seq) throws Exception {
    Path outputPath = null;
    try {
      String output = seq.toString().replace(".seq", ".html");
      outputPath = Paths.get(output);
      Optional<String> fileName = Seqs.getFileName(seq);
      List<Opcode> opcodes = SeqKing.getOpcodes(seq, fileName.get(), VERBOSE, PERMISSIVE);
      for (Opcode opcode : opcodes) {
        byte[] bytes = opcode.getBytes();
        if (bytes[0] == 0x47 && bytes[1] == 0x00) {
          if (bytes[19] != 0) {
            System.out.println(seq + " " + opcode);
          }
        }
      }
    } finally {
      if (DELETE_FILE && outputPath != null) {
        Files.deleteIfExists(outputPath);
      }
    }
  }

  /**
   * Find branches in seq files that do not branch to valid opcodes.
   *
   * @param seq The seq to search.
   * @throws Exception If any Exception occurs.
   */
  private void findBranchMatch(Path seq) throws Exception {
    Path outputPath = null;
    try {
      String output = seq.toString().replace(".seq", ".html");
      outputPath = Paths.get(output);
      Optional<String> fileName = Seqs.getFileName(seq);
      List<Opcode> opcodes = SeqKing.getOpcodes(seq, fileName.get(), VERBOSE, PERMISSIVE);
      for (Opcode opcode : opcodes) {
        if (opcode instanceof BranchingOpcode bo) {
          int dest = bo.getDestination().offset();
          boolean match = opcodes.stream().anyMatch(o -> o.getOffset() == dest);
          if (!match) {
            System.out.printf("###### NO MATCH FOR BRANCH AT OFFSET 0x%X OF %s\n", opcode.getOffset(), fileName.get());
          }
        }
      }
    } finally {
      if (DELETE_FILE && outputPath != null) {
        Files.deleteIfExists(outputPath);
      }
    }
  }
}
