package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.span;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

public class BranchGreaterThanOrEqualToZero implements Opcode {

  private final int offset;
  private final int destination;
  private final byte secondByte;

  /**
   * @param offset      The offset of the opcode.
   * @param destination The destination of the branch.
   * @param secondByte  The second byte parameter exists because this opcode can have either 0x37 or
   *                    0x39 for the second byte.
   */
  public BranchGreaterThanOrEqualToZero(int offset, int destination, byte secondByte) {
    this.offset = offset;
    this.destination = destination;
    this.secondByte = secondByte;
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
  public String toString() {
    return String.format("%05X | bgez 0x%X {01%02X0000 %08X}", offset, destination, secondByte, destination);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", destination);
    return div(attrs(id))
        .with(span(String.format("%05X | bgez ", offset)))
        .with(a(String.format("0x%X", destination)).withHref(dest))
        .with(span(String.format(" {01%02X0000 %08X}", secondByte, destination)));
  }
}
