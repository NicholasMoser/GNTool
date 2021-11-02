package com.github.nicholasmoser.gnt4.chr;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class KisamePhantomSwordFixTest {
  @Test
  public void testSeqNotModified() throws Exception {
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    assertFalse(KisamePhantomSwordFix.isSeqModified(uncompressedDir));
  }
}
