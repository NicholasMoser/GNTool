package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
        boolean endsEarly = endsEarly(bs);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bs.peekBytes(endsEarly ? 10 : 12));
        first = bs.readShort();
        second = bs.readShort();
        type = bs.readShort();
        fourth = bs.readShort();
        fifth = bs.readShort();
        if (endsEarly) {
            sixth = 0;
        } else {
            sixth = bs.readShort();
            switch (type) {
                case 1 -> {
                    baos.write(bs.readNBytes(12));
                    while (!Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00})) {
                        baos.write(bs.readNBytes(10));
                    }
                    baos.write(bs.readNBytes(2));
                }
                case 2 -> {
                    inputType = fourth;
                    input = fifth;
                    if (bs.peekWord() == 0) {
                        baos.write(bs.readNBytes(4));
                    } else {
                        if (inputType == 0x2270) {
                            baos.write(bs.readNBytes(14));
                        } else if (inputType == 0x227F) {
                            baos.write(bs.readNBytes(22));
                            while (!Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00})) {
                                baos.write(bs.readNBytes(10));
                            }
                            baos.write(bs.readNBytes(2));
                        } else if (inputType == 0x4000) {
                            boolean isSCON4Itachi = false;
                            while (!Arrays.equals(bs.peekBytes(8), new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                                baos.write(bs.readNBytes(4));
                                // Workaround for Itachi in SCON4, who has String Active Strings
                                // begin 4 bytes earlier than expected
                                if (Arrays.equals(bs.peekBytes(0xE), new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x19, (byte) 0x88, 0x00, 0x02, (byte) 0x92, 0x58})) {
                                    isSCON4Itachi = true;
                                    break;
                                }
                            }
                            baos.write(bs.readNBytes(isSCON4Itachi ? 6 : 8));
                        }
                    }
                }
                case 3 -> {
                    inputType = fourth;
                    input = fifth;
                    baos.write(bs.readNBytes(4));
                }
            }
        }
        this.bytes = baos.toByteArray();
    }

    /**
     * In SCON4, some ExtraData end early at 10 bytes instead of 12 bytes. This method checks for
     * that by looking at where the next ExtraData starts and ends.
     *
     * @param bs The ByteStream.
     * @return If the ExtraData ends early.
     * @throws IOException If any I/O exception occurs.
     */
    private boolean endsEarly(ByteStream bs) throws IOException {
        int offset = bs.offset();
        bs.skip(10);
        boolean endsEarly = bs.readWord() == 0x1000A;
        bs.seek(offset);
        return endsEarly;
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
