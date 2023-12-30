package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Binary implements Symbol {
    private static final int TYPE = 1;
    private final String name;
    private final byte[] bytes;

    public Binary(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int dataOffset() {
        return Symbol.getNameBytes(name).length + 0x10;
    }

    @Override
    public int length() {
        int codeLength = Symbol.getNameBytes(name).length + 0x10 + bytes.length;
        int mod = codeLength % 16;
        if (mod != 0) {
            return codeLength + (16 - mod); // add padding
        }
        return codeLength;
    }

    @Override
    public byte[] bytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(bytes.length));
            baos.write(new byte[0x4]); // 4 null bytes
            baos.write(bytes);
            int mod = baos.size() % 16;
            if (mod != 0) {
                baos.write(new byte[16 - mod]);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int binaryLength() {
        return bytes.length;
    }

    public byte[] binaryBytes() {
        return bytes;
    }
}
