package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import j2html.tags.ContainerTag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ComboList implements Opcode {

  private final List<Combo> combos;

  public ComboList(byte[] header, List<Combo> combos) {
    this.combos = combos;
  }

  @Override
  public int getOffset() {
    return combos.get(0).getOffset();
  }

  @Override
  public byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      for (Combo combo : combos) {
        baos.write(combo.getBytes());
      }
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return combos.stream()
        .map(Combo::toString)
        .collect(Collectors.joining("\n"));
  }

  @Override
  public ContainerTag toHTML() {
    List<ContainerTag> comboDivs = combos.stream()
        .map(Combo::toHTML)
        .collect(Collectors.toList());
    return div(comboDivs.toArray(new ContainerTag[combos.size()]));
  }
}
