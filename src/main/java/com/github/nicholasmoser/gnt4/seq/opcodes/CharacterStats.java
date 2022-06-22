package com.github.nicholasmoser.gnt4.seq.opcodes;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class CharacterStats implements Opcode{

    private final static String MNEMONIC = "Character Stats";
    private final int offset;
    private final byte[] bytes;
    private String info;

    public CharacterStats(int offset, byte[] bytes) {
        this.offset = offset;
        this.bytes = bytes;
    }

    public String getInfo() {
        return info;
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
        return bytes;
    }

    @Override
    public String toString() {
        return String.format("%05X | %s %s", offset, MNEMONIC, formatRawBytes(bytes));
    }

    @Override
    public String toAssembly() {
        return String.format("%s",MNEMONIC);
    }

    @Override
    public String toAssembly(int offset) {
        return toAssembly();
    }

    @Override
    public ContainerTag toHTML() {
        String id = String.format("#%X", offset);
        return div(attrs(id))
                .withText(String.format("%05X | %s", offset, MNEMONIC))
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }
}
