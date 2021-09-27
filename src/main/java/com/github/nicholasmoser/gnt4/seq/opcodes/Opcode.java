package com.github.nicholasmoser.gnt4.seq.opcodes;

import j2html.tags.ContainerTag;
import java.util.Optional;

public interface Opcode {

  int getOffset();

  byte[] getBytes();

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

  default Optional<String> description() {
    return Optional.empty();
  }
}
