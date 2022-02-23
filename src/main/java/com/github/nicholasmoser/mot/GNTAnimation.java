package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
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
 * A GNTAnimation is a single animation. It contains one or more {@link BoneAnimation} objects.
 * It is the .gnta file extracted from a .mot file ({@link Motion}.
 */
public class GNTAnimation {

  private static final Charset JUNK_ENCODING = StandardCharsets.ISO_8859_1;
  private final int id;
  private final int unknown1;
  private final float bounciness;
  private final float repeatDelay;
  private final int playbackSpeed;
  private final short unknown2;
  private final short numberOfFunctionCurveValues;
  private final float unknown4;
  private final int dataOffset;
  private final int boneAnimationHeadersOffset;
  private final List<Float> functionCurveValues;
  private final String junk;
  private final List<BoneAnimation> boneAnimations;

  public GNTAnimation(int id, int unknown1, float bounciness,
      float repeatDelay, int playbackSpeed, short unknown2, short numberOfFunctionCurveValues,
      float unknown4, int dataOffset, int boneAnimationHeadersOffset,
      List<Float> functionCurveValues, String junk, List<BoneAnimation> boneAnimations) {
    this.id = id;
    this.unknown1 = unknown1;
    this.bounciness = bounciness;
    this.repeatDelay = repeatDelay;
    this.playbackSpeed = playbackSpeed;
    this.unknown2 = unknown2;
    this.numberOfFunctionCurveValues = numberOfFunctionCurveValues;
    this.unknown4 = unknown4;
    this.dataOffset = dataOffset;
    this.boneAnimationHeadersOffset = boneAnimationHeadersOffset;
    this.functionCurveValues = functionCurveValues;
    this.junk = junk;
    this.boneAnimations = boneAnimations;
  }

  public int getUnknown1() {
    return unknown1;
  }

  public float getBounciness() {
    return bounciness;
  }

  public float getRepeatDelay() {
    return repeatDelay;
  }

  public int getPlaybackSpeed() {
    return playbackSpeed;
  }

  public short getUnknown2() {
    return unknown2;
  }

  public short getNumberOfFunctionCurveValues() {
    return numberOfFunctionCurveValues;
  }

  public float getUnknown4() {
    return unknown4;
  }

  public int getDataOffset() {
    return dataOffset;
  }

  public int getBoneAnimationHeadersOffset() {
    return boneAnimationHeadersOffset;
  }

  public List<Float> getFunctionCurveValues() {
    return functionCurveValues;
  }

  public String getJunk() {
    return junk;
  }

  public List<BoneAnimation> getBoneAnimations() {
    return boneAnimations;
  }

  /**
   * Parse the animation from the file at the current offset with the given animation id.
   *
   * @param raf The file to read from.
   * @param id The animation id.
   * @return The animation object.
   * @throws IOException If an I/O error occurs
   */
  public static GNTAnimation parseFrom(RandomAccessFile raf, int id) throws IOException {
    int animationOffset = (int) raf.getFilePointer();

    // Parse the animation header
    int numOfBoneAnimations = ByteUtils.readInt32(raf) - 1; // one less
    int unknown1 = ByteUtils.readInt32(raf);
    float bounciness = ByteUtils.readFloat(raf);
    float repeatDelay = ByteUtils.readFloat(raf);
    int playbackSpeed = ByteUtils.readInt32(raf);
    short unknown2 = ByteUtils.readInt16(raf);
    short numberOfFunctionCurveValues = ByteUtils.readInt16(raf);
    float unknown4 = ByteUtils.readFloat(raf);
    skipWordPadding(raf);
    int functionCurveValuesOffset = ByteUtils.readInt32(raf);

    // Save the spot of the bone animation headers
    ByteUtils.byteAlign(raf, 16);
    int boneAnimationHeadersOffset = (int) (raf.getFilePointer() - animationOffset);

    // Parse the function curve values
    raf.seek(animationOffset + functionCurveValuesOffset);
    List<Float> functionCurveValues = new ArrayList<>();
    for (int i = 0; i < numberOfFunctionCurveValues; i++) {
      functionCurveValues.add(ByteUtils.readFloat(raf));
    }
    String junk = readJunkData(raf);

    // Parse the bone animations
    raf.seek(animationOffset + boneAnimationHeadersOffset);
    List<BoneAnimation> boneAnimations = new ArrayList<>();
    for (int i = 0; i < numOfBoneAnimations; i++) {
      boneAnimations.add(BoneAnimation.parseFrom(raf, animationOffset));
    }
    return new Builder()
        .id(id)
        .unknown1(unknown1)
        .bounciness(bounciness)
        .repeatDelay(repeatDelay)
        .playbackSpeed(playbackSpeed)
        .unknown2(unknown2)
        .numberOfFunctionCurveValues(numberOfFunctionCurveValues)
        .unknown4(unknown4)
        .functionCurveValuesOffset(functionCurveValuesOffset)
        .boneAnimationHeadersOffset(boneAnimationHeadersOffset)
        .functionCurveValues(functionCurveValues)
        .junk(junk)
        .boneAnimations(boneAnimations)
        .create();
  }

  /**
   * @return The animation id.
   */
  public int getId() {
    return id;
  }

  /**
   * @return The bytes of the animation.
   * @throws IOException If an I/O error occurs
   */
  public byte[] getBytes() throws IOException {
    ByteArrayOutputStream header = new ByteArrayOutputStream();
    ByteArrayOutputStream data = new ByteArrayOutputStream();

    // Write out the animation header and function curve values
    header.write(ByteUtils.fromInt32(boneAnimations.size() + 1));
    header.write(ByteUtils.fromInt32(unknown1));
    header.write(ByteUtils.fromFloat(bounciness));
    header.write(ByteUtils.fromFloat(repeatDelay));
    header.write(ByteUtils.fromInt32(playbackSpeed));
    header.write(ByteUtils.fromUint16(unknown2));
    header.write(ByteUtils.fromUint16(numberOfFunctionCurveValues));
    header.write(ByteUtils.fromFloat(unknown4));
    header.write(new byte[4]);
    header.write(ByteUtils.fromInt32(dataOffset)); // TODO: Calculate the offset
    header.write(new byte[12]);
    for (Float functionCurveValue : functionCurveValues) {
      data.write(ByteUtils.fromFloat(functionCurveValue));
    }
    if (junk != null) {
      data.write(junk.getBytes(JUNK_ENCODING));
    }

    // Write out each bone animation header and data for them
    for (BoneAnimation boneAnimation : boneAnimations) {
      header.write(boneAnimation.getHeaderBytes());
      data.write(boneAnimation.getDataBytes());
    }
    return Bytes.concat(header.toByteArray(), data.toByteArray());
  }

  /**
   * @return The size in bytes of this animation.
   * @throws IOException If an I/O error occurs
   */
  public int getSize() throws IOException {
    return getBytes().length;
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
    GNTAnimation that = (GNTAnimation) o;
    return id == that.id
        && unknown1 == that.unknown1 && Float.compare(that.bounciness, bounciness) == 0
        && Float.compare(that.repeatDelay, repeatDelay) == 0
        && playbackSpeed == that.playbackSpeed && unknown2 == that.unknown2
        && numberOfFunctionCurveValues == that.numberOfFunctionCurveValues
        && unknown4 == that.unknown4 && dataOffset == that.dataOffset
        && boneAnimationHeadersOffset == that.boneAnimationHeadersOffset && Objects.equals(
        functionCurveValues, that.functionCurveValues) && Objects.equals(junk, that.junk)
        && Objects.equals(boneAnimations, that.boneAnimations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, unknown1, bounciness, repeatDelay, playbackSpeed,
        unknown2, numberOfFunctionCurveValues, unknown4, dataOffset, boneAnimationHeadersOffset,
        functionCurveValues, junk, boneAnimations);
  }

  public static class Builder {

    private int id;
    private int unknown1;
    private float bounciness;
    private float repeatDelay;
    private int playbackSpeed;
    private short unknown2;
    private short numberOfFunctionCurveValues;
    private float unknown4;
    private int functionCurveValuesOffset;
    private int boneAnimationHeadersOffset;
    private List<Float> functionCurveValues;
    private String junk;
    private List<BoneAnimation> boneAnimations;

    public GNTAnimation.Builder id(int id) {
      this.id = id;
      return this;
    }

    public GNTAnimation.Builder unknown1(int unknown1) {
      this.unknown1 = unknown1;
      return this;
    }

    public GNTAnimation.Builder bounciness(float bounciness) {
      this.bounciness = bounciness;
      return this;
    }

    public GNTAnimation.Builder repeatDelay(float repeatDelay) {
      this.repeatDelay = repeatDelay;
      return this;
    }

    public GNTAnimation.Builder playbackSpeed(int playbackSpeed) {
      this.playbackSpeed = playbackSpeed;
      return this;
    }

    public GNTAnimation.Builder unknown2(short unknown2) {
      this.unknown2 = unknown2;
      return this;
    }

    public GNTAnimation.Builder numberOfFunctionCurveValues(short numberOfFunctionCurveValues) {
      this.numberOfFunctionCurveValues = numberOfFunctionCurveValues;
      return this;
    }

    public GNTAnimation.Builder unknown4(float unknown4) {
      this.unknown4 = unknown4;
      return this;
    }

    public GNTAnimation.Builder functionCurveValuesOffset(int functionCurveValuesOffset) {
      this.functionCurveValuesOffset = functionCurveValuesOffset;
      return this;
    }

    public GNTAnimation.Builder boneAnimationHeadersOffset(int boneAnimationHeadersOffset) {
      this.boneAnimationHeadersOffset = boneAnimationHeadersOffset;
      return this;
    }

    public GNTAnimation.Builder functionCurveValues(List<Float> functionCurveValues) {
      this.functionCurveValues = Collections.unmodifiableList(functionCurveValues);
      return this;
    }

    public GNTAnimation.Builder junk(String junk) {
      this.junk = junk;
      return this;
    }

    public GNTAnimation.Builder boneAnimations(List<BoneAnimation> boneAnimations) {
      this.boneAnimations = Collections.unmodifiableList(boneAnimations);
      return this;
    }

    public GNTAnimation create() {
      return new GNTAnimation(id, unknown1, bounciness, repeatDelay,
          playbackSpeed, unknown2, numberOfFunctionCurveValues, unknown4, functionCurveValuesOffset,
          boneAnimationHeadersOffset, functionCurveValues, junk, boneAnimations);
    }
  }
}
