package com.github.nicholasmoser;

/**
 * An uncompressor for Eighting PRS compressed files. Takes in the full byte array of the file and
 * returns the uncompressed byte stream. Thanks to Luigi Auriemma for porting to QuickBMS. Thanks to
 * tpu for originally writing it: http://forum.xentax.com/viewtopic.php?p=30387#p30387
 */
public class PRSUncompressor {

  // Input bytes to uncompress.
  private byte[] input;

  // The expected output length (uncompressed size).
  private int outputLength;

  // The current index of the input bytes.
  private int inputIndex;

  // Number of bits still remaining in the flag byte.
  private int bitsLeft;

  // Buffer that holds an input byte to be uncompressed.
  private byte flagByte;

  /**
   * PRSUncompressor constructor using an input byte array and output length.
   * 
   * @param input Eighting PRS compressed byte array.
   * @param outputLength Expected output length (uncompressed size).
   */
  public PRSUncompressor(byte[] input, int outputLength) {
    this.input = input;
    this.outputLength = outputLength;
    inputIndex = 0;
    bitsLeft = 0;
    flagByte = 0;
  }

  /**
   * Runs the uncompressor and returns the associated uncompressed byte array.
   * 
   * @return The associated uncompressed byte array.
   */
  public byte[] uncompress() {
    byte[] output = new byte[outputLength];
    int outputPtr = 0;
    int flag = 0;
    int len = 0;
    int pos = 0;
    while (inputIndex < input.length) {
      flag = getFlagBits(1);
      if (flag == 1) // Uncompressed value
      {
        if (outputPtr < output.length)
          output[outputPtr++] = input[inputIndex++];
      } else // Compressed value
      {
        flag = getFlagBits(1);
        if (flag == 0) // Short search (length between 2 and 5)
        {
          len = getFlagBits(2) + 2;
          pos = input[inputIndex++] | 0xffffff00;
        } else // Long search
        {
          pos = (input[inputIndex++] << 8) | 0xffff0000;
          pos |= input[inputIndex++] & 0xff;
          len = pos & 0x07;
          pos >>= 3;
          if (len == 0) {
            len = (input[inputIndex++] & 0xff) + 1;
          } else {
            len += 2;
          }
        }
        pos += outputPtr;
        for (int i = 0; i < len; i++) {
          if (outputPtr < output.length)
            output[outputPtr++] = output[pos++];
        }
      }
    }

    return output;
  }

  /**
   * Retrieves a number of bits from the flag byte. A single 1 means the next byte is uncompressed.
   * A 0 followed by a 0 means that the next bytes are compressed via short search. A 0 followed by
   * a 1 means that the next bytes are compressed via long search. Short searches will be followed
   * by two bits used to calculate the length of the compressed bytes.
   * 
   * @param n The number of bits to get.
   * @return The flag bit(s).
   */
  private int getFlagBits(int n) {
    int bits = 0;

    bits = 0;
    while (n > 0) {
      bits <<= 1;
      if (bitsLeft == 0) {
        flagByte = input[inputIndex];
        inputIndex++;
        bitsLeft = 8;
      }

      if ((flagByte & 0x80) > 0) {
        bits |= 1;
      }

      flagByte <<= 1;
      bitsLeft--;
      n--;
    }
    return bits;
  }
}
