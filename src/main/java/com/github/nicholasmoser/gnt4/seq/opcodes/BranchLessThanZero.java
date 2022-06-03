package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchLessThanZero implements Opcode, BranchingOpcode {

  private final static String MNEMONIC = "bltz";
  private final int offset;
  private final int destination;
  private final byte secondByte;
  private String destFuncName;

  /**
   * @param offset      The offset of the opcode.
   * @param destination The destination of the branch.
   * @param secondByte  The second byte parameter exists because this opcode can have either 0x37 or
   *                    0x39 for the second byte.
   */
  public BranchLessThanZero(int offset, int destination, byte secondByte) {
    this.offset = offset;
    this.destination = destination;
    this.secondByte = secondByte;
  }

  @Override
  public int getDestination() {
    return destination;
  }

  @Override
  public void setDestinationFunctionName(String destFuncName) {
    this.destFuncName = destFuncName;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(new byte[]{0x01, secondByte, 0x00, 0x00}, ByteUtils.fromInt32(destination));
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    if (destination > offset && destination < offset + size) {
      return Bytes.concat(new byte[] {0x01, secondByte, 0x00, 0x00}, ByteUtils.fromInt32(destination + offset));
    }
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s 0x%X {01%02X0000 %08X}", offset, MNEMONIC, destination, secondByte, destination);
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%X",MNEMONIC,destination);
  }

  @Override
  public String toAssembly(int offset) {
    if (destination > offset) {
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
