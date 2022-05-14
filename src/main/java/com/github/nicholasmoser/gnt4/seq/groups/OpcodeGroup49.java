package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup49 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> UnknownOpcode.of(0x4, bs);
      case 0x01 -> UnknownOpcode.of(0x4, bs);
      case 0x02 -> UnknownOpcode.of(0x8, bs);
      case 0x03 -> UnknownOpcode.of(0x10, bs);
      case 0x08 -> UnknownOpcode.of(0xc, bs);
      case 0x09 -> op_4909(bs);
      case 0x0A -> UnknownOpcode.of(0xC, bs);
      case 0x0B -> UnknownOpcode.of(0x4, bs);
      case 0x0C -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  public static Opcode op_4909(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    Operand operandOne = ea.getFirstOperand();
    if (operandOne instanceof ImmediateOperand) {
      Operand operandTwo = ea.getSecondOperand();
      boolean immediate = operandTwo instanceof ImmediateOperand;
      if (!immediate) {
        throw new IllegalStateException("Operand type not yet supported: " + operandTwo);
      }
      ImmediateOperand immediateTwo = (ImmediateOperand) operandTwo;
      switch (immediateTwo.getImmediateValue()) {
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
    } else if (operandOne instanceof SeqOperand || operandOne instanceof ChrOperand) {
      // Do nothing
    } else {
      throw new IllegalStateException("This operand is not yet supported: " + operandOne);
    }
    return new UnknownOpcode(offset, baos.toByteArray(), ea.getDescription());
  }
}