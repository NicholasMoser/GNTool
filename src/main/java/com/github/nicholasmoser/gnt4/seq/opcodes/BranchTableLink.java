package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;
import java.util.List;
import java.util.logging.Logger;

public class BranchTableLink implements Opcode {

  private static final Logger LOGGER = Logger.getLogger(Opcode.class.getName());
  private final static String MNEMONIC = "branch_table_link";
  private final int offset;
  private byte[] bytes;
  private final String info;
  private final List<Destination> destinations;

  public BranchTableLink(int offset, byte[] bytes, String info, List<Destination> destinations) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
    this.destinations = destinations;
  }

  public List<Destination> getDestinations() {
    return destinations;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s (%d branches) %s", offset, MNEMONIC, info, destinations.size(), formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    StringBuilder assembly = new StringBuilder();
    for (Destination destination : destinations) {
      assembly.append(String.format(", %s", destination.toString()));
    }
    return String.format("%s %s%s",MNEMONIC, info, assembly);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    DivTag div =  div(attrs(id))
        .withText(String.format("%05X | %s %s (%d branches):", offset, MNEMONIC, info, destinations.size()));
    for (Destination destination : destinations) {
      div.withText(" ");
      div.with(a(destination.toString()).withHref(destination.toString()));
    }
    div.withText(" ");
    return div.with(formatRawBytesHTML(getBytes()));
  }
}
