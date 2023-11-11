package com.github.nicholasmoser.gnt4.seq.opcodes;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;


public class PointerFromOffset implements Opcode {

    private final static String MNEMONIC = "ptr_from_offset";
    private final int offset;
    private final byte[] bytes;
    private final String info;

    public PointerFromOffset(int offset, byte[] bytes, String info) {
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
    public byte[] getBytes(int offset, int size) {
        return getBytes();
    }

    @Override
    public String toString() {
        return String.format("%05X | %s %s %s ", offset, MNEMONIC, info, formatRawBytes(bytes));
    }

    @Override
    public String toAssembly() {
        return String.format("%s %s",MNEMONIC,info);
    }

    @Override
    public String toAssembly(int offset) {
        return toAssembly();
    }

    @Override
    public ContainerTag toHTML() {
        String id = String.format("#%X", offset);
        String infoNoTypes = info.replace("byte", "").replace("short", "");
        Integer target = Integer.decode(infoNoTypes.split(",")[1].replace(" ", ""));
        if (target != null) {
            String targetHref = String.format("#%X", target);
            return div(attrs(id))
                    .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info.split(",")[0]))
                    .with(a(String.format("%s ",info.split(",")[1])).withHref(targetHref))
                    .with(formatRawBytesHTML(bytes));
        }
        return div(attrs(id))
            .withText(String.format("%05X | %s %s ", offset, MNEMONIC, info))
            .with(a())
            .with(formatRawBytesHTML(bytes));
    }
}
