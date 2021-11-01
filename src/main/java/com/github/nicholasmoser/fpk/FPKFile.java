package com.github.nicholasmoser.fpk;

import com.google.common.primitives.Bytes;

/**
 * A compressed data file contained inside of an FPK file. Each FPK file will have one or more of
 * these.
 */
public class FPKFile {

  private final FPKFileHeader header;

  private final byte[] data;

  /**
   * Creates a new FPK file. The data will be adjusted for 16-byte alignment if necessary.
   * 
   * @param header The FPK file header.
   * @param data The date of the file.
   */
  public FPKFile(FPKFileHeader header, byte[] data) {
    int modDifference = data.length % 16;
    if (modDifference != 0) {
      this.data = Bytes.concat(data, new byte[16 - modDifference]);
    } else {
      this.data = data;
    }
    this.header = header;
  }

  /**
   * @return The FPK file header.
   */
  public FPKFileHeader getHeader() {
    return header;
  }

  /**
   * @return The file data.
   */
  public byte[] getData() {
    return data;
  }
}
