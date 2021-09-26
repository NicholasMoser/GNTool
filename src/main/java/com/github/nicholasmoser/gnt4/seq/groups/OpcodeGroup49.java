package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.EffectiveAddresses;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup49 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x49, 0x00, 0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x49, 0x01, 0x4, bs);
      case 0x02 -> UnknownOpcode.of(0x49, 0x02, 0x8, bs);
      case 0x03 -> UnknownOpcode.of(0x49, 0x03, 0x10, bs);
      case 0x09 -> op_4909(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4909(ByteStream bs) throws IOException {
    int offset = bs.offset();
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    String info = String.format(" %s", ea.getDescription());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    Operand operand = ea.getSecondOperand();
    boolean isImmediate = operand instanceof ImmediateOperand;
    if (!isImmediate) {
      throw new IllegalStateException(operand.getClass() + " is not supported for op_4909 currently.");
    }
    ImmediateOperand immediate = (ImmediateOperand) operand;
    int value = immediate.getImmediateValue();
    switch (value) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 6:
      case 7:
        baos.write(bs.readBytes(4));
        break;
      default:
        break;
    }
    return new UnknownOpcode(offset, baos.toByteArray(), info);
  }
}