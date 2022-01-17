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

/**
 * A GNTAnimation contains one or more {@link BoneAnimation} objects. It is the .gnta file extracted
 * from a .mot file ({@link Motion}.
 */
public class GNTAnimation {

  private static final Charset JUNK_ENCODING = StandardCharsets.ISO_8859_1;
  private final int id;
  private final int numOfBoneAnimations;
  private final int unknown1;
  private final float bounciness;
  private final float repeatDelay;
  private final int playbackSpeed;
  private final short unknown2;
  private final short numberOfFunctionCurveValues;
  private final int unknown4;
  private final int dataOffset;
  private final long boneAnimationHeadersOffset;
  private final List<Float> functionCurveValues;
  private final String junk;
  private final List<BoneAnimation> boneAnimations;

  public GNTAnimation(int id, int numOfBoneAnimations, int unknown1, float bounciness,
      float repeatDelay, int playbackSpeed, short unknown2, short numberOfFunctionCurveValues,
      int unknown4, int dataOffset, long boneAnimationHeadersOffset,
      List<Float> functionCurveValues, String junk, List<BoneAnimation> boneAnimations) {
    this.id = id;
    this.numOfBoneAnimations = numOfBoneAnimations;
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
    int unknown4 = ByteUtils.readInt32(raf);
    skipPadding(raf, 4);
    int functionCurveValuesOffset = ByteUtils.readInt32(raf);

    // Save the spot of the bone animation headers
    ByteUtils.byteAlign(raf, 16);
    long boneAnimationHeadersOffset = raf.getFilePointer();

    // Parse the function curve values
    raf.seek(animationOffset + functionCurveValuesOffset);
    List<Float> functionCurveValues = new ArrayList<>();
    for (int i = 0; i < numberOfFunctionCurveValues; i++) {
      functionCurveValues.add(ByteUtils.readFloat(raf));
    }
    String junk = readJunkData(raf);

    // Parse the bone animations
    raf.seek(boneAnimationHeadersOffset);
    List<BoneAnimation> boneAnimations = new ArrayList<>();
    for (int i = 0; i < numOfBoneAnimations; i++) {
      boneAnimations.add(BoneAnimation.parseFrom(raf, animationOffset));
    }
    return new Builder()
        .id(id)
        .numOfBoneAnimations(numOfBoneAnimations)
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

  public int getId() {
    return id;
  }

  public byte[] getBytes() throws IOException {
    ByteArrayOutputStream header = new ByteArrayOutputStream();
    ByteArrayOutputStream data = new ByteArrayOutputStream();

    // Write out the animation header and function curve values
    header.write(ByteUtils.fromInt32(numOfBoneAnimations + 1));
    header.write(ByteUtils.fromInt32(unknown1));
    header.write(ByteUtils.fromFloat(bounciness));
    header.write(ByteUtils.fromFloat(repeatDelay));
    header.write(ByteUtils.fromInt32(playbackSpeed));
    header.write(ByteUtils.fromUint16(unknown2));
    header.write(ByteUtils.fromUint16(numberOfFunctionCurveValues));
    header.write(ByteUtils.fromInt32(unknown4));
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
      return new String(bytes, JUNK_ENCODING);
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

    private int id;
    private int numOfBoneAnimations;
    private int unknown1;
    private float bounciness;
    private float repeatDelay;
    private int playbackSpeed;
    private short unknown2;
    private short numberOfFunctionCurveValues;
    private int unknown4;
    private int functionCurveValuesOffset;
    private long boneAnimationHeadersOffset;
    private List<Float> functionCurveValues;
    private String junk;
    private List<BoneAnimation> boneAnimations;

    public GNTAnimation.Builder id(int id) {
      this.id = id;
      return this;
    }

    public GNTAnimation.Builder numOfBoneAnimations(int numOfBoneAnimations) {
      this.numOfBoneAnimations = numOfBoneAnimations;
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

    public GNTAnimation.Builder unknown4(int unknown4) {
      this.unknown4 = unknown4;
      return this;
    }

    public GNTAnimation.Builder functionCurveValuesOffset(int functionCurveValuesOffset) {
      this.functionCurveValuesOffset = functionCurveValuesOffset;
      return this;
    }

    public GNTAnimation.Builder boneAnimationHeadersOffset(long boneAnimationHeadersOffset) {
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
      return new GNTAnimation(id, numOfBoneAnimations, unknown1, bounciness, repeatDelay,
          playbackSpeed, unknown2, numberOfFunctionCurveValues, unknown4, functionCurveValuesOffset,
          boneAnimationHeadersOffset, functionCurveValues, junk, boneAnimations);
    }
  }
}
