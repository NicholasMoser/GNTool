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

public class ActiveAttacks implements Opcode{

    private final static String MNEMONIC = "Active Attacks";
    private final int offset;
    private final byte[] bytes;
    private List<ActiveAttack> attacks;
    private List<ExtraData> extraData;

    public ActiveAttacks(ByteStream bs) throws IOException {
        this.offset = bs.offset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        attacks = new LinkedList<>();
        byte[] delimiter = new byte[]{0x00, 0x00, 0x00, 0x00};
        while (!Arrays.equals(bs.peekBytes(4), delimiter)) {
            baos.write(bs.peekBytes(20));
            attacks.add(new ActiveAttack(bs.offset(), bs.readBytes(20)));
        }
        attacks.add(new ActiveAttack(bs.offset(), bs.readBytes(20)));
        extraData = new LinkedList<>();
        delimiter = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        while (!Arrays.equals(bs.peekBytes(8),delimiter)) {
            ExtraData ed = new ExtraData(bs.offset(), bs);
            extraData.add(ed);
            baos.write(ed.getBytes());
        }
        while (bs.peekWord() == 0) {
            ExtraData ed = new ExtraData(bs.offset(), bs);
            extraData.add(ed);
            baos.write(ed.getBytes());
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
        for (ActiveAttack atk : attacks) {
            sb.append(String.format("%05X | Active attack, condition: 0x%X, ATK ID: %X Extra Data Offset 0x%08X, %s\n", atk.getOffset(), atk.getCondition(), atk.getAtk_id(), atk.getExtra_data_offset(), formatRawBytes(atk.getBytes())));
        }
        for (ExtraData ed : extraData) {
            sb.append(String.format("%05X | Extra Data, %s\n", ed.getOffset(), formatRawBytes(ed.getBytes())));
        }
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
        for (ActiveAttack atk : attacks) {
            sb.append(String.format("%05X | Active attack, condition: 0x%08X, ATK ID: %X Extra Data Offset 0x%08X <br />", atk.getOffset(), atk.getCondition(), atk.getAtk_id(), atk.getExtra_data_offset()));
        }
        for (ExtraData ed : extraData) {
            sb.append(String.format("%05X | Extra Data: %s<br />", ed.getOffset(), formatRawBytes(ed.getBytes())));
        }
        //String.format("%05X | %s", offset, MNEMONIC)
        return div(attrs(id))
                .withText(sb.toString())
                .withText(" ")
                .with(formatRawBytesHTML(bytes));
    }


}
