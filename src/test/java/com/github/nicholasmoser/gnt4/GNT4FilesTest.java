package com.github.nicholasmoser.gnt4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class GNT4FilesTest {

  @Test
  public void validateGNT4SoundEffectFiles() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path tempDir = FileUtils.getTempDirectory();
    Path defaultState = Paths.get(
        "src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    Path testState = tempDir.resolve(UUID.randomUUID().toString());
    Files.copy(defaultState, testState);
    GNT4Files gnt4Files = new GNT4Files(uncompressed, testState);
    gnt4Files.loadExistingState();

    // Validate that most files in the game have exactly one parent FPK
    List<Path> oneParentFPKFiles = Files.walk(uncompressed)
        .filter(GNT4FilesTest::hasOneParentFPK)
        .collect(Collectors.toList());
    for (Path path : oneParentFPKFiles) {
      String relativePath = path.toString().replace('\\', '/')
          .replace("src/test/gnt/gnt4/uncompressed/", "");
      assertEquals(1, gnt4Files.getParentFPKs(relativePath).size());
    }

    // Validate the few files that have multiple FPK parents
    assertEquals(2, gnt4Files.getParentFPKs("files/omake/0002.txg").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/omake/0010.txg").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/game/3003.pro").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/game/3003.sam").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/game/3003.sdi").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/kid/3000.poo").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/kid/3000.pro").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/kid/3000.sam").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/kid/3000.sdi").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/gar/3000.poo").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/gar/3000.pro").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/gar/3000.sam").size());
    assertEquals(2, gnt4Files.getParentFPKs("files/chr/gar/3000.sdi").size());
    assertEquals(3, gnt4Files.getParentFPKs("files/stg/010/3000.poo").size());
    assertEquals(3, gnt4Files.getParentFPKs("files/stg/010/3000.pro").size());
    assertEquals(3, gnt4Files.getParentFPKs("files/stg/010/3000.sam").size());
    assertEquals(3, gnt4Files.getParentFPKs("files/stg/010/3000.sdi").size());
  }

  /**
   * Return if this file should have one parent FPK.
   *
   * @param path The file path.
   * @return If it should have one parent FPK.
   */
  private static boolean hasOneParentFPK(Path path) {
    String pathString = path.toString().replace("\\", "/");
    switch (pathString) {
      case "src/test/gnt/gnt4/uncompressed/files/omake/0002.txg":
      case "src/test/gnt/gnt4/uncompressed/files/omake/0010.txg":
      case "src/test/gnt/gnt4/uncompressed/files/game/3003.poo":
      case "src/test/gnt/gnt4/uncompressed/files/game/3003.pro":
      case "src/test/gnt/gnt4/uncompressed/files/game/3003.sam":
      case "src/test/gnt/gnt4/uncompressed/files/game/3003.sdi":
      case "src/test/gnt/gnt4/uncompressed/files/chr/kid/3000.poo":
      case "src/test/gnt/gnt4/uncompressed/files/chr/kid/3000.pro":
      case "src/test/gnt/gnt4/uncompressed/files/chr/kid/3000.sam":
      case "src/test/gnt/gnt4/uncompressed/files/chr/kid/3000.sdi":
      case "src/test/gnt/gnt4/uncompressed/files/chr/gar/3000.poo":
      case "src/test/gnt/gnt4/uncompressed/files/chr/gar/3000.pro":
      case "src/test/gnt/gnt4/uncompressed/files/chr/gar/3000.sam":
      case "src/test/gnt/gnt4/uncompressed/files/chr/gar/3000.sdi":
      case "src/test/gnt/gnt4/uncompressed/files/stg/010/3000.poo":
      case "src/test/gnt/gnt4/uncompressed/files/stg/010/3000.pro":
      case "src/test/gnt/gnt4/uncompressed/files/stg/010/3000.sam":
      case "src/test/gnt/gnt4/uncompressed/files/stg/010/3000.sdi":
        return false;
      default:
        return Files.isRegularFile(path) && isCompressedFile(pathString);
    }
  }

  /**
   * Return if the file is FPK compressed. .trk, .h4m, .bnr, .img, .bin, and .dol files are not
   * FPK compressed.
   *
   * @param path The file path.
   * @return If it is FPK compressed.
   */
  private static boolean isCompressedFile(String path) {
    return !path.endsWith(".trk")
        && !path.endsWith(".h4m")
        && !path.endsWith(".bnr")
        && !path.endsWith(".img")
        && !path.endsWith(".bin")
        && !path.endsWith(".dol");
  }
}
