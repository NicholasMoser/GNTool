package com.github.nicholasmoser.gnt4.seq.opcodes;

import com.github.nicholasmoser.utils.ByteStream;
import j2html.tags.ContainerTag;

import java.io.IOException;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.div;

public class ActiveAttack implements Comparable<ActiveAttack>, Opcode {

    private static String MNEMONIC = "Attack";
    private final int offset;
    private final byte[] bytes;
    private final int condition;
    private final int atk_id;
    private final int unknown1;
    private final int unknown2;
    private final int unknown3;
    private final int extra_data_offset;

    public ActiveAttack(int offset, byte[] bytes) throws IOException {
        this.offset = offset;
        this.bytes = bytes;
        ByteStream bs = new ByteStream(bytes);
        condition = bs.readWord();
        atk_id = bs.readShort();
        unknown1 = bs.readShort();
        unknown2 = bs.readWord();
        unknown3 = bs.readWord();
        extra_data_offset = bs.readWord();
    }

    public int getAtk_id() {
        return atk_id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getCondition() {
        return condition;
    }

    public int getOffset() {
        return offset;
    }

    public int getExtra_data_offset() {
        return extra_data_offset;
    }

    public int getUnknown1() {
        return unknown1;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public int getUnknown3() {
        return unknown3;
    }

    @Override
    public String toString() {
        return String.format("%05X | %s: condition 0x%08X, ATK_ID 0x%X, extra data offset: 0x%08X  %s",
                offset, MNEMONIC, condition, atk_id, extra_data_offset, formatRawBytes(bytes));
    }

    @Override
    public int compareTo(ActiveAttack a) {
        if (this.extra_data_offset < a.getExtra_data_offset()) {
            return -1;
        } else if (this.extra_data_offset > a.getExtra_data_offset()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toAssembly() {
        return String.format("%s, %s", MNEMONIC, formatRawBytes(bytes));
    }

    @Override
    public ContainerTag toHTML() {
        String id = String.format("#%X", offset);
        String dest = String.format("#%X", extra_data_offset);
        return div(attrs(id))
                .withText(String.format("%05X | %s condition: 0x%08X, ATK ID: %X Extra Data Offset ", this.offset, MNEMONIC, this.condition, this.atk_id))
                .with(a(String.format("0x%X", extra_data_offset)).withHref(dest))
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }
}