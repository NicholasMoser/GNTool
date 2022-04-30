package com.github.nicholasmoser.audio;

import static com.github.nicholasmoser.utils.TestUtil.assertDirectoriesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
import java.util.Arrays;
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
   * Test extracting and repacking each .sam and .sdi file in GNT4. Then extract it again and make
   * sure the files are as expected.
   *
   * @param inputSdi The input .sdi file.
   * @param inputSam The input .sam file.
   * @throws Exception If any exception occurs.
   */
  @ParameterizedTest(name = "#{index} - Test with Argument={0},{1}")
  @MethodSource("samSdiPathProvider")
  public void testExtractAndRepack(Path inputSdi, Path inputSam) throws Exception {
    // Outputs
    Path tempDir = FileUtils.getTempDirectory();
    Path outputDir = tempDir.resolve(UUID.randomUUID().toString());
    Path outputDir2 = tempDir.resolve(UUID.randomUUID().toString());
    Path outputSdi = tempDir.resolve(UUID.randomUUID() + ".sdi");
    Path outputSam = tempDir.resolve(UUID.randomUUID() + ".sam");
    Files.createDirectories(outputDir);
    Files.createDirectories(outputDir2);

    try {
      // Create dsp files from sam and sdi
      MusyXExtract.extract_samples(inputSdi, inputSam, outputDir);
      // Create new sam and sdi from dsp files
      MusyXExtract.pack_samples(outputDir, outputSdi, outputSam);
      // Create new dsp files from new sam and sdi files
      MusyXExtract.extract_samples(outputSdi, outputSam, outputDir2);

      if (areStageLoopingSounds(inputSdi)) {
        // .sdi files with looping will have different looping lengths due to loss of accuracy of
        // converting back and forth between .sdi and .dsp which converts back and forth between
        // samples and nibbles.
        assertEquals(Files.size(inputSdi), Files.size(outputSdi));
        long delta = (long) (Files.size(inputSam) * 0.03);
        assertEquals(Files.size(inputSam), Files.size(outputSam), delta);
        assertEquals(Files.list(outputDir).count(), Files.list(outputDir2).count());
      } else {
        int mismatch = (int) Files.mismatch(inputSdi, outputSdi);
        System.out.println("mismatch at offset " + mismatch);
        byte[] inputSdiBytes = Files.readAllBytes(inputSdi);
        System.out.println(inputSdi);
        System.out.println(Arrays.toString(Arrays.copyOfRange(inputSdiBytes, mismatch, mismatch + 0x10)));
        byte[] outputSdiBytes = Files.readAllBytes(outputSdi);
        System.out.println(outputSdi);
        System.out.println(Arrays.toString(Arrays.copyOfRange(outputSdiBytes, mismatch, mismatch + 0x10)));
        assertFalse(Files.mismatch(inputSdi, outputSdi) != -1);
        long delta = (long) (Files.size(inputSam) * 0.03);
        assertEquals(Files.size(inputSam), Files.size(outputSam), delta);
        assertDirectoriesEqual(outputDir, outputDir2);
      }
    } finally {
      Files.deleteIfExists(outputSam);
      Files.deleteIfExists(outputSdi);
      if (Files.isDirectory(outputDir)) {
        MoreFiles.deleteRecursively(outputDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(outputDir2)) {
        MoreFiles.deleteRecursively(outputDir2, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  /**
   * Return if the given sdi file is part of the looping sound of a stage.
   *
   * @param filePath The sdi file to check.
   * @return If it is the looping sound of a stage.
   */
  private boolean areStageLoopingSounds(Path filePath) {
    return filePath.endsWith("stg/007/3000.sdi")
        || filePath.endsWith("stg/008/3000.sdi")
        || filePath.endsWith("stg/019/3000.sdi")
        || filePath.endsWith("stg/021/3000.sdi")
        || filePath.endsWith("stg/022/3000.sdi")
        || filePath.endsWith("stg/024/3000.sdi")
        || filePath.endsWith("stg/032/3000.sdi");
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
