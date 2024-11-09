package com.github.nicholasmoser.gnt4.seq.ext.controller;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.util.List;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InsertAsmController implements SymbolController {

  public TextArea name;
  public TextField offset;
  public TextField hijackedBytesLength;
  public TextArea hijackedBytes;
  public TextArea newBytes;

  public String getName() {
    if (name.getText().isBlank()) {
      throw new IllegalStateException("Name field is blank");
    }
    return name.getText();
  }

  public void setName(String value) {
    name.setText(value);
  }

  public int getOffset() {
    if (offset.getText().isBlank()) {
      throw new IllegalStateException("Offset field is blank");
    }
    return readNumber(offset.getText());
  }

  public void setOffset(int value) {
    offset.setText(String.format("0x%X", value));
  }

  public int getHijackedBytesLength() {
    if (hijackedBytesLength.getText().isBlank()) {
      throw new IllegalStateException("Hijacked bytes length field is blank");
    }
    return readNumber(hijackedBytesLength.getText());
  }

  public byte[] getHijackedBytes() {
    String text = hijackedBytes.getText();
    if (text.isBlank()) {
      throw new IllegalStateException("Hijacked bytes field is blank");
    }
    verifyIsHex(text, "Hijacked bytes");
    return readHex(text);
  }

  public void setHijackedBytes(List<Opcode> opcodes) {
    StringBuilder sb = new StringBuilder();
    int length = 0;
    for (Opcode op : opcodes) {
      byte[] bytes = op.getBytes();
      length += bytes.length;
      sb.append(String.format("%s\n", ByteUtils.bytesToHexStringWords(bytes)));
    }
    hijackedBytes.setText(sb.toString());
    hijackedBytesLength.setText(String.format("0x%X", length));
  }

  public void setHijackedBytes(byte[] bytes) {
    hijackedBytes.setText(ByteUtils.bytesToHexStringWords(bytes));
    hijackedBytesLength.setText(String.format("0x%X", bytes.length));
  }

  public byte[] getNewBytes() {
    String text = newBytes.getText();
    if (text.isBlank()) {
      throw new IllegalStateException("New bytes field is blank");
    }
    verifyIsHex(text, "Hijacked bytes");
    return readHex(text);
  }

  public void setNewBytes(String value) {
    newBytes.setText(value);
  }
}
