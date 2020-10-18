package com.github.nicholasmoser.gecko.active;

import com.github.nicholasmoser.gecko.Write32BitsCode;
import com.github.nicholasmoser.utils.ByteUtils;

/**
 * An active Write32BitsCode. This means that the code is currently applied to the dol.
 */
public class ActiveWrite32BitsCode extends Write32BitsCode {

  private final byte[] replacedBytes;

  /**
   * @param bytes         The 32 bits (4 bytes) that this code wishes to insert.
   * @param targetAddress The target address of the insertion.
   * @param replacedBytes The four bytes replaced at the target address.
   */
  private ActiveWrite32BitsCode(byte[] bytes, long targetAddress, byte[] replacedBytes) {
    super(bytes, targetAddress);
    this.replacedBytes = replacedBytes;
  }

  /**
   * @return The original bytes that this code replaced.
   */
  public byte[] getReplacedBytes() {
    return replacedBytes;
  }

  @Override
  public String toString() {
    return "ActiveWrite32BitsCode{" +
        "replacedBytes=" + ByteUtils.bytesToHexString(replacedBytes) +
        "}->" + super.toString();
  }

  public static class Builder {

    private byte[] bytes;
    private long targetAddress;
    private byte[] replacedBytes;

    public Builder bytes(byte[] bytes) {
      this.bytes = bytes;
      return this;
    }

    public Builder targetAddress(long targetAddress) {
      this.targetAddress = targetAddress;
      return this;
    }

    public Builder replacedBytes(byte[] replacedBytes) {
      this.replacedBytes = replacedBytes;
      return this;
    }

    public ActiveWrite32BitsCode create() {
      return new ActiveWrite32BitsCode(bytes, targetAddress, replacedBytes);
    }
  }
}
