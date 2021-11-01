package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class FileUtilsTest {
  @Test
  public void testCopyFolder() throws Exception {
    String fileName = "myfile";
    String fileText = "WE GET SIGNAL. WHAT !";
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = tempDir.resolve(UUID.randomUUID().toString());
    Path file1 = testDir1.resolve(fileName);
    Path file2 = testDir2.resolve(fileName);

    try {
      Files.createDirectories(testDir1);
      Files.writeString(file1, fileText);
      assertFalse(Files.isDirectory(testDir2));
      assertFalse(Files.isRegularFile(file2));
      FileUtils.copyFolder(testDir1, testDir2);
      assertTrue(Files.isDirectory(testDir2));
      assertTrue(Files.isRegularFile(file2));
      assertEquals(fileText, Files.readString(file2));
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir2)) {
        MoreFiles.deleteRecursively(testDir2, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }
}
