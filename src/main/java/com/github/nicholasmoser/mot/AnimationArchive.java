package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AnimationArchive {

  private AnimationArchive(int totalAnimationIds) {

  }

  public static AnimationArchive parseFrom(Path inputFile) throws IOException {
    try(RandomAccessFile raf = new RandomAccessFile(inputFile.toFile(), "r")) {
      // Parse and validate the header
      int nullBytes = ByteUtils.readInt32(raf);
      int totalAnimationIds = ByteUtils.readInt32(raf);
      int headerSize = ByteUtils.readInt32(raf);
      int fileSize = ByteUtils.readInt32(raf);
      if (nullBytes != 0x00) {
        throw new IllegalStateException(String.format("Null bytes not 0x00: 0x%x", nullBytes));
      } else if (headerSize != 0x10) {
        throw new IllegalStateException("Header size not supported: " + headerSize);
      } else if (totalAnimationIds < 1) {
        throw new IllegalStateException("Invalid animation id size: " + totalAnimationIds);
      } else if (fileSize < 1) {
        throw new IllegalStateException("Invalid file size: " + fileSize);
      }
      // Parse the animations
      Map<Integer, Integer> offsetToId = new HashMap<>();
      offsetToId.put(fileSize, -1);
      for (int i = 0; i < totalAnimationIds; i++) {
        int offset = ByteUtils.readInt32(raf);
        if (offset != 0) {
          offsetToId.put(offset, i);
        }
      }
      return null;
      //return new AnimationArchive(totalAnimationIds, fileSize)
    }
  }
}
