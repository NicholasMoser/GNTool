package com.github.nicholasmoser.mot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

  public void writeTo(Path outputFile) throws IOException {
    try(BufferedWriter bw = Files.newBufferedWriter(outputFile)) {
      bw.write(Integer.toString(totalAnimationIds));
      bw.write('\n');
      for (String fileName : fileNames) {
        bw.write(fileName);
        bw.write('\n');
      }
    }
  }
}
