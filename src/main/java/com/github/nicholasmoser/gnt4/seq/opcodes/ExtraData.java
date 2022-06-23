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
    private final short first; // Always 0001
    private final short second; // Always 000A
    private final short type;
    private final short fourth;
    private final short fifth;
    private final short sixth;
    private short inputType;
    private short input;

    public ExtraData(ByteStream bs) throws IOException {
        this.offset = bs.offset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bs.peekBytes(12));
        first = bs.readShort();
        second = bs.readShort();
        type = bs.readShort();
        fourth = bs.readShort();
        fifth = bs.readShort();
        sixth = bs.readShort();
        switch (type) {
            case 1 -> {
                /*
                baos.write(bs.readBytes(8));
                if (fourth != 2) {
                    while (!Arrays.equals(bs.peekBytes(10), new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                        baos.write(bs.readBytes(2));
                    }
                    baos.write(bs.readBytes(10));
                } else if (fourth == 2) {
                    while (!Arrays.equals(bs.peekBytes(12), new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                        baos.write(bs.readBytes(2));
                    }
                    baos.write(bs.readBytes(12));
                }*/
                baos.write(bs.readBytes(12));
                while (!Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00})) {
                    baos.write(bs.readBytes(10));
                }
                baos.write(bs.readBytes(2));
                /*
                if (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x6C})) {
                    baos.write(bs.readBytes())
                }*/
            }
            case 2 -> {
                inputType = fourth;
                input = fifth;
                if (bs.peekWord() == 0) {
                    baos.write(bs.readBytes(4));
                } else {
                    if (inputType == 0x2270) {
                        baos.write(bs.readBytes(14));
                    } else if (inputType == 0x227F) {
                        baos.write(bs.readBytes(24));
                    } else if (inputType == 0x4000) {
                        while (!Arrays.equals(bs.peekBytes(8), new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                            baos.write(bs.readBytes(4));
                        }
                        baos.write(bs.readBytes(8));
                    }
                }
            }
            case 3 -> {
                inputType = fourth;
                input = fifth;
                baos.write(bs.readBytes(4));
            }
        }
        /*
        while (!Arrays.equals(bs.peekBytes(6), new byte[]{0x00, 0x01})) {
            baos.write(bs.readBytes(2));
        }

         */
        this.bytes = baos.toByteArray();
        //System.out.println(this);
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return String.format("%05X | %s %s", offset, MNEMONIC, formatRawBytes(bytes));
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
