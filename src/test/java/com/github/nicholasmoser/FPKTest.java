package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(CONCURRENT)
public class FPKTest {

  private static Path TEST_STATE;
  private static GNT4Files GNT4_FILES;

  @BeforeAll
  static void setup() throws Exception {
    Path defaultState = Paths.get(
        "src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    TEST_STATE = FileUtils.getTempDirectory().resolve(UUID.randomUUID().toString());
    Files.copy(defaultState, TEST_STATE);
    GNT4_FILES = new GNT4Files(null, TEST_STATE);
    GNT4_FILES.loadExistingState();
  }

  @AfterAll
  static void tearDown() throws Exception {
    Files.deleteIfExists(TEST_STATE);
  }

  @ParameterizedTest(name="#{index} - Test with Argument={0}")
  @MethodSource("fpkPathProvider")
  void testUnpackAllGNT4FPKS(Path fpkPath) throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Path testFPK = testDir.resolve(UUID.randomUUID() + ".fpk");
    Path expectedDir = Prereqs.getUncompressedGNT4().resolve("files");
    Files.createDirectories(testDir);
    try {
      // Copy and unpack the FPK file
      Files.copy(fpkPath, testFPK);
      Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
      FPKUnpacker.extractFPK(testFPK, testDir, gnt4FileNames, false, true);
      // Get the unpacked files
      List<Path> uncompressedFiles = Files.walk(testDir)
          .filter(FPKTest::isNotFPK)
          .collect(Collectors.toList());
      // Assert the unpacked files are as expected
      for (Path uncompressedFile : uncompressedFiles) {
        Path relativePath = testDir.relativize(uncompressedFile);
        Path expectedFile = expectedDir.resolve(relativePath);
        assertTrue(Files.isRegularFile(expectedFile));
        assertTrue(Files.isRegularFile(uncompressedFile));
        assertArrayEquals(Files.readAllBytes(expectedFile), Files.readAllBytes(uncompressedFile));
      }
    } finally {
      if (Files.isDirectory(testDir)) {
        MoreFiles.deleteRecursively(testDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  // Note to self: a single, non-concurrent run took 14 min, 24 seconds
  @ParameterizedTest(name="#{index} - Test with Argument={0}")
  @MethodSource("fpkPathProvider")
  void testPackAndUnpackAllGNT4FPKS(Path fpkPath) throws Exception {
    // Get test paths
    Path tempDir = FileUtils.getTempDirectory();
    Path compressedDir = Prereqs.getCompressedGNT4();
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path workspaceDir = tempDir.resolve(UUID.randomUUID().toString());
    Path testCompressedDir = workspaceDir.resolve(UUID.randomUUID().toString());
    Path testUncompressedDir = workspaceDir.resolve(UUID.randomUUID().toString());
    Path uncompressedFiles = uncompressedDir.resolve("files");

    // Create directories
    Files.createDirectories(testCompressedDir);
    Files.createDirectories(testUncompressedDir);
    try {
      // Get FPK children
      String relativePath = compressedDir.relativize(fpkPath).toString().replace("\\", "/");
      Path testFPK = testCompressedDir.resolve(relativePath);
      List<GNTChildFile> children = GNT4_FILES.getFPKChildren(relativePath);
      assertFalse(children.isEmpty());
      // Pack the children into an FPK file
      FPKPacker.repackFPK(children, testFPK, uncompressedDir, true, false);
      // Unpack the FPK
      Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
      FPKUnpacker.extractFPK(testFPK, testUncompressedDir, gnt4FileNames, false, true);
      // Compare unpacked files to expected output
      Files.walk(testUncompressedDir)
          .filter(Files::isRegularFile)
          .forEach(file -> assertMatching(file, uncompressedFiles, testUncompressedDir));
    } finally {
      if (Files.isDirectory(workspaceDir)) {
        MoreFiles.deleteRecursively(workspaceDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  private void assertMatching(Path actualFile, Path expectedDir, Path actualDir) {
    try {
      Path relativePath = actualDir.relativize(actualFile);
      Path expectedFile = expectedDir.resolve(relativePath);
      byte[] expectedBytes = Files.readAllBytes(expectedFile);
      byte[] actualBytes = Files.readAllBytes(actualFile);
      assertArrayEquals(expectedBytes, actualBytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Stream<Arguments> fpkPathProvider() throws IOException {
    Path uncompressed = Prereqs.getCompressedGNT4();
    return Files.walk(uncompressed)
        .filter(FPKTest::isFPK)
        .map(Arguments::arguments);
  }

  private static boolean isFPK(Path path) {
    return path.toString().endsWith(".fpk");
  }

  private static boolean isNotFPK(Path path) {
    return Files.isRegularFile(path) && !path.toString().endsWith(".fpk");
  }
}
