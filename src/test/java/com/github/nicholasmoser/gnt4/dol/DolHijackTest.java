package com.github.nicholasmoser.gnt4.dol;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class DolHijackTest {


  /**
   * Tests a vanilla dol is not using old code hijacking (hijacking recording code).
   */
  @Test
  public void testVanillaNotUsingOldCodeHijacking() throws Exception {
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    assertFalse(DolHijack.isUsingOldCodeHihacking(uncompressedDir.resolve("sys/main.dol")));
  }
}
