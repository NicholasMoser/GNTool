package com.github.nicholasmoser.gnt4.seq.opcodes;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;


public class PointerToOffset implements Opcode {

    private final static String MNEMONIC = "ptr_to_offset";
    private final int offset;
    private final byte[] bytes;
    private final String info;

    public PointerToOffset(int offset, byte[] bytes, String info) {
        this.offset = offset;
        this.bytes = bytes;
        this.info = info;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public byte[] getBytes() { return bytes; }

    @Override
    public String toString() {
        return String.format("%05X | %s %s %s", offset, MNEMONIC, info, formatRawBytes(bytes));
    }

    @Override
    public ContainerTag toHTML() {
        String id = String.format("#%X", offset);
        return div(attrs(id)).withText(toString());
    }
}
