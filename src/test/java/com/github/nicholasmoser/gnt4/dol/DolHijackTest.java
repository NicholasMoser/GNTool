package com.github.nicholasmoser.gnt4.dol;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.gnt4.dol.CodeCaves.CodeCave;
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
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        CodeCave.EXI2));
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        CodeCave.TRK));
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        CodeCave.RECORDING));
  }
}
