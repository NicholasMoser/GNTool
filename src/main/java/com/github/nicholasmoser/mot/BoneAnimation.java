package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The animation of a specific bone. One {@link GNTAnimation} will have one or more of these.
 */
public class BoneAnimation {

  private static final Charset JUNK_ENCODING = StandardCharsets.ISO_8859_1;
  private final int offset;
  private final short numOfKeyFrames;
  private int timeValuesOffset;
  private int coordinatesOffset;
  private final List<Coordinate> coordinates;
  private final List<Float> timeValues;
  private final String junk1;
  private final String junk2;
  private short flags1;
  private short trackFlag;
  private short boneId;
  private float totalTime;

  public BoneAnimation(int offset, short flags1, short trackFlag, short boneId,
      short numOfKeyFrames, float totalTime, int timeValuesOffset,
      int coordinatesOffset, List<Coordinate> coordinates, List<Float> timeValues,
      String junk1, String junk2) {
    this.offset = offset;
    this.flags1 = flags1;
    this.trackFlag = trackFlag;
    this.boneId = boneId;
    this.numOfKeyFrames = numOfKeyFrames;
    this.totalTime = totalTime;
    this.timeValuesOffset = timeValuesOffset;
    this.coordinatesOffset = coordinatesOffset;
    this.coordinates = coordinates;
    this.timeValues = timeValues;
    this.junk1 = junk1;
    this.junk2 = junk2;
  }

  public int getOffset() {
    return offset;
  }

  public short getNumOfKeyFrames() {
    return numOfKeyFrames;
  }

  public short getFlags1() {
    return flags1;
  }

  public short getTrackFlag() {
    return trackFlag;
  }

  public short getBoneId() {
    return boneId;
  }

  public float getTotalTime() {
    return totalTime;
  }

  public List<Coordinate> getCoordinates() {
    return coordinates;
  }

  public int getCoordinatesOffset() {
    return coordinatesOffset;
  }

  public List<Float> getTimeValues() {
    return timeValues;
  }

  public int getTimeValuesOffset() {
    return timeValuesOffset;
  }

  public void setFlags1(short flags1) {
    this.flags1 = flags1;
  }

  public void setTrackFlag(short trackFlag) {
    this.trackFlag = trackFlag;
  }

  public void setBoneId(short boneId) {
    this.boneId = boneId;
  }

  public void setTotalTime(float totalTime) {
    this.totalTime = totalTime;
  }

  public void setTimeValuesOffset(int timeValuesOffset) {
    this.timeValuesOffset = timeValuesOffset;
  }

  public void setCoordinatesOffset(int coordinatesOffset) {
    this.coordinatesOffset = coordinatesOffset;
  }

  /**
   * Parse the bone animation from the file at the current offset. The offset of the parent
   * animation is also required to correctly read the data.
   *
   * @param raf The file to read from.
   * @param animationOffset The offset of the parent animation.
   * @return The bone animation.
   * @throws IOException If an I/O error occurs
   */
  public static BoneAnimation parseFrom(RandomAccessFile raf, int animationOffset)
      throws IOException {
    // Read the bone animation header
    int offset = (int) (raf.getFilePointer() - animationOffset);
    short flags1 = ByteUtils.readInt16(raf);
    short trackFlag = ByteUtils.readInt16(raf);
    short boneId = ByteUtils.readInt16(raf);
    short numOfKeyFrames = ByteUtils.readInt16(raf);
    float totalTime = ByteUtils.readFloat(raf);
    skipWordPadding(raf);
    int timeValuesOffset = ByteUtils.readInt32(raf);
    int coordinatesOffset = ByteUtils.readInt32(raf);
    ByteUtils.byteAlign(raf, 16);

    // Save the spot of the next bone animation header
    long nextKeyFrameHeaderOffset = raf.getFilePointer();

    raf.seek(animationOffset + timeValuesOffset);
    List<Float> timeValues = new ArrayList<>();
    for (int i = 0; i < numOfKeyFrames; i++) {
      timeValues.add(ByteUtils.readFloat(raf));
    }
    String junk1 = readJunkData(raf);

    // Handle coordinates, if they exist
    List<Coordinate> coordinates = new ArrayList<>();
    String junk2 = null;
    if ((flags1 & 0x0200) != 0) {
      if (raf.getFilePointer() != animationOffset + coordinatesOffset) {
        throw new IOException("Second animation values do not follow first.");
      }
      if ((flags1 & 0x0002) != 0) {
        for (int i = 0; i < numOfKeyFrames; i++) {
          short x = ByteUtils.readInt16(raf);
          short y = ByteUtils.readInt16(raf);
          short z = ByteUtils.readInt16(raf);
          short w = ByteUtils.readInt16(raf);
          coordinates.add(new Coordinate(x, y, z, w));
        }
      } else if ((flags1 & 0x0004) != 0) {
        for (int i = 0; i < numOfKeyFrames; i++) {
          float x = ByteUtils.readFloat(raf);
          float y = ByteUtils.readFloat(raf);
          float z = ByteUtils.readFloat(raf);
          float w = ByteUtils.readFloat(raf);
          coordinates.add(new Coordinate(x, y, z, w));
        }
      }
      junk2 = readJunkData(raf);
    }

    // Move the offset back to the next bone animation
    raf.seek(nextKeyFrameHeaderOffset);

    return new Builder()
        .offset(offset)
        .flags1(flags1)
        .trackFlag(trackFlag)
        .boneId(boneId)
        .numOfKeyFrames(numOfKeyFrames)
        .totalTime(totalTime)
        .timeValuesOffset(timeValuesOffset)
        .coordinatesOffset(coordinatesOffset)
        .coordinates(coordinates)
        .timeValues(timeValues)
        .junk1(junk1)
        .junk2(junk2)
        .create();
  }

  /**
   * @return The header bytes of this bone animation.
   * @throws IOException If an I/O error occurs
   */
  public byte[] getHeaderBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ByteUtils.fromUint16(flags1));
    baos.write(ByteUtils.fromUint16(trackFlag));
    baos.write(ByteUtils.fromUint16(boneId));
    baos.write(ByteUtils.fromUint16(numOfKeyFrames));
    baos.write(ByteUtils.fromFloat(totalTime));
    baos.write(new byte[4]);
    baos.write(ByteUtils.fromInt32(timeValuesOffset));
    baos.write(ByteUtils.fromInt32(coordinatesOffset));
    baos.write(new byte[8]);
    return baos.toByteArray();
  }

  /**
   * @return The bytes, excluding the header, of this bone animation.
   * @throws IOException If an I/O error occurs
   */
  public byte[] getDataBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (Float value : timeValues) {
      baos.write(ByteUtils.fromFloat(value));
    }
    if (junk1 != null) {
      baos.write(junk1.getBytes(JUNK_ENCODING));
    }
    // TODO: Use logic to actually pad when more animations are added
    //if (baos.size() % 16 != 0) {
    //  // Pad to 16-byte alignment
    //  int size = 16 - (baos.size() % 16);
    //  baos.write(new byte[size]);
    //}
    for (Coordinate coordinate : coordinates) {
      baos.write(ByteUtils.fromUint16(coordinate.getX()));
      baos.write(ByteUtils.fromUint16(coordinate.getY()));
      baos.write(ByteUtils.fromUint16(coordinate.getZ()));
      baos.write(ByteUtils.fromUint16(coordinate.getW()));
    }
    if (junk2 != null) {
      baos.write(junk2.getBytes(JUNK_ENCODING));
    }
    return baos.toByteArray();
  }

  /**
   * Reads junk data until 16 byte alignment in the file.
   *
   * @param raf The file to read from.
   * @return The junk, if any. May be null.
   * @throws IOException If an I/O error occurs
   */
  private static String readJunkData(RandomAccessFile raf) throws IOException {
    long offset = raf.getFilePointer();
    if (offset % 16 != 0) {
      int size = (int) (16 - (offset % 16));
      byte[] bytes = new byte[size];
      if (raf.read(bytes) != size) {
        throw new IOException(String.format("Failed to read %d bytes at offset %d", size, offset));
      }
      return new String(bytes, JUNK_ENCODING);
    }
    return null;
  }

  /**
   * Skips a word of padding. This will throw an {@link IOException} if it is not 0.
   *
   * @param raf The file to read from.
   * @throws IOException If an I/O error occurs or the padding is not 0
   */
  private static void skipWordPadding(RandomAccessFile raf) throws IOException {
    int padding2 = ByteUtils.readInt32(raf);
    if (padding2 != 0) {
      long offset = raf.getFilePointer();
      throw new IOException("Padding must be 0 at offset " + (offset - 4));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoneAnimation that = (BoneAnimation) o;
    return offset == that.offset && flags1 == that.flags1 && trackFlag == that.trackFlag
        && boneId == that.boneId && numOfKeyFrames == that.numOfKeyFrames
        && Float.compare(that.totalTime, totalTime) == 0
        && timeValuesOffset == that.timeValuesOffset
        && coordinatesOffset == that.coordinatesOffset && Objects.equals(coordinates,
        that.coordinates) && Objects.equals(junk1, that.junk1) && Objects.equals(
        junk2, that.junk2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offset, flags1, trackFlag, boneId, numOfKeyFrames,
        totalTime, timeValuesOffset, coordinatesOffset, coordinates, junk1, junk2);
  }

  public static class Builder {

    private int offset;
    private short flags1;
    private short trackFlag;
    private short boneId;
    private short numOfKeyFrames;
    private float totalTime;
    private int timeValuesOffset;
    private int coordinatesOffset;
    private List<Coordinate> coordinates;
    private List<Float> timeValues;
    private String junk1;
    private String junk2;

    public BoneAnimation.Builder offset(int offset) {
      this.offset = offset;
      return this;
    }

    public BoneAnimation.Builder flags1(short flags1) {
      this.flags1 = flags1;
      return this;
    }

    public BoneAnimation.Builder trackFlag(short trackFlag) {
      this.trackFlag = trackFlag;
      return this;
    }

    public BoneAnimation.Builder boneId(short boneId) {
      this.boneId = boneId;
      return this;
    }

    public BoneAnimation.Builder numOfKeyFrames(short numOfKeyFrames) {
      this.numOfKeyFrames = numOfKeyFrames;
      return this;
    }

    public BoneAnimation.Builder totalTime(float totalTime) {
      this.totalTime = totalTime;
      return this;
    }

    public BoneAnimation.Builder timeValuesOffset(int timeValuesOffset) {
      this.timeValuesOffset = timeValuesOffset;
      return this;
    }

    public BoneAnimation.Builder coordinatesOffset(int coordinatesOffset) {
      this.coordinatesOffset = coordinatesOffset;
      return this;
    }

    public BoneAnimation.Builder coordinates(List<Coordinate> coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    public BoneAnimation.Builder timeValues(List<Float> timeValues) {
      this.timeValues = timeValues;
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
      return new BoneAnimation(offset, flags1, trackFlag, boneId, numOfKeyFrames,
          totalTime, timeValuesOffset, coordinatesOffset, coordinates,
          timeValues, junk1, junk2);
    }
  }
}
