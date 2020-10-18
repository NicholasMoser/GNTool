package com.github.nicholasmoser.gecko.active;

import com.github.nicholasmoser.gecko.InsertAsmCode;
import com.github.nicholasmoser.utils.ByteUtils;

/**
 * An active InsertAsmCode. This means that the code is currently applied to the dol, and because it
 * is an InsertAsmCode, has hijacked other code in the dol.
 */
public class ActiveInsertAsmCode extends InsertAsmCode {

  private final byte[] replacedBytes;
  private final long hijackedAddress;
  private final byte[] hijackedBytes;

  /**
   * @param bytes           The assembly that the code wishes to insert.
   * @param targetAddress   The target address of the insertion.
   * @param replacedBytes   The four bytes replaced at the target address.
   * @param hijackedAddress The address of where code in the dol was hijacked.
   * @param hijackedBytes   The original bytes hijacked by this code.
   */
  private ActiveInsertAsmCode(byte[] bytes, long targetAddress, byte[] replacedBytes,
      long hijackedAddress, byte[] hijackedBytes) {
    super(bytes, targetAddress);
    this.replacedBytes = replacedBytes;
    this.hijackedAddress = hijackedAddress;
    this.hijackedBytes = hijackedBytes;
  }

  /**
   * @return The original bytes that this code replaced.
   */
  public byte[] getReplacedBytes() {
    return replacedBytes;
  }

  /**
   * @return The address that this code puts new assembly instructions at, which is branched to and
   * from.
   */
  public long getHijackedAddress() {
    return hijackedAddress;
  }


  /**
   * @return The instruction bytes that this code inserts and branches to and returns from.
   */
  public byte[] getHijackedBytes() {
    return hijackedBytes;
  }

  @Override
  public String toString() {
    return "ActiveInsertAsmCode{" +
        "replaceBytes=" + ByteUtils.bytesToHexString(replacedBytes) +
        ", hijackedAddress=" + String.format("%08X", hijackedAddress) +
        ", hijackedBytes=" + ByteUtils.bytesToHexString(hijackedBytes) +
        "}->" + super.toString();
  }

  public static class Builder {

    private byte[] bytes;
    private long targetAddress;
    private byte[] replacedBytes;
    private long hijackedAddress;
    private byte[] hijackedBytes;

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

    public Builder hijackedAddress(long hijackedAddress) {
      this.hijackedAddress = hijackedAddress;
      return this;
    }

    public Builder hijackedBytes(byte[] hijackedBytes) {
      this.hijackedBytes = hijackedBytes;
      return this;
    }

    public ActiveInsertAsmCode create() {
      return new ActiveInsertAsmCode(bytes, targetAddress, replacedBytes, hijackedAddress,
          hijackedBytes);
    }
  }
}
