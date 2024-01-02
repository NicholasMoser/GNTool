package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExistingBinary implements Symbol {
    public static final int TYPE = 2;
    private final String name;
    private final int binaryOffset;
    private final int binaryLength;

    public ExistingBinary(String name, int binaryOffset, int binaryLength) {
        this.name = name;
        this.binaryOffset = binaryOffset;
        this.binaryLength = binaryLength;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int dataOffset() {
        return binaryOffset;
    }

    @Override
    public int length() {
        int length = Symbol.getNameBytes(name).length;
        length = ByteUtils.align(length, 16);
        return length + 0x10;
    }

    @Override
    public byte[] bytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            ByteUtils.align(baos, 16);
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(binaryOffset));
            baos.write(ByteUtils.fromInt32(binaryLength));
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int binaryLength() {
        return binaryLength;
    }
}
