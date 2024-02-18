package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Binary data, that is, bytes that can be referenced.
 */
public class Binary implements Symbol {
    public static final int TYPE = 1;
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
        int dataOffset = Symbol.getNameBytes(name).length;
        dataOffset = ByteUtils.align(dataOffset, 16);
        return dataOffset + 0x10;
    }

    @Override
    public int length() {
        int length = Symbol.getNameBytes(name).length;
        length = ByteUtils.align(length, 16);
        length += 0x10;
        length += bytes.length;
        return ByteUtils.align(length, 16);
    }

    @Override
    public byte[] bytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            ByteUtils.align(baos, 16);
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(bytes.length));
            baos.write(new byte[0x4]); // 4 null bytes of padding
            baos.write(bytes);
            ByteUtils.align(baos, 16);
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
