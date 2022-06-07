package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class BranchingOpcode implements Opcode {

  private String MNEMONIC;
  private byte[] bytes;
  private int offset;
  private int destination;
  private String destFuncName;

public BranchingOpcode(String MNEMONIC, byte[] bytes, int offset, int destination) {
    this.MNEMONIC = MNEMONIC;
    this.bytes = bytes;
    this.offset = offset;
    this.destination = destination;
    this.destFuncName = null;
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
    return Bytes.concat(bytes, ByteUtils.fromInt32(destination));
  }

  @Override
  public byte[] getBytes(int position, int size) {
    if (destination > position && destination < position + size) {
      return Bytes.concat(bytes, ByteUtils.fromInt32(destination - position));
    } else if (destination < size) {
      return Bytes.concat(bytes, ByteUtils.fromInt32(destination + position));
    }
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {%s %08X}", offset, MNEMONIC, destination, ByteUtils.bytesToHexString(bytes), destination);
  }

  @Override
  public String toAssembly() {
    if (destFuncName == null) {
      return String.format("%s 0x%X", MNEMONIC, destination);
    }
    return String.format("%s %s", MNEMONIC, destFuncName);
  }

  @Override
  public String toAssembly(int position) {
    if (destFuncName == null) {
      if (destination < position) {
        return String.format("%s 0x%X", MNEMONIC, destination);
      } else {
        return String.format("%s 0x%X", MNEMONIC, destination - position);
      }
    }
    return String.format("%s %s", MNEMONIC, destFuncName);
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
