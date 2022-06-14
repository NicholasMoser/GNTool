package com.github.nicholasmoser.mot;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Represents a three-dimensional vertex with a fourth homogeneous vertex coordinate. For more info
 * on the fourth coordinate, see the following links:
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/Homogeneous_coordinates">Homogeneous Coordinates</a></li>
 *   <li><a href="https://www.reddit.com/r/computergraphics/comments/9fe1fq/why_xyzw_for_point_projection/">Why xyzw? (For point projection)</a></li>
 *   <li><a href="https://stackoverflow.com/questions/2422750/in-opengl-vertex-shaders-what-is-w-and-why-do-i-divide-by-it">What is w, and why do I divide by it?</a></li>
 * </ul>
 */
public class Coordinate {

  private final float x;
  private final float y;
  private final float z;
  private final float w;

  public Coordinate(short x, short y, short z, short w) {
    this.x = fromShort(x);
    this.y = fromShort(y);
    this.z = fromShort(z);
    this.w = fromShort(w);
  }

  public Coordinate(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public short getX() {
    return toShort(x);
  }

  public short getY() {
    return toShort(y);
  }

  public short getZ() {
    return toShort(z);
  }

  public short getW() {
    return toShort(w);
  }

  public float getFloatX() {
    return x;
  }

  public float getFloatY() {
    return y;
  }

  public float getFloatZ() {
    return z;
  }

  public float getFloatW() {
    return w;
  }

  /**
   * Converts a short to a float. This is accomplished by shifting the two bytes left and converting
   * that to a float. For example: 0x4048 -> 0x40480000 -> 3.125
   *
   * @param value The short to convert to a float.
   * @return The float.
   */
  private float fromShort(short value) {
    ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.putShort(value);
    buffer.putShort((short) 0);
    buffer.flip();
    return buffer.getFloat();
  }

  /**
   * Converts a float to a short. This results in a loss of precision, as it's done by taking the
   * first two bytes of the float and converting it to a short. Therefore, as per IEEE 754, only
   * 7 of the 23 significand precision (fraction) bits are preserved. For example: 3.1415927 ->
   * 0x40490FDB -> 0x4049
   *
   * @param value
   * @return
   */
  private short toShort(float value) {
    ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.putFloat(value);
    buffer.flip();
    return buffer.getShort();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return x == that.x && y == that.y && z == that.z && w == that.w;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, w);
  }
}
