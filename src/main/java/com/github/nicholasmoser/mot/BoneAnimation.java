package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The animation of a specific bone. One {@link GNTAnimation} will have one or more of these.
 */
public class BoneAnimation {

  private static final Charset JUNK_ENCODING = StandardCharsets.ISO_8859_1;
  private final int offset;
  private final short unknown4;
  private final short unknown5;
  private final short maybeBoneId;
  private final short numOfKeyFrames;
  private final float unknown6;
  private final int functionCurveOffset;
  private final int coordinatesOffset;
  private final List<Coordinate> coordinates;
  private final String junk1;
  private final String junk2;

  public BoneAnimation(int offset, short unknown4, short unknown5, short maybeBoneId,
      short numOfKeyFrames, float unknown6, int functionCurveOffset, int coordinatesOffset,
      List<Coordinate> coordinates, String junk1, String junk2) {
    this.offset = offset;
    this.unknown4 = unknown4;
    this.unknown5 = unknown5;
    this.maybeBoneId = maybeBoneId;
    this.numOfKeyFrames = numOfKeyFrames;
    this.unknown6 = unknown6;
    this.functionCurveOffset = functionCurveOffset;
    this.coordinatesOffset = coordinatesOffset;
    this.coordinates = coordinates;
    this.junk1 = junk1;
    this.junk2 = junk2;
  }

  public int getOffset() {
    return offset;
  }

  public short getUnknown4() {
    return unknown4;
  }

  public short getUnknown5() {
    return unknown5;
  }

  public short getMaybeBoneId() {
    return maybeBoneId;
  }

  public short getNumOfKeyFrames() {
    return numOfKeyFrames;
  }

  public float getUnknown6() {
    return unknown6;
  }

  public int getFunctionCurveOffset() {
    return functionCurveOffset;
  }

  public int getCoordinatesOffset() {
    return coordinatesOffset;
  }

  public List<Coordinate> getCoordinates() {
    return coordinates;
  }

  public String getJunk1() {
    return junk1;
  }

  public String getJunk2() {
    return junk2;
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
    short unknown4 = ByteUtils.readInt16(raf);
    short unknown5 = ByteUtils.readInt16(raf);
    short maybeBoneId = ByteUtils.readInt16(raf);
    short numOfKeyFrames = ByteUtils.readInt16(raf);
    float unknown6 = ByteUtils.readFloat(raf);
    skipWordPadding(raf);
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
      coordinates.add(new Coordinate(x, y, z, w, functionCurveValues.get(i)));
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
        .coordinates(coordinates)
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
    baos.write(ByteUtils.fromUint16(unknown4));
    baos.write(ByteUtils.fromUint16(unknown5));
    baos.write(ByteUtils.fromUint16(maybeBoneId));
    baos.write(ByteUtils.fromUint16(numOfKeyFrames));
    baos.write(ByteUtils.fromFloat(unknown6));
    baos.write(new byte[4]);
    baos.write(ByteUtils.fromInt32(functionCurveOffset));
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
    for (Coordinate coordinate : coordinates) {
      baos.write(ByteUtils.fromFloat(coordinate.getFunctionCurveValue()));
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
    return offset == that.offset && unknown4 == that.unknown4 && unknown5 == that.unknown5
        && maybeBoneId == that.maybeBoneId && numOfKeyFrames == that.numOfKeyFrames
        && Float.compare(that.unknown6, unknown6) == 0
        && functionCurveOffset == that.functionCurveOffset
        && coordinatesOffset == that.coordinatesOffset && Objects.equals(coordinates,
        that.coordinates) && Objects.equals(junk1, that.junk1) && Objects.equals(
        junk2, that.junk2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offset, unknown4, unknown5, maybeBoneId, numOfKeyFrames, unknown6,
        functionCurveOffset, coordinatesOffset, coordinates, junk1, junk2);
  }

  public static class Builder {

    private int offset;
    private short unknown4;
    private short unknown5;
    private short maybeBoneId;
    private short numOfKeyFrames;
    private float unknown6;
    private int functionCurveOffset;
    private int coordinatesOffset;
    private List<Coordinate> coordinates;
    private String junk1;
    private String junk2;

    public BoneAnimation.Builder offset(int offset) {
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

    public BoneAnimation.Builder unknown6(float unknown6) {
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
          functionCurveOffset, coordinatesOffset, coordinates, junk1, junk2);
    }
  }
}
