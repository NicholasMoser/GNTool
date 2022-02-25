package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
  private float bounciness;
  private float repeatDelay;

  public GNTAnimation(int id, float bounciness,
      float repeatDelay, List<BoneAnimation> boneAnimations) {
    this.id = id;
    this.bounciness = bounciness;
    this.repeatDelay = repeatDelay;
    this.boneAnimations = boneAnimations;
  }

  public float getBounciness() {
    return bounciness;
  }

  public float getRepeatDelay() {
    return repeatDelay;
  }

  public List<BoneAnimation> getBoneAnimations() {
    return boneAnimations;
  }

  public void setBounciness(float bounciness) {
    this.bounciness = bounciness;
  }

  public void setRepeatDelay(float repeatDelay) {
    this.repeatDelay = repeatDelay;
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
    float bounciness = ByteUtils.readFloat(raf);
    float repeatDelay = ByteUtils.readFloat(raf);

    List<BoneAnimation> boneAnimations = new ArrayList<>();
    for (int i = 0; i < numOfBoneAnimations; i++) {
      boneAnimations.add(BoneAnimation.parseFrom(raf, animationOffset));
    }
    return new Builder()
        .id(id)
        .bounciness(bounciness)
        .repeatDelay(repeatDelay)
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

    // Write out the animation header and function curve values
    header.write(ByteUtils.fromInt32(boneAnimations.size()));
    header.write(ByteUtils.fromInt32(0x10));
    header.write(ByteUtils.fromFloat(bounciness));
    header.write(ByteUtils.fromFloat(repeatDelay));

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
        && Float.compare(that.bounciness, bounciness) == 0
        && Float.compare(that.repeatDelay, repeatDelay) == 0
        && Objects.equals(boneAnimations, that.boneAnimations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bounciness, repeatDelay, boneAnimations);
  }

  public static class Builder {

    private int id;
    private float bounciness;
    private float repeatDelay;
    private List<BoneAnimation> boneAnimations;

    public GNTAnimation.Builder id(int id) {
      this.id = id;
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

    public GNTAnimation.Builder boneAnimations(List<BoneAnimation> boneAnimations) {
      this.boneAnimations = Collections.unmodifiableList(boneAnimations);
      return this;
    }

    public GNTAnimation create() {
      return new GNTAnimation(id, bounciness, repeatDelay, boneAnimations);
    }
  }
}
