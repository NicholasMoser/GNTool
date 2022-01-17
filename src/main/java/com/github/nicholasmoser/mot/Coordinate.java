package com.github.nicholasmoser.mot;

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
}
