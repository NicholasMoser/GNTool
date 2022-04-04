package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SEQ_ReqLoadPrev;
import com.github.nicholasmoser.gnt4.seq.opcodes.SEQ_ReqLoadPrev_I;
import com.github.nicholasmoser.gnt4.seq.opcodes.SEQ_ReqSetPrev;
import com.github.nicholasmoser.gnt4.seq.opcodes.TskExecFunc;
import com.github.nicholasmoser.gnt4.seq.opcodes.TskSendMsg;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpcodeGroup02 {
  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x01 -> SEQ_ReqSetPrev(bs);
      case 0x03 -> SEQ_ReqLoadPrev_I(bs);
      case 0x05 -> SEQ_ReqLoadPrev(bs);
      case 0x06 -> TskSendMsg(bs);
      case 0x07 -> TskExecFunc(bs);
      case 0x08 -> op_0208(bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode SEQ_ReqSetPrev(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    byte[] bytes = bs.readBytes(4);
    return new SEQ_ReqSetPrev(offset, Bytes.concat(ea.getBytes(), bytes), ea.getDescription());
  }

  private static Opcode SEQ_ReqLoadPrev_I(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ea.getBytes());
    StringBuilder info = new StringBuilder(ea.getDescription());
    byte[] textBytes = SeqHelper.readString(bs);
    baos.write(textBytes);
    String fileName = SeqHelper.getString(textBytes);
    info.append(" with SEQ file \"");
    info.append(fileName);
    info.append(".seq\"");
    return new SEQ_ReqLoadPrev_I(offset, baos.toByteArray(), info.toString());
  }

  private static Opcode SEQ_ReqLoadPrev(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    StringBuilder info = new StringBuilder(ea.getDescription());
    info.append(" with SEQ file from the first operand");
    return new SEQ_ReqLoadPrev(offset, ea.getBytes(), info.toString());
  }

  private static Opcode TskSendMsg(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new TskSendMsg(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode TskExecFunc(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new TskExecFunc(offset, ea.getBytes(), ea.getDescription());
  }

  private static Opcode op_0208(ByteStream bs) throws IOException {
    int offset = bs.offset();
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    return new UnknownOpcode(offset, ea.getBytes(), ea.getDescription());
  }
}