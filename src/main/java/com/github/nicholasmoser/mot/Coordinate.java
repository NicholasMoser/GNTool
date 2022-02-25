package com.github.nicholasmoser.mot;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Represents a three-dimensional vertex with a fourth homogeneous vertex coordinate. See the
 * following links:
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/Homogeneous_coordinates">Homogeneous Coordinates</a></li>
 *   <li><a href="https://www.reddit.com/r/computergraphics/comments/9fe1fq/why_xyzw_for_point_projection/">Why xyzw? (For point projection)</a></li>
 *   <li><a href="https://stackoverflow.com/questions/2422750/in-opengl-vertex-shaders-what-is-w-and-why-do-i-divide-by-it">What is w, and why do I divide by it?</a></li>
 * </ul>
 */
public class Coordinate {

  private short x;
  private short y;
  private short z;
  private short w;

  public Coordinate(short x, short y, short z, short w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Coordinate(float x, float y, float z, float w) {
    this.x = fromFloat(x);
    this.y = fromFloat(y);
    this.z = fromFloat(z);
    this.w = fromFloat(w);
  }

  public short getX() {
    return x;
  }

  public short getY() {
    return y;
  }

  public short getZ() {
    return z;
  }

  public short getW() {
    return w;
  }

  public float getFloatX() {
    return toFloat(x);
  }

  public float getFloatY() {
    return toFloat(y);
  }

  public float getFloatZ() {
    return toFloat(z);
  }

  public float getFloatW() {
    return toFloat(w);
  }

  /**
   * Sets the X coordinate. It will be truncated from 32 to 16 bits, resulting in precision loss.
   * @param value The X coordinate.
   */
  public void setFloatX(float value) {
    x = fromFloat(value);
  }


  /**
   * Sets the Y coordinate. It will be truncated from 32 to 16 bits, resulting in precision loss.
   * @param value The Y coordinate.
   */
  public void setFloatY(float value) {
    y = fromFloat(value);
  }


  /**
   * Sets the Z coordinate. It will be truncated from 32 to 16 bits, resulting in precision loss.
   * @param value The Z coordinate.
   */
  public void setFloatZ(float value) {
    z = fromFloat(value);
  }


  /**
   * Sets the W coordinate. It will be truncated from 32 to 16 bits, resulting in precision loss.
   * @param value The W coordinate.
   */
  public void setFloatW(float value) {
    w = fromFloat(value);
  }

  /**
   * Converts a short to a float. This is accomplished by shifting the two bytes left and converting
   * that to a float. For example: 0x4048 -> 0x40480000 -> 3.125
   *
   * @param value The short to convert to a float.
   * @return The float.
   */
  private float toFloat(short value) {
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
  private short fromFloat(float value) {
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
