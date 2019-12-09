package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import com.github.nicholasmoser.FPKFileHeader;

public class FPKUtils {
  /**
   * Reads the header of the FPK file itself. This will return only the number of files from the
   * header. Make sure to only call this method on a newly opened FPK file, since the header is
   * first in the file. This method will always read exactly 16 bytes.
   * 
   * @param is The input stream to read it from.
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  public static int readFPKHeader(InputStream is) throws IOException {
    byte[] fileCountWord = new byte[4];
    is.skip(4);
    is.read(fileCountWord);
    is.skip(8);
    return ByteBuffer.wrap(fileCountWord).getInt();
  }

  /**
   * Reads an individual file header from the FPK file. This will return the relevant FPKFileHeader
   * object. Make sure to only call this method once you have already read the FPK header (first 16
   * bytes of the file). You will want to call this equivalent to the number of files contained in
   * the FPK file. This method will always read exactly 32 bytes.
   * 
   * @param is The input stream to read it from.
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  public static FPKFileHeader readFPKFileHeader(InputStream is) throws IOException {
    byte[] fileNameWord = new byte[16];
    byte[] offsetWord = new byte[4];
    byte[] compressedSizeWord = new byte[4];
    byte[] uncompressedSizeWord = new byte[4];
    is.read(fileNameWord);
    is.skip(4);
    is.read(offsetWord);
    is.read(compressedSizeWord);
    is.read(uncompressedSizeWord);
    String fileName = new String(fileNameWord, Charset.forName("Shift_JIS")).trim();
    int offset = ByteBuffer.wrap(offsetWord).getInt();
    int compressedSize = ByteBuffer.wrap(compressedSizeWord).getInt();
    int uncompressedSize = ByteBuffer.wrap(uncompressedSizeWord).getInt();
    return new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize);
  }
}
