package com.github.nicholasmoser.dol;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class DolParserTest {

  /**
   * Tests reading and writing a dol file.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testReadingWritingDol() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path dolPath = uncompressed.resolve("sys/main.dol");
    DolParser parser = new DolParser(dolPath);
    Dol dol = parser.parse();
    Path tempDir = FileUtils.getTempDirectory();
    Path exportFile = tempDir.resolve(UUID.randomUUID().toString());
    try {
      dol.writeToFile(exportFile);
      assertTrue(Files.exists(exportFile));
      assertTrue(Files.size(exportFile) > 0);
    } finally {
      Files.deleteIfExists(exportFile);
    }
  }
}
