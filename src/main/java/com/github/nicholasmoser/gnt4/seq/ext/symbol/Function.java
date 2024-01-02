package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Function implements Symbol {
    public static final int TYPE = 4;
    private static final Logger LOGGER = Logger.getLogger(Function.class.getName());
    private final String name;
    private final List<Opcode> opcodes;
    private final Map<String, Integer> innerLabels;

    public Function(String name, List<Opcode> opcodes, Map<String, Integer> innerLabels) {
        this.name = name;
        this.opcodes = opcodes;
        this.innerLabels = innerLabels;
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
        int codeLength = Symbol.getNameBytes(name).length;
        codeLength = ByteUtils.align(codeLength, 16);
        codeLength += 0x10;
        codeLength += opcodesByteLength();
        codeLength = ByteUtils.align(codeLength, 16);
        for (String name : innerLabels.keySet()) {
            codeLength += 0x4;
            codeLength += Symbol.getNameBytes(name).length;
            codeLength = ByteUtils.align(codeLength, 16);
        }
        return codeLength;
    }

    @Override
    public byte[] bytes() {
        try {
            byte[] opcodeBytes = opcodesToBytes(opcodes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            ByteUtils.align(baos, 16);
            baos.write(ByteUtils.fromInt32(TYPE));
            baos.write(ByteUtils.fromInt32(length(opcodeBytes)));
            baos.write(ByteUtils.fromInt32(opcodeBytes.length));
            baos.write(innerLabels.size());
            baos.write(opcodeBytes);
            ByteUtils.align(baos, 16);
            for (Entry<String, Integer> entry : innerLabels.entrySet()) {
                String labelName = entry.getKey();
                int labelOffset = entry.getValue();
                baos.write(ByteUtils.fromInt32(labelOffset));
                baos.write(Symbol.getNameBytes(labelName));
                ByteUtils.align(baos, 16);
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
        return ByteUtils.align(codeLength, 16);
    }

    public int opcodesByteLength() {
        return opcodesToBytes(opcodes).length;
    }

    public Map<String, Integer> innerLabels() {
        return innerLabels;
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
