package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExistingBinary implements Symbol {
    private final String name;
    private final int binarySeqOffset;
    private final byte[] binaryBytes;

    public ExistingBinary(String name, int binarySeqOffset, byte[] binaryBytes) {
        this.name = name;
        this.binarySeqOffset = binarySeqOffset;
        this.binaryBytes = binaryBytes;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int offset() {
        return binarySeqOffset;
    }

    @Override
    public int length() {
        return Symbol.getNameBytes(name).length + 0x10;
    }

    @Override
    public byte[] bytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(binarySeqOffset));
            baos.write(ByteUtils.fromInt32(binaryBytes.length));
            baos.write(new byte[0x4]); // 4 null bytes
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
