package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.utils.ByteUtils;
import java.util.Arrays;
import java.util.Objects;

public class Write32BitsCode implements GeckoCode {

  private final byte[] bytes;
  private final long targetAddress;

  public Write32BitsCode(byte[] bytes, long targetAddress) {
    this.bytes = bytes;
    this.targetAddress = targetAddress;
  }

  @Override
  public int getLength() {
    return 8;
  }

  @Override
  public byte[] getBytes() {
    byte[] targetAddressBytes = ByteUtils.fromUint32(targetAddress);
    byte[] fullBytes = new byte[8];
    fullBytes[0] = (byte) 0x04;
    fullBytes[1] = targetAddressBytes[1];
    fullBytes[2] = targetAddressBytes[2];
    fullBytes[3] = targetAddressBytes[3];
    fullBytes[4] = bytes[0];
    fullBytes[5] = bytes[1];
    fullBytes[6] = bytes[2];
    fullBytes[7] = bytes[3];
    return fullBytes;
  }

  @Override
  public long getTargetAddress() {
    return targetAddress;
  }

  @Override
  public String toString() {
    byte[] bytes = getBytes();
    StringBuilder builder = new StringBuilder();
    builder.append("Code bytes:\n");
    for (int i = 0; i < bytes.length; i++) {
      byte currentByte = bytes[i];
      builder.append(String.format("%02X", currentByte));
      if (i % 4 == 3) {
        builder.append(' ');
      }
      if (i % 8 == 7) {
        builder.append('\n');
      }
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Write32BitsCode that = (Write32BitsCode) o;
    return targetAddress == that.targetAddress &&
        Arrays.equals(bytes, that.bytes);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(targetAddress);
    result = 31 * result + Arrays.hashCode(bytes);
    return result;
  }
}
