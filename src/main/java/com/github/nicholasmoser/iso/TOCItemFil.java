package com.github.nicholasmoser.iso;

import java.util.Objects;
import javax.swing.tree.TreeNode;

public class TOCItemFil {

  public int TOCIdx;

  public int dirIdx;

  public int nextIdx;

  public int prevIdx;

  public int pos;

  public int len;

  public boolean isDir;

  public String name;

  public String path;

  public String gamePath;

  public TreeNode node;

  public TOCItemFil()
  {
  }

  public TOCItemFil(int TOCIdx, int dirIdx, int pos, int len, boolean isDir, String name, String gamePath, String path)
  {
    this.TOCIdx = TOCIdx;
    this.dirIdx = dirIdx;
    this.pos = pos;
    this.len = len;
    this.isDir = isDir;
    this.name = name;
    this.gamePath = gamePath;
    this.path = path;
  }

  public int getTOCIdx() {
    return TOCIdx;
  }

  public int getDirIdx() {
    return dirIdx;
  }

  public int getNextIdx() {
    return nextIdx;
  }

  public int getPrevIdx() {
    return prevIdx;
  }

  public int getPos() {
    return pos;
  }

  public int getLen() {
    return len;
  }

  public boolean isDir() {
    return isDir;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public String getGamePath() {
    return gamePath;
  }

  public TreeNode getNode() {
    return node;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TOCItemFil that = (TOCItemFil) o;
    return TOCIdx == that.TOCIdx &&
        dirIdx == that.dirIdx &&
        nextIdx == that.nextIdx &&
        prevIdx == that.prevIdx &&
        pos == that.pos &&
        len == that.len &&
        isDir == that.isDir &&
        Objects.equals(name, that.name) &&
        Objects.equals(path, that.path) &&
        Objects.equals(gamePath, that.gamePath) &&
        Objects.equals(node, that.node);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(TOCIdx, dirIdx, nextIdx, prevIdx, pos, len, isDir, name, path, gamePath, node);
  }

  @Override
  public String toString() {
    return "TOCItemFil{" +
        "TOCIdx=" + TOCIdx +
        ", dirIdx=" + dirIdx +
        ", nextIdx=" + nextIdx +
        ", prevIdx=" + prevIdx +
        ", pos=" + pos +
        ", len=" + len +
        ", isDir=" + isDir +
        ", name='" + name + '\'' +
        ", path='" + path + '\'' +
        ", gamePath='" + gamePath + '\'' +
        ", node=" + node +
        '}';
  }
}
