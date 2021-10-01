package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SoftReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class OpcodeGroup00 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> softReset(bs);
      case 0x01 -> hardReset(bs);
      case 0x02 -> UnknownOpcode.of(0x00, 0x02, 0x4, bs);
      case 0x07 -> UnknownOpcode.of(0x00, 0x07, 0x4, bs);
      case 0x20 -> parseMaybeBinaryData(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode softReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.peekWordBytes();
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      Optional<Opcode> binaryData = getBinaryData(bs);
      if (binaryData.isPresent()) {
        return binaryData.get();
      }
      throw new IllegalStateException(
          "Soft reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    bs.skip(4);
    return new SoftReset(offset);
  }

  /**
   * Reads a currently unknown 4-byte struct. The first three bytes are read at instructions
   * 0x8010704c, 0x80107050, and 0x80107054, which is used in opcodes such as op_4700.
   */
  public static Optional<Opcode> getBinaryData(ByteStream bs) throws IOException {
    int offset = bs.offset();
    if (SeqHelper.atOp04700Binary(bs)) {
      byte[] bytes = bs.readBytes(0x10);
      return Optional.of(new BinaryData(offset, bytes, "; Binary data referenced by op_4700"));
    } else if (SeqHelper.atComboList(bs)) {
      return Optional.of(SeqHelper.readComboList(bs));
    }
    return Optional.empty();
  }

  public static Opcode hardReset(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] bytes = bs.readBytes(4);
    int opcode = ByteUtils.toInt32(bytes);
    if ((opcode & 0xFFFF) != 0x0000) {
      throw new IllegalStateException(
          "Hard reset should have 0 for third and fourth byte: " + Arrays.toString(bytes));
    }
    return new HardReset(offset);
  }

  private static Opcode parseMaybeBinaryData(ByteStream bs) throws IOException {
    if (SeqHelper.isUnknownBinary1(bs)) {
      return SeqHelper.readUnknownBinary1(bs);
    }
    byte opcodeByte = bs.readBytes(2)[1];
    throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
  }
}
