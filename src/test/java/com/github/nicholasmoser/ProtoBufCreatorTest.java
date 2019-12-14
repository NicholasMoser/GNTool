package com.github.nicholasmoser;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.gnt4.GNT4DiffChecker;
import com.github.nicholasmoser.gnt4.GNT4Files;

public class ProtoBufCreatorTest {

  @Test
  public void ok() {
    GNT4Files gnt4Files = GNT4Files.getInstance();
    List<String> files = gnt4Files.getAllFPKChildren();
  }
  
  /**
   * Tests creating a diff binary file.
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testCreateDiffBinary() throws Exception {
    Path root = Paths.get("D:/GNT/aaa/uncompressed");
    Path output = Paths.get("D:/GNT/aaa/workspace.bin");
    GNTFiles gntFiles = GNT4DiffChecker.createDiffBinary(root);
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
    GNTFiles gntFiles = GNT4DiffChecker.createDiffBinary(root, true);
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
  }
}
