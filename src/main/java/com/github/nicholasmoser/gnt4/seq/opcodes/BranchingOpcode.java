package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class BranchingOpcode implements Opcode {

  private static String MNEMONIC;
  private byte[] bytes;
  private int offset;
  private int destination;
  private String destFuncName;

public BranchingOpcode(String MNEMONIC, byte[] bytes, int offset, int destination) {
    this.MNEMONIC = MNEMONIC;
    this.bytes = bytes;
    this.offset = offset;
    this.destination = destination;
    this.destFuncName = "";
  }

  public BranchingOpcode(String MNEMONIC, byte[] bytes, int offset, String destFuncName) {
    this.MNEMONIC = MNEMONIC;
    this.bytes = bytes;
    this.offset = offset;
    this.destination = -1;
    this.destFuncName = destFuncName;
  }

  public BranchingOpcode(String MNEMONIC, byte[] bytes, int offset, int destination, String destFuncName) {
    this.MNEMONIC = MNEMONIC;
    this.bytes = bytes;
    this.offset = offset;
    this.destination = destination;
    this.destFuncName = destFuncName;
  }

  public String getMnemonic() {
    return MNEMONIC;
  }

  public int getDestination() {
    return destination;
  }

  public void setDestination(int destination) {
    this.destination = destination;
  }

  public String getDestinationFunctionName() {
    return destFuncName;
  }

  public void setDestinationFunctionName(String destFuncName) {
    this.destFuncName = destFuncName;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[] { 0x01, 0x32, 0x00, 0x00 }, ByteUtils.fromInt32(destination));
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    if (destination > offset && destination < offset + size) {
      return Bytes.concat(new byte[] {0x01, 0x32, 0x00, 0x00}, ByteUtils.fromInt32(destination + offset));
    }
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {%s %08X}", offset, MNEMONIC, destination, ByteUtils.bytesToHexString(bytes), destination);
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%X", MNEMONIC, destination);
  }

  @Override
  public String toAssembly(int offset) {
    if (destination < offset) {
      return String.format("%s 0x%X", MNEMONIC, destination);
    } else {
      return String.format("%s 0x%X", MNEMONIC, destination - offset);
    }
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String destName = destFuncName != null ? destFuncName : String.format("0x%X", destination);
    String destHref = String.format("#%X", destination);
    return div(attrs(id))
            .withText(String.format("%05X | %s ", offset, MNEMONIC))
            .with(a(destName).withHref(destHref))
            .withText(" ")
            .with(formatRawBytesHTML(getBytes()));
  }

}
