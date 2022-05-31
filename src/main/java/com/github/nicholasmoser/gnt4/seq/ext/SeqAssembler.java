package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;

import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class SeqAssembler {

    public static LinkedList<Opcode> assembleLines(String[] lines) {
        LinkedList<Opcode> opcodes = new LinkedList<>();
        int offset = 0;
        ByteBuffer buffer = ByteBuffer.allocate(0x50);
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
            String[] op;
            switch (opcode[0].replace(" ","")) {
                case "":
                    continue;
                case "end":
                    buffer.putInt(0);
                    break;
                case "hard":
                    switch (opcode[1]) {
                        case "reset":
                            buffer.putInt(1);
                            break;
                    }
                    break;
                case "b":
                    buffer.putInt(0x01320000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "beqz":
                    buffer.putInt(0x01330000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bnez":
                    buffer.putInt(0x01340000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bgtz":
                    buffer.putInt(0x01350000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bgez":
                    buffer.putInt(0x01360000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bltz":
                    buffer.putInt(0x01370000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "blez":
                    buffer.putInt(0x01380000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bdnz":
                    buffer.putInt(0x013B0000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bl":
                    buffer.putInt(0x013C0000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "beqzal":
                    buffer.putInt(0x013D0000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bnezal":
                    buffer.putInt(0x013E0000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bgtzal":
                    buffer.putInt(0x013F0000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bgezal":
                    buffer.putInt(0x01400000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "bltzal":
                    buffer.putInt(0x01410000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "blezal":
                    buffer.putInt(0x01420000);
                    buffer.putInt(Integer.decode(operands));
                    break;
                case "blr":
                    buffer.putInt(0x01450000);
                    break;
                case "blreqz":
                    buffer.putInt(0x01460000);
                    break;
                case "blrnez":
                    buffer.putInt(0x01470000);
                    break;
                case "blrgtz":
                    buffer.putInt(0x01480000);
                    break;
                case "blrgez":
                    buffer.putInt(0x01490000);
                    break;
                case "blrltz":
                    buffer.putInt(0x014A0000);
                    break;
                case "blrlez":
                    buffer.putInt(0x014B0000);
                    break;
                case "SEQ":
                    switch (opcode[1]) {
                        case "ReqSetPrev":
                            //TODO
                            break;
                        case "ReqLoadPrev":
                            if (opcode.length > 2) {
                                switch (opcode[2]) {
                                    case "I":
                                        //TODO
                                        break;
                                }
                            } else {
                                //TODO
                                //ReqLoadPrev
                            }
                            break;
                    }
                case "TskSendMsg":
                    //TODO
                    break;
                case "TskExecFunc":
                    //TODO
                    break;
                case "movr":
                    buffer.putShort((short) 0x0301);
                    buffer.put(SEQ_RegCMD2(operands.split(",")[0],operands.split(",")[1]));
                    break;
                case "push":
                    buffer.putShort((short) 0x0303);
                    buffer.put(SEQ_RegCMD1(operands));
                    break;
                case "pop":
                    buffer.putShort((short) 0x0304);
                    buffer.put(SEQ_RegCMD1(operands));
                    break;
                case "i32":
                    buffer.put(getInt((byte)0x04, opcode[1], operands.split(",")));
                    break;
                case "i8":
                    buffer.put(getInt((byte)0x05, opcode[1], operands.split(",")));
                    break;
                case "i16":
                    buffer.put(getInt((byte)0x06, opcode[1], operands.split(",")));
                    break;
                case "i64":
                    buffer.put(getInt((byte)0x07, opcode[1], operands.split(",")));
                    break;
                case "f32":
                    switch (opcode[1]) {
                        case "move":
                            buffer.putShort((short) 0x0802);
                            break;
                        case "sub":
                            buffer.putShort((short) 0x0804);
                            break;
                        case "mul":
                            buffer.putShort((short) 0x0805);
                            break;
                        case "div":
                            buffer.putShort((short) 0x0806);
                            break;
                        case "cmp":
                            buffer.putShort((short) 0x0807);
                            break;
                    }
                    buffer.put(SEQ_RegCMD2(operands.split(",")[0],operands.split(",")[1]));
                    break;
                case "ptr":
                    String op1 = operands.split(",")[0];
                    String op2 = operands.split(",")[1];
                    switch (opcode[1]){
                        case "debug":
                            buffer.putShort((short) 0x0900);
                            buffer.put(SEQ_RegCMD1(operands));
                            break;
                        case "mov":
                            buffer.putShort((short) 0x0901);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "add":
                            buffer.putShort((short) 0x0904);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "move":
                            buffer.putShort((short) 0x0907);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "from":
                            buffer.putShort((short) 0x0908);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "to":
                            buffer.putShort((short) 0x0909);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "push":
                            buffer.putShort((short) 0x090A);
                            buffer.put(SEQ_RegCMD1(operands));
                            break;
                        case "table":
                            buffer.putShort((short) 0x090C);
                            buffer.put(SEQ_RegCMD2(op1,op2));
                            break;
                        case "b":
                            buffer.putShort((short) 0x0914);
                            buffer.put(SEQ_RegCMD1(operands));
                            break;
                        case "bl":
                            buffer.putShort((short) 0x091D);
                            buffer.put(SEQ_RegCMD1(operands));
                            break;
                    }
                    break;
                case "sync":
                    switch (opcode[1]) {
                        case "timer":
                            if (opcode.length == 2) {
                                buffer.put(Bytes.concat(ByteUtils.fromInt32(0x2011263F), ByteUtils.fromInt32(Integer.decode(operands))));
                            } else {
                                switch (opcode[2]) {
                                    case "run":
                                        buffer.put(ByteUtils.fromUint32(0x20120026));
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
                            buffer.put(Bytes.concat(ByteUtils.fromUint32(0x21040026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint32(0)));
                            break;
                    }
                    break;
                case "set":
                    switch (opcode[1]) {
                        case "pow":
                            op = operands.split(",");
                            buffer.put(Bytes.concat(ByteUtils.fromUint32(0x21050026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint16(Integer.decode(op[2])), ByteUtils.fromUint16(0)));
                            break;
                        case "ang":
                            op = operands.split(",");
                            buffer.put(Bytes.concat(ByteUtils.fromUint32(0x21060026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1]))));
                            break;
                        case "hitbox":
                            switch (opcode[2]) {
                                case "timer":
                                    op = operands.split(",");
                                    buffer.put(Bytes.concat(ByteUtils.fromUint32(0x21070026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1]))));
                                    break;
                            }
                            break;
                    }
                    break;
                case "flags":
                    buffer.putShort((short)0x241A);
                    byte action = 0;
                    byte group = 0;
                    switch (opcode[1]) {
                        case "set":
                            break;
                        case "remove":
                            action = 1;
                            break;
                        case "and":
                            action = 2;
                            break;
                        case "add":
                            action = 3;
                            break;
                        case "xor":
                            action = 4;
                            break;
                        case "get":
                            action = 5;
                            break;
                    }
                    switch (opcode[2]) {
                        case "af":
                            break;
                        case "nf":
                            group = 0x06;
                            break;
                        case "pf":
                            group = 0x0C;
                            break;
                        case "kf":
                            group = 0x12;
                            break;
                        case "df":
                            group = 0x18;
                            break;
                        case "ef":
                            group = 0x1E;
                            break;
                        case "mf":
                            group = 0x24;
                            break;
                        case "rf":
                            group = 0x2A;
                            break;
                        case "sf":
                            group = 0x30;
                            break;
                        case "unknown":
                            group = 0x36;
                            break;
                        case "cf":
                            group = 0x3C;
                            break;
                        case "chr":
                        case "cmf":
                            group = 0x42;
                            break;
                        case "k2f":
                            group = 0x48;
                            break;
                        case "d2f":
                            group = 0x4E;
                            break;
                        case "n2f":
                            group = 0x54;
                            break;
                    }
                    action += group;
                    buffer.put(action);
                    //No alternative actions supported, only pure flag operations
                    buffer.put((byte) 0);
                    buffer.putInt(getFlags(opcode[2],operands.replace("\"","")));
                    break;
                case "op":
                    switch (opcode[1]) {
                        case "0208":
                            //TODO
                            break;
                        case "0926":
                            //TODO
                            break;
                        default:
                            buffer.put(UnknownOpcode.of(opcode[1], operands));
                            break;
                    }
                    break;
                default:
                    System.err.println(opcode[0]);
            }
            byte[] bytes = new byte[buffer.position()];
            buffer.position(0);
            buffer.get(bytes);
            opcodes.add(new UnknownOpcode(offset, bytes));
            offset += bytes.length;
            buffer.position(0);
        }
        return opcodes;
    }

    static private int getFlags(String group, String operands) {
        int flags = 0;
        Map<Integer, String> flagValues;
        switch (group) {
            case "af":
                flagValues = Seq.AF_FLAGS;
                break;
            case "nf":
                flagValues = Seq.NF_FLAGS;
                break;
            case "pf":
                flagValues = Seq.PF_FLAGS;
                break;
            case "kf":
                flagValues = Seq.KF_FLAGS;
                break;
            case "df":
                flagValues = Seq.KF_FLAGS;
                break;
            case "ef":
                flagValues = Seq.EF_FLAGS;
                break;
            case "mf":
                flagValues = Seq.MF_FLAGS;
                break;
            case "rf":
                flagValues = Seq.RF_FLAGS;
                break;
            case "sf":
                flagValues = Seq.SF_FLAGS;
                break;
            case "cf":
                flagValues = Seq.CF_FLAGS;
                break;
            case "chr":
            case "cmf":
                flagValues = Seq.CHR_MOD_FLAGS;
                break;
            case "k2f":
                flagValues = Seq.K2F_FLAGS;
                break;
            case "d2f":
                flagValues = Seq.D2F_FLAGS;
                break;
            case "n2f":
                flagValues = Seq.N2F_FLAGS;
                break;
            case "unknown":
            default:
                throw new IllegalStateException("Unexpected value: " + group);
        }
        for (Entry<Integer, String> entry : flagValues.entrySet()) {
            if (operands.contains(entry.getValue())) {
                flags |= (entry.getKey());
            }
        }
        return flags;
    }

    static private byte[] getInt(byte group, String opcode, String[] operands) {
        ByteBuffer buffer = ByteBuffer.allocate(0x24);
        buffer.put(group);
        switch (opcode) {
            case "debug":
                buffer.put((byte) 0);
                buffer.putShort((short) 0);
                break;
            case "float":
                buffer.put((byte) 0x01);
                break;
            case "mov":
                buffer.put((byte) 0x02);
                break;
            case "andc":
                buffer.put((byte) 0x03);
                break;
            case "nimply":
                buffer.put((byte) 0x04);
                break;
            case "inc":
                buffer.put((byte) 0x05);
                break;
            case "dec":
                buffer.put((byte) 0x06);
                break;
            case "add":
                buffer.put((byte) 0x07);
                break;
            case "sub":
                buffer.put((byte) 0x08);
                break;
            case "mul":
                buffer.put((byte) 0x09);
                break;
            case "div":
                buffer.put((byte) 0x0A);
                break;
            case "shl":
                buffer.put((byte) 0x0B);
                break;
            case "shr":
                buffer.put((byte) 0x0C);
                break;
            case "and":
                buffer.put((byte) 0x0D);
                break;
            case "or":
                buffer.put((byte) 0x0E);
                break;
            case "xor":
                buffer.put((byte) 0x0F);
                break;
            case "not":
                buffer.put((byte) 0x10);
                break;
            case "subc":
                buffer.put((byte) 0x11);
                break;
            case "chs":
                buffer.put((byte) 0x12);
                break;
            case "cuhw":
                buffer.put((byte) 0x13);
                break;
            case "range":
                buffer.put((byte) 0x14);
                break;
            case "rand":
                buffer.put((byte) 0x15);
                break;
            case "andcz":
                buffer.put((byte) 0x16);
                break;
            case "mod":
                buffer.put((byte) 0x17);
                break;
            case "abs":
                buffer.put((byte) 0x18);
                break;
        }
        switch (opcode) {
            case "inc":
            case "dec":
            case "abs":
                buffer.put(SEQ_RegCMD1(operands[0].replace(" ", "")));
                break;
            case "float":
            case "mov":
            case "andc":
            case "nimply":
            case "add":
            case "sub":
            case "mul":
            case "div":
            case "shl":
            case "shr":
            case "and":
            case "or":
            case "xor":
            case "not":
            case "subc":
            case "chs":
            case "cuhw":
            case "range":
            case "rand":
            case "andcz":
            case "mod":
                buffer.put(SEQ_RegCMD2(operands[0].replace(" ", ""), operands[1].replace(" ", "")));
                break;
        }
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
            new SimpleEntry<>("cr_companion", (byte)0x13),
            new SimpleEntry<>("gpr20", (byte)0x14),
            new SimpleEntry<>("ctr", (byte)0x14),
            new SimpleEntry<>("gpr21", (byte)0x15),
            new SimpleEntry<>("cr", (byte)0x15),
            new SimpleEntry<>("gpr22", (byte)0x16),
            new SimpleEntry<>("stored_pc", (byte)0x16),
            new SimpleEntry<>("gpr23", (byte)0x17),
            new SimpleEntry<>("sp", (byte)0x17),
            new SimpleEntry<>("seq_p_sp0", (byte)0x18),
            new SimpleEntry<>("seq_p_sp1", (byte)0x19),
            new SimpleEntry<>("seq_p_sp2", (byte)0x1A),
            new SimpleEntry<>("seq_p_sp3", (byte)0x1B),
            new SimpleEntry<>("seq_p_sp4", (byte)0x1C),
            new SimpleEntry<>("seq_p_sp5", (byte)0x1D),
            new SimpleEntry<>("final_instruction_seq", (byte)0x1D),
            new SimpleEntry<>("seq_p_sp6", (byte)0x1E),
            new SimpleEntry<>("seq_p_sp7", (byte)0x1F),
            new SimpleEntry<>("seq_p_sp8", (byte)0x20),
            new SimpleEntry<>("mot", (byte)0x20),
            new SimpleEntry<>("animation", (byte)0x20),
            new SimpleEntry<>("seq_p_sp9", (byte)0x21),
            new SimpleEntry<>("movement", (byte)0x21),
            new SimpleEntry<>("seq_p_sp10", (byte)0x22),
            new SimpleEntry<>("chr_p_field_0x4C", (byte)0x22),
            new SimpleEntry<>("seq_p_sp11", (byte)0x23),
            new SimpleEntry<>("seq_p_sp12", (byte)0x24),
            new SimpleEntry<>("seq_p_sp13", (byte)0x25),
            new SimpleEntry<>("seq_p_sp14", (byte)0x26),
            new SimpleEntry<>("chr_p", (byte)0x26),
            new SimpleEntry<>("seq_p_sp15", (byte)0x27),
            new SimpleEntry<>("foe_chr_p", (byte)0x27),
            new SimpleEntry<>("seq_p_sp16", (byte)0x28),
            new SimpleEntry<>("seq_p_sp17", (byte)0x29),
            new SimpleEntry<>("seq_p_sp18", (byte)0x2A),
            new SimpleEntry<>("seq_p_sp19", (byte)0x2B),
            new SimpleEntry<>("seq_p_sp", (byte)0x2B),
            new SimpleEntry<>("seq_p_sp20", (byte)0x2C),
            new SimpleEntry<>("seq_p_sp21", (byte)0x2D),
            new SimpleEntry<>("seq_p_sp22", (byte)0x2E),
            new SimpleEntry<>("seq_p_sp23", (byte)0x2F),
            new SimpleEntry<>("seq_file", (byte)0x2F),
            new SimpleEntry<>("hitbox_identity_matrix", (byte)0x30),
            new SimpleEntry<>("controllers", (byte)0x32),
            new SimpleEntry<>("primary_controller", (byte)0x33),
            new SimpleEntry<>("display", (byte)0x34),
            new SimpleEntry<>("save_data", (byte)0x39),
            new SimpleEntry<>("debug_mode", (byte)0x3A),
            new SimpleEntry<>("pause_game", (byte)0x3B),
            new SimpleEntry<>("game_info", (byte)0x3C),
            new SimpleEntry<>("unused", (byte)0x3D));

    static private byte[] SEQ_RegCMD1(String op) {
        if (op.startsWith("*")) {
            op = op.substring(1);
        }
        ByteBuffer buffer = ByteBuffer.allocate(0x10);
        String[] opParts = op.split("->");
        String register = opParts[0].toLowerCase();
        Byte registerVal = registers.get(register);
        Integer opDirect = 0;
        buffer.put((byte) 0);
        if (registerVal == null) {
            opDirect = Long.decode(op).intValue();
            registerVal = (byte)0x3f;
        } else if(opParts.length > 1) {
            String offset = opParts[1];
            registerVal = (byte) (registerVal | 0x10 << opParts.length);
            if (offset.startsWith("field_")) {
                opDirect = Long.decode(offset.replace("field_", "")).intValue();
            } else {
                Optional<Integer> asd = null;
                switch (registerVal & 0x3f) {
                    case 0x26:
                    case 0x27:
                        asd = Chr.getOffset(offset);
                        break;
                }
                if (asd != null) {
                    opDirect = asd.get();
                } else {
                    opDirect = Integer.decode(offset);
                }
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

    static private byte[] SEQ_RegCMD2(String op1, String op2) {
        if (op1.startsWith("*")) {
            op1 = op1.substring(1);
        }
        String[] op1Parts = op1.split("->");
        String register1 = op1Parts[0].toLowerCase();
        Byte op1v = registers.get(register1);
        Integer op1Direct = 0;
        if (op1v == null) {
            op1v = 0x3f;
            op1Direct = Long.decode(op1Parts[0]).intValue();
        } else if(op1Parts.length > 1) {
            String offset1 = op1Parts[1];
            //String indirectOffset;
            op1v = (byte) (op1v | 0x10 << op1Parts.length);
            offset1 = op1Parts[1];
            if (offset1.startsWith("field_")) {
                op1Direct = Long.decode(offset1.replace("field_", "")).intValue();
            } else {
                Integer asd = null;
                switch (op1v & 0x3f) {
                    case 0x26:
                    case 0x27:
                        asd = Chr.getOffset(offset1).get();
                        break;
                }
                if (asd == null) {
                    try {
                        op1Direct = Integer.decode(offset1);
                    } catch (Exception e) {
                        System.err.println(op1);
                        System.err.println(String.format("0x%02X",op1v));
                        System.err.println(String.format("0x%02X",offset1));
                        System.err.println(String.format("0x%02X",op2));
                    }

                }
            }
        }

        if (op2.startsWith("*")) {
            op2 = op2.substring(1);
        }
        String[] op2Parts = op2.split("->");
        String register2 = op2Parts[0].toLowerCase();
        Byte op2v = registers.get(register2);
        Integer op2Direct = 0;
        if (op2v == null) {
            op2v = 0x3f;
            switch (op1v & 0x3f) {
                case 0x26:
                case 0x27:
                    op2 = op2.substring(op2.indexOf("0x"));
                    if (op2.contains(")")) {
                        op2 = op2.replace(")","");
                    }
                    break;
            }
            op2Direct = Long.decode(op2).intValue();
        } else if(op1Parts.length > 1) {
            String offset2 = op2Parts[1];
            //String indirectOffset;
            op2v = (byte) (op2v | 0x10 << op2Parts.length);
            offset2 = op1Parts[1];
            if (offset2.startsWith("field_")) {
                op2Direct = Long.decode(offset2.replace("field_", "")).intValue();
            } else {
                Optional<Integer> asd = null;
                switch (op2v & 0x3f) {
                    case 0x26:
                    case 0x27:
                        asd = Chr.getOffset(offset2);
                        break;
                }
                if (asd != null) {
                    op2Direct = asd.get();
                } else {
                    op2Direct = Integer.decode(offset2);
                }
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
