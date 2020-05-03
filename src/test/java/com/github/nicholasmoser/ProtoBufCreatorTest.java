package com.github.nicholasmoser;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.utils.ProtobufUtils;

public class ProtoBufCreatorTest {

  /**
   * Tests creating a diff binary file.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Disabled("Requires refactors to work from GNT4 iso")
  public void testCreateDiffBinary() throws Exception {
    Path compressed = Paths.get("D:/GNT/aaa/uncompressed");
    Path output = Paths.get("D:/GNT/aaa/testCreateDiffBinary.bin");
    GNTFiles gntFiles = ProtobufUtils.createBinary(compressed);
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
  }

  /**
   * Tests creating a diff binary file.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Disabled("Requires refactors to work from GNT4 iso")
  public void testCreateDiffBinaryWithFpks() throws Exception {
    Path compressed = Paths.get("D:/GNT/bbb/compressed");
    Path output = Paths
        .get("./src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    GNTFiles gntFiles = ProtobufUtils.createBinary(compressed, true);
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
  }
}
