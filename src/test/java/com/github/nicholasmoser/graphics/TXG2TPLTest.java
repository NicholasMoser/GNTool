package com.github.nicholasmoser.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TXG2TPLTest {

  public static Stream<Arguments> txgPathProvider() throws IOException {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    return Files.walk(uncompressed)
        .filter(TXG2TPLTest::isTxg)
        .map(Arguments::arguments);
  }

  private static boolean isTxg(Path path) {
    return path.toString().endsWith(".txg");
  }

  @ParameterizedTest(name="#{index} - Test with Argument={0}")
  @MethodSource("txgPathProvider")
  public void testAllGNT4Textures(Path txgPath) throws Exception {
    // First extract to testDir, then create newTxg with unique name.
    // Then, extract newTxg to testDir2 and make sure it is equal to testDir.
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir);
    Path newTxg = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir2);

    try {
      TXG2TPL.unpack(txgPath, testDir);
      if (!txgPath.endsWith("chr/tem/2000.txg")) {
        // Temari's 2000.txg doesn't have any files in it
        assertTrue(Files.list(testDir).count() > 0);
      }
      TXG2TPL.pack(testDir, newTxg);
      assertTrue(Files.exists(newTxg));
      assertTrue(Files.size(newTxg) > 0);
      assertTrue(Files.size(txgPath) > 0);
      TXG2TPL.unpack(newTxg, testDir2);
      FileUtils.areDirectoriesEqual(testDir, testDir2);
    } finally {
      Files.deleteIfExists(newTxg);
      if (Files.isDirectory(testDir)) {
        MoreFiles.deleteRecursively(testDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir2)) {
        MoreFiles.deleteRecursively(testDir2, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }
}
