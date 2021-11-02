package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.GNT4Audio;
import com.github.nicholasmoser.gnt4.GNT4AudioTest;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class SeqsTest {
  /**
   * Validate that the list of GNT4 seq files is accurate.
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void validateGNT4SeqFiles() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> seqPaths = Files.walk(uncompressed)
        .filter(SeqsTest::isSeq)
        .collect(Collectors.toList());
    assertEquals(seqPaths.size(), Seqs.ALL.size());
    for (String seqPath : Seqs.ALL) {
      assertTrue(Files.isRegularFile(uncompressed.resolve(seqPath)));
    }
  }

  private static boolean isSeq(Path path) {
    return path.toString().endsWith(".seq");
  }
}
