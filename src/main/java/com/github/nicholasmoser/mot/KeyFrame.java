package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.gecko.active.ActiveWrite32BitsCode;
import com.github.nicholasmoser.gecko.active.ActiveWrite32BitsCode.Builder;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KeyFrame {

  private final long offset;
  private final short unknown4;
  private final short unknown5;
  private final short maybeBoneId;
  private final short numOfFloats;
  private final int unknown6;
  private final int animValuesOffset1;
  private final int animValuesOffset2;
  private final List<Float> floats;
  private final List<Coordinate> coordinates;
  private final String junk1;
  private final String junk2;

  public KeyFrame(long offset, short unknown4, short unknown5, short maybeBoneId, short numOfFloats,
      int unknown6, int animValuesOffset1, int animValuesOffset2, List<Float> floats,
      List<Coordinate> coordinates, String junk1, String junk2) {
    this.offset = offset;
    this.unknown4 = unknown4;
    this.unknown5 = unknown5;
    this.maybeBoneId = maybeBoneId;
    this.numOfFloats = numOfFloats;
    this.unknown6 = unknown6;
    this.animValuesOffset1 = animValuesOffset1;
    this.animValuesOffset2 = animValuesOffset2;
    this.floats = floats;
    this.coordinates = coordinates;
    this.junk1 = junk1;
    this.junk2 = junk2;
  }

  public static KeyFrame parseFrom(RandomAccessFile raf, long animationOffset) throws IOException {
    long offset = raf.getFilePointer();
    short unknown4 = ByteUtils.readInt16(raf);
    short unknown5 = ByteUtils.readInt16(raf);
    short maybeBoneId = ByteUtils.readInt16(raf);
    short numOfFloats = ByteUtils.readInt16(raf);
    int unknown6 = ByteUtils.readInt32(raf);
    skipPadding(raf, 4);
    int animValuesOffset1 = ByteUtils.readInt32(raf);
    int animValuesOffset2 = ByteUtils.readInt32(raf);
    ByteUtils.byteAlign(raf, 16);

    // Save the spot of the next key frame header
    long nextKeyFrameHeaderOffset = raf.getFilePointer();

    // Parse the data of this key frame
    raf.seek(animationOffset + animValuesOffset1);
    List<Float> floats = new ArrayList<>();
    for (int i = 0; i < numOfFloats; i++) {
      floats.add(ByteUtils.readFloat(raf));
    }
    String junk1 = readJunkData(raf);
    if (raf.getFilePointer() != animationOffset + animValuesOffset2) {
      throw new IOException("Second animation values do not follow first.");
    }
    List<Coordinate> coordinates = new ArrayList<>();
    for (int i = 0; i < numOfFloats; i++) {
      short x = ByteUtils.readInt16(raf);
      short y = ByteUtils.readInt16(raf);
      short z = ByteUtils.readInt16(raf);
      short w = ByteUtils.readInt16(raf);
      coordinates.add(new Coordinate(x, y, z, w));
    }
    String junk2 = readJunkData(raf);

    // Move the offset back to the next key frame header
    raf.seek(nextKeyFrameHeaderOffset);
    return new Builder()
        .offset(offset)
        .unknown4(unknown4)
        .unknown5(unknown5)
        .maybeBoneId(maybeBoneId)
        .numOfFloats(numOfFloats)
        .unknown6(unknown6)
        .animValuesOffset1(animValuesOffset1)
        .animValuesOffset2(animValuesOffset2)
        .floats(floats)
        .coordinates(coordinates)
        .junk1(junk1)
        .junk2(junk2)
        .create();
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

  public static class Builder {

    private long offset;
    private short unknown4;
    private short unknown5;
    private short maybeBoneId;
    private short numOfFloats;
    private int unknown6;
    private int animValuesOffset1;
    private int animValuesOffset2;
    private List<Float> floats;
    private List<Coordinate> coordinates;
    private String junk1;
    private String junk2;

    public KeyFrame.Builder offset(long offset) {
      this.offset = offset;
      return this;
    }

    public KeyFrame.Builder unknown4(short unknown4) {
      this.unknown4 = unknown4;
      return this;
    }

    public KeyFrame.Builder unknown5(short unknown5) {
      this.unknown5 = unknown5;
      return this;
    }

    public KeyFrame.Builder maybeBoneId(short maybeBoneId) {
      this.maybeBoneId = maybeBoneId;
      return this;
    }

    public KeyFrame.Builder numOfFloats(short numOfFloats) {
      this.numOfFloats = numOfFloats;
      return this;
    }

    public KeyFrame.Builder unknown6(int unknown6) {
      this.unknown6 = unknown6;
      return this;
    }

    public KeyFrame.Builder animValuesOffset1(int animValuesOffset1) {
      this.animValuesOffset1 = animValuesOffset1;
      return this;
    }

    public KeyFrame.Builder animValuesOffset2(int animValuesOffset2) {
      this.animValuesOffset2 = animValuesOffset2;
      return this;
    }

    public KeyFrame.Builder floats(List<Float> floats) {
      this.floats = Collections.unmodifiableList(floats);
      return this;
    }

    public KeyFrame.Builder coordinates(List<Coordinate> coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    public KeyFrame.Builder junk1(String junk1) {
      this.junk1 = junk1;
      return this;
    }

    public KeyFrame.Builder junk2(String junk2) {
      this.junk2 = junk2;
      return this;
    }

    public KeyFrame create() {
      return new KeyFrame(offset, unknown4, unknown5, maybeBoneId, numOfFloats, unknown6, animValuesOffset1, animValuesOffset2, floats, coordinates, junk1, junk2);
    }
  }
}
