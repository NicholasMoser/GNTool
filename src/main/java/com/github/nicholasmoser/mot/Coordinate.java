package com.github.nicholasmoser.mot;

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

  private final short x;
  private final short y;
  private final short z;
  private final short w;

  public Coordinate(short x, short y, short z, short w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
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
