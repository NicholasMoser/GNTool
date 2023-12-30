package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExistingFunction implements Symbol {
    private static final int TYPE = 3;
    private final String name;
    private final int functionOffset;
    private final int functionLength;

    public ExistingFunction(String name, int functionOffset, int functionLength) {
        this.name = name;
        this.functionOffset = functionOffset;
        this.functionLength = functionLength;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int dataOffset() {
        return functionOffset;
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
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(functionOffset));
            baos.write(ByteUtils.fromInt32(functionLength));
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int functionLength() {
        return functionLength;
    }
}
