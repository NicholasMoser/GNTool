package com.github.nicholasmoser.utils;

import static com.github.nicholasmoser.utils.TestUtil.assertDirectoriesEqual;
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

  /**
   * Test that two directories are equal. The two directories have two files with the same name and
   * text and a directory. Each subdirectory contains a file with the same name and text as well.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testDirectoriesEqual() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = testDir1.resolve("subdirectory");
    Path testDir3 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir4 = testDir3.resolve("subdirectory");
    Files.createDirectories(testDir2);
    Files.createDirectories(testDir4);
    try {
      Files.writeString(testDir1.resolve("123"), "ORLY");
      Files.writeString(testDir3.resolve("123"), "ORLY");
      Files.writeString(testDir1.resolve("456"), "YARLY");
      Files.writeString(testDir3.resolve("456"), "YARLY");
      Files.writeString(testDir2.resolve("789"), "NOWAI");
      Files.writeString(testDir4.resolve("789"), "NOWAI");
      assertDirectoriesEqual(testDir1, testDir3);
      assertDirectoriesEqual(testDir2, testDir4);
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir3)) {
        MoreFiles.deleteRecursively(testDir3, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  /**
   * Test that two directories are not equal if files with the same contents do not have the same
   * name. Also, if files with the same contents but not the same name are in a subdirectory.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testDirectoriesNotEqualFileName() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = testDir1.resolve("subdirectory");
    Path testDir3 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir4 = testDir3.resolve("subdirectory");
    Files.createDirectories(testDir2);
    Files.createDirectories(testDir4);
    try {
      Files.writeString(testDir1.resolve("123"), "ORLY");
      Files.writeString(testDir3.resolve("123"), "ORLY");
      Files.writeString(testDir1.resolve("456"), "YARLY");
      Files.writeString(testDir3.resolve("456"), "YARLY");
      Files.writeString(testDir2.resolve("789"), "NOWAI");
      Files.writeString(testDir4.resolve("DIFFERENT"), "NOWAI");
      assertDirectoriesEqual(testDir1, testDir3);
      assertDirectoriesEqual(testDir2, testDir4);
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir3)) {
        MoreFiles.deleteRecursively(testDir3, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  /**
   * Test that two directories are not equal if files with the same name do not have the same
   * contents. Also, if files with the same name but not the same contents are in a subdirectory.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testDirectoriesNotEqualFileContents() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = testDir1.resolve("subdirectory");
    Path testDir3 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir4 = testDir3.resolve("subdirectory");
    Files.createDirectories(testDir2);
    Files.createDirectories(testDir4);
    try {
      Files.writeString(testDir1.resolve("123"), "ORLY");
      Files.writeString(testDir3.resolve("123"), "ORLY");
      Files.writeString(testDir1.resolve("456"), "YARLY");
      Files.writeString(testDir3.resolve("456"), "YARLY");
      Files.writeString(testDir2.resolve("789"), "DIFFERENT");
      Files.writeString(testDir4.resolve("789"), "NOWAI");
      assertDirectoriesEqual(testDir1, testDir3);
      assertDirectoriesEqual(testDir2, testDir4);
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir3)) {
        MoreFiles.deleteRecursively(testDir3, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  /**
   * Test that two directories are not equal if the number of files differs. Also, if the number of
   * files in a subdirectory differs.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testDirectoryNotEqualDiffSize() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = testDir1.resolve("subdirectory");
    Path testDir3 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir4 = testDir3.resolve("subdirectory");
    Files.createDirectories(testDir2);
    Files.createDirectories(testDir4);
    try {
      Files.writeString(testDir1.resolve("123"), "ORLY");
      Files.writeString(testDir3.resolve("123"), "ORLY");
      Files.writeString(testDir1.resolve("456"), "YARLY");
      Files.writeString(testDir3.resolve("456"), "YARLY");
      Files.writeString(testDir2.resolve("789"), "NOWAI");
      Files.writeString(testDir4.resolve("789"), "NOWAI");
      Files.writeString(testDir4.resolve("ANOTHERONE"), "NOWAI");
      assertDirectoriesEqual(testDir1, testDir3);
      assertDirectoriesEqual(testDir2, testDir4);
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir3)) {
        MoreFiles.deleteRecursively(testDir3, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

  /**
   * Test that two directories are equal if they are empty. Also that they are equal if they have
   * a single equal subdirectory.
   *
   * @throws Exception If any Exception occurs.
   */
  @Test
  public void testEmptyDirectoryEqual() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir1 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir2 = testDir1.resolve("subdirectory");
    Path testDir3 = tempDir.resolve(UUID.randomUUID().toString());
    Path testDir4 = testDir3.resolve("subdirectory");
    Files.createDirectories(testDir2);
    Files.createDirectories(testDir4);
    try {
      assertDirectoriesEqual(testDir1, testDir3);
      assertDirectoriesEqual(testDir2, testDir4);
    } finally {
      if (Files.isDirectory(testDir1)) {
        MoreFiles.deleteRecursively(testDir1, RecursiveDeleteOption.ALLOW_INSECURE);
      }
      if (Files.isDirectory(testDir3)) {
        MoreFiles.deleteRecursively(testDir3, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }

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
