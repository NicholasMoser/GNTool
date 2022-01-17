package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
   * @param numOfAnimations Total possible number of animations. May not reflect the actual size.
   * @param fileSize The existing file size of the animation archive (.mot).
   * @param animations The actual animations in the animation archive.
   */
  private Motion(int numOfAnimations, int fileSize, List<GNTAnimation> animations) {
    this.totalAnimationIds = numOfAnimations;
    this.fileSize = fileSize;
    this.animations = animations;
  }

  public static Motion parseFrom(Path inputFile) throws IOException {
    try(RandomAccessFile raf = new RandomAccessFile(inputFile.toFile(), "r")) {
      // Parse and validate the header
      int nullBytes = ByteUtils.readInt32(raf);
      int numOfAnimations = ByteUtils.readInt32(raf); // may not reflect actual size
      int headerSize = ByteUtils.readInt32(raf);
      int fileSize = ByteUtils.readInt32(raf);
      if (nullBytes != 0x00) {
        throw new IllegalStateException(String.format("Null bytes not 0x00: 0x%x", nullBytes));
      } else if (headerSize != 0x10) {
        throw new IllegalStateException("Header size not supported: " + headerSize);
      } else if (numOfAnimations < 1) {
        throw new IllegalStateException("Invalid animation size: " + numOfAnimations);
      } else if (fileSize < 1) {
        throw new IllegalStateException("Invalid file size: " + fileSize);
      }
      // Parse the animation offsets and ids
      Map<Integer, Integer> offsetToId = new HashMap<>();
      for (int i = 0; i < numOfAnimations; i++) {
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
      return new Motion(numOfAnimations, fileSize, animations);
    }
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
  }
}
