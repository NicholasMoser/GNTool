package com.github.nicholasmoser.utils;

import com.github.nicholasmoser.FPKFileHeader;
import com.github.nicholasmoser.PRSUncompressor;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FPKUtils {

  /**
   * Reads the header of the FPK file itself. This will return only the number of files from the
   * header. Make sure to only call this method on a newly opened FPK file, since the header is
   * first in the file. This method will always read exactly 16 bytes.
   *
   * @param is        The input stream to read it from.
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  public static int readFPKHeader(InputStream is, boolean bigEndian) throws IOException {
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
    if (!bigEndian) {
      return ByteBuffer.wrap(fileCountWord).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    return ByteBuffer.wrap(fileCountWord).getInt();
  }

  /**
   * Reads an individual file header from an FPK file. This will return the relevant FPKFileHeader
   * object. Make sure to only call this method once you have already read the FPK header. You will
   * want to call this equivalent to the number of files contained in the FPK file. This method will
   * always read exactly 32 bytes.
   *
   * @param is        The input stream to read it from.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   * @return The number of files in the FPK file.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  public static FPKFileHeader readFPKFileHeader(InputStream is, boolean longPaths,
      boolean bigEndian) throws IOException {
    int pathLength = longPaths ? 32 : 16;
    byte[] fileNameWord = new byte[pathLength];
    byte[] offsetWord = new byte[4];
    byte[] compressedSizeWord = new byte[4];
    byte[] uncompressedSizeWord = new byte[4];
    if (is.read(fileNameWord) != pathLength) {
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
    ByteBuffer buf = ByteBuffer.wrap(offsetWord);
    int offset = bigEndian ? buf.getInt() : buf.order(ByteOrder.LITTLE_ENDIAN).getInt();
    buf = ByteBuffer.wrap(compressedSizeWord);
    int compressedSize = bigEndian ? buf.getInt() : buf.order(ByteOrder.LITTLE_ENDIAN).getInt();
    buf = ByteBuffer.wrap(uncompressedSizeWord);
    int uncompressedSize = bigEndian ? buf.getInt() : buf.order(ByteOrder.LITTLE_ENDIAN).getInt();
    return new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize, longPaths);
  }

  /**
   * Returns the bytes for a child from the given fpk file.
   *
   * @param fpkPath   The fpk file path to extract the child from.
   * @param child     The child compressed path to retrieve the bytes for.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   * @return The child bytes (compressed).
   * @throws IOException If an I/O error occurs.
   */
  public static byte[] getChildBytes(Path fpkPath, String child, boolean longPaths,
      boolean bigEndian) throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = readFPKHeader(is, bigEndian);
      int bytesRead = 16;
      for (int i = 0; i < fileCount; i++) {
        FPKFileHeader header = readFPKFileHeader(is, longPaths, bigEndian);
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

  /**
   * Returns the header of the FPK file. The first four bytes are zeroes. The next four are the
   * number of files. The next four is the size of this header, which is always 16. The last is the
   * output size of the whole FPK file. The byte array returned will always be 16 bytes exactly.
   *
   * @param numberOfFiles The number of files being packed.
   * @param outputSize    The total size of the FPK file, including this header.
   * @param bigEndian     If the FPK is big-endian (instead of little-endian).
   * @return The FPK header.
   */
  public static byte[] createFPKHeader(int numberOfFiles, int outputSize, boolean bigEndian) {
    byte[] first = bigEndian ? ByteUtils.fromUint32(0) : ByteUtils.fromUint32LE(0);
    byte[] second =
        bigEndian ? ByteUtils.fromUint32(numberOfFiles) : ByteUtils.fromUint32LE(numberOfFiles);
    byte[] third = bigEndian ? ByteUtils.fromUint32(16) : ByteUtils.fromUint32LE(16);
    byte[] fourth =
        bigEndian ? ByteUtils.fromUint32(outputSize) : ByteUtils.fromUint32LE(outputSize);
    return Bytes.concat(first, second, third, fourth);
  }
}
