package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.tools.FPKRepackerTool;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Function implements Symbol {
    private static final Logger LOGGER = Logger.getLogger(Function.class.getName());
    private static final int TYPE = 4;
    private final String name;
    private final List<Opcode> opcodes;

    public Function(String name, List<Opcode> opcodes) {
        this.name = name;
        this.opcodes = opcodes;
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
        int codeLength = Symbol.getNameBytes(name).length + 0x10 + opcodesByteLength();
        int mod = codeLength % 16;
        if (mod != 0) {
            return codeLength + (16 - mod); // add padding
        }
        return codeLength;
    }

    @Override
    public byte[] bytes() {
        try {
            byte[] opcodeBytes = opcodesToBytes(opcodes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length(opcodeBytes)));
            baos.write(ByteUtils.fromInt32(opcodeBytes.length));
            baos.write(new byte[0x4]); // 4 null bytes
            baos.write(opcodeBytes);
            int mod = baos.size() % 16;
            if (mod != 0) {
                baos.write(new byte[16 - mod]);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate length with pre-determined opcode bytes, to save processing them an additional
     * time.
     *
     * @param opcodeBytes The opcode bytes.
     * @return The length of this symbol.
     */
    private int length(byte[] opcodeBytes) {
        int codeLength = Symbol.getNameBytes(name).length + 0x10 + opcodeBytes.length;
        int mod = codeLength % 16;
        if (mod != 0) {
            return codeLength + (16 - mod); // add padding
        }
        return codeLength;
    }

    public int opcodesByteLength() {
        return opcodesToBytes(opcodes).length;
    }

    /**
     * Convert the given opcodes to bytes.
     *
     * @param opcodes The opcodes to convert.
     * @return The bytes of the opcodes.
     */
    public byte[] opcodesToBytes(List<Opcode> opcodes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes) {
            try {
                baos.write(opcode.getBytes());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error getting opcode bytes", e);
            }
        }
        return baos.toByteArray();
    }
}
