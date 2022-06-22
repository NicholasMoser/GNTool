package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class ExtraData implements Opcode{

    private static final String MNEMONIC = "Extra Data";
    private final int offset;
    private final byte[] bytes;

    public ExtraData(int offset, ByteStream bs) throws IOException {
        this.offset = offset;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (!Arrays.equals(bs.peekWordBytes(), new byte[]{0x00, 0x00, 0x00, 0x00})) {
            baos.write(bs.readBytes(4));
        }
        baos.write(bs.readBytes(4));
        this.bytes = baos.toByteArray();
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return String.format("%05X | %s", offset, bytes);
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
                .withText(String.format("%05X | %s ", offset, MNEMONIC))
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }
}
