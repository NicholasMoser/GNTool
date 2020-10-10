package com.github.nicholasmoser.gecko;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.BaseEncoding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GeckoReader {

  /**
   * Parse the given text into a list of Gecko codes.
   *
   * @param codeText The text to parse.
   * @return The list of Gecko codes.
   */
  public List<GeckoCode> parseCodes(String codeText) {
    List<GeckoCode> codes = new ArrayList<>();
    if (codeText == null) {
      return codes;
    }
    String codeTextNoWhitespace = codeText.replaceAll("\\s+", "").toUpperCase(Locale.US);
    byte[] bytes = BaseEncoding.base16().decode(codeTextNoWhitespace);
    for (int i = 0; i < bytes.length; ) {
      GeckoCode newCode = switch (bytes[i]) {
        case GeckoCode.THIRTY_TWO_BITS_WRITE -> parse32BitWrite(bytes, i);
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

  /**
   * Parses the Insert ASM code at the given index in the given code bytes and returns a GeckoCode.
   *
   * @param bytes The code bytes.
   * @param i     The index of the code.
   * @return The Insert ASM GeckoCode
   */
  private GeckoCode parseInsertAsm(byte[] bytes, int i) {
    if (i + 8 > bytes.length) {
      throw new IllegalArgumentException(
          "C2 code missing target address and number of instructions");
    }
    long targetAddress = getTargetAddress(bytes, i);
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

  /**
   * Parses the 32 bits Write code at the given index in the given code bytes and returns a
   * GeckoCode.
   *
   * @param bytes The code bytes.
   * @param i     The index of the code.
   * @return The 32 bits Write GeckoCode
   */
  private GeckoCode parse32BitWrite(byte[] bytes, int i) {
    if (i + 8 > bytes.length) {
      throw new IllegalArgumentException("04 code missing target address and bytes to write");
    }
    long targetAddress = getTargetAddress(bytes, i);
    byte[] code = Arrays.copyOfRange(bytes, i + 4, i + 8);
    return new Write32BitsCode(code, targetAddress);
  }

  /**
   * Returns the target address from the given code bytes at the given index. The first byte is
   * ignored since this byte defines which code is being used. Bytes 2, 3, and 4 are appended to
   * 0x80 to get the actual target address. This will throw an IllegalArgumentException if the
   * target address is invalid.
   *
   * @param bytes The code bytes.
   * @param i     The index of the target address.
   * @return The target address.
   */
  private long getTargetAddress(byte[] bytes, int i) {
    byte[] targetAddressBytes = new byte[4];
    targetAddressBytes[0] = (byte) 0x80;
    targetAddressBytes[1] = bytes[i + 1];
    targetAddressBytes[2] = bytes[i + 2];
    targetAddressBytes[3] = bytes[i + 3];
    long targetAddress = ByteUtils.toUint32(targetAddressBytes);
    assertValidAddress(targetAddress);
    return targetAddress;
  }

  /**
   * Asserts that the target address is valid. Throws an IllegalArgumentException if not. A target
   * address is considered valid if it is within the bounds of the init, text, ctors, dtors, rodata,
   * data, sdata, or sdata2. Codes outside the bounds of the dol itself are not currently supported.
   * Target addresses within the bss, sbss, or sbss2 are invalid since they are zeroed when the game
   * launches, so any code will be overwritten immediately.
   *
   * @param targetAddress The target address to check.
   */
  private void assertValidAddress(long targetAddress) {
    if (targetAddress >= 0x8027C578L) {
      String message = "Target address of code is outside the bounds of the dol (0x8027C578+): ";
      throw new IllegalArgumentException(message + String.format("%08x", targetAddress));
    } else if (targetAddress >= 0x8027C560L) {
      String message = "Target address of code is within the bounds of the sbss2 (0x8027C560 - 0x8027C578). ";
      message += "This is a problem since the sbss2 is zero initialized when the game launches: ";
      throw new IllegalArgumentException(message + String.format("%08x", targetAddress));
    } else if (targetAddress >= 0x80277CA0L) {
      // 0x80277CA0 - 0x8027C560 is the sdata2 which is valid
    } else if (targetAddress >= 0x80276FE0L) {
      String message = "Target address of code is within the bounds of the sbss (0x80276FE0 - 0x80277CA0). ";
      message += "This is a problem since the sbss is zero initialized when the game launches: ";
      throw new IllegalArgumentException(message + String.format("%08x", targetAddress));
    } else if (targetAddress >= 0x80276920L) {
      // 0x80276920 - 0x80276FE0 is the sdata which is valid
    } else if (targetAddress >= 0x802229E0L) {
      String message = "Target address of code is within the bounds of the bss (0x802229E0 - 0x80276920). ";
      message += "This is a problem since the bss is zero initialized when the game launches: ";
      throw new IllegalArgumentException(message + String.format("%08x", targetAddress));
    } else if (targetAddress >= 0x80003100L) {
      // 0x80003100 - 0x802229E0 is the init, text, ctors, dtors, rodata, and data which is valid
    } else {
      String message = "Target address of code is outside the bounds the of dol (0x80003100-): ";
      throw new IllegalArgumentException(message + String.format("%08x", targetAddress));
    }
  }
}
