package com.github.nicholasmoser.utils;

import com.github.nicholasmoser.FPKFileHeader;
import com.github.nicholasmoser.PRSUncompressor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

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
    if (is.skip(4) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.read(fileCountWord) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.skip(8) != 8) {
      throw new IOException("Unable to read FPK header.");
    }
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
    if (is.read(fileNameWord) != 16) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.skip(4) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.read(offsetWord) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.read(compressedSizeWord) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    if (is.read(uncompressedSizeWord) != 4) {
      throw new IOException("Unable to read FPK header.");
    }
    String fileName = new String(fileNameWord, Charset.forName("Shift_JIS")).trim();
    int offset = ByteBuffer.wrap(offsetWord).getInt();
    int compressedSize = ByteBuffer.wrap(compressedSizeWord).getInt();
    int uncompressedSize = ByteBuffer.wrap(uncompressedSizeWord).getInt();
    return new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize);
  }

  /**
   * Returns the bytes for a child from the given fpk file.
   *
   * @param fpkPath The fpk file path to extract the child from.
   * @param child   The child compressed path to retrieve the bytes for.
   * @return The child bytes (compressed).
   * @throws IOException If an I/O error occurs.
   */
  public static byte[] getChildBytes(Path fpkPath, String child) throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = readFPKHeader(is);
      int bytesRead = 16;
      for (int i = 0; i < fileCount; i++) {
        FPKFileHeader header = readFPKFileHeader(is);
        bytesRead += 32;
        if (child.equals(header.getFileName())) {
          int bytesToSkip = header.getOffset() - bytesRead;
          if (is.skip(bytesToSkip) != bytesToSkip) {
            throw new IOException(String.format("Failed to skip to binary data of %s", child));
          }
          int compressedSize = header.getCompressedSize();
          int uncompressedSize = header.getUncompressedSize();
          byte[] compressedBytes = is.readNBytes(compressedSize);
          if (compressedSize == uncompressedSize) {
            return compressedBytes;
          } else {
            PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, uncompressedSize);
            return uncompressor.uncompress();
          }
        }
      }
    }
    throw new IOException(String.format("%s could not be found in %s", child, fpkPath));
  }
}
