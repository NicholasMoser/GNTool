package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SeqEdit {

  private final String name;
  private final int offset;
  private final byte[] oldBytes;
  private final byte[] newBytes;

  // stop, used to signal end of old and new bytes
  public static final byte[] STOP = { 0x73, 0x74, 0x6F, 0x70 };

  /**
   *
   * @param name The name of this seq edit (do not include null bytes).
   * @param offset The offset in the seq file of this seq edit.
   * @param oldBytes The old bytes that this seq edit overrides.
   * @param newBytes The new bytes that this seq edit executes.
   */
  public SeqEdit(String name, int offset, byte[] oldBytes, byte[] newBytes) {
    if (oldBytes.length % 4 != 0) {
      throw new IllegalArgumentException("Original bytes size must be multiple of 4");
    } else if (newBytes.length % 4 != 0) {
      throw new IllegalArgumentException("New bytes size must be multiple of 4");
    } else if (containsStop(oldBytes)) {
      throw new IllegalArgumentException("Original bytes contain string \"stop\"");
    } else if (containsStop(newBytes)) {
      throw new IllegalArgumentException("New bytes contain string \"stop\"");
    }
    this.name = name;
    this.offset = offset;
    this.oldBytes = oldBytes;
    this.newBytes = newBytes;
  }

  public String getName() {
    return name;
  }

  public int getOffset() {
    return offset;
  }

  public byte[] getOldBytes() {
    return oldBytes;
  }

  public byte[] getNewBytes() {
    return newBytes;
  }

  public byte[] getFullBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(getNameBytes());
      baos.write(ByteUtils.fromInt32(offset));
      baos.write(oldBytes);
      baos.write(STOP);
      baos.write(newBytes);
      baos.write(STOP);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] getNameBytes() throws IOException {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(nameBytes);
    int nullBytes = 4 - (nameBytes.length % 4); // 4-byte aligned
    baos.write(new byte[nullBytes]);
    return baos.toByteArray();
  }

  /**
   * Returns if the given bytes contain the String "stop", which is a magic word used to separate
   * parts of the seq edit binary data. If the binary data naturally contains the String, it will
   * break parsing.
   *
   * @param bytes The bytes to check for the String "stop".
   * @return If the bytes contain the String "stop".
   */
  private boolean containsStop(byte[] bytes) {
    return Bytes.indexOf(bytes, STOP) != -1;
  }

  @Override
  public String toString() {
    String buffer = "SeqEdit\nName: "
        + name
        + "\nOffset: "
        + String.format("0x%X", offset)
        + "\nOld Bytes: 0x"
        + ByteUtils.bytesToHexString(oldBytes)
        + "\nNew Bytes: 0x"
        + ByteUtils.bytesToHexString(newBytes)
        + "\nFull Bytes: 0x"
        + ByteUtils.bytesToHexString(getFullBytes());
    return buffer;
  }
}
