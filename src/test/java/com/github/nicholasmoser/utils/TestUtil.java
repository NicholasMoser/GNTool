package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtil {


  /**
   * Compare the contents of two files. This will buffer both files in memory, do not use it on
   * large files.
   *
   * @param expected The expected file for comparison.
   * @param actual The actual file for comparison.
   * @throws IOException If an I/O error occurs
   */
  public static void assertFilesEqual(Path expected, Path actual) throws IOException {
    byte[] expectedBytes = Files.readAllBytes(expected);
    byte[] actualBytes = Files.readAllBytes(actual);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Tests for non-equality of two directories. Two directories are considered equals if all files
   * contained in them have the same bytes, recursively for all directories contained.
   *
   * @param expectedDir The first directory to compare.
   * @param actualDir The second directory to compare.
   * @return If the directories are not equal.
   * @throws IOException If an I/O error occurs
   */
  public static boolean assertDirectoriesNotEqual(Path expectedDir, Path actualDir) throws IOException {
    if (!Files.isDirectory(expectedDir)) {
      throw new IllegalArgumentException("Path is not a directory: " + expectedDir);
    } else if (!Files.isDirectory(actualDir)) {
      throw new IllegalArgumentException("Path is not a directory: " + actualDir);
    }
    Set<Path> paths1 = Files.list(expectedDir).map(Path::getFileName).collect(Collectors.toSet());
    Set<Path> paths2 = Files.list(actualDir).map(Path::getFileName).collect(Collectors.toSet());
    if (!paths1.equals(paths2)) {
      // Directories either have different number of files or different file names
      return true;
    }
    for (Path fileName : paths1) {
      Path path1 = expectedDir.resolve(fileName);
      Path path2 = actualDir.resolve(fileName);
      if (Files.isDirectory(path1) && Files.isDirectory(path2)) {
        // Compare directories
        if (assertDirectoriesNotEqual(path1, path2)) {
          return true;
        }
      } else if (Files.isRegularFile(path1) && Files.isRegularFile(path2)) {
        // Compare files
        long mismatch = Files.mismatch(path1, path2);
        if (mismatch != -1) {
          return true;
        }
      }
    }
    fail("Directories are equal\nExpected: " + expectedDir + "\nActual: " + actualDir);
    return false;
  }

  /**
   * Tests for equality of two directories. Two directories are considered equals if all files
   * contained in them have the same bytes, recursively for all directories contained.
   *
   * @param expectedDir The first directory to compare.
   * @param actualDir The second directory to compare.
   * @return If the directories are equal.
   * @throws IOException If an I/O error occurs
   */
  public static void assertDirectoriesEqual(Path expectedDir, Path actualDir) throws IOException {
    if (!Files.isDirectory(expectedDir)) {
      throw new IllegalArgumentException("Path is not a directory: " + expectedDir);
    } else if (!Files.isDirectory(actualDir)) {
      throw new IllegalArgumentException("Path is not a directory: " + actualDir);
    }
    Set<Path> paths1 = Files.list(expectedDir).map(Path::getFileName).collect(Collectors.toSet());
    Set<Path> paths2 = Files.list(actualDir).map(Path::getFileName).collect(Collectors.toSet());
    if (!paths1.equals(paths2)) {
      // Directories either have different number of files or different file names
      fail(String.format("Directories have different files:\n%s:\n%s\n%s:\n%s", expectedDir, paths1, actualDir, paths2));
    }
    for (Path fileName : paths1) {
      Path path1 = expectedDir.resolve(fileName);
      Path path2 = actualDir.resolve(fileName);
      if (Files.isDirectory(path1) && Files.isDirectory(path2)) {
        // Compare directories
        assertDirectoriesEqual(path1, path2);
      } else if (Files.isRegularFile(path1) && Files.isRegularFile(path2)) {
        // Compare files
        long mismatch = Files.mismatch(path1, path2);
        if (mismatch != -1) {
          fail(String.format("Files %s and %s not equal at offset %d", path1, path2, mismatch));
        }
      }
    }
  }
}
