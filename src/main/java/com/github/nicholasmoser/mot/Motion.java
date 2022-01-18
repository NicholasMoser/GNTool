package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A motion contains one or more {@link GNTAnimation} objects. It is the .mot file found in GNT
 * games.
 */
public class Motion {

  private final int totalAnimationIds;
  private final int fileSize;
  private final List<GNTAnimation> animations;

  /**
   * Create a new animation archive (.mot file data).
   *
   * @param totalAnimationIds Total possible number of animations. May not reflect the actual size.
   * @param fileSize          The existing file size of the animation archive (.mot).
   * @param animations        The actual animations in the animation archive.
   */
  private Motion(int totalAnimationIds, int fileSize, List<GNTAnimation> animations) {
    this.totalAnimationIds = totalAnimationIds;
    this.fileSize = fileSize;
    this.animations = animations;
  }

  public static Motion parseFromFile(Path inputFile) throws IOException {
    if (!Files.isRegularFile(inputFile)) {
      throw new IllegalArgumentException("inputFile not a file: " + inputFile);
    }
    try (RandomAccessFile raf = new RandomAccessFile(inputFile.toFile(), "r")) {
      // Parse and validate the header
      int nullBytes = ByteUtils.readInt32(raf);
      int totalAnimationIds = ByteUtils.readInt32(raf); // may not reflect actual size
      int headerSize = ByteUtils.readInt32(raf);
      int fileSize = ByteUtils.readInt32(raf);
      if (nullBytes != 0x00) {
        throw new IllegalStateException(String.format("Null bytes not 0x00: 0x%x", nullBytes));
      } else if (headerSize != 0x10) {
        throw new IllegalStateException("Header size not supported: " + headerSize);
      } else if (totalAnimationIds < 1) {
        throw new IllegalStateException("Invalid animation size: " + totalAnimationIds);
      } else if (fileSize < 1) {
        throw new IllegalStateException("Invalid file size: " + fileSize);
      }
      // Parse the animation offsets and ids
      Map<Integer, Integer> offsetToId = new HashMap<>();
      for (int i = 0; i < totalAnimationIds; i++) {
        int offset = ByteUtils.readInt32(raf);
        if (offset != 0) {
          if (offsetToId.containsKey(offset)) {
            throw new IllegalStateException("Duplicate offset: " + offset);
          }
          offsetToId.put(offset, i);
        }
      }
      // Read the data for each animation
      List<GNTAnimation> animations = new ArrayList<>();
      for (Entry<Integer, Integer> entry : offsetToId.entrySet()) {
        int offset = entry.getKey();
        int id = entry.getValue();
        raf.seek(offset);
        animations.add(GNTAnimation.parseFrom(raf, id));
      }
      return new Motion(totalAnimationIds, fileSize, animations);
    }
  }

  public static Motion parseFromDirectory(Path inputDirectory) throws IOException {
    if (!Files.isDirectory(inputDirectory)) {
      throw new IllegalArgumentException("inputDirectory not a directory: " + inputDirectory);
    }
    List<Path> files = Files.walk(inputDirectory).filter(Files::isRegularFile)
        .collect(Collectors.toList());
    int largestId = getLargestId(inputDirectory);
    int fileSize = 0;
    List<GNTAnimation> animations = new ArrayList<>();
    for (Path file : files) {
      String fileName = file.getFileName().toString();
      if (!fileName.startsWith("0x") || !fileName.endsWith(".gnta")) {
        continue;
      }
      int id = Integer.decode(fileName.replace(".gnta", ""));
      if (id > largestId) {
        largestId = id;
      }
      try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
        GNTAnimation amimation = GNTAnimation.parseFrom(raf, id);
        fileSize += amimation.getSize();
        animations.add(amimation);
      }
    }
    return new Motion(largestId, fileSize, animations);
  }

  private static int getLargestId(Path inputDirectory) throws IOException {
    Path totalAnimationIdsPath = inputDirectory.resolve("totalAnimationIds");
    if (Files.isRegularFile(totalAnimationIdsPath)) {
      String number = Files.readString(totalAnimationIdsPath);
      return Integer.valueOf(number);
    }
    return 0;
  }

  public void unpack(Path directory) throws IOException {
    if (Files.isRegularFile(directory)) {
      throw new IllegalArgumentException("should be a directory but is a file: " + directory);
    }
    Files.createDirectories(directory);
    for (GNTAnimation animation : animations) {
      String name = String.format("0x%04X.gnta", animation.getId());
      Path filePath = directory.resolve(name);
      byte[] animationData = animation.getBytes();
      Files.write(filePath, animationData);
    }
    byte[] totalAnimationIdsBytes = Integer.toString(totalAnimationIds)
        .getBytes(StandardCharsets.UTF_8);
    Files.write(directory.resolve("totalAnimationIds"), totalAnimationIdsBytes);
  }

  public void pack(Path testFile) {

  }
}
