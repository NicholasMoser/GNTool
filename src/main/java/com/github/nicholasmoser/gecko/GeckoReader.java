package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.BaseEncoding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeckoReader {

  public List<GeckoCode> parseCodes(String codeText) {
    List<GeckoCode> codes = new ArrayList<>();
    if (codeText == null) {
      return codes;
    }
    String codeTextNoWhitespace = codeText.replaceAll("\\s+", "");
    byte[] bytes = BaseEncoding.base16().decode(codeTextNoWhitespace);
    for (int i = 0; i < bytes.length; ) {
      GeckoCode newCode = switch (bytes[i]) {
        case GeckoCode.THIRTY_TWO_BITS_WRITE_AND_FILL -> parseWriteAndFill(bytes, i);
        case GeckoCode.INSERT_ASM -> parseInsertAsm(bytes, i);
        default -> throw new IllegalArgumentException(
            String.format("Code type not supported: %02X", bytes[i]));
      };
      if (codes.stream().anyMatch(code -> code.getTargetAddress() == newCode.getTargetAddress())) {
        throw new IllegalArgumentException("Each code must have a unique target address.");
      }
      codes.add(newCode);
      i += newCode.getLength();
    }
    return codes;
  }

  private GeckoCode parseInsertAsm(byte[] bytes, int i) {
    if (i + 8 >= bytes.length) {
      throw new IllegalArgumentException(
          "C2 code missing target address and number of instructions");
    }
    byte[] targetAddressBytes = new byte[4];
    targetAddressBytes[0] = (byte) 0x80;
    targetAddressBytes[1] = bytes[i + 1];
    targetAddressBytes[2] = bytes[i + 2];
    targetAddressBytes[3] = bytes[i + 3];
    long targetAddress = ByteUtils.toUint32(targetAddressBytes);
    int linesOfInstructions = ByteUtils.toInt32(Arrays.copyOfRange(bytes, i + 4, i + 8));
    if (linesOfInstructions <= 0) {
      throw new IllegalArgumentException(
          "C2 code requires positive non-zero lines of instructions.");
    }
    int endOfInstructions = i + 8 + (linesOfInstructions * 8);
    if (endOfInstructions > bytes.length) {
      String msg = String.format("C2 code has %d line(s) of instructions but not that many bytes.",
          linesOfInstructions);
      throw new IllegalArgumentException(msg);
    }
    byte[] instructions = Arrays.copyOfRange(bytes, i + 8, endOfInstructions);
    return new InsertAsmCode(instructions, targetAddress);
  }

  private GeckoCode parseWriteAndFill(byte[] bytes, int i) {
    throw new IllegalArgumentException("Write and fill codes not yet supported.");
  }
}
