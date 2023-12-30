package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertAsm implements Symbol {
  private static final Logger LOGGER = Logger.getLogger(InsertAsm.class.getName());
  private final int TYPE = 5;
  private final String name;
  private final int offset;
  private final byte[] oldBytes;
  private final List<Opcode> oldCodes;
  private final List<Opcode> newCodes;

  public InsertAsm(String name, int offset, List<Opcode> oldCodes, List<Opcode> newCodes) {
    this.name = name;
    this.offset = offset;
    this.oldCodes = oldCodes;
    this.newCodes = newCodes;
    this.oldBytes = opcodesToBytes(oldCodes);
  }

  public InsertAsm(String name, int offset, byte[] oldBytes, List<Opcode> newCodes) {
    this.name = name;
    this.offset = offset;
    this.oldCodes = null; // old bytes are not valid opcodes
    this.newCodes = newCodes;
    this.oldBytes = oldBytes;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int dataOffset() {
    return Symbol.getNameBytes(name).length + 0x10;
  }

  @Override
  public int length() {
    int codeLength = Symbol.getNameBytes(name).length + 0x10 + opcodesToBytes(newCodes).length;
    int mod = codeLength % 16;
    if (mod != 0) {
      codeLength += (16 - mod); // add padding
    }
    codeLength += oldBytes.length;
    mod = codeLength % 16;
    if (mod != 0) {
      return codeLength + (16 - mod); // add padding
    }
    return codeLength;
  }

  @Override
  public byte[] bytes() {
    try {
      byte[] newBytes = opcodesToBytes(newCodes);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(Symbol.getNameBytes(name));
      baos.write(ByteUtils.fromInt32(TYPE));
      baos.write(ByteUtils.fromInt32(length(newBytes)));
      baos.write(ByteUtils.fromInt32(newBytes.length));
      baos.write(ByteUtils.fromInt32(oldBytes.length));
      baos.write(newBytes);
      int mod = baos.size() % 16;
      if (mod != 0) {
        baos.write(new byte[16 - mod]);
      }
      baos.write(oldBytes);
      mod = baos.size() % 16;
      if (mod != 0) {
        baos.write(new byte[16 - mod]);
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

  /**
   * Calculate length with pre-determined opcode bytes, to save processing them an additional
   * time.
   *
   * @param newBytes The new opcode bytes.
   * @return The length of this symbol.
   */
  private int length(byte[] newBytes) {
    int codeLength = Symbol.getNameBytes(name).length + 0x10 + newBytes.length;
    int mod = codeLength % 16;
    if (mod != 0) {
      codeLength += (16 - mod); // add padding
    }
    codeLength += oldBytes.length;
    mod = codeLength % 16;
    if (mod != 0) {
      return codeLength + (16 - mod); // add padding
    }
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
