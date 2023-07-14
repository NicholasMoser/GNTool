package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;
import j2html.tags.specialized.DivTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class ActiveStrings implements Opcode{

    private final static String MNEMONIC = "Active Strings";
    private final int offset;
    private final byte[] bytes;
    private List<Integer> stringOffsets;
    private String info;

    public ActiveStrings(ByteStream bs) throws IOException {
        this.offset = bs.offset();
        this.stringOffsets = new LinkedList<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (!Arrays.equals(bs.peekBytes(4), new byte[]{0x00, 0x00, 0x00, 0x00})) {
            baos.write(bs.peekWordBytes());
            stringOffsets.add(bs.readWord());
        }
        baos.write(bs.readBytes(4));
        this.bytes = baos.toByteArray();
    }

    public String getInfo() {
        return info;
    }

    public List<Integer> getStringOffsets() {
        return stringOffsets;
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
        return String.format("%05X | %s %s", offset, MNEMONIC, stringOffsets);
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
        DivTag div = div(attrs(id))
                .withText(String.format("%05X | String %s ", offset, MNEMONIC));
        for (int off : stringOffsets) {
            String dest = String.format("#%X", off);
            div.withText(" ");
            div.with(a(String.format("0x%X", off)).withHref(dest));
        }
        div.withText(" ");
        return div.with(formatRawBytesHTML(getBytes()));
    }

}
