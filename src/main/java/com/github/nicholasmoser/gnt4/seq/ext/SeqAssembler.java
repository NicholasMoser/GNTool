package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class SeqAssembler {

    public static LinkedList<Opcode> assembleLines(String[] lines) {
        LinkedList<Opcode> opcodes = new LinkedList<>();
        int offset = 0;
        for (int i = 0; i < lines.length; i++) {
            String operation = lines[i];
            String[] opcode;
            String operands = "";
            try {
                opcode = operation.substring(0, operation.indexOf(" ")).replace("*","").split("_");
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
                    if (operands.contains("->")) {
                        continue;
                    }
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


    static private byte[] SEQ_REGCMD1(String op) {
        ByteBuffer buffer = ByteBuffer.allocate(0xA);
        byte opv;
        int opdirect = 0;
        buffer.put((byte) 0);
        if (op.startsWith("gpr")) {
            opv = Byte.decode(op.replace("gpr",""));
        } else if (op.startsWith("seqr")) {
            opv = Byte.decode(op.replace("seqr",""));
        } else {
            try {
                opdirect = Integer.decode(op);
                opv = 0x3f;
            } catch (Exception e) {
                if (op.equals("chr_p")) {
                    opv = 0x26;
                } else {
                    opv = -1;
                }
            }
        }

        buffer.put(opv);
        if (opv >= 0x3E) {
            buffer.putInt(Integer.reverseBytes(opdirect));
        }

        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }

    static private byte[] SEQ_REGCMD2(String op1, String op2) {
        byte op1v;
        int op1direct = 0;
        if (op1.startsWith("gpr")) {
            op1v = Byte.decode(op1.substring(3));
        } else if (op1.startsWith("seqr")) {
            op1v = (byte) (Byte.decode(op1.substring(4)) + 0x18);
        } else {
            try {
                op1direct = Integer.decode(op1);
                op1v = 0x3f;
            } catch (Exception e) {
                if (op1.equals("chr_p")) {
                    op1v = 0x26;
                } else {
                    throw e;
                }
            }
        }
        byte op2v;
        int op2direct = 0;
        if (op2.startsWith("gpr")) {
            op2v = Byte.decode(op2.replace("gpr",""));
        } else if (op2.contains("seqr")) {
            op2v = (byte) (Byte.decode(op2.replace("seqr", "")) + 0x18);
        } else {
            try {
                op2direct = Integer.decode(op2);
                op2v = 0x3f;
            } catch (Exception e) {
                if (op2.equals("chr_p")) {
                    op2v = 0x26;
                } else {
                    throw e;
                }
            }
        }

        ByteBuffer buffer = ByteBuffer.allocate(0x22);
        buffer.put(op1v);
        if (op1v >= 0x3E && op1v < 0x80) {
            buffer.put((byte) 0);
            buffer.putInt(op1direct);
            buffer.put(op2v);
            buffer.put((byte)0);
            buffer.put((byte)0);
            buffer.put((byte)0);
            if (op2v >= 0x3E) {
                buffer.putInt(op2direct);
            }
        } else if (op1v < 0x3E) {
            buffer.put(op2v);
            if (op2v >= 0x3E && op2v < 0x80) {
                buffer.putInt(op2direct);
            }
        }
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }
}
