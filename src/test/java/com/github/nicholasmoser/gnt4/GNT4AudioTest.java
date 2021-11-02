package com.github.nicholasmoser.gnt4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class GNT4AudioTest {

  /**
   * Validate that the list of GNT4 sound effects is accurate.
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void validateGNT4SoundEffectFiles() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> samPaths = Files.walk(uncompressed)
        .filter(GNT4AudioTest::isSam)
        .collect(Collectors.toList());
    assertEquals(samPaths.size(), GNT4Audio.SOUND_EFFECTS.size());
    for (String samPath : GNT4Audio.SOUND_EFFECTS) {
      assertTrue(Files.isRegularFile(uncompressed.resolve(samPath)));
    }
  }

  private static boolean isSam(Path path) {
    return path.toString().endsWith(".sam");
  }

  /**
   * Validate that the list of GNT4 music files is accurate.
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void validateGNT4MusicFiles() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    List<Path> trkPaths = Files.walk(uncompressed)
        .filter(GNT4AudioTest::isTrk)
        .collect(Collectors.toList());
    assertEquals(trkPaths.size(), GNT4Audio.MUSIC.size());
    for (String trkPath : GNT4Audio.MUSIC) {
      assertTrue(Files.isRegularFile(uncompressed.resolve(trkPath)));
    }
  }

  private static boolean isTrk(Path path) {
    return path.toString().endsWith(".trk");
  }

}
