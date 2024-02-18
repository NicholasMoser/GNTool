package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.i;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComboList implements Opcode {

  private final int offset;
  private final List<Combo> combos;

  public ComboList(int offset, List<Combo> combos) {
    this.offset = offset;
    this.combos = combos;
  }

  public void removeCombo(int index) {
    combos.remove(index);
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(ByteUtils.fromInt32(combos.size()));
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
    int comboSize = combos.size();
    byte[] comboSizeBytes = ByteUtils.fromInt32(comboSize);
    String text = String.format("%05X | combo_list (0x%X total) %s\n", offset, comboSize, formatRawBytes(comboSizeBytes));
    return text + combos.stream()
        .map(Combo::toString)
        .collect(Collectors.joining("\n"));
  }

  @Override
  public String toAssembly() {
    return toString();
  }

  @Override
  public ContainerTag toHTML() {
    int comboSize = combos.size();
    byte[] comboSizeBytes = ByteUtils.fromInt32(comboSize);
    List<ContainerTag> comboDivs = new ArrayList<>(combos.size() + 1);

    String id = String.format("#%X", offset);
    ContainerTag start = div(attrs(id))
        .withText(String.format("%05X | combo_list (0x%X total) ", offset, comboSize))
        .with(formatRawBytesHTML(ByteUtils.fromInt32(combos.size())));
    comboDivs.add(start);

    comboDivs.addAll(combos.stream()
        .map(Combo::toHTML)
        .toList());
    return div(comboDivs.toArray(new ContainerTag[combos.size()]));
  }
}
