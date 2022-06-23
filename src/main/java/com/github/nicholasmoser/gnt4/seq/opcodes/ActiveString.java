package com.github.nicholasmoser.gnt4.seq.opcodes;


import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class ActiveString implements Opcode {

    private final static String MNEMONIC = "String";
    private final int offset;
    private final byte[] bytes;
    private short type; // Only 1
    private List<Short> from_atk;
    private short before;
    private short delayPress;
    private short delay;
    private short unknown;
    private short nrPress;
    private int followType;
    private short input;
    private short unknown1;
    private short followUpAllowed;
    private short to_atk;
    private int last;
    private short divider;
    private List<FollowUp> followUps;

    public ActiveString(ByteStream bs) throws IOException {
        this.offset = bs.offset();
        from_atk = new LinkedList<>();
        followUps = new LinkedList<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bs.peekBytes(2));
        type = bs.readShort();
        if ((bs.peekWord() & 0x40000000) == 0) {
            baos.write(bs.peekBytes(2));
            from_atk.add(bs.readShort());
        } else {
            baos.write(bs.peekBytes(2));
            short tmp = bs.readShort();
            from_atk.add(tmp);
            for (short i = 0; i < (tmp & 0x0FFF); i++) {
                baos.write(bs.peekBytes(2));
                from_atk.add(bs.readShort());
            }
        }
        baos.write(bs.peekBytes(2));
        before = bs.readShort();
        baos.write(bs.peekBytes(2));
        delayPress = bs.readShort();
        baos.write(bs.peekBytes(2));
        delay = bs.readShort();
        baos.write(bs.peekBytes(2));
        unknown = bs.readShort();
        baos.write(bs.peekBytes(2));
        nrPress = bs.readShort();
        baos.write(bs.peekBytes(4));
        followType = bs.readWord();
        baos.write(bs.peekBytes(2));
        input = bs.readShort();
        baos.write(bs.peekBytes(2));
        unknown1 = bs.readShort();
        baos.write(bs.peekBytes(2));
        followUpAllowed = bs.readShort();
        baos.write(bs.peekBytes(2));
        to_atk = bs.readShort();
        if (Arrays.equals(bs.peekBytes(8), new byte[]{0x00, 0x00, 0x00, 0x28, 0x00, 0x00, 0x00, 0x01})) {
            baos.write(bs.readBytes(6));
        } else if (followType == 0x062270) {
            if (to_atk == 0) {
                baos.write(bs.readBytes(4));
                baos.write(bs.peekBytes(2));
                to_atk = bs.readShort();
            }
            //baos.write(bs.readBytes(2));
            if (bs.peekWord() == 0x28) {
                baos.write(bs.readBytes(4));
                if (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00}) && bs.peekWord() != 0) {
                    baos.write(bs.readBytes(2));
                }
            }
            if (bs.peekWord() == 4) {
                baos.write(bs.readBytes(2));
            }
            while (true) {
                if (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x28})) {
                    baos.write(bs.readBytes(2));
                    continue;
                }
                FollowUp fu = new FollowUp(bs);
                followUps.add(fu);
                baos.write(fu.bytes);
                if (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00})) {
                    if (bs.peekWord() == 4) {
                        baos.write(bs.readBytes(2));
                    } else {
                        break;
                    }
                }
            }
            if (bs.peekWord() == 0) {
                baos.write(bs.readBytes(2));
            } else {
                baos.write(bs.readBytes(2));
            }
        } else if (followType == 0x06227F) {
            while (true) {
                System.out.println(formatRawBytes(baos.toByteArray()));
                if (Arrays.equals(bs.peekBytes(2), new byte[]{0x00, 0x00})) {
                    if (bs.peekWord() == 0x28) {
                        baos.write(bs.readBytes(10));
                    } else {
                        break;
                    }
                }
                baos.write(bs.readBytes(12));
            }
            baos.write(bs.readBytes(2));
        }
        this.bytes = baos.toByteArray();
        System.out.println(this);
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
        for (Short atk : from_atk) {
            sb.append(String.format(" ATK ID %X", atk));
        }
        return div(attrs(id))
                .withText(String.format("%05X | %s from%s", offset, MNEMONIC, sb))
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }

    @Override
    public String toString() {
        return String.format("%5X | %s %s", offset, MNEMONIC, formatRawBytes(bytes));
    }

    private class FollowUp {

        private final byte[] bytes;

        private short before;
        private short delayPress;
        private short delay;
        private short unknown;
        private short nrPress;
        private int followType;
        private short input;
        private short unknown1;
        private short followUpAllowed;
        private short to_atk;

        FollowUp(ByteStream bs) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(bs.peekBytes(2));
            before = bs.readShort();
            baos.write(bs.peekBytes(2));
            delayPress = bs.readShort();
            baos.write(bs.peekBytes(2));
            delay = bs.readShort();
            baos.write(bs.peekBytes(2));
            unknown = bs.readShort();
            baos.write(bs.peekBytes(2));
            nrPress = bs.readShort();
            baos.write(bs.peekBytes(4));
            followType = bs.readWord();
            baos.write(bs.peekBytes(2));
            input = bs.readShort();
            baos.write(bs.peekBytes(2));
            unknown1 = bs.readShort();
            baos.write(bs.peekBytes(2));
            followUpAllowed = bs.readShort();
            baos.write(bs.peekBytes(2));
            to_atk = bs.readShort();
            System.out.println(String.format("%X",bs.peekWord()));
            if (bs.peekWord() == 0x280000) {
                baos.write(bs.readBytes(2));
            } /*else if (bs.peekWord() == 0x00) {
                baos.write(bs.readBytes(2));
            }*/ else if (bs.peekWord() == 0x28) {
                baos.write(bs.readBytes(4));
            }
            bytes = baos.toByteArray();
            //System.out.println(this);
        }

        @Override
        public String toString() {
            return String.format("%s", formatRawBytes(bytes));
        }
    }
}