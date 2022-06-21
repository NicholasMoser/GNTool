package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * A builder for a seq edit.
 */
public class SeqEditBuilder {

  private Path seqPath;
  private byte[] seqBytes;
  private Integer startOffset;
  private Integer endOffset;
  private byte[] newBytes;
  private List<Opcode> newCodes;
  private String name;
  private int length;
  private int position;

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

  public SeqEditBuilder newCodes(List<Opcode> newCodes) {
    this.newCodes = newCodes;
    return this;
  }

  public SeqEditBuilder newLength(int length) {
    this.length = length;
    return this;
  }

  public SeqEditBuilder position(int position) {
    this.position = position;
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
    } else if (endOffset % 4 != 0) {
      throw new IllegalArgumentException("endOffset must be 4-byte aligned");
    } else if (startOffset % 4 != 0) {
      throw new IllegalArgumentException("startOffset must be 4-byte aligned");
    } else if (endOffset - startOffset < 8) {
      throw new IllegalArgumentException("endOffset must be more than or equal to 8 bytes larger than startOffset");
    } else if (newCodes == null && newBytes.length % 4 != 0 ) {
      throw new IllegalArgumentException("newBytes length must be 4-byte aligned");
    } else if (name == null) {
      throw new IllegalArgumentException("name is null");
    } else if (seqBytes != null && seqPath != null) {
      throw new IllegalArgumentException("only provide one of seqPath and seqBytes");
    }
    // Read old bytes from seq file or seq file bytes
    byte[] oldBytes;
    if (seqBytes != null) {
      try {
        oldBytes = Arrays.copyOfRange(seqBytes, startOffset, endOffset);
      } catch (Exception e) {
        throw new IOException("Failed to read old bytes at offset " + startOffset);
      }
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
    if (oldBytes.length < 8) {
      throw new IllegalArgumentException("oldBytes less than 8 bytes");
    }
    if (newBytes == null) {
      return new SeqEdit(name, startOffset, position, oldBytes, newCodes, length);
    }
    return new SeqEdit(name, startOffset, position, oldBytes, newBytes);
  }
}
