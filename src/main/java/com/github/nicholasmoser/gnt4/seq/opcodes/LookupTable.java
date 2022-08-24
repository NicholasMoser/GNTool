package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class LookupTable implements Opcode{

    private final static String MNEMONIC = "lookup_table";
    private final int offset;
    private final byte[] bytes;
    private List<Integer> pointers = new LinkedList<>();

    public LookupTable(ByteStream bs) throws IOException {
        this.offset = bs.offset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bs.peekWordBytes());
        pointers.add(bs.peekWord());
        int startOfData = bs.readWord();
        while (bs.offset() != startOfData && bs.peekWord() != 0) {
            baos.write(bs.peekWordBytes());
            pointers.add(bs.peekWord());
            baos.write(bs.readWord());
        }
        bytes = baos.toByteArray();
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
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%05X | ", offset));
        for (Integer pnt : this.pointers) {
            sb.append(String.format("%08X ", pnt));
        }
        sb.append(formatRawBytes(bytes));
        return String.format(sb.toString());
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
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%05X | %s ", offset, MNEMONIC));
        for (Integer pnt : this.pointers) {
            sb.append(String.format("<a href=#%X>0x%X</a> ", pnt, pnt));
        }
        return div(attrs(id))
                .withText(sb.toString())
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }


}
