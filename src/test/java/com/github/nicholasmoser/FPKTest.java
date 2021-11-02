package com.github.nicholasmoser;

import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FPKPacker} and {@link FPKUnpacker}.
 */
public class FPKTest {

  /**
   * Unpack FPKs to a directory and repack them back into FPKs. Compare the results of both actions
   * to the expected outputs. The first time you run most of the unit tests it will generate a
   * compressed and uncompressed folder of files from GNT4 in the test directory. This test will do
   * the same thing but in the temp directory. This test will then compare the new results in the
   * temp directory to the files in the test directory, therefore this is really only useful if NOT
   * running this test for the first time.
   * @throws Exception If any Exceptions occur.
   */
  @Test
  public void unpackAndRepack() throws Exception {
  }
}
