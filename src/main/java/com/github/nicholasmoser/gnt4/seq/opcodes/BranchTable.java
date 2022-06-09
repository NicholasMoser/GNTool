package com.github.nicholasmoser.gnt4.seq.opcodes;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BranchTable implements Opcode {

  private final static String MNEMONIC = "branch_table";
  private final int offset;
  private byte[] bytes;
  private final String info;
  private List<Integer> offsets;
  private List<String> branches;

  public BranchTable(int offset, byte[] bytes, String info, List<Integer> offsets) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
    this.offsets = offsets;
    this.branches = new LinkedList<>();
  }

  public BranchTable(int offset, byte[] bytes, String info, List<Integer> offsets, List<String> branches) {
    this.offset = offset;
    this.bytes = bytes;
    this.info = info;
    this.offsets = offsets;
    this.branches = branches;
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
  public byte[] getBytes(int offset, int size) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bytes, 0, 8);
    for (Integer branch : offsets) {
      try {
        int destination = branch;
        if (destination >= offset && destination <= offset + size) {
          baos.write(ByteUtils.fromInt32(branch - offset));
        } else if (destination <= size) {
          baos.write(ByteUtils.fromInt32(branch + offset));
        } else {
          baos.write(ByteUtils.fromInt32(branch));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return baos.toByteArray();
  }

  private void setBytes() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bytes, 0, 8);
    for (Integer offset : offsets) {
      try {
        baos.write(ByteUtils.fromInt32(offset));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    bytes = baos.toByteArray();
  }

  public String setOffsets(List<Integer> offsets) {
    if (this.offsets.size() != offsets.size()) {
      return "Error, wrong number of offsets";
    }
    this.offsets = offsets;
    return null;
  }

  public List<Integer> getOffsets() {
    return offsets;
  }

  public void setBranches(List<String> branches) {
    this.branches = branches;
  }

  public List<String> getBranches() {
    return branches;
  }

  @Override
  public String toString() {
    return String.format("%05X | %s %s (%d branches) %s", offset, MNEMONIC, info, offsets.size(), formatRawBytes(bytes));
  }

  @Override
  public String toAssembly() {
    StringBuilder branches = new StringBuilder();
    for (Integer branch : offsets) {
      branches.append(String.format(", 0x%08X", branch));
    }
    return String.format("%s %s%s",MNEMONIC, info, branches);
  }

  @Override
  public String toAssembly(int position) {
    StringBuilder branches = new StringBuilder();
    for (Integer branch : offsets) {
      if (branch > position) {
        branches.append(String.format(", 0x%08X", branch - position));
      } else {
        branches.append(String.format(", 0x%08X", branch));
      }
    }
    return String.format("%s %s%s",MNEMONIC, info, branches);
  }

  @Override
  public ContainerTag toHTML() {
    String id = String.format("#%X", offset);
    DivTag div =  div(attrs(id))
        .withText(String.format("%05X | %s %s (%d branches):", offset, MNEMONIC, info, offsets.size()));
    for (int offset : offsets) {
      String dest = String.format("#%X", offset);
      div.withText(" ");
      div.with(a(String.format("0x%X", offset)).withHref(dest));
    }
    div.withText(" ");
    return div.with(formatRawBytesHTML(getBytes()));
  }
}
