package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A GNTAnimation contains one or more {@link KeyFrame} objects. It is the .gnta file extracted from
 * a .mot file ({@link Motion}.
 */
public class GNTAnimation {

  private final int offset;
  private final int id;
  private final byte[] bytes;

  public GNTAnimation(int offset, int id, byte[] bytes) {
    this.offset = offset;
    this.id = id;
    this.bytes = bytes;
  }

  public static GNTAnimation parseFrom(RandomAccessFile raf, int id) throws IOException {
    int animationOffset = (int) raf.getFilePointer();

    // Parse the animation header
    int numOfEntries = ByteUtils.readInt32(raf);
    int unknown1 = ByteUtils.readInt32(raf);
    float bounciness = ByteUtils.readFloat(raf);
    float repeatDelay = ByteUtils.readFloat(raf);
    int playbackSpeed = ByteUtils.readInt32(raf);
    short unknown2 = ByteUtils.readInt16(raf);
    short count = ByteUtils.readInt16(raf);
    int unknown4 = ByteUtils.readInt32(raf);
    skipPadding(raf, 4);
    int dataOffset = ByteUtils.readInt32(raf);

    // Save the spot of the key frame headers
    ByteUtils.byteAlign(raf, 16);
    long keyFrameHeadersOffset = raf.getFilePointer();

    // Parse the initial key frame
    raf.seek(animationOffset + dataOffset);
    List<Float> values = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      values.add(ByteUtils.readFloat(raf));
    }
    String junk = readJunkData(raf);

    // Parse the rest of the key frames
    raf.seek(keyFrameHeadersOffset);
    List<KeyFrame> keyFrames = new ArrayList<>();
    for (int i = 0; i < numOfEntries - 1; i++) {
      keyFrames.add(KeyFrame.parseFrom(raf, animationOffset));
    }
    return null;
  }

  /**
   * Reads junk data until 16 byte alignment in the file.
   *
   * @param raf
   * @return
   * @throws IOException
   */
  private static String readJunkData(RandomAccessFile raf) throws IOException {
    long offset = raf.getFilePointer();
    if (offset % 16 != 0) {
      int size = (int) (16 - (offset % 16));
      byte[] bytes = new byte[size];
      if (raf.read(bytes) != size) {
        throw new IOException(String.format("Failed to read %d bytes at offset %d", size, offset));
      }
      return new String(bytes, StandardCharsets.ISO_8859_1);
    }
    return null;
  }

  private static void skipPadding(RandomAccessFile raf, int bytes) throws IOException {
    int padding2 = ByteUtils.readInt32(raf);
    if (padding2 != 0) {
      long offset = raf.getFilePointer();
      throw new IOException("Padding must be 0 at offset " + (offset - 4));
    }
  }
}
