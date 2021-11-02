package com.github.nicholasmoser.gnt4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class GNT4GraphicsTest {

  /**
   * Validate that the list of GNT4 graphics is accurate.
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void validateGNT4GraphicsFiles() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> txgPaths = Files.walk(uncompressed)
        .filter(GNT4GraphicsTest::isTxg)
        .collect(Collectors.toList());
    assertEquals(txgPaths.size(), GNT4Graphics.TEXTURES.size());
    for (String txgPath : GNT4Graphics.TEXTURES) {
      assertTrue(Files.isRegularFile(uncompressed.resolve(txgPath)));
    }
  }

  private static boolean isTxg(Path path) {
    return path.toString().endsWith(".txg");
  }
}
