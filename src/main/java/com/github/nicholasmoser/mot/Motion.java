package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.collect.Maps;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A motion contains one or more {@link GNTAnimation} objects. It is the .mot file found in GNT
 * games.
 */
public class Motion {

  private final int totalAnimationIds;
  private final Set<GNTAnimation> animations;
  private final Map<Integer, GNTAnimation> idToAnimation;

  /**
   * Create a new animation archive (.mot file data). The total animation ids usually does not
   * match the total number of animations. This is likely because some animation ids are unused
   * for certain characters or possibly fallback to a default animation.
   *
   * @param totalAnimationIds Total possible number of animations. May not reflect the actual size.
   * @param animations        The actual animations in the animation archive.
   */
  private Motion(int totalAnimationIds, Set<GNTAnimation> animations) {
    this.totalAnimationIds = totalAnimationIds;
    this.animations = animations;
    this.idToAnimation = Maps.newHashMapWithExpectedSize(animations.size());
    for (GNTAnimation animation : animations) {
      idToAnimation.put(animation.getId(), animation);
    }
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
      Set<GNTAnimation> animations = new HashSet<>();
      for (Entry<Integer, Integer> entry : offsetToId.entrySet()) {
        int offset = entry.getKey();
        int id = entry.getValue();
        raf.seek(offset);
        animations.add(GNTAnimation.parseFrom(raf, id));
      }
      return new Motion(totalAnimationIds, animations);
    }
  }

  public static Motion parseFromDirectory(Path inputDirectory) throws IOException {
    if (!Files.isDirectory(inputDirectory)) {
      throw new IllegalArgumentException("inputDirectory not a directory: " + inputDirectory);
    }
    List<Path> files = Files.walk(inputDirectory).filter(Files::isRegularFile)
        .collect(Collectors.toList());
    int largestId = getLargestId(inputDirectory);
    Set<GNTAnimation> animations = new HashSet<>();
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
        animations.add(amimation);
      }
    }
    return new Motion(largestId, animations);
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

  /**
   * Packs the current motion into a .mot file. The logic is a little weird because the id offsets
   * are in increasing order but the animations are in decreasing order.
   *
   * @param testFile
   * @throws IOException
   */
  public void pack(Path testFile) throws IOException {
    int fileSize = 0;
    try(RandomAccessFile raf = new RandomAccessFile(testFile.toFile(), "rw")) {
      raf.write(new byte[4]); // padding
      raf.write(ByteUtils.fromInt32(totalAnimationIds)); // total animation ids
      raf.write(new byte[]{0x00, 0x00, 0x00, 0x10}); // header size
      raf.write(new byte[4]); // file size (will be filled out last)

      // Header size + 4-byte offsets for each animation id, 16-byte aligned
      int currentDataOffset = byteAlign(0x10 + (totalAnimationIds * 4));
      raf.seek(currentDataOffset);
      fileSize += currentDataOffset;
      // The animation offsets that will be reversed before written to the file
      List<Integer> offsets = new ArrayList<>();

      // Get the animations and write them to the offsets and data section
      for (int i = totalAnimationIds - 1; i >= 0; i--) {
        GNTAnimation animation = idToAnimation.get(i);
        if (animation != null) {
          offsets.add(currentDataOffset);
          byte[] bytes = animation.getBytes();
          currentDataOffset += bytes.length;
          fileSize += bytes.length;
          raf.write(bytes);
        } else {
          offsets.add(0);
        }
      }

      // Write out the rest of the mot header, specifically the offsets and file size
      raf.seek(0x10);
      // Write the offsets forwards, since they were written out to the list backwards
      Collections.reverse(offsets);
      for (Integer offset : offsets) {
        raf.write(ByteUtils.fromInt32(offset));
      }
      raf.seek(0xC);
      raf.write(ByteUtils.fromInt32(fileSize));
    }
  }

  private static int byteAlign(int number) {
    if (number % 16 != 0) {
      return number + (16 - (number % 16));
    }
    return number;
  }

  private static int getLargestId(Path inputDirectory) throws IOException {
    Path totalAnimationIdsPath = inputDirectory.resolve("totalAnimationIds");
    if (Files.isRegularFile(totalAnimationIdsPath)) {
      String number = Files.readString(totalAnimationIdsPath);
      return Integer.valueOf(number);
    }
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Motion motion = (Motion) o;
    return totalAnimationIds == motion.totalAnimationIds && animations.equals(motion.animations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalAnimationIds, animations);
  }
}
