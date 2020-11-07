package com.github.nicholasmoser.zip;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the zip patching functionality.
 */
public class PatchZipTest {

  /**
   * Tests that a patch zip can be successfully used.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void testPatchZip() throws Exception {
    Path patchZip = Paths.get("src/test/resources/zip/test.zip");
    Path outputDir = Paths.get("src/test/resources/zip/test");
    PatchZip.patch(patchZip, outputDir);
  }

  /**
   * Tests that a patch zip with an invalid directory will throw an IOException.
   *
   */
  @Test
  public void testPatchZipInvalidDirectory() {
    Path patchZip = Paths.get("src/test/resources/zip/invalid.zip");
    Path outputDir = Paths.get("src/test/resources/zip/test");
    Assertions.assertThrows(IOException.class, () -> PatchZip.patch(patchZip, outputDir));
  }

  /**
   * Tests that using a file that is not a patch zip will throw an IOException.
   *
   */
  @Test
  public void testNotAPatchZip() {
    Path patchZip = Paths.get("src/test/resources/CodeJson/empty.json");
    Path outputDir = Paths.get("src/test/resources/zip/test");
    Assertions.assertThrows(IOException.class, () -> PatchZip.patch(patchZip, outputDir));
  }

  /**
   * Tests that using a file that does not exist will throw an IOException.
   *
   */
  @Test
  public void testFileDoesNotExist() {
    Path patchZip = Paths.get("src/test/resources/CodeJson/test.zip");
    Path outputDir = Paths.get("src/test/resources/zip/test");
    Assertions.assertThrows(IOException.class, () -> PatchZip.patch(patchZip, outputDir));
  }
}
