package com.github.nicholasmoser.gnt4.dol;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.gnt4.dol.CodeCaves.Location;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class DolHijackTest {


  /**
   * Tests a vanilla dol is not using old code hijacking (hijacking recording code).
   */
  @Test
  public void testVanillaNotUsingOldCodeHijacking() throws Exception {
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        Location.EXI2));
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        Location.TRK));
    assertFalse(DolHijack.isUsingCodeCave(uncompressedDir.resolve("sys/main.dol"),
        Location.RECORDING));
  }

  @Test
  public void test() throws Exception {
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\main.dol"), Location.RECORDING));
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\main.dol"), Location.TRK));
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\main.dol"), Location.EXI2));
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\before\\sys\\main.dol"), Location.RECORDING));
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\before\\sys\\main.dol"), Location.TRK));
    System.out.println(DolHijack.isUsingCodeCave(Paths.get("D:\\GNT\\Mods\\SCON4-1.4.321\\uncompressed\\sys\\before\\sys\\main.dol"), Location.EXI2));
  }
}
