package com.github.nicholasmoser.iso;

public class ISOItem {

  public int TOCIdx;

  public int dirIdx;

  public int pos;

  public int len;

  public boolean isDir;

  public String name;

  public String gamePath;

  public ISOItem(int TOCIdx, int dirIdx, int pos, int len, boolean isDir, String name, String gamePath)
  {
    this.TOCIdx = TOCIdx;
    this.dirIdx = dirIdx;
    this.pos = pos;
    this.len = len;
    this.isDir = isDir;
    this.name = name;
    this.gamePath = gamePath;
  }

  @Override
  public String toString() {
    return "ISOItem{" +
        "ISOItem=" + String.format("%08X", TOCIdx) +
        ", dirIdx=" + String.format("%08X", dirIdx) +
        ", pos=" + String.format("%08X", pos) +
        ", len=" + String.format("%08X", len) +
        ", isDir=" + isDir +
        ", name='" + name + '\'' +
        ", gamePath='" + gamePath + '\'' +
        '}';
  }
}
