package com.github.nicholasmoser;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

public class FPKFileHeader {

  private final String fileName;

  private int offset;

  private final int compressedSize;

  private final int uncompressedSize;

  private final boolean isWii;

  /**
   * Creates an FPK file header.
   * 
   * @param fileName The name of the file.
   * @param offset The offset to the file.
   * @param compressedSize The size of the file when compressed.
   * @param uncompressedSize The size of the file when uncompressed.
   * @param isWii Whether or not this FPK file is for the Wii (GameCube otherwise).
   */
  public FPKFileHeader(String fileName, int offset, int compressedSize, int uncompressedSize, boolean isWii) {
    this.fileName = fileName;
    this.offset = offset;
    this.compressedSize = compressedSize;
    this.uncompressedSize = uncompressedSize;
    this.isWii = isWii;
  }

  /**
   * Creates an FPK file header without an offset. The offset will be -1 until it is set.
   * 
   * @param fileName The name of the file.
   * @param compressedSize The size of the file when compressed.
   * @param uncompressedSize The size of the file when uncompressed.
   */
  public FPKFileHeader(String fileName, int compressedSize, int uncompressedSize, boolean isWii) {
    this.fileName = fileName;
    this.offset = -1;
    this.compressedSize = compressedSize;
    this.uncompressedSize = uncompressedSize;
    this.isWii = isWii;
  }

  /**
   * @return The name of the file.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @return The offset of the file. This will be -1 if not yet set.
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Sets an offset for the header. Will only apply if the offset is not already set, which is
   * represented by the integer -1.
   * 
   * @param offset The offset to set the header to.
   */
  public void setOffset(int offset) {
    if (this.offset == -1) {
      this.offset = offset;
    }
  }

  /**
   * @return The compressed size of the file.
   */
  public int getCompressedSize() {
    return compressedSize;
  }

  /**
   * @return The uncompressed size of the file.
   */
  public int getUncompressedSize() {
    return uncompressedSize;
  }

  /**
   * @return a byte array of the file header. This will be 48 bytes for Wii FPKs and 32 for
   * GameCube FPKs.
   */
  public byte[] getBytes() {
    byte[] fileNameBytes = ByteUtils.fromString(fileName);
    // Need to pad with zeroes if the name is not a full 16 bytes on GameCube and 32 bytes on Wii.
    int fullLength = isWii ? 32 : 16;
    if (fileNameBytes.length < fullLength) {
      int difference = fullLength - fileNameBytes.length;
      fileNameBytes = Bytes.concat(fileNameBytes, new byte[difference]);
    }
    return Bytes.concat(fileNameBytes, ByteUtils.fromUint32(0), ByteUtils.fromUint32(offset),
        ByteUtils.fromUint32(compressedSize), ByteUtils.fromUint32(uncompressedSize));
  }
}
