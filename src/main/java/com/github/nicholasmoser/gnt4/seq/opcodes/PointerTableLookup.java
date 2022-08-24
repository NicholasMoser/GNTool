package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PointerTableLookup implements Opcode {

  private final static String MNEMONIC = "ptr_table_lookup";
  private final int opcode = 0x090C;
  private final int offset;
  private final byte[] bytes;
  private final int tableOffset;
  private final String info;
  private int operand1;
  private int operand2;
  private LookupTable lut;

  public PointerTableLookup(int offset, byte[] bytes, int tableOffset, String info) {
    this.offset = offset;
    this.bytes = bytes;
    this.tableOffset = tableOffset;
    this.info = info;
  }

  public PointerTableLookup(ByteStream bs) throws IOException {
    this.offset = bs.offset();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bs.readNBytes(2));
    operand1 = bs.peekBytes(1)[0];
    baos.write(bs.readNBytes(1));
    operand2 = bs.peekBytes(1)[0];
    baos.write(bs.readNBytes(1));
    this.tableOffset = bs.peekWord();
    baos.write(bs.readNBytes(4));
    this.bytes = baos.toByteArray();
    this.info = "";
    int tmp = bs.offset();
    bs.seek(this.tableOffset);
    this.lut = new LookupTable(bs);
    bs.seek(tmp);
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() { return bytes; }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    return String.format("%s %s",MNEMONIC,info);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  public LookupTable getLut() {
    return lut;
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String dest = String.format("#%X", tableOffset);
    return div(attrs(id))
        .withText(String.format("%05X | %s %s (table at ", offset, MNEMONIC, info))
        .with(a(String.format("0x%X", tableOffset)).withHref(dest))
        .withText(") ")
        .with(formatRawBytesHTML(getBytes()));
  }
}
