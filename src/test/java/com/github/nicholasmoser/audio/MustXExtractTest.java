package com.github.nicholasmoser.audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MustXExtractTest {
  @Test
  @Disabled("Cannot test without using actual game files. Left in for white box testing.")
  public void testExtract() throws Exception {
    Path sdiPath = Paths.get("");
    Path samPath = Paths.get("");
    Path output = Paths.get("");
    MusyXExtract.extract_samples(sdiPath, samPath, output);
  }

  @Test
  @Disabled("Cannot test without using actual game files. Left in for white box testing.")
  public void testPack() throws Exception {
    Path input = Paths.get("");
    Path sdiPath = Paths.get("");
    Path samPath = Paths.get("");
    MusyXExtract.pack_samples(input, sdiPath, samPath);
  }

  @Test
  public void testGetIdRegex() throws Exception {
    Path dspPath = Paths.get("C:\\00000 (0x005D).dsp");
    int actual = MusyXExtract.getId(dspPath);
    int expected = 93;
    assertEquals(actual, expected);
  }

  @Test
  public void testGetIdRegexZero() throws Exception {
    Path dspPath = Paths.get("C:\\00000 (0x0000).dsp");
    int actual = MusyXExtract.getId(dspPath);
    int expected = 0;
    assertEquals(actual, expected);
  }

  @Test()
  public void testGetIdRegexFail() {
    Path dspPath = Paths.get("C:\\0x005D.dsp");
    assertThrows(IOException.class, () -> MusyXExtract.getId(dspPath));
  }
}
