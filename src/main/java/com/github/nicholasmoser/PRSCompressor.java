package com.github.nicholasmoser;

import java.util.Arrays;

/**
 * A compressor for Eighting PRS compressed files. Takes in the full byte array of the file and
 * returns the uncompressed byte stream. Thanks to Luigi Auriemma for porting to QuickBMS. Thanks to
 * RupertAvery for originally writing it:
 * 
 * https://gbatemp.net/threads/tvc-fpk-tool.207232/ http://www.mediafire.com/?vty5jymlmm2
 */
public class PRSCompressor {

  // Input bytes to compress.
  private byte[] input;

  // Output bytes that have been compressed.
  private byte[] output;

  // The location in the output bytes of the current flag byte.
  private int flagIndex;

  // The current index of the input bytes.
  private int inputIndex;

  // The current index of the output bytes.
  private int outputIndex;

  // The current bit index inside of the flag byte.
  private int flagBitIndex;

  // The length of the input bytes currently being compressed.
  private int currentCompressionLength;

  private int pos;

  /**
   * Creates a new Eighting PRS compressor.
   * 
   * @param input The input bytes to compress.
   */
  public PRSCompressor(byte[] input) {
    this.input = input;
    flagIndex = 0;
    outputIndex = 1;
    flagBitIndex = 7;
    currentCompressionLength = 0;
    pos = 0;
    inputIndex = 0;
    output = new byte[input.length * 2];
  }

  /**
   * Compresses the file by using the Eighting PRS compression algorithm.
   * 
   * @return The compressed bytes.
   */
  public byte[] compress() {
    while (inputIndex < input.length) {
      if (checkWindow()) {
        writeCompressedBytes();
        inputIndex += currentCompressionLength;
      } else if (checkRunLengthEncoding()) {
        writeCompressedBytes();
        inputIndex += currentCompressionLength;
      } else {
        writeUncompressedByte();
        inputIndex++;
      }
    }
    terminateFile();

    // Return the length of the output plus three so that the file ends with three 0s.
    return Arrays.copyOfRange(output, 0, (outputIndex) + 3);
  }

  /**
   * Terminates the file by adding a 0 and 1 bit to the flag. This is equivalent to a long search,
   * but yields no result.
   */
  private void terminateFile() {
    writeBit(0);
    writeBit(1);
  }

  /**
   * Tests the current buffer position for run length encoding (RLE). This will scan ahead of the
   * current position until the byte changes or the maximum of 256 is reached.
   * 
   * @return If run length encoding is found.
   */
  private boolean checkRunLengthEncoding() {
    currentCompressionLength = 0;
    pos = 1;
    if (inputIndex < 1) {
      return false;
    }
    int scanIndex = inputIndex;
    while (scanIndex < input.length && input[inputIndex - 1] == input[scanIndex]
        && currentCompressionLength < 256) {
      currentCompressionLength++;
      scanIndex++;
    }

    return currentCompressionLength > 1;
  }

  /**
   * Scans backwards from the current buffer position
   * 
   * @return If previous repeated occurrences were found.
   */
  private boolean checkWindow() {
    if (inputIndex < 1) {
      return false;
    }

    // 256 bytes is the maximum match length unless there are fewer bytes left than that
    int maxMatchLength = 256;
    int bytesLeft = input.length - inputIndex;
    if (bytesLeft < maxMatchLength) {
      maxMatchLength = bytesLeft + 1;
    }
    int matchLength = 1;
    int scanIndex = inputIndex - 1;
    int currentIndex = inputIndex;
    int savedIndex = scanIndex;

    // Don't exceed scan area of 8192 bytes
    // Do not scan beyond start of input bytes
    // Limit to 256 bytes match length
    while (((currentIndex - scanIndex) < 8192) && (scanIndex >= 0) && (matchLength < maxMatchLength)) {
      while (memcmp(currentIndex, scanIndex, matchLength) && (matchLength < maxMatchLength)) {
        savedIndex = scanIndex;
        matchLength++;
      }
      scanIndex--;
    }

    matchLength--;
    currentCompressionLength = matchLength;
    pos = currentIndex - savedIndex;
    if ((matchLength == 2) && (pos > 255)) {
      return false;
    }

    return matchLength >= 2;
  }

  /**
   * Partial Java re-implementation of the memcmp(...) function in C. Given two indices for the
   * input bytes, checks that the given size number of bytes are the same.
   * 
   * @param index1 The first index.
   * @param index2 The second index.
   * @param size The number of bytes to compare.
   * @return Whether the bytes are the same.
   */
  private boolean memcmp(int index1, int index2, int size) {
    byte[] range1 = Arrays.copyOfRange(input, index1, index1 + size);
    byte[] range2 = Arrays.copyOfRange(input, index2, index2 + size);
    return Arrays.equals(range1, range2);
  }

  /**
   * Writes a bit to the current flag byte. If there are no more bits left in the current flag byte,
   * creates a new flag byte at the current output index and writes to it.
   * 
   * @param bit The bit to write.
   */
  private void writeBit(int bit) {
    if (flagBitIndex == -1) {
      flagBitIndex = 7;
      flagIndex = outputIndex;
      outputIndex = flagIndex + 1;
    }
    output[flagIndex] |= bit << flagBitIndex;
    flagBitIndex--;
  }

  /**
   * Compresses between 2 and 5 bytes reachable through a short search. This includes the bits 0 and
   * 0 in the flag byte, which indicates that this byte is compressed and is short.
   * 
   * @param len
   * @param posy
   */
  private void writeBytesShortCompression(int len, int posy) {
    writeBit(0);
    writeBit(0);
    len -= 2;
    writeBit((len >> 1) & 0x01);
    len = (len << 1) & 0x02;
    writeBit((len >> 1) & 0x01);
    len = (len << 1) & 0x02;

    output[outputIndex++] = (byte) (~posy + 1);
  }

  /**
   * Compressed a long length of bytes. This includes the bits 0 and 1 in the flag byte, which
   * indicates that this byte is compressed and is long.
   * 
   * @param len
   * @param posy
   */
  private void writeBytesLongCompression(int len, int posy) {
    writeBit(0);
    writeBit(1);

    posy = (~posy + 1) << 3;

    if (len <= 9) {
      posy |= ((len - 2) & 0x07);
    }
    // else lower 3 bits are empty...

    output[outputIndex++] = (byte) (posy >> 8);
    output[outputIndex++] = (byte) posy;

    // ... and next byte encodes full length
    if (len > 9) {
      output[outputIndex++] = (byte) (len - 1);
    }
  }

  /**
   * Write compressed bytes to the output byte array.
   */
  private void writeCompressedBytes() {
    if (pos > 255 || currentCompressionLength > 5) {
      writeBytesLongCompression(currentCompressionLength, pos);
    } else {
      writeBytesShortCompression(currentCompressionLength, pos);
    }
  }

  /**
   * Writes an uncompressed byte to the output. This includes the single bit 1 in the flag byte,
   * which indicates that this byte is uncompressed.
   */
  private void writeUncompressedByte() {
    writeBit(1);
    output[outputIndex++] = input[inputIndex];
  }

  @Override
  public String toString() {
    return "\n\nPRSCompressor\nflagIndex=" + String.format("0x%02X", flagIndex) + "\ninputIndex="
        + String.format("0x%02X", inputIndex) + "\noutputIndex="
        + String.format("0x%02X", outputIndex) + "\nflagBitIndex=" + flagBitIndex
        + "\ncurrentCompressionLength=" + currentCompressionLength + "\npos=" + pos
        + "\nCurrent Flag Bits=" + toBinary(output[flagIndex]) + "\nCurrent Input Byte="
        + String.format("%02X", input[inputIndex]);
  }

  String toBinary(byte this_byte) {
    StringBuilder sb = new StringBuilder(Byte.SIZE);
    for (int i = 0; i < Byte.SIZE; i++)
      sb.append((this_byte << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
    return sb.toString();
  }
}
