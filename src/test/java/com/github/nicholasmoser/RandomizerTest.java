package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class RandomizerTest {

  @Test
  public void testNullListRandomizeFilesFails() {
    assertThrows(IllegalArgumentException.class, () -> Randomizer.randomizeFiles(null));
  }

  @Test
  public void testEmptyListRandomizeFilesFails() {
    assertThrows(IllegalArgumentException.class,
        () -> Randomizer.randomizeFiles(Collections.emptyList()));
  }

  @Test
  public void testDirectoryRandomizeFilesFails() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir);
    try {
      assertThrows(IllegalArgumentException.class,
          () -> Randomizer.randomizeFiles(Collections.singletonList(testDir)));
    } finally {
      Files.deleteIfExists(testDir);
    }
  }

  @Test
  public void testRandomizeUnmodifiableListFails() {
    assertThrows(IllegalArgumentException.class,
        () -> Randomizer.randomizeFiles(List.of(Paths.get("test"))));
  }

  @Test
  public void testRandomizeOneFile() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir);

    try {
      Path file1 = testDir.resolve(UUID.randomUUID().toString());
      Files.writeString(file1, "Test");

      // Base tests
      assertEquals(1, (int) Files.list(testDir).count());
      assertTrue(Files.exists(file1));
      assertEquals(4, Files.size(file1));

      // Randomize and retest
      Randomizer.randomizeFiles(Collections.singletonList(file1));
      assertEquals(1, (int) Files.list(testDir).count());
      assertTrue(Files.exists(file1));
      assertEquals(4, Files.size(file1));
    } finally {
      MoreFiles.deleteRecursively(testDir, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  @Test
  public void testRandomizeFourFiles() throws Exception {
    Path tempDir = FileUtils.getTempDirectory();
    Path testDir = tempDir.resolve(UUID.randomUUID().toString());
    Files.createDirectories(testDir);

    try {
      Path file1 = testDir.resolve(UUID.randomUUID().toString());
      Path file2 = testDir.resolve(UUID.randomUUID().toString());
      Path file3 = testDir.resolve(UUID.randomUUID().toString());
      Path file4 = testDir.resolve(UUID.randomUUID().toString());
      Files.writeString(file1, "Test1");
      Files.writeString(file2, "Test2");
      Files.writeString(file3, "Test3");
      Files.writeString(file4, "Test4");
      List<Path> paths = new ArrayList<>();
      paths.add(file1);
      paths.add(file2);
      paths.add(file3);
      paths.add(file4);

      // Base tests
      assertEquals(4, (int) Files.list(testDir).count());
      assertTrue(Files.exists(file1));
      assertEquals(5, Files.size(file1));
      assertTrue(Files.exists(file2));
      assertEquals(5, Files.size(file2));
      assertTrue(Files.exists(file3));
      assertEquals(5, Files.size(file3));
      assertTrue(Files.exists(file4));
      assertEquals(5, Files.size(file4));

      // Randomize and retest
      Randomizer.randomizeFiles(paths);
      assertEquals(4, (int) Files.list(testDir).count());
      assertTrue(Files.exists(file1));
      assertEquals(5, Files.size(file1));
      assertTrue(Files.exists(file2));
      assertEquals(5, Files.size(file2));
      assertTrue(Files.exists(file3));
      assertEquals(5, Files.size(file3));
      assertTrue(Files.exists(file4));
      assertEquals(5, Files.size(file4));
    } finally {
      MoreFiles.deleteRecursively(testDir, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }
}
