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
   * Creates <code>ByteStream</code> that uses <code>buf</code> as its buffer array. The initial
   * value of <code>pos</code> is <code>offset</code> and the initial value of <code>count</code> is
   * the minimum of <code>offset+length</code> and <code>buf.length</code>. The buffer array is not
   * copied. The buffer's mark is set to the specified offset.
   *
   * @param buf    the input buffer.
   * @param offset the offset in the buffer of the first byte to read.
   * @param length the maximum number of bytes to read from the buffer.
   */
  public ByteStream(byte[] buf, int offset, int length) {
    super(buf, offset, length);
  }

  /**
   * @return The current offset this stream is in the byte array.
   */
  public int offset() {
    return pos;
  }

  /**
   * Read the next big-endian 4-byte word and return it. Then move the position of the stream back
   * prior to having read the word.
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
}
