package com.github.nicholasmoser.utils;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;

public class FileUtils {

  /**
   * Copies a folder using Java NIO. From the following link: https://stackoverflow.com/questions/29076439/java-8-copy-directory-recursively/60621544#60621544
   *
   * @param source  The source folder.
   * @param target  The target folder.
   * @param options The copy options to use.
   * @throws IOException If an I/O error occurs
   */
  public static void copyFolder(Path source, Path target, CopyOption... options)
      throws IOException {
    Files.walkFileTree(source, new SimpleFileVisitor<>() {

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
        createDirectories(target.resolve(source.relativize(dir)));
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
          throws IOException {
        copy(file, target.resolve(source.relativize(file)), options);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  /**
   * @return The temp directory.
   * @throws IOException IF the temp directory does not exist
   */
  public static Path getTempDirectory() throws IOException {
    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    if (!Files.isDirectory(tempDir)) {
      throw new IOException("Temp directory does not exist: " + tempDir);
    }
    return tempDir;
  }

  /**
   * Tests for equality of two directories. Two directories are considered equals if all files
   * contained in them have the same bytes, recursively for all directories contained.
   *
   * @param dir1 The first directory to compare.
   * @param dir2 The second directory to compare.
   * @return If the directories are equal.
   * @throws IOException If an I/O error occurs
   */
  public static boolean areDirectoriesEqual(Path dir1, Path dir2) throws IOException {
    if (!Files.isDirectory(dir1)) {
      throw new IllegalArgumentException("Path is not a directory: " + dir1);
    } else if (!Files.isDirectory(dir2)) {
      throw new IllegalArgumentException("Path is not a directory: " + dir2);
    }
    Set<Path> paths1 = Files.list(dir1).map(Path::getFileName).collect(Collectors.toSet());
    Set<Path> paths2 = Files.list(dir2).map(Path::getFileName).collect(Collectors.toSet());
    if (!paths1.equals(paths2)) {
      // Directories either have different number of files or different file names
      return false;
    }
    for (Path fileName : paths1) {
      Path path1 = dir1.resolve(fileName);
      Path path2 = dir2.resolve(fileName);
      if (Files.isDirectory(path1) && Files.isDirectory(path2)) {
        // Compare directories
        if (!areDirectoriesEqual(path1, path2)) {
          return false;
        }
      } else if (Files.isRegularFile(path1) && Files.isRegularFile(path2)) {
        // Compare files
        if (Files.mismatch(path1, path2) != -1) {
          return false;
        }
      }
    }
    return true;
  }
}
