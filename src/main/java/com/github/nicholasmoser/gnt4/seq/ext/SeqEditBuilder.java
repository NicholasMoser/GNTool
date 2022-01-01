package com.github.nicholasmoser.gnt4.seq.ext;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;

public class SeqEditBuilder {

  private Path seqPath;
  private byte[] seqBytes;
  private Integer startOffset;
  private Integer endOffset;
  private byte[] newBytes;
  private String name;

  private SeqEditBuilder() {

  }

  public static SeqEditBuilder getBuilder() {
    return new SeqEditBuilder();
  }

  public SeqEditBuilder seqPath(Path seqPath) {
    this.seqPath = seqPath;
    return this;
  }

  public SeqEditBuilder seqBytes(byte[] seqBytes) {
    this.seqBytes = seqBytes;
    return this;
  }

  public SeqEditBuilder startOffset(int startOffset) {
    this.startOffset = startOffset;
    return this;
  }

  public SeqEditBuilder endOffset(int endOffset) {
    this.endOffset = endOffset;
    return this;
  }

  public SeqEditBuilder newBytes(byte[] newBytes) {
    this.newBytes = newBytes;
    return this;
  }

  public SeqEditBuilder name(String name) {
    this.name = name;
    return this;
  }

  public SeqEdit create() throws IOException {
    // Check that builder values are valid
    if (endOffset == null) {
      throw new IllegalArgumentException("endOffset is null");
    } else if (startOffset == null) {
      throw new IllegalArgumentException("startOffset is null");
    } else if (newBytes == null) {
      throw new IllegalArgumentException("newBytes is null");
    } else if (endOffset % 4 != 0) {
      throw new IllegalArgumentException("endOffset must be 4-byte aligned");
    } else if (startOffset % 4 != 0) {
      throw new IllegalArgumentException("startOffset must be 4-byte aligned");
    } else if (newBytes.length % 4 != 0) {
      throw new IllegalArgumentException("newBytes length must be 4-byte aligned");
    } else if (name == null) {
      throw new IllegalArgumentException("name is null");
    } else if (seqBytes != null && seqPath != null) {
      throw new IllegalArgumentException("only provide one of seqPath and seqBytes");
    }
    // Read old bytes from seq file or seq file bytes
    byte[] oldBytes;
    if (seqBytes != null) {
      oldBytes = Arrays.copyOfRange(seqBytes, startOffset, endOffset);
    } else if (seqPath != null) {
      try (RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
        raf.seek(startOffset);
        int length = endOffset - startOffset;
        oldBytes = new byte[length];
        if (raf.read(oldBytes) != length) {
          throw new IOException("Failed to read old bytes at offset " + startOffset);
        }
      }
    } else {
      throw new IllegalArgumentException("seqBytes and seqPath both are null");
    }
    SeqEdit edit = new SeqEdit(name, startOffset, oldBytes, newBytes);
    return edit;
  }
}
