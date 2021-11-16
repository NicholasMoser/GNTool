package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SeqEdit {

  private final String name;
  private final int offset;
  private final byte[] oldBytes;
  private final byte[] newBytes;

  // stop, used to signal end of old and new bytes
  public static final byte[] STOP = { 0x73, 0x74, 0x6F, 0x70 };

  public SeqEdit(String name, int offset, byte[] oldBytes, byte[] newBytes) {
    if (oldBytes.length % 4 != 0) {
      throw new IllegalArgumentException("Original bytes size must be multiple of 4");
    } else if (newBytes.length % 4 != 0) {
      throw new IllegalArgumentException("New bytes size must be multiple of 4");
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

  public byte[] getFullBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(getNameBytes());
    baos.write(ByteUtils.fromInt32(offset));
    baos.write(oldBytes);
    baos.write(STOP);
    baos.write(newBytes);
    baos.write(STOP);
    return baos.toByteArray();
  }

  private byte[] getNameBytes() throws IOException {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(nameBytes);
    int nullBytes = 4 - (nameBytes.length % 4); // 4-byte aligned
    baos.write(new byte[nullBytes]);
    return baos.toByteArray();
  }
}
