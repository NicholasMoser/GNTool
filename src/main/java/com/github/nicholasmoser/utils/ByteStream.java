package com.github.nicholasmoser.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A ByteArrayInputStream enhanced with a few extra features. These include:
 * <ul>
 *     <li>The ability to return the current position via {@link #offset()}</li>
 *     <li>The ability to read a word via {@link #readWord()}</li>
 *     <li>The ability to mark, read a word, and reset via {@link #peekWord()}</li>
 * </ul>
 * <p>
 * Note: The words read will be read as big-endian signed ints.
 */
public class ByteStream extends ByteArrayInputStream {

  /**
   * Creates a <code>ByteStream</code> so that it  uses <code>buf</code> as its buffer array. The
   * buffer array is not copied. The initial value of <code>pos</code> is <code>0</code> and the
   * initial value of  <code>count</code> is the length of
   * <code>buf</code>.
   *
   * @param buf the input buffer.
   */
  public ByteStream(byte[] buf) {
    super(buf);
  }

  /**
   * @return The current offset this stream is in the byte array.
   */
  public int offset() {
    return pos;
  }

  /**
   * Read the next big-endian 4-byte word and return it. Then move the position of the stream back
   * prior to having read the word. Do not call this method if you have already called mark.
   *
   * @return The big-endian 4-byte word.
   * @throws IOException If an I/O error occurs.
   */
  public int peekWord() throws IOException {
    mark(4);
    byte[] bytes = new byte[4];
    if (read(bytes) != 4) {
      throw new IOException("Failed to peek word at offset " + pos);
    }
    reset();
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  /**
   * Read the next 4 bytes and return them. Then move the position of the stream back
   * prior to having read the bytes. Do not call this method if you have already called mark.
   *
   * @return The 4 bytes.
   * @throws IOException If an I/O error occurs.
   */
  public byte[] peekWordBytes() throws IOException {
    mark(4);
    byte[] bytes = new byte[4];
    if (read(bytes) != 4) {
      throw new IOException("Failed to peek word at offset " + pos);
    }
    reset();
    return bytes;
  }

  /**
   * Read the next requested number of bytes and return them. Then move the position of the stream
   * back prior to having read the bytes. Do not call this method if you have already called mark.
   *
   * @param num The number of bytes to peek.
   * @return The bytes.
   * @throws IOException If an I/O error occurs.
   */
  public byte[] peekBytes(int num) throws IOException {
    mark(num);
    byte[] bytes = new byte[num];
    if (read(bytes) != num) {
      throw new IOException("Failed to peek bytes at offset " + pos);
    }
    reset();
    return bytes;
  }

  /**
   * Read the next big-endian 4-byte word and return it.
   *
   * @return The big-endian 4-byte word.
   * @throws IOException If an I/O error occurs.
   */
  public int readWord() throws IOException {
    byte[] bytes = new byte[4];
    if (read(bytes) != 4) {
      throw new IOException("Failed to read word at offset " + pos);
    }
    return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }

  /**
   * Reads a number of bytes from the ByteStream.
   *
   * @param num The number of bytes to read.
   * @return The bytes read in an array.
   * @throws IOException If an I/O error occurs.
   */
  public byte[] readBytes(int num) throws IOException {
    int startingOffset = pos;
    byte[] bytes = new byte[num];
    if (read(bytes) != num) {
      throw new IOException(String.format("Failed to read %d bytes at offset %d", num, startingOffset));
    }
    return bytes;
  }

  /**
   * Skips the current 4-byte word.
   *
   * @throws IOException If 4 bytes are not skipped.
   */
  public void skipWord() throws IOException {
    if (skip(4) != 4) {
      throw new IOException("Failed to skip 4 bytes at offset " + pos);
    }
  }

  /**
   * Calls {@link #mark(int)} with a value of zero, since ByteArrayInputStream doesn't care about
   * the readAheadLimit.
   */
  public void mark() {
    this.mark(0);
  }

  /**
   * @return The length of the byte buffer.
   */
  public int length() {
    return buf.length;
  }

  /**
   * Seeks the ByteStream to a specific position. The position must be greater than or equal to 0
   * and less than the size of the byte array, or it will throw IllegalArgumentException.
   *
   * @param pos The position in the byte array to seek to.
   */
  public void seek(int pos) {
    if (pos >= count) {
      throw new IllegalArgumentException("ByteStream new pos " + pos + " >= count " + count);
    } else if (pos < 0) {
      throw new IllegalArgumentException("ByteStream new pos " + pos + " < 0");
    }
    this.pos = pos;
  }
}
