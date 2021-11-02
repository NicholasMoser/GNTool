package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.FileUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CodeTest {

  /**
   * Verify that inserting, overwriting, or deleting 0 bytes has no effect on a file.
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testEmptyActionsCode() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());
    String testString = "Hello World!";
    try {
      Files.writeString(testFile, testString);
      Code.getBuilder()
          .withInsert(3, new byte[0])
          .withOverwrite(2, new byte[0])
          .withDelete(0, 0)
          .execute(testFile);
      assertEquals(testString, Files.readString(testFile));
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  @Test
  public void testInsertCode() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());
    String testString = "Hello World!";
    try {
      Files.writeString(testFile, testString);
      Code.getBuilder()
          .withInsert(6, "strange ".getBytes(StandardCharsets.UTF_8))
          .execute(testFile);
      assertEquals("Hello strange World!", Files.readString(testFile));
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  @Test
  public void testDeleteCode() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());
    String testString = "Hello World!";
    try {
      Files.writeString(testFile, testString);
      Code.getBuilder()
          .withDelete(5, 6)
          .execute(testFile);
      assertEquals("Hello!", Files.readString(testFile));
    } finally {
      Files.deleteIfExists(testFile);
    }
  }

  @Test
  public void testModifyCode() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());
    String testString = "Hello World!";
    try {
      Files.writeString(testFile, testString);
      Code.getBuilder()
          .withOverwrite(6, "Beans".getBytes(StandardCharsets.UTF_8))
          .execute(testFile);
      assertEquals("Hello Beans!", Files.readString(testFile));
    } finally {
      Files.deleteIfExists(testFile);
    }
  }
}
