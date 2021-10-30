package com.github.nicholasmoser.audio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class MustXExtractTest {
  @Test
  public void testExtractAndRepack() throws Exception {
    // Inputs
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path inputSdi = uncompressed.resolve("files/chr/ank/3000.sdi");
    Path inputSam = uncompressed.resolve("files/chr/ank/3000.sam");

    // Outputs
    Path tempDir = FileUtils.getTempDirectory();
    Path outputDir = tempDir.resolve(UUID.randomUUID().toString());
    Path outputSdi = tempDir.resolve(UUID.randomUUID() + ".sdi");
    Path outputSam = tempDir.resolve(UUID.randomUUID() + ".sam");
    Files.createDirectories(outputDir);

    try {
      MusyXExtract.extract_samples(inputSdi, inputSam, outputDir);
      MusyXExtract.pack_samples(outputDir, outputSdi, outputSam);
      long expectedSdiLength = Files.size(inputSdi);
      long actualSdiLength = Files.size(outputSdi);
      assertEquals(expectedSdiLength, actualSdiLength);
      long expectedSamLength = Files.size(inputSam);
      long actualSamLength = Files.size(outputSam);
      assertEquals(expectedSamLength, actualSamLength, 1024);
    } finally {
      Files.deleteIfExists(outputSam);
      Files.deleteIfExists(outputSdi);
      MoreFiles.deleteRecursively(outputDir, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  @Test
  public void testGetIdRegex() throws Exception {
    Path dspPath = Paths.get("00000 (0x005D).dsp");
    int actual = MusyXExtract.getId(dspPath);
    int expected = 0x5D;
    assertEquals(actual, expected);
  }

  @Test
  public void testGetIdRegexZero() throws Exception {
    Path dspPath = Paths.get("00000 (0x0000).dsp");
    int actual = MusyXExtract.getId(dspPath);
    int expected = 0;
    assertEquals(actual, expected);
  }

  @Test()
  public void testGetIdRegexFail() {
    Path dspPath = Paths.get("0x005D.dsp");
    assertThrows(IOException.class, () -> MusyXExtract.getId(dspPath));
  }
}
