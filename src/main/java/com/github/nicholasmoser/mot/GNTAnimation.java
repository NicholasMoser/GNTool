package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A GNTAnimation is a single animation. It contains one or more {@link BoneAnimation} objects.
 * It is the .gnta file extracted from a .mot file ({@link Motion}.
 */
public class GNTAnimation {

  private final int id;
  private final List<BoneAnimation> boneAnimations;
  private float playSpeed;
  private float endTime;

  public GNTAnimation(int id, float playSpeed,
      float endTime, List<BoneAnimation> boneAnimations) {
    this.id = id;
    this.playSpeed = playSpeed;
    this.endTime = endTime;
    this.boneAnimations = boneAnimations;
  }

  public float getPlaySpeed() {
    return playSpeed;
  }

  public float getEndTime() {
    return endTime;
  }

  public List<BoneAnimation> getBoneAnimations() {
    return boneAnimations;
  }

  public void setPlaySpeed(float playSpeed) {
    this.playSpeed = playSpeed;
  }

  public void setEndTime(float endTime) {
    this.endTime = endTime;
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
    raf.skipBytes(2);
    int numOfBoneAnimations = ByteUtils.readInt16(raf);
    int headerSize = ByteUtils.readInt32(raf);
    if (headerSize != 0x10) {
      throw new IllegalStateException("Header size not 16 bytes, is actually: " + headerSize);
    }
    float playSpeed = ByteUtils.readFloat(raf);
    float endTime = ByteUtils.readFloat(raf);

    List<BoneAnimation> boneAnimations = new ArrayList<>();
    for (int i = 0; i < numOfBoneAnimations; i++) {
      boneAnimations.add(BoneAnimation.parseFrom(raf, animationOffset));
    }
    return new Builder()
        .id(id)
        .playSpeed(playSpeed)
        .endTime(endTime)
        .boneAnimations(boneAnimations)
        .create();
  }

  public void writeTo(Path gntaPath) throws IOException {
    Files.write(gntaPath, getBytes());
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

    // Write out the animation header and time values
    header.write(ByteUtils.fromInt32(boneAnimations.size()));
    header.write(ByteUtils.fromInt32(0x10));
    header.write(ByteUtils.fromFloat(playSpeed));
    header.write(ByteUtils.fromFloat(endTime));

    // Write out each bone animation header and data for them
    for (BoneAnimation boneAnimation : boneAnimations) {
      header.write(boneAnimation.getHeaderBytes());
      data.write(boneAnimation.getDataBytes());
    }
    return Bytes.concat(header.toByteArray(), data.toByteArray());
  }

  /**
   * Rewrite a GNTAnimation to be compatible with GNT4. This involves changing 0x0204 animations
   * (with floats) to 0x0202 animations (with shorts).
   */
  public void rewriteForGNT4() {
    for (BoneAnimation boneAnimation : boneAnimations) {
      if (boneAnimation.getFlags1() == 0x0204) {
        int currentOffset = boneAnimation.getCoordinatesOffset();
        int lastSize = boneAnimation.getNumOfKeyFrames();
        boneAnimation.setFlags1((short) 0x0202);
        for (BoneAnimation boneAnimation1 : boneAnimations) {
          if (boneAnimation1.getTimeValuesOffset() > currentOffset) {
            int thisSize = boneAnimation1.getNumOfKeyFrames();
            int alignment = 0;
            if ((lastSize*8) % 16 != 0){
              alignment = 16 - (lastSize*8) % 16;
            }
            // int timeValueOffset = currentOffset + (lastSize * 8);
            boneAnimation1.setTimeValuesOffset(currentOffset + (lastSize * 8) + alignment);
            if ((thisSize*4) % 16 != 0) {
              alignment = 16 - (thisSize*4) % 16;
            } else {
              alignment = 0;
            }
            boneAnimation1.setCoordinatesOffset(boneAnimation1.getTimeValuesOffset() + (thisSize * 4) + alignment);
            currentOffset = boneAnimation1.getCoordinatesOffset();
            lastSize = thisSize;
            boneAnimation1.setFlags1((short) 0x0202);
          }
        }
      }
    }
  }

  /**
   * @return The size in bytes of this animation.
   * @throws IOException If an I/O error occurs
   */
  public int getSize() throws IOException {
    return getBytes().length;
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
        && Float.compare(that.playSpeed, playSpeed) == 0
        && Float.compare(that.endTime, endTime) == 0
        && Objects.equals(boneAnimations, that.boneAnimations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, playSpeed, endTime, boneAnimations);
  }

  public static class Builder {

    private int id;
    private float playSpeed;
    private float endTime;
    private List<BoneAnimation> boneAnimations;

    public GNTAnimation.Builder id(int id) {
      this.id = id;
      return this;
    }

    public GNTAnimation.Builder playSpeed(float playSpeed) {
      this.playSpeed = playSpeed;
      return this;
    }

    public GNTAnimation.Builder endTime(float endTime) {
      this.endTime = endTime;
      return this;
    }

    public GNTAnimation.Builder boneAnimations(List<BoneAnimation> boneAnimations) {
      this.boneAnimations = Collections.unmodifiableList(boneAnimations);
      return this;
    }

    public GNTAnimation create() {
      return new GNTAnimation(id, playSpeed, endTime, boneAnimations);
    }
  }
}
