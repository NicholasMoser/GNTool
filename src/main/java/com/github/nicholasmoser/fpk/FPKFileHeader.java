package com.github.nicholasmoser.fpk;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

public class FPKFileHeader {

  private final String fileName;
  private final int compressedSize;
  private final int uncompressedSize;
  private final boolean longPaths;
  private final boolean bigEndian;
  private int offset;

  /**
   * Creates an FPK file header.
   * 
   * @param fileName The name of the file.
   * @param offset The offset to the file.
   * @param compressedSize The size of the file when compressed.
   * @param uncompressedSize The size of the file when uncompressed.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  public FPKFileHeader(String fileName, int offset, int compressedSize, int uncompressedSize, boolean longPaths, boolean bigEndian) {
    this.fileName = fileName;
    this.offset = offset;
    this.compressedSize = compressedSize;
    this.uncompressedSize = uncompressedSize;
    this.longPaths = longPaths;
    this.bigEndian = bigEndian;
  }

  /**
   * Creates an FPK file header without an offset. The offset will be -1 until it is set.
   * 
   * @param fileName The name of the file.
   * @param compressedSize The size of the file when compressed.
   * @param uncompressedSize The size of the file when uncompressed.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  public FPKFileHeader(String fileName, int compressedSize, int uncompressedSize, boolean longPaths, boolean bigEndian) {
    this.fileName = fileName;
    this.offset = -1;
    this.compressedSize = compressedSize;
    this.uncompressedSize = uncompressedSize;
    this.longPaths = longPaths;
    this.bigEndian = bigEndian;
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
    // Need to pad with zeroes if the name does not fill the full path length
    int fullLength = longPaths ? 32 : 16;
    if (fileNameBytes.length < fullLength) {
      int difference = fullLength - fileNameBytes.length;
      fileNameBytes = Bytes.concat(fileNameBytes, new byte[difference]);
    }
    byte[] first = bigEndian ? ByteUtils.fromUint32(0) : ByteUtils.fromUint32LE(0);
    byte[] second = bigEndian ? ByteUtils.fromUint32(offset) : ByteUtils.fromUint32LE(offset);
    byte[] third = bigEndian ? ByteUtils.fromUint32(compressedSize) : ByteUtils.fromUint32LE(compressedSize);
    byte[] fourth = bigEndian ? ByteUtils.fromUint32(uncompressedSize) : ByteUtils.fromUint32LE(uncompressedSize);
    return Bytes.concat(fileNameBytes, first, second, third, fourth);
  }
}
