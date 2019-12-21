package com.github.nicholasmoser;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.utils.ProtobufUtils;

public class ProtoBufCreatorTest {  
  /**
   * Tests creating a diff binary file.
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testCreateDiffBinary() throws Exception {
    Path root = Paths.get("D:/GNT/aaa/uncompressed");
    Path output = Paths.get("D:/GNT/aaa/workspace.bin");
    GNTFiles gntFiles = ProtobufUtils.createBinary(root);
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
  }
  
  /**
   * Tests creating a diff binary file.
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testCreateDiffBinaryWithFpks() throws Exception {
    Path root = Paths.get("D:/GNT/aaa/root");
    Path output = Paths.get("D:/GNT/aaa/fpks.bin");
    GNTFiles gntFiles = ProtobufUtils.createBinary(root, true);
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
  }
}
