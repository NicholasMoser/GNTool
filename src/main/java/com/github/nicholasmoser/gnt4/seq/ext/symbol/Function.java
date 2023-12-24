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
        return Symbol.getNameBytes(name).length + 0x10 + opcodesToBytes().length;
    }

    public byte[] opcodesToBytes() {
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

    @Override
    public byte[] bytes() {
        try {
            byte[] opcodeBytes = opcodesToBytes();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Symbol.getNameBytes(name));
            baos.write(ByteUtils.fromInt32(length()));
            baos.write(ByteUtils.fromInt32(opcodeBytes.length));
            baos.write(new byte[0x8]); // 8 null bytes
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
}
