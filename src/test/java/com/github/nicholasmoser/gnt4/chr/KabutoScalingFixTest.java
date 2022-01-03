package com.github.nicholasmoser.gnt4.chr;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class KabutoScalingFixTest {
  @Test
  public void testSeqNotModified() throws Exception {
    Path seqPath = Prereqs.getUncompressedGNT4().resolve(Seqs.KAB_0000);
    assertFalse(KabutoScalingFix.isUsingNewFix(seqPath));
    assertFalse(KabutoScalingFix.isUsingOldFix(seqPath));
  }
}
