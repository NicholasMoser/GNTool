package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.operands.OperandParser;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Generalized logic across both SEQ_RegCMD1 and SEQ_RegCMD2 operand logic.
 */
public class SEQOperand {
  public record OperandBytes(byte flag, byte[] bytes) { }

  /**
   * Load affective address
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  public static OperandBytes parseEADescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = 0;
    if (description.startsWith("*")) {
      throw new IOException("Unexpected * for load effective address: " + description);
    }
    Byte registerVal = OperandParser.getByte(description);
    if (registerVal == null) {
      int immediate = Long.decode(description).intValue();
      flag = (byte) (flag | (byte)0x3f);
      baos.write(ByteUtils.fromInt32(immediate));
    } else {
      flag = (byte) (flag | registerVal);
    }
    return new OperandBytes(flag, baos.toByteArray());
  }

  /**
   * Load effective address with offset
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  public static OperandBytes parseEAPlusOffsetDescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = 0x40;
    // Remove dereference
    if (description.startsWith("*")) {
      description = description.substring(1);
    }
    String[] parts = description.split("->");
    Byte registerVal = OperandParser.getByte(parts[0]);
    if (registerVal == null) {
      flag = (byte) (flag | (byte)0x3f); // immediate value
    } else {
      flag = (byte) (flag | registerVal);
    }
    if (parts[1].startsWith("field_")) {
      baos.write(ByteUtils.fromUint32(Long.decode(parts[1].replace("field_", "")).intValue()));
    } else {
      // Known struct
      if (registerVal == null) {
        throw new IOException("Referencing known struct but no register value found for: " + parts[0]);
      }
      Optional<Integer> chrField = switch (registerVal & 0x3f) {
        case 0x26, 0x27 -> Chr.getOffset(parts[1]);
        default -> throw new IOException("Unknown struct for register val " + registerVal);
      };
      if (chrField.isEmpty()) {
        throw new IOException(String.format("Unknown field for %s", description));
      }
      baos.write(ByteUtils.fromInt32(chrField.get()));
    }
    if (registerVal == null) {
      int immediate = Long.decode(parts[0]).intValue(); // write immediate value
      baos.write(ByteUtils.fromInt32(immediate));
    }
    return new OperandBytes(flag, baos.toByteArray());
  }

  /**
   * Load effective address sum with offset
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  public static OperandBytes parseEASumPlusOffsetDescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = (byte) 0x80;
    String[] parts = description.split(" \\+ ");
    if (parts.length != 3) {
      throw new IOException("Missing three parts for EA sum plus offset: " + description);
    }
    String first = parts[0].startsWith("*") ? parts[0].substring(1) : parts[0];
    String second = parts[1].startsWith("*") ? parts[1].substring(1) : parts[1];
    String third = parts[2].startsWith("*") ? parts[2].substring(1) : parts[2];

    // Handle first part
    Byte registerVal = OperandParser.getByte(first);
    if (registerVal == null) {
      flag = (byte) (flag | (byte)0x3f); // immediate value
    } else {
      flag = (byte) (flag | registerVal);
    }

    // Handle second part
    ByteArrayOutputStream secondBytes = new ByteArrayOutputStream();
    Byte secondRegisterVal = OperandParser.getByte(second);
    if (secondRegisterVal == null) {
      throw new IOException("Failed to find register for operand " + second);
    }
    secondBytes.write(0);
    secondBytes.write(secondRegisterVal);

    // Handle third part
    ByteArrayOutputStream thirdBytes = new ByteArrayOutputStream();
    int value = Long.decode(third).intValue();
    thirdBytes.write(ByteUtils.fromUint16(value));

    // Parts are written out in reverse order
    baos.write(thirdBytes.toByteArray());
    baos.write(secondBytes.toByteArray());
    if (registerVal == null) {
      int immediate = Long.decode(first).intValue(); // write immediate value
      baos.write(ByteUtils.fromInt32(immediate));
    }
    return new OperandBytes(flag, baos.toByteArray());
  }
}
