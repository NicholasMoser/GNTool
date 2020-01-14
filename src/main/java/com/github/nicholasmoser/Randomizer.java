package com.github.nicholasmoser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilities methods for randomization.
 */
public class Randomizer {

  /**
   * Randomizes the contents of a list of files. This will take the contents of one file, put them
   * in another, and take the contents of that file and put it into another, and so on. The list be
   * must non-null, non-empty, and comprised of regular resolvable file paths.
   *
   * @param filePaths The list of file paths.
   * @throws IOException If an I/O error occurs
   */
  public static void randomizeFiles(List<Path> filePaths) throws IOException {
    if (filePaths == null) {
      throw new IllegalArgumentException("filePaths is null");
    } else if (filePaths.isEmpty()) {
      throw new IllegalArgumentException("filePaths is empty");
    }
    List<Path> notFilePaths = filePaths.stream()
        .filter(file -> file == null || !Files.isRegularFile(file))
        .collect(Collectors.toList());
    if (!notFilePaths.isEmpty()) {
      throw new IllegalArgumentException("The following paths are not files: " + notFilePaths);
    }
    Collections.shuffle(filePaths);
    Path parentDirectory = filePaths.get(0).getParent();
    Path tempFile = parentDirectory.resolve("temp" + System.currentTimeMillis());
    int numOfFiles = filePaths.size();
    Files.copy(filePaths.get(0), tempFile);
    for (int i = 0; i < numOfFiles - 1; i++) {
      Files.move(filePaths.get(i + 1), filePaths.get(i), StandardCopyOption.REPLACE_EXISTING);
    }
    Files.move(tempFile, filePaths.get(numOfFiles - 1), StandardCopyOption.REPLACE_EXISTING);
  }
}
