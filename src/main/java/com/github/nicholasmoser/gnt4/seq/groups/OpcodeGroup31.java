package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetText;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetTextColor;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetTextPosition;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetTextPosition2;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetTextSize;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.Colors;
import java.io.IOException;

public class OpcodeGroup31 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x00 -> set_text_p(bs);
      case 0x01 -> op_3101(bs);
      case 0x03 -> set_text_size(bs);
      case 0x06 -> set_text_color(bs);
      case 0x0B -> op_310B(bs);
      case 0x13 -> op_3113(bs); // Does not appear to be in GNT4?
      case 0x1B -> op_311B(bs);
      case 0x1C -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode set_text_p(ByteStream bs) throws IOException {
    int offset = bs.offset();
    bs.skipWord();
    byte[] textBytes = SeqHelper.readString(bs);
    String text = SeqHelper.getString(textBytes);
    String info = String.format("with text \"%s\"", text);
    return new SetText(offset, textBytes, info);
  }

  private static Opcode op_3101(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode set_text_size(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new SetTextSize(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode set_text_color(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    StringBuilder info = new StringBuilder();
    if (ea.getOperand() instanceof ImmediateOperand immediate) {
      int value = immediate.getImmediateValue();
      info.append(Colors.hexToColor(value));
    } else {
      throw new IOException("Unexpected operand for set_text_color: " + ea.getOperand().getClass());
    }
    return new SetTextColor(offset, ea.getBytes(), info.toString());
  }

  private static Opcode op_310B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new SetTextPosition(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_3113(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_311B(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    return new SetTextPosition2(offset, ea.getBytes(), ea.getDescription());
  }
}