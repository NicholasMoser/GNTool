package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;
import static j2html.TagCreator.p;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import j2html.tags.ContainerTag;
import java.util.ArrayList;
import java.util.List;

public class SeqEditOpcode implements Opcode {

  private final int offset;
  private final SeqEdit edit;

  public SeqEditOpcode(int offset, SeqEdit edit) {
    this.offset = offset;
    this.edit = edit;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return edit.getFullBytes();
  }

  @Override
  public String toString() {
    return String.format("%05X | Seq Edit: %s %s", offset, edit.getName(), formatRawBytes(getBytes()));
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    List<ContainerTag> opcodes = new ArrayList<>();
    try {
      int newBytesOffset = offset + edit.getNewBytesOffset();
      opcodes.addAll(SeqHelper.getOpcodesString(edit.getNewBytesWithBranchBack(), newBytesOffset));
    } catch (Exception e) {
      opcodes.add(p("Unable to parse opcodes: " + e.getLocalizedMessage()));
    }
    String text = this
        + String.format("<br>Offset: 0x%X", edit.getOffset())
        + "<br>Old Bytes: "
        + formatRawBytes(edit.getOldBytes())
        + "<br>New Bytes: "
        + formatRawBytes(edit.getNewBytes())
        + "<br>New Bytes with Branch Back: "
        + formatRawBytes(edit.getNewBytesWithBranchBack())
        + "<br>New Byte Opcodes: ";
    return div(attrs(id)).withText(text).with(opcodes);
  }
}
