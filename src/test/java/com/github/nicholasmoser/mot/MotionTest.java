package com.github.nicholasmoser.mot;

import static com.github.nicholasmoser.utils.TestUtil.assertDirectoriesEqual;
import static com.github.nicholasmoser.utils.TestUtil.assertFilesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MotionTest {

  public static Stream<Arguments> motPathProvider() throws IOException {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    return Files.walk(uncompressed)
        .filter(MotionTest::isMot)
        .map(Arguments::arguments);
  }

  private static boolean isMot(Path path) {
    return path.toString().endsWith(".mot");
  }

  /**
   * This test tests unpacking a MOT file, repacking it, and verifying that they result in the same
   * file contents.
   *
   * @param motPath The path to the mot.
   * @throws Exception If any exception occurs.
   */
  @ParameterizedTest(name = "#{index} - Test with Argument={0}")
  @MethodSource("motPathProvider")
  public void testRepackAllGNT4Textures(Path motPath) throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir);
    try {
      Motion motion1 = Motion.parseFromFile(motPath);
      motion1.unpack(testDir);
      Motion motion2 = Motion.parseFromDirectory(testDir);
      assertEquals(motion1, motion2);
      motion2.pack(testFile);
      assertFilesEqual(motPath, testFile);
    } finally {
      Files.deleteIfExists(testFile);
      if (Files.isDirectory(testDir)) {
        MoreFiles.deleteRecursively(testDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  @Test
  @Disabled("Used for testing.")
  public void asd() throws Exception {
    Map<Integer, Integer> valueToCount = new HashMap<>();

    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> paths = Files.walk(uncompressed)
        .filter(MotionTest::isMot)
        .collect(Collectors.toList());
    for (Path path : paths) {
      Motion motion = Motion.parseFromFile(path);
      for (GNTAnimation anim : motion.getAnimations()) {
        for (BoneAnimation boneAnim : anim.getBoneAnimations()) {
          List<Float> values = boneAnim.getFunctionCurveValues();
          List<Coordinate> coordinates = boneAnim.getCoordinates();
          if (!coordinates.isEmpty()) {
            if (values.size() != coordinates.size()) {
              System.out.println();
            }
          }
        }
      }
    }

    for (Entry<Integer, Integer> entry : valueToCount.entrySet()) {
      System.out.printf("0x%08X %d\n", entry.getKey(), entry.getValue());
    }
  }

  /**
   * This test compares the results of unpacking a MOT via GNTool to unpacking a MOT via the
   * MOT-Dumping-Tool https://github.com/mitchellhumphrey/MOT-Dumping-Tool/blob/master/MOTTool.py
   *
   * @param motPath The path to the mot file to test.
   * @throws Exception If any exception occurs.
   */
  @ParameterizedTest(name = "#{index} - Test with Argument={0}")
  @MethodSource("motPathProvider")
  @Disabled("Requires running https://github.com/NicholasMoser/Naruto-GNT-Modding/blob/master/utils/unpack_all_mot.py")
  public void testAllGNT4TexturesCorrect(Path motPath) throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path actualDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(actualDir);
    try {
      String expectedDirName = motPath.getFileName().toString().replace(".mot", "");
      Path expectedDir = motPath.getParent().resolve(expectedDirName);
      if (!Files.isDirectory(expectedDir)) {
        throw new IllegalStateException(
            expectedDir + " does not exist, please run unpack_all_mot.py");
      }
      Motion motion = Motion.parseFromFile(motPath);
      motion.unpack(actualDir);
      assertDirectoriesEqual(expectedDir, actualDir);
    } finally {
      if (Files.isDirectory(actualDir)) {
        MoreFiles.deleteRecursively(actualDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }
}
