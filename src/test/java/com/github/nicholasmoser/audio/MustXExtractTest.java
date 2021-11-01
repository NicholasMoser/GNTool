package com.github.nicholasmoser.audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the ported functionality of MusyXExtract.
 */
public class MustXExtractTest {

  /**
   * Streams each .sam and .sdi file from GNT4 as arguments.
   *
   * @return Each .sam and .sdi file from GNT4 as arguments.
   * @throws IOException If GNT4 cannot be parsed.
   */
  public static Stream<Arguments> samSdiPathProvider() throws IOException {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> samFiles = Files.walk(uncompressed).filter(MustXExtractTest::isSamWithSdi)
        .collect(Collectors.toList());
    List<Arguments> args = new ArrayList<>();
    for (Path samFile : samFiles) {
      Path sdiFile = Paths.get(samFile.toString().replace(".sam", ".sdi"));
      args.add(arguments(sdiFile, samFile));
    }
    return args.stream();
  }

  /**
   * If the given file is a .sam file with a correlated .sdi file.
   *
   * @param path The file to check if it is a .sam file.
   * @return If the given file is a .sam file with a correlated .sdi file.
   */
  private static boolean isSamWithSdi(Path path) {
    if (path.toString().endsWith(".sam")) {
      Path sdi = Paths.get(path.toString().replace(".sam", ".sdi"));
      if (!Files.exists(sdi)) {
        throw new IllegalStateException("Found .sam without .sdi: " + path);
      }
      return true;
    }
    return false;
  }

  /**
   * Test extracting and repacking each .sam and .sdi file in GNT4.
   *
   * @param inputSdi The input .sdi file.
   * @param inputSam The input .sam file.
   * @throws Exception If any exception occurs.
   */
  @ParameterizedTest(name="#{index} - Test with Argument={0},{1}")
  @MethodSource("samSdiPathProvider")
  public void testExtractAndRepack(Path inputSdi, Path inputSam) throws Exception {
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
