package com.github.nicholasmoser.mot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MOTUtil {
  public static void unpack(Path inputFile, Path outputDir) throws IOException {
    if (!Files.isRegularFile(inputFile)) {
      throw new IllegalArgumentException("inputFile not a file: " + inputFile);
    } else if (Files.isRegularFile(outputDir)) {
      throw new IllegalArgumentException("outputDir is a file: " + outputDir);
    }
    Files.createDirectories(outputDir);
    AnimationArchive motion = AnimationArchive.parseFrom(inputFile);
  }

  public static void pack(Path inputDir, Path outputFile) throws IOException {
    if (!Files.isDirectory(inputDir)) {
      throw new IllegalArgumentException("inputDir not a directory: " + inputDir);
    } else if (Files.isDirectory(outputFile)) {
      throw new IllegalArgumentException("outputFile is a directory: " + outputFile);
    }
  }
}
