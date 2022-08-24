package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static j2html.TagCreator.*;

public class SpawnProjectile implements Opcode {

  private final static String MNEMONIC = "spawn_projectile";
  private final int offset;
  private final byte[] bytes;
  private int first;
  private int second;
  private int target;
  private int fourth;
  private int fifth;

  public SpawnProjectile(int offset, byte[] bytes) throws IOException {
    this.offset = offset;
    this.bytes = bytes;
    ByteStream bs = new ByteStream(bytes);
    bs.skipWord();
    this.first = bs.readWord();
    this.second = bs.readWord();
    this.target = bs.readWord();
    this.fourth = bs.readWord();
    this.fifth = bs.readWord();
  }

  public SpawnProjectile(int offset, int first, int second, int target, int fourth, int fifth) throws IOException {
    this.offset = offset;
    this.first = first;
    this.second = second;
    this.target = target;
    this.fourth = fourth;
    this.fifth = fifth;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(ByteUtils.fromInt32(0x47000026));
    baos.write(ByteUtils.fromInt32(first));
    baos.write(ByteUtils.fromInt32(second));
    baos.write(ByteUtils.fromInt32(target));
    baos.write(ByteUtils.fromInt32(fourth));
    baos.write(ByteUtils.fromInt32(fifth));
    this.bytes = baos.toByteArray();
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return Bytes.concat(ByteUtils.fromUint32(0x47000026), ByteUtils.fromUint32(first),
            ByteUtils.fromUint32(second), ByteUtils.fromUint32(target), ByteUtils.fromUint32(fourth),
            ByteUtils.fromUint32(fifth));
  }

  @Override
  public byte[] getBytes(int offset, int size) {
    return getBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s", offset, MNEMONIC, formatRawBytes(getBytes()));
  }

  @Override
  public String toAssembly() {
    return String.format("%s 0x%04X, 0x%04X, 0x%04X, 0x%04X, 0x%04X", MNEMONIC, first, second, target, fourth, fifth);
  }

  @Override
  public String toAssembly(int offset) {
    return toAssembly();
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    String targetHref = String.format("#%X", target);
    return div(attrs(id))
        .withText(String.format("%05X | %s 0x%X, 0x%X, ", offset, MNEMONIC,
            first, second))
        .with(a(String.format("0x%X",target)).withHref(targetHref))
        .withText(String.format(", 0x%X, 0x%X ", fourth, fifth))
        .with(formatRawBytesHTML(getBytes()));
  }
}
