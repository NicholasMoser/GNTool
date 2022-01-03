package com.github.nicholasmoser.gnt4.chr;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class ZabuzaPhantomSwordFixTest {
  @Test
  public void testSeqNotModified() throws Exception {
    Path seqPath = Prereqs.getUncompressedGNT4().resolve(Seqs.ZAB_0000);
    assertFalse(ZabuzaPhantomSwordFix.isUsingNewFix(seqPath));
    assertFalse(ZabuzaPhantomSwordFix.isUsingOldFix(seqPath));
  }
}
