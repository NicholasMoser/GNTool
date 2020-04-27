package com.github.nicholasmoser.iso;

import java.util.Objects;

/**
 * An ISO directory.
 */
public class ISODirectory implements ISOItem {

  private final String parent;
  private final String name;
  private final String gamePath;
  private int fstExitIndex;
  private boolean isRoot;

  /**
   * Creates a new ISODirectory.
   *
   * @param parent       The parent game path.
   * @param name         The name of this file.
   * @param gamePath     The game path of this file.
   * @param fstExitIndex The last index in files for this directory.
   * @param isRoot       If this directory is root.
   */
  public ISODirectory(String parent, String name, String gamePath,
      int fstExitIndex, boolean isRoot) {
    if (parent == null) {
      throw new IllegalArgumentException("parent cannot be null");
    } else if (name == null) {
      throw new IllegalArgumentException("name cannot be null");
    } else if (gamePath == null) {
      throw new IllegalArgumentException("gamePath cannot be null");
    }
    this.parent = parent;
    this.name = name;
    this.gamePath = gamePath;
    this.fstExitIndex = fstExitIndex;
    this.isRoot = isRoot;
  }

  @Override
  public String getParent() {
    return parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getGamePath() {
    return gamePath;
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public boolean isRoot() {
    return isRoot;
  }

  /**
   * Sets the last index in files for this directory.
   *
   * @param fstExitIndex The last index in files for this directory.
   */
  public void setFstExitIndex(int fstExitIndex) {
    this.fstExitIndex = fstExitIndex;
  }

  /**
   * @return The last index in files for this directory.
   */
  public int getFstExitIndex() {
    return fstExitIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ISODirectory that = (ISODirectory) o;
    return fstExitIndex == that.fstExitIndex &&
        parent.equals(that.parent) &&
        name.equals(that.name) &&
        gamePath.equals(that.gamePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, name, gamePath, fstExitIndex);
  }

  @Override
  public String toString() {
    if (isRoot) {
      return "ISODirectory{ root }";
    }
    return "ISODirectory{" +
        "parent='" + parent + '\'' +
        ", name='" + name + '\'' +
        ", gamePath='" + gamePath + '\'' +
        ", fstExitIndex=" + fstExitIndex +
        '}';
  }

  /**
   * Builder class for ISODirectory.
   */
  public static class Builder {

    private String parent;
    private String name;
    private String gamePath;
    private int fstExitIndex;
    private boolean isRoot;

    public ISODirectory.Builder setParent(String parent) {
      this.parent = parent;
      return this;
    }

    public ISODirectory.Builder setName(String name) {
      this.name = name;
      return this;
    }

    public ISODirectory.Builder setGamePath(String gamePath) {
      this.gamePath = gamePath;
      return this;
    }

    public ISODirectory.Builder setFstExitIndex(int fstExitIndex) {
      this.fstExitIndex = fstExitIndex;
      return this;
    }

    public ISODirectory.Builder setIsRoot(boolean isRoot) {
      this.isRoot = isRoot;
      return this;
    }

    public ISODirectory build() {
      return new ISODirectory(parent, name, gamePath, fstExitIndex, isRoot);
    }
  }
}
