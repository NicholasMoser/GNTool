package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
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
  public void testCreateDiffBinary() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path exportFile = tempDir.resolve(UUID.randomUUID().toString());
    Path uncompressed = Prereqs.getUncompressedGNT4();

    System.out.println("Getting allowed workspace files.");
    GNTFiles files = GNT4Files.getVanillaFiles();
    Set<String> allowedFiles = GNT4Files.getAllowedWorkspaceFiles(files);

    System.out.println("Creating diff binary.");
    try {
      GNTFiles gntFiles = ProtobufUtils.createBinary(uncompressed, allowedFiles, false, true);
      try (OutputStream os = Files.newOutputStream(exportFile)) {
        gntFiles.writeTo(os);
      }
      assertTrue(Files.exists(exportFile));
      assertTrue(Files.size(exportFile) > 0);
      try (InputStream is = Files.newInputStream(exportFile)) {
        GNTFiles newGntFiles = GNTFiles.parseFrom(is);
        assertEquals(2574, newGntFiles.getGntFileCount());
      }
    } finally {
      Files.deleteIfExists(exportFile);
    }
  }

  /**
   * Tests creating a diff binary file.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testCreateDiffBinaryWithFpks() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path exportFile = tempDir.resolve(UUID.randomUUID().toString());
    Path compressed = Prereqs.getCompressedGNT4();

    System.out.println("Getting allowed workspace files.");
    GNTFiles files = GNT4Files.getVanillaFiles();
    Set<String> allowedFiles = GNT4Files.getAllowedWorkspaceFiles(files);

    System.out.println("Creating diff binary with fpks.");
    try {
      GNTFiles gntFiles = ProtobufUtils.createBinary(compressed, allowedFiles, true, false, true);
      try (OutputStream os = Files.newOutputStream(exportFile)) {
        gntFiles.writeTo(os);
      }
      assertTrue(Files.exists(exportFile));
      assertTrue(Files.size(exportFile) > 0);
      try (InputStream is = Files.newInputStream(exportFile)) {
        GNTFiles newGntFiles = GNTFiles.parseFrom(is);
        assertEquals(120, newGntFiles.getGntFileCount());
      }
    } finally {
      Files.deleteIfExists(exportFile);
    }
  }
}
