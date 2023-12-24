package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExistingFunction implements Symbol {
    private final String name;
    private final int functionSeqOffset;
    private final byte[] functionBytes;

    public ExistingFunction(String name, int functionSeqOffset, byte[] functionBytes) {
        this.name = name;
        this.functionSeqOffset = functionSeqOffset;
        this.functionBytes = functionBytes;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int dataOffset() {
        return functionSeqOffset;
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
            baos.write(ByteUtils.fromInt32(functionSeqOffset));
            baos.write(ByteUtils.fromInt32(functionBytes.length));
            baos.write(new byte[0x4]); // 4 null bytes
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
