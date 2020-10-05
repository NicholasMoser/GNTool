package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.utils.ByteUtils;
import java.util.Arrays;
import java.util.Objects;

/**
 * An Insert ASM code used for inserting assembly code directly into the dol. For more information
 * see https://geckocodes.org/index.php?arsenal=1
 */
public class InsertAsmCode implements GeckoCode {

  private final byte[] instructions;
  private final long targetAddress;

  public InsertAsmCode(byte[] instructions, long targetAddress) {
    this.instructions = instructions;
    this.targetAddress = targetAddress;
  }

  @Override
  public int getLength() {
    return 8 + instructions.length;
  }

  @Override
  public byte[] getBytes() {
    int linesOfInstructions = instructions.length / 8;
    byte[] linesOfInstructionsBytes = ByteUtils.fromInt32(linesOfInstructions);
    byte[] targetAddressBytes = ByteUtils.fromUint32(targetAddress);
    byte[] bytes = new byte[8 + instructions.length];
    bytes[0] = (byte) 0xC2;
    bytes[1] = targetAddressBytes[1];
    bytes[2] = targetAddressBytes[2];
    bytes[3] = targetAddressBytes[3];
    bytes[4] = linesOfInstructionsBytes[0];
    bytes[5] = linesOfInstructionsBytes[1];
    bytes[6] = linesOfInstructionsBytes[2];
    bytes[7] = linesOfInstructionsBytes[3];
    System.arraycopy(instructions, 0, bytes, 8, instructions.length);
    return bytes;
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
    InsertAsmCode that = (InsertAsmCode) o;
    return targetAddress == that.targetAddress &&
        Arrays.equals(instructions, that.instructions);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(targetAddress);
    result = 31 * result + Arrays.hashCode(instructions);
    return result;
  }
}
