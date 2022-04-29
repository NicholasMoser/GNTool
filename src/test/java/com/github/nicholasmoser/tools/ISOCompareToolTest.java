package com.github.nicholasmoser.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.sun.jna.Platform;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class ISOCompareToolTest {
  private static final String ISO1 = "ISO1";
  private static final String ISO2 = "ISO2";

  /**
   * Compare two empty directories. Neither directory should list any differences or changes.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void compareEmptyDirectories() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with a single empty file and one empty. The directory with a
   * single empty file should have that file listed under "Files only in ISO1".
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareOneEmptyFileDirectoryAndEmptyDirectory() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createFile(temp1.resolve("test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "test\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one empty and one with a single empty file. The directory with a
   * single empty file should have that file listed under "Files only in ISO2".
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareEmptyDirectoryAndOneEmptyFileDirectory() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createFile(temp2.resolve("test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "test\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, each with a single empty file. Neither directory should list any
   * differences or changes.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareOneEmptyFileDirectories() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createFile(temp1.resolve("test"));
    Files.createFile(temp2.resolve("test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, each with a single empty file in a subdirectory. Neither directory
   * should list any differences or changes.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareOneEmptyFileSubdirectoryDirectories() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createDirectories(temp1.resolve("test"));
    Files.createDirectories(temp2.resolve("test"));
    Files.createFile(temp1.resolve("test/test"));
    Files.createFile(temp2.resolve("test/test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with an empty file in the main directory and one with an empty
   * file in a subdirectory. Each file should be listed under "Files only in" for their respective
   * section.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareEmptyFileMainDirectoryAndSubdirectory() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createDirectories(temp1.resolve("test"));
    Files.createFile(temp1.resolve("test/test"));
    Files.createFile(temp2.resolve("test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String windowsMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "test\\test\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "test\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      String message = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "test/test\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "test\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      if (Platform.isWindows()) {
        assertEquals(windowsMessage, difference);
      } else {
        assertEquals(message, difference);
      }
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with an empty file in a subdirectory and an empty directory.
   * The empty file in the subdirectory should be listed.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void compareEmptyFileSubdirectoryAndEmptyDirectory() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Files.createDirectories(temp1.resolve("test"));
    Files.createFile(temp1.resolve("test/test"));
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String windowsMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "test\\test\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      String message = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "test/test\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      if (Platform.isWindows()) {
        assertEquals(windowsMessage, difference);
      } else {
        assertEquals(message, difference);
      }
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with an empty file and one with a file with a byte in it. This
   * file should report as being changed.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void compareEmptyFileAndFileWithByte() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Path file1 = temp1.resolve("test");
    Path file2 = temp2.resolve("test");
    Files.createFile(file1);
    Files.createFile(file2);
    try(OutputStream os = Files.newOutputStream(file1)) {
      os.write(69);
    }
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n"
          + "test\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with a file with a byte in it and another with a file with the
   * same byte in it. No files should be shown as having been changed since it is the same size and
   * byte in the file.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void compareFileWithByteAndFileWithSameByte() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Path file1 = temp1.resolve("test");
    Path file2 = temp2.resolve("test");
    Files.createFile(file1);
    Files.createFile(file2);
    try(OutputStream os = Files.newOutputStream(file1)) {
      os.write(69);
    }
    try(OutputStream os = Files.newOutputStream(file2)) {
      os.write(69);
    }
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with a file with a byte in it and another with a file with a
   * different byte in it. This file should be shown as having been changed since the files have
   * different bytes in them.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void compareFileWithByteAndFileWithDifferentByte() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Path file1 = temp1.resolve("test");
    Path file2 = temp2.resolve("test");
    Files.createFile(file1);
    Files.createFile(file2);
    try(OutputStream os = Files.newOutputStream(file1)) {
      os.write(69);
    }
    try(OutputStream os = Files.newOutputStream(file2)) {
      os.write(56);
    }
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n"
          + "test\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Compare two directories, one with a file with a byte in it and another with a file with
   * two bytes in it. The file should be reports as having been changed since they have different
   * sizes.
   *
   * @throws Exception If any exception occurs
   */
  @Test
  public void compareFilesWithDifferentSizes() throws Exception {
    Path temp1 = Files.createTempDirectory(ISO1);
    Path temp2 = Files.createTempDirectory(ISO2);
    Path file1 = temp1.resolve("test");
    Path file2 = temp2.resolve("test");
    Files.createFile(file1);
    Files.createFile(file2);
    try(OutputStream os = Files.newOutputStream(file1)) {
      os.write(69);
    }
    try(OutputStream os = Files.newOutputStream(file2)) {
      os.write(69);
      os.write(69);
    }
    try {
      String difference = ISOCompareTool.getDifference(temp1, temp2, ISO1, ISO2);
      String expectedMessage = "\nFiles only in ISO1\n"
          + "------------------\n"
          + "\n"
          + "Files only in ISO2\n"
          + "------------------\n"
          + "\n"
          + "Changed Files\n"
          + "-------------\n"
          + "test\n";
      assertEquals(expectedMessage, difference);
    } finally {
      MoreFiles.deleteRecursively(temp1, RecursiveDeleteOption.ALLOW_INSECURE);
      MoreFiles.deleteRecursively(temp2, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }
}
