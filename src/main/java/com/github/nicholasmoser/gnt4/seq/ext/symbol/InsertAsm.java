package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertAsm implements Symbol {
  public static final int TYPE = 5;
  private static final Logger LOGGER = Logger.getLogger(InsertAsm.class.getName());
  private final String name;
  private final int hijackOffset;
  private final byte[] oldBytes;
  private final List<Opcode> oldCodes;
  private final List<Opcode> newCodes;
  private final Map<String, Integer> innerLabels;

  public InsertAsm(String name, int hijackOffset, List<Opcode> newCodes, List<Opcode> oldCodes, Map<String, Integer> innerLabels) {
    this.name = name;
    this.hijackOffset = hijackOffset;
    this.oldCodes = oldCodes;
    this.newCodes = newCodes;
    this.oldBytes = opcodesToBytes(oldCodes);
    this.innerLabels = innerLabels;
  }

  public InsertAsm(String name, int hijackOffset, List<Opcode> newCodes, byte[] oldBytes, Map<String, Integer> innerLabels) {
    this.name = name;
    this.hijackOffset = hijackOffset;
    this.oldCodes = null; // old bytes are not valid opcodes
    this.newCodes = newCodes;
    this.oldBytes = oldBytes;
    this.innerLabels = innerLabels;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int dataOffset() {
    int dataOffset = Symbol.getNameBytes(name).length;
    dataOffset = ByteUtils.align(dataOffset, 16);
    return dataOffset + 0x20;
  }

  @Override
  public int length() {
    int codeLength = Symbol.getNameBytes(name).length;
    codeLength = ByteUtils.align(codeLength, 16);
    codeLength += 0x20;
    codeLength += opcodesToBytes(newCodes).length;
    codeLength = ByteUtils.align(codeLength, 16);
    codeLength += oldBytes.length;
    codeLength = ByteUtils.align(codeLength, 16);
    for (String name : innerLabels.keySet()) {
      codeLength += 0x4;
      codeLength += Symbol.getNameBytes(name).length;
      codeLength = ByteUtils.align(codeLength, 16);
    }
    return codeLength;
  }

  @Override
  public byte[] bytes() {
    try {
      byte[] newBytes = opcodesToBytes(newCodes);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(Symbol.getNameBytes(name));
      ByteUtils.align(baos, 16);
      baos.write(ByteUtils.fromInt32(TYPE));
      baos.write(ByteUtils.fromInt32(length(newBytes)));
      baos.write(ByteUtils.fromInt32(hijackOffset));
      baos.write(ByteUtils.fromInt32(newBytes.length));
      baos.write(ByteUtils.fromInt32(oldBytes.length));
      baos.write(ByteUtils.fromInt32(innerLabels.size()));
      baos.write(new byte[8]); // 8 null bytes of padding
      baos.write(newBytes);
      ByteUtils.align(baos, 16);
      baos.write(oldBytes);
      ByteUtils.align(baos, 16);
      for (Entry<String, Integer> entry : innerLabels.entrySet()) {
        String labelName = entry.getKey();
        int labelOffset = entry.getValue();
        baos.write(ByteUtils.fromInt32(labelOffset));
        baos.write(Symbol.getNameBytes(labelName));
        ByteUtils.align(baos, 16);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Opcode> oldOpcodes() {
    return oldCodes;
  }

  public List<Opcode> newOpcodes() {
    return newCodes;
  }

  public Map<String, Integer> innerLabels() {
    return innerLabels;
  }

  /**
   * Calculate length with pre-determined opcode bytes, to save processing them an additional
   * time.
   *
   * @param newBytes The new opcode bytes.
   * @return The length of this symbol.
   */
  private int length(byte[] newBytes) {
    int codeLength = Symbol.getNameBytes(name).length + 0x10 + newBytes.length;
    codeLength = ByteUtils.align(codeLength, 16);
    codeLength += oldBytes.length;
    codeLength = ByteUtils.align(codeLength, 16);
    return codeLength;
  }

  /**
   * Convert the given opcodes to bytes.
   *
   * @param opcodes The opcodes to convert.
   * @return The bytes of the opcodes.
   */
  public byte[] opcodesToBytes(List<Opcode> opcodes) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for (Opcode opcode : opcodes) {
      try {
        baos.write(opcode.getBytes());
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error getting opcode bytes", e);
      }
    }
    return baos.toByteArray();
  }
}
