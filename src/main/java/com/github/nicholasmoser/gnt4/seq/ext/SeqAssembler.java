package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SeqAssembler {

    public static LinkedList<Opcode> assembleLines(String[] lines) {
        LinkedList<Opcode> opcodes = new LinkedList<>();
        int offset = 0;
        for (int i = 0; i < lines.length; i++) {
            String operation = lines[i];
            String[] opcode;
            String operands = "";
            try {
                opcode = operation.substring(0, operation.indexOf(" ")).split("_");
                operands = operation.substring(operation.indexOf(" ")).replace(" ", "");
            } catch (Exception e) {
                opcode = operation.split("_");
            }
            byte[] bytes = new byte[0];
            String[] op;
            switch (opcode[0].replace(" ","")) {
                case "":
                    continue;
                case "b":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01320000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "beqz":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01330000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bnez":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01340000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bgtz":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01350000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bgez":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01360000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bltz":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01370000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "blez":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01380000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bdnz":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x013B0000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bl":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x013C0000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "beqzal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x013D0000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bnezal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x013E0000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bgtzal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x013F0000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bgezal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01400000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "bltzal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01410000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "blezal":
                    bytes = Bytes.concat(ByteUtils.fromInt32(0x01420000), ByteUtils.fromInt32(Integer.decode(operands)));
                    break;
                case "blr":
                    bytes = ByteUtils.fromInt32(0x01450000);
                    break;
                case "blreqz":
                    bytes = ByteUtils.fromInt32(0x01460000);
                    break;
                case "blrnez":
                    bytes = ByteUtils.fromInt32(0x01470000);
                    break;
                case "blrgtz":
                    bytes = ByteUtils.fromInt32(0x01480000);
                    break;
                case "blrgez":
                    bytes = ByteUtils.fromInt32(0x01490000);
                    break;
                case "blrltz":
                    bytes = ByteUtils.fromInt32(0x014A0000);
                    break;
                case "blrlez":
                    bytes = ByteUtils.fromInt32(0x014B0000);
                    break;
                case "i32":
                    /*
                    if (operands.contains("->")) {
                        continue;
                    }
                     */
                    bytes = getInt((byte)0x04, opcode[1], operands.split(","));
                    break;
                case "sync":
                    switch (opcode[1]) {
                        case "timer":
                            if (opcode.length == 2) {
                                bytes = Bytes.concat(ByteUtils.fromInt32(0x2011263F), ByteUtils.fromInt32(Integer.decode(operands)));
                            } else {
                                switch (opcode[2]) {
                                    case "run":
                                        bytes = ByteUtils.fromUint32(0x20120026);
                                        break;
                                }
                            }
                            break;
                    }
                    break;
                case "create":
                    switch (opcode[1]) {
                        case "hitbox":
                            op = operands.split(",");
                            bytes = Bytes.concat(ByteUtils.fromUint32(0x21040026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint32(0));
                            break;
                    }
                    break;
                case "set":
                    switch (opcode[1]) {
                        case "pow":
                            op = operands.split(",");
                            bytes = Bytes.concat(ByteUtils.fromUint32(0x21050026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint16(Integer.decode(op[2])), ByteUtils.fromUint16(0));
                            break;
                        case "ang":
                            op = operands.split(",");
                            bytes = Bytes.concat(ByteUtils.fromUint32(0x21060026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])));
                            break;
                        case "hitbox":
                            switch (opcode[2]) {
                                case "timer":
                                    op = operands.split(",");
                                    bytes = Bytes.concat(ByteUtils.fromUint32(0x21070026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])));
                                    break;
                            }
                            break;
                    }
                    break;
                case "flag":
                    bytes = ByteUtils.fromUint16(0x241A);
                    continue;
                case "op":
                    bytes = UnknownOpcode.of(opcode[1], operands);
                    break;
                default:
                    System.err.println(opcode[0]);
            }
            opcodes.add(new UnknownOpcode(offset, bytes));
        }
        return opcodes;
    }

    static private byte[] getInt(byte group, String opcode, String[] operands) {
        ByteBuffer buffer = ByteBuffer.allocate(0x24);
        buffer.put(group);
        switch (opcode) {
            case "mov":
                buffer.put((byte) 0x02);
                break;
        }
        buffer.put(SEQ_REGCMD2(operands[0].replace(" ", ""), operands[1].replace(" ", "")));
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }

    private static final Map<String,Byte> registers = Map.ofEntries(new SimpleEntry<>("gpr0", (byte)0x0),
            new SimpleEntry<>("gpr1", (byte)0x1),
            new SimpleEntry<>("gpr2", (byte)0x2),
            new SimpleEntry<>("gpr3", (byte)0x3),
            new SimpleEntry<>("gpr4", (byte)0x4),
            new SimpleEntry<>("gpr5", (byte)0x5),
            new SimpleEntry<>("gpr6", (byte)0x6),
            new SimpleEntry<>("gpr7", (byte)0x7),
            new SimpleEntry<>("gpr8", (byte)0x8),
            new SimpleEntry<>("gpr9", (byte)0x9),
            new SimpleEntry<>("gpr10", (byte)0xA),
            new SimpleEntry<>("gpr11", (byte)0xB),
            new SimpleEntry<>("gpr12", (byte)0xC),
            new SimpleEntry<>("gpr13", (byte)0xD),
            new SimpleEntry<>("gpr14", (byte)0xE),
            new SimpleEntry<>("gpr15", (byte)0xF),
            new SimpleEntry<>("gpr16", (byte)0x10),
            new SimpleEntry<>("gpr17", (byte)0x11),
            new SimpleEntry<>("gpr18", (byte)0x12),
            new SimpleEntry<>("gpr19", (byte)0x13),
            new SimpleEntry<>("gpr20", (byte)0x14),
            new SimpleEntry<>("gpr21", (byte)0x15),
            new SimpleEntry<>("gpr22", (byte)0x16),
            new SimpleEntry<>("gpr23", (byte)0x17),
            new SimpleEntry<>("mot", (byte)0x20),
            new SimpleEntry<>("chr_p", (byte)0x26),
            new SimpleEntry<>("foe_chr_p", (byte)0x27),
            new SimpleEntry<>("hitbox_identity_matrix", (byte)0x30));

    static private byte[] SEQ_REGCMD1(String op) {
        if (op.startsWith("*")) {
            op = op.substring(1);
        }
        ByteBuffer buffer = ByteBuffer.allocate(0x10);
        String[] opParts = op.split("->");
        String register = opParts[0].toLowerCase();
        Byte registerVal = registers.get(register);
        Long opDirect = (long) 0;
        buffer.put((byte) 0);
        if (registerVal == null) {
            opDirect = Long.decode(op);
            registerVal = (byte)0x3f;
        } else if(opParts.length > 1) {
            String offset = opParts[1];
            //String indirectOffset;
            registerVal = (byte) (registerVal | 0x10 << opParts.length);
            offset = opParts[1];
            if (offset.startsWith("field_")) {
                opDirect = Long.decode(offset.replace("field_", ""));
            }
        }

        buffer.put(registerVal);
        if (registerVal >= 0x3E) {
            buffer.putInt(Integer.reverseBytes(opDirect.intValue()));
        }

        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }

    static private byte[] SEQ_REGCMD2(String op1, String op2) {
        if (op1.startsWith("*")) {
            op1 = op1.substring(1);
        }
        String[] op1Parts = op1.split("->");
        String register1 = op1Parts[0].toLowerCase();
        Byte op1v = registers.get(register1);
        Long op1Direct = (long) 0;
        if (op1v == null) {
            op1Direct = Long.decode(op1Parts[0]);
            op1v = 0x3f;
        } else if(op1Parts.length > 1) {
            String offset1 = op1Parts[1];
            //String indirectOffset;
            op1v = (byte) (op1v | 0x10 << op1Parts.length);
            offset1 = op1Parts[1];
            if (offset1.startsWith("field_")) {
                op1Direct = Long.decode(offset1.replace("field_", ""));
            }
        }

        if (op2.startsWith("*")) {
            op2 = op2.substring(1);
        }
        String[] op2Parts = op2.split("->");
        String register2 = op2Parts[0].toLowerCase();
        Byte op2v = registers.get(register2);
        Long op2Direct = (long) 0;
        if (op2v == null) {
            op2Direct = Long.decode(op2Parts[0]);
            op2v = 0x3f;
        } else if(op1Parts.length > 1) {
            String offset2 = op2Parts[1];
            //String indirectOffset;
            op2v = (byte) (op2v | 0x10 << op2Parts.length);
            offset2 = op1Parts[1];
            if (offset2.startsWith("field_")) {
                op1Direct = Long.decode(offset2.replace("field_", ""));
            }
        }

        ByteBuffer buffer = ByteBuffer.allocate(0x30);
        buffer.put(op1v);
        if (op1v >= 0x3E && op1v < 0x80) {
            buffer.put((byte) 0);
            buffer.putInt(op1Direct.intValue());
            buffer.put(op2v);
            buffer.put((byte)0);
            buffer.put((byte)0);
            buffer.put((byte)0);
            if (op2v >= 0x3E) {
                buffer.putInt(op2Direct.intValue());
            }
        } else if (op1v < 0x3E) {
            buffer.put(op2v);
            if (op2v >= 0x3E && op2v < 0x80) {
                buffer.putInt(op2Direct.intValue());
            }
        }
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }
}
