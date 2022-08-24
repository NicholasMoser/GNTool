package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.span;

import j2html.tags.ContainerTag;
import j2html.tags.specialized.SpanTag;

public interface Opcode{

  int getOffset();

  byte[] getBytes();

  byte[] getBytes(int offset, int size);

  String toAssembly();

  String toAssembly(int offset);

  ContainerTag toHTML();

  default String formatRawBytes(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (int i = 0; i < bytes.length; i++) {
      sb.append(String.format("%02X", bytes[i]));
      if (i % 4 == 3 && i != bytes.length - 1) {
        sb.append(' ');
      }
    }
    sb.append('}');
    return sb.toString();
  }

  default SpanTag formatRawBytesHTML(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(String.format("%02X", bytes[i]));
      if (i % 4 == 3 && i != bytes.length - 1) {
        sb.append(' ');
      }
    }
    return span(sb.toString()).withClass("g");
  }

}
