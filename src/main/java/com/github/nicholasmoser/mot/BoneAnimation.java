package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoneAnimation {

  private final long offset;
  private final short unknown4;
  private final short unknown5;
  private final short maybeBoneId;
  private final short numOfKeyFrames;
  private final int unknown6;
  private final int animValuesOffset1;
  private final int animValuesOffset2;
  private final List<Float> functionCurveValues;
  private final List<Coordinate> coordinates;
  private final String junk1;
  private final String junk2;

  public BoneAnimation(long offset, short unknown4, short unknown5, short maybeBoneId,
      short numOfKeyFrames, int unknown6, int animValuesOffset1, int animValuesOffset2,
      List<Float> functionCurveValues, List<Coordinate> coordinates, String junk1, String junk2) {
    this.offset = offset;
    this.unknown4 = unknown4;
    this.unknown5 = unknown5;
    this.maybeBoneId = maybeBoneId;
    this.numOfKeyFrames = numOfKeyFrames;
    this.unknown6 = unknown6;
    this.animValuesOffset1 = animValuesOffset1;
    this.animValuesOffset2 = animValuesOffset2;
    this.functionCurveValues = functionCurveValues;
    this.coordinates = coordinates;
    this.junk1 = junk1;
    this.junk2 = junk2;
  }

  public static BoneAnimation parseFrom(RandomAccessFile raf, long animationOffset)
      throws IOException {
    // Read the bone animation header
    long offset = raf.getFilePointer();
    short unknown4 = ByteUtils.readInt16(raf);
    short unknown5 = ByteUtils.readInt16(raf);
    short maybeBoneId = ByteUtils.readInt16(raf);
    short numOfKeyFrames = ByteUtils.readInt16(raf);
    int unknown6 = ByteUtils.readInt32(raf);
    skipPadding(raf, 4);
    int functionCurveOffset = ByteUtils.readInt32(raf);
    int coordinatesOffset = ByteUtils.readInt32(raf);
    ByteUtils.byteAlign(raf, 16);

    // Save the spot of the next bone animation header
    long nextKeyFrameHeaderOffset = raf.getFilePointer();

    raf.seek(animationOffset + functionCurveOffset);
    List<Float> functionCurveValues = new ArrayList<>();
    for (int i = 0; i < numOfKeyFrames; i++) {
      functionCurveValues.add(ByteUtils.readFloat(raf));
    }
    String junk1 = readJunkData(raf);
    if (raf.getFilePointer() != animationOffset + coordinatesOffset) {
      throw new IOException("Second animation values do not follow first.");
    }
    List<Coordinate> coordinates = new ArrayList<>();
    for (int i = 0; i < numOfKeyFrames; i++) {
      short x = ByteUtils.readInt16(raf);
      short y = ByteUtils.readInt16(raf);
      short z = ByteUtils.readInt16(raf);
      short w = ByteUtils.readInt16(raf);
      coordinates.add(new Coordinate(x, y, z, w));
    }
    String junk2 = readJunkData(raf);

    // Move the offset back to the next bone animation
    raf.seek(nextKeyFrameHeaderOffset);

    return new Builder()
        .offset(offset)
        .unknown4(unknown4)
        .unknown5(unknown5)
        .maybeBoneId(maybeBoneId)
        .numOfKeyFrames(numOfKeyFrames)
        .unknown6(unknown6)
        .functionCurveOffset(functionCurveOffset)
        .coordinatesOffset(coordinatesOffset)
        .functionCurveValues(functionCurveValues)
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
    private short numOfKeyFrames;
    private int unknown6;
    private int functionCurveOffset;
    private int coordinatesOffset;
    private List<Float> functionCurveValues;
    private List<Coordinate> coordinates;
    private String junk1;
    private String junk2;

    public BoneAnimation.Builder offset(long offset) {
      this.offset = offset;
      return this;
    }

    public BoneAnimation.Builder unknown4(short unknown4) {
      this.unknown4 = unknown4;
      return this;
    }

    public BoneAnimation.Builder unknown5(short unknown5) {
      this.unknown5 = unknown5;
      return this;
    }

    public BoneAnimation.Builder maybeBoneId(short maybeBoneId) {
      this.maybeBoneId = maybeBoneId;
      return this;
    }

    public BoneAnimation.Builder numOfKeyFrames(short numOfKeyFrames) {
      this.numOfKeyFrames = numOfKeyFrames;
      return this;
    }

    public BoneAnimation.Builder unknown6(int unknown6) {
      this.unknown6 = unknown6;
      return this;
    }

    public BoneAnimation.Builder functionCurveOffset(int functionCurveOffset) {
      this.functionCurveOffset = functionCurveOffset;
      return this;
    }

    public BoneAnimation.Builder coordinatesOffset(int coordinatesOffset) {
      this.coordinatesOffset = coordinatesOffset;
      return this;
    }

    public BoneAnimation.Builder functionCurveValues(List<Float> functionCurveValues) {
      this.functionCurveValues = Collections.unmodifiableList(functionCurveValues);
      return this;
    }

    public BoneAnimation.Builder coordinates(List<Coordinate> coordinates) {
      this.coordinates = Collections.unmodifiableList(coordinates);
      return this;
    }

    public BoneAnimation.Builder junk1(String junk1) {
      this.junk1 = junk1;
      return this;
    }

    public BoneAnimation.Builder junk2(String junk2) {
      this.junk2 = junk2;
      return this;
    }

    public BoneAnimation create() {
      return new BoneAnimation(offset, unknown4, unknown5, maybeBoneId, numOfKeyFrames, unknown6,
          functionCurveOffset, coordinatesOffset, functionCurveValues, coordinates, junk1, junk2);
    }
  }
}
