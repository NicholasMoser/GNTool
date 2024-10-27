package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.TCG;
import com.github.nicholasmoser.gnt4.seq.dest.AbsoluteDestination;
import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBandnz;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBandz;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBeq;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBge;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBgt;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBle;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBlt;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBmeq;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgBne;
import com.github.nicholasmoser.gnt4.seq.opcodes.tcg.TcgMov;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup3B {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> tcg_beq(bs);
      case 0x02 -> tcg_bne(bs);
      case 0x03 -> tcg_blt(bs);
      case 0x04 -> tcg_ble(bs);
      case 0x05 -> tcg_bgt(bs);
      case 0x06 -> tcg_bge(bs);
      case 0x07 -> tcg_bandnz(bs);
      case 0x08 -> tcg_bandz(bs);
      case 0x09 -> tcg_bmeq(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode tcg_beq(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBeq(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bne(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBne(offset, baos.toByteArray(), description, destination);
  }


  private static Opcode tcg_blt(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBlt(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_ble(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBle(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bgt(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBgt(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bge(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBge(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bandnz(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBandnz(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bandz(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBandz(offset, baos.toByteArray(), description, destination);
  }

  private static Opcode tcg_bmeq(ByteStream bs) throws IOException {
    int offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(4));
    int op1 = bs.readWord();
    int op2 = bs.readWord();
    int op3 = bs.readWord();
    baos.write(ByteUtils.fromInt32(op1));
    baos.write(ByteUtils.fromInt32(op2));
    baos.write(ByteUtils.fromInt32(op3));
    String description = String.format("%s, %s", TCG.readValue(op2), TCG.readValue(op3));
    Destination destination = new AbsoluteDestination(op1);
    return new TcgBmeq(offset, baos.toByteArray(), description, destination);
  }
}
