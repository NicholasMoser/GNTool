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

public class FileUtils {

  /**
   * Copies a folder using Java NIO. From the following link:
   * https://stackoverflow.com/questions/29076439/java-8-copy-directory-recursively/60621544#60621544
   *
   * @param source The source folder.
   * @param target The target folder.
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
   *
   * @throws IOException IF the temp directory does not exist
   */
  public static Path getTempDirectory() throws IOException {
    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    if (!Files.isDirectory(tempDir)) {
      throw new IOException("Temp directory does not exist: " + tempDir);
    }
    return tempDir;
  }
}
