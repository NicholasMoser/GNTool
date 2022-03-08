package com.github.nicholasmoser.mot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the list of animations of a motion. When stored to the file, the first
 * line is the total number of animation ids, and each line after is the filename in the current
 * directory of each specific .gnta file.
 */
public class AnimationList {

  public static final String NAME = "AnimationList.dat";
  private final int totalAnimationIds;
  private final List<String> fileNames;

  public AnimationList(int totalAnimationIds, List<String> fileNames) {
    this.totalAnimationIds = totalAnimationIds;
    this.fileNames = fileNames;
  }

  public int getTotalAnimationIds() {
    return totalAnimationIds;
  }

  public List<String> getFileNames() {
    return fileNames;
  }

  /**
   * If this directory is a valid unpacked motion, that is, if it contains an AnimationList.dat
   * file.
   *
   * @param directory The directory to check.
   * @return If the directory is valid in regard to having an AnimationList.dat
   */
  public static boolean isValidDirectory(Path directory) {
    return Files.exists(directory.resolve(NAME));
  }

  public static AnimationList parseFrom(Path directory) throws IOException {
    if (!isValidDirectory(directory)) {
      throw new IOException("Missing animation list file: " + NAME);
    }
    List<String> lines = Files.readAllLines(directory.resolve(NAME));
    int totalAnimationIds = Integer.parseInt(lines.get(0));
    List<String> fileNames = new ArrayList<>(lines.size() - 1);
    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      if (!line.isBlank()) {
        fileNames.add(line);
      }
    }
    return new AnimationList(totalAnimationIds, fileNames);
  }

  /**
   * Write this AnimationList object to a file.
   *
   * @param outputFile The output file.
   * @throws IOException If an I/O error occurs.
   */
  public void writeTo(Path outputFile) throws IOException {
    try (BufferedWriter bw = Files.newBufferedWriter(outputFile)) {
      bw.write(Integer.toString(totalAnimationIds));
      bw.write('\n');
      for (String fileName : fileNames) {
        bw.write(fileName);
        bw.write('\n');
      }
    }
  }
}
