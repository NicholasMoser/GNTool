package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.*;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import javafx.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class SeqAssembler {

    public static Pair<List<Opcode>, Integer> assembleLines(String[] lines, Path SEQPath) throws IOException {
        LinkedList<Opcode> opcodes = new LinkedList<>();
        int offset = 0;
        Map<String, Integer> labelMap = new HashMap<>();
        Map<Integer, Function> globalFunctionMap = Functions.getFunctions(SEQPath.toString());
        Map<String, Integer> globalLabelMap = new HashMap<>();
        for (Entry<Integer, Function> me : globalFunctionMap.entrySet()) {
            globalLabelMap.put(me.getValue().name(), me.getKey());
        }
        for (int i = 0; i < lines.length; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String operation = lines[i];
            if (operation.endsWith(":")) {
                labelMap.put(operation.replace(":","").replace(" ",""),offset);
                continue;
            }
            String[] opcode;
            String operands = "";
            try {
                opcode = operation.substring(0, operation.indexOf(" ")).split("_");
                operands = operation.substring(operation.indexOf(" ")).replace(" ", "");
            } catch (Exception e) {
                opcode = operation.split("_");
            }
            String[] op = operands.split(",");
            String op1 = "";
            String op2 = "";
            try {
                op1 = op[0];
                op2 = op[1];
            } catch (Exception e) {

            }
            Opcode currentOpcode = new UnknownOpcode(offset, new byte[] {0,0,0,0});
            switch (opcode[0].replace(" ","")) {
                case "":
                    continue;
                case "end":
                    currentOpcode = new End(offset);
                    break;
                case "hard":
                    switch (opcode[1]) {
                        case "reset":
                            currentOpcode = new HardReset(offset);
                            break;
                    }
                    break;
                case "b":
                    try {
                        currentOpcode = new Branch(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new Branch(offset,operands);
                    }
                    break;
                case "beqz":
                    try {
                        currentOpcode = new BranchEqualToZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchEqualToZero(offset,operands);
                    }
                    break;
                case "bnez":
                    try {
                        currentOpcode = new BranchNotEqualToZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchNotEqualToZero(offset,operands);
                    }
                    break;
                case "bgtz":
                    try {
                        currentOpcode = new BranchGreaterThanZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchGreaterThanZero(offset,operands);
                    }
                    break;
                case "bgez":
                    try {
                        currentOpcode = new BranchGreaterThanOrEqualToZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchGreaterThanOrEqualToZero(offset,operands);
                    }
                    break;
                case "bltz":
                    try {
                        currentOpcode = new BranchLessThanZero(offset, Integer.decode(operands),(byte) 0x37);
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanZero(offset,operands);
                    }
                    break;
                case "blez":
                    try {
                        currentOpcode = new BranchLessThanOrEqualToZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanOrEqualToZero(offset,operands);
                    }
                    break;
                case "bdnz":
                    try {
                        currentOpcode = new BranchDecrementNotZero(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchDecrementNotZero(offset,operands);
                    }
                    break;
                case "bl":
                    try {
                        currentOpcode = new BranchLink(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLink(offset,operands);
                    }
                    break;
                case "beqzal":
                    try {
                        currentOpcode = new BranchEqualToZeroLink(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchEqualToZeroLink(offset,operands);
                    }
                    break;
                case "bnezal":
                    try {
                        currentOpcode = new BranchNotEqualZeroLink(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchNotEqualZeroLink(offset,operands);
                    }
                    break;
                case "bgtzal":
                    try {
                        currentOpcode = new BranchingOpcode("bgtzal", new byte[] {0x01, 0x3F, 0x00, 0x00}, offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchingOpcode("bgtzal", new byte[] {0x01, 0x3F, 0x00, 0x00}, offset, operands);
                    }
                    break;
                case "bgezal":
                    try {
                        currentOpcode = new BranchingOpcode("bgezal", new byte[] {0x01, 0x40, 0x00, 0x00}, offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchingOpcode("bgezal", new byte[] {0x01, 0x40, 0x00, 0x00}, offset, operands);
                    }
                    break;
                case "bltzal":
                    try {
                        currentOpcode = new BranchLessThanZeroLink(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanZeroLink(offset,operands);
                    }
                    break;
                case "blezal":
                    try {
                        currentOpcode = new BranchLessThanEqualZeroLink(offset,Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanEqualZeroLink(offset,operands);
                    }
                    break;
                case "blr":
                    currentOpcode = new BranchLinkReturn(offset);
                    break;
                case "blreqz":
                    currentOpcode = new BranchLinkReturnEqualZero(offset);
                    break;
                case "blrnez":
                    currentOpcode = new BranchLinkReturnNotEqualZero(offset);
                    break;
                case "blrgtz":
                    currentOpcode = new BranchLinkReturnGreaterThanZero(offset);
                    break;
                case "blrgez":
                    currentOpcode = new BranchLinkReturnGreaterThanEqualZero(offset);
                    break;
                case "blrltz":
                    currentOpcode = new BranchLinkReturnLessThanZero(offset);
                    break;
                case "blrlez":
                    currentOpcode = new BranchLinkReturnLessThanEqualZero(offset);
                    break;
                case "branch":
                    switch (opcode[1]) {
                        case "table":
                            if (opcode.length == 2) {
                                baos.write(new byte[] {0x01, 0x50});
                                baos.write(SEQ_RegCMD1(op[0]));
                                baos.write(ByteUtils.fromInt32(op.length - 1));
                                List<Integer> offsets = new LinkedList<>();
                                List<String> branches = new LinkedList<>();
                                for (int j = 1; j < op.length; j++) {
                                    try {
                                        int off = Integer.decode(op[j]);
                                        offsets.add(off);
                                        baos.write(ByteUtils.fromInt32(off));
                                    } catch (Exception e) {
                                        baos.write(ByteUtils.fromInt32(0));
                                        branches.add(op[j]);
                                    }
                                }
                                if (offsets.size() > 0) {
                                    currentOpcode = new BranchTable(offset, baos.toByteArray(), op[0], offsets);
                                } else if (branches.size() > 0) {
                                    currentOpcode = new BranchTable(offset, baos.toByteArray(), op[0], offsets, branches);
                                } else {
                                    System.err.println(String.format("Neither offsets nor label names given for branch table at offset %d", offset));
                                    continue;
                                }
                            } else {
                                baos.write(new byte[] {0x01, 0x51});
                                baos.write(SEQ_RegCMD1(op[0]));
                                baos.write(ByteUtils.fromInt32(op.length - 1));
                                List<Integer> offsets = new LinkedList<>();
                                List<String> branches = new LinkedList<>();
                                for (int j = 1; j < op.length; j++) {
                                    try {
                                        int off = Integer.decode(op[j]);
                                        offsets.add(off);
                                        baos.write(ByteUtils.fromInt32(off));
                                    } catch (Exception e) {
                                        baos.write(ByteUtils.fromInt32(0));
                                        branches.add(op[j]);
                                    }
                                }
                                if (offsets.size() > 0) {
                                    currentOpcode = new BranchTableLink(offset, baos.toByteArray(), op[0], offsets);
                                } else if (branches.size() > 0) {
                                    currentOpcode = new BranchTableLink(offset, baos.toByteArray(), op[0], offsets, branches);
                                } else {
                                    System.err.println(String.format("Neither offsets nor label names given for branch table link at offset %d", offset));
                                    continue;
                                }
                            }
                            break;
                    }
                    break;
                case "chr":
                    switch (opcode[1]) {
                        case "init":
                            baos.write(0x20);
                            baos.write(0);
                            baos.write(SEQ_RegCMD2(op1, op2));
                            break;
                        case "update":
                            if (opcode.length > 2) {
                                if (opcode[2].equals("2")) {
                                        baos.write(0x20);
                                        baos.write(3);
                                        baos.write(SEQ_RegCMD2(op1, op2));
                                }
                            } else {
                                baos.write(0x20);
                                baos.write(2);
                                baos.write(SEQ_RegCMD2(op1, op2));
                            }
                            break;
                        case "CmdPAUSE":
                            baos.write(0x15);
                            baos.write(3);
                            baos.write(SEQ_RegCMD1(operands));
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
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
                        case "CmdPAUSE":
                            baos.write(0x15);
                            baos.write(3);
                            baos.write(SEQ_RegCMD1(operands));
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "TskSendMsg":
                    //TODO
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "TskExecFunc":
                    //TODO
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "movr":
                    baos.write(3);
                    baos.write(1);
                    baos.write(SEQ_RegCMD2(operands.split(",")[0],operands.split(",")[1]));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "push":
                    baos.write(3);
                    baos.write(3);
                    baos.write(SEQ_RegCMD1(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "pop":
                    baos.write(3);
                    baos.write(4);
                    baos.write(SEQ_RegCMD1(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), (byte) 3, (byte) 4);
                    break;
                case "i32":
                    currentOpcode = getInt((byte)0x04, opcode[1], operands.split(","));
                    break;
                case "i8":
                    currentOpcode = getInt((byte)0x05, opcode[1], operands.split(","));
                    break;
                case "i16":
                    currentOpcode = getInt((byte)0x06, opcode[1], operands.split(","));
                    break;
                case "i64":
                    currentOpcode = getInt((byte)0x07, opcode[1], operands.split(","));
                    break;
                case "f32":
                    baos.write(8);
                    switch (opcode[1]) {
                        case "move":
                            baos.write(2);
                            break;
                        case "sub":
                            baos.write(4);
                            break;
                        case "mul":
                            baos.write(5);
                            break;
                        case "div":
                            baos.write(6);
                            break;
                        case "cmp":
                            baos.write(7);
                            break;
                    }
                    baos.write(SEQ_RegCMD2(operands.split(",")[0],operands.split(",")[1]));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "ptr":
                    baos.write(9);
                    switch (opcode[1]){
                        case "debug":
                            baos.write(0);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "mov":
                            baos.write(1);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "add":
                            baos.write(4);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "move":
                            baos.write(7);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "from":
                            baos.write(8);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "to":
                            baos.write(9);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "push":
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "table":
                            baos.write(0xC);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "b":
                            baos.write(0x14);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "bl":
                            baos.write(0x1D);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "fvec":
                    baos.write(0xB);
                    switch (opcode[1]) {
                        case "mov":
                            baos.write(2);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "add":
                            baos.write(3);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "sub":
                            baos.write(4);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "muls":
                            baos.write(7);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "movs":
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "mulm":
                            baos.write(0x13);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "mtx":
                    baos.write(0xC);
                    switch (opcode[1]) {
                        case "copy":
                            baos.write(1);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "concat":
                            baos.write(2);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "transpose":
                            baos.write(3);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "inverse":
                            baos.write(4);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                        case "identity":
                            baos.write(6);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "scale":
                            baos.write(9);
                            baos.write(SEQ_RegCMD2(op1,op2));
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "sync":
                    switch (opcode[1]) {
                        case "timer":
                            if (opcode.length == 2) {
                                baos.write(Bytes.concat(ByteUtils.fromInt32(0x2011263F), ByteUtils.fromInt32(Integer.decode(operands))));
                            } else {
                                switch (opcode[2]) {
                                    case "run":
                                        baos.write(ByteUtils.fromUint32(0x20120026));
                                        break;
                                }
                            }
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "projectile":
                    switch (opcode[1]) {
                        case "pow":
                            if (opcode[2].equals("dmg") && opcode[3].equals("grd")) {
                                baos.write(0x47);
                                baos.write(4);
                                baos.write(0);
                                baos.write(0);
                                baos.write(Integer.decode(op[0]));
                                baos.write(Integer.decode(op[1]));
                                baos.write(Integer.decode(op[2]));
                            }
                            break;
                        case "num":
                            if (opcode[2].equals("hits")) {
                                baos.write(0x47);
                                baos.write(5);
                                baos.write(0);
                                baos.write(0);
                                baos.write(Integer.decode(op[0]));
                            }
                            break;
                        case "axisrotation":
                            baos.write(0x47);
                            baos.write(6);
                            baos.write(SEQ_RegCMD2(op1, op2));
                            break;
                        case "dir":
                            if (opcode[2].equals("ang")) {
                                baos.write(0x47);
                                baos.write(7);
                                baos.write(SEQ_RegCMD2(op1, op2));
                            }
                            break;
                        case "velocity":
                            baos.write(0x47);
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1(op[0]));
                            break;
                        case "launching":
                            if (opcode[2].equals("height")) {
                                baos.write(0x47);
                                baos.write(0xE);
                                baos.write(0);
                                baos.write(Integer.decode(op[0]));
                            }
                            break;
                        case "random":
                            switch (opcode[2]) {
                                case "launching":
                                    if (opcode[3].equals("height")) {
                                        baos.write(0x47);
                                        baos.write(0x1D);
                                        baos.write(0);
                                        baos.write(0);
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
                case "create":
                    switch (opcode[1]) {
                        case "hitbox":
                            if (opcode.length == 2) {
                                op = operands.split(",");
                                baos.write(Bytes.concat(ByteUtils.fromUint32(0x21040026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint32(0)));
                            } else if (opcode[2].equals("with") && opcode[3].equals("offset")) {
                                baos.write(ByteUtils.fromUint32(0x21110026));
                                baos.write(ByteUtils.fromUint16(Integer.decode(op[0])));
                                baos.write(ByteUtils.fromUint16(Integer.decode(op[1])));
                                baos.write(ByteUtils.fromUint16(Integer.decode(op[2])));
                                baos.write(ByteUtils.fromUint16(Integer.decode(op[3])));
                                baos.write(ByteUtils.fromUint16(Integer.decode(op[4])));
                                baos.write(ByteUtils.fromUint16(0));
                                baos.write(ByteUtils.fromInt32(Integer.decode(op[5])));
                                continue;
                            }
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "set":
                    switch (opcode[1]) {
                        case "pow":
                            op = operands.split(",");
                            baos.write(Bytes.concat(ByteUtils.fromUint32(0x21050026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1])), ByteUtils.fromUint16(Integer.decode(op[2])), ByteUtils.fromUint16(0)));
                            break;
                        case "ang":
                            op = operands.split(",");
                            baos.write(Bytes.concat(ByteUtils.fromUint32(0x21060026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1]))));
                            break;
                        case "hitbox":
                            switch (opcode[2]) {
                                case "timer":
                                    op = operands.split(",");
                                    baos.write(Bytes.concat(ByteUtils.fromUint32(0x21070026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1]))));
                                    break;
                            }
                            break;
                        case "pause":
                            baos.write(0x15);
                            baos.write(0x03);
                            baos.write(SEQ_RegCMD1(operands));
                            break;
                        case "timer":
                            switch (opcode[2]) {
                                case "decrement":
                                    baos.write(0x21);
                                    baos.write(0x12);
                                    baos.write(SEQ_RegCMD2(op1,op2));
                                    break;
                            }
                        case "rev":
                            baos.write(0x21);
                            if (opcode.length == 2) {
                                baos.write(0xD);
                                baos.write(0);
                                baos.write(0x26);
                                baos.write(ByteUtils.fromInt32(Integer.decode(operands)));
                            } else if (opcode[2].equals("and") && opcode[3].equals("rev2")) {
                                        baos.write(0xC);
                                        baos.write(0);
                                        baos.write(0x26);
                                        baos.write(ByteUtils.fromInt32(Integer.decode(op1)));
                                        baos.write(ByteUtils.fromInt32(Integer.decode(op2)));
                                        break;
                            }
                            break;
                        case "rev2":
                            baos.write(0x21);
                            baos.write(0xE);
                            baos.write(0);
                            baos.write(0x26);
                            baos.write(ByteUtils.fromInt32(Integer.decode(operands)));
                            break;
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "rev2":
                    if (opcode[1].equals("knockback") && opcode[2].equals("modify")) {
                        baos.write(0x21);
                        baos.write(0xB);
                        baos.write(Byte.decode(operands));
                        baos.write(0x26);
                    }
                    break;
                case "transform":
                    switch (opcode[1]) {
                        case "chr":
                            switch (opcode[2]) {
                                case "model":
                                    baos.write(0x21);
                                    baos.write(0x08);
                                    baos.write(SEQ_RegCMD1("chr_p"));
                                    break;
                            }
                            break;
                    }
                    break;
                case "flags":
                    baos.write(0x24);
                    baos.write(0x1A);
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
                    baos.write(action);
                    //No alternative actions supported, only pure flag operations
                    baos.write(0);
                    baos.write(ByteUtils.fromInt32(getFlags(opcode[2], operands.replace("\"", ""))));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "loadTexture":
                    //TODO
                    // Still todo
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "op":
                    String firstByte = opcode[1].substring(0,1);
                    String secondByte = opcode[1].substring(2,3);
                    switch (firstByte) {
                        case "02":
                            switch (secondByte) {
                                case "08":
                                    baos.write(UnknownOpcode.of(opcode[1], operands));
                                    //TODO
                                    // Still TODO
                                    break;
                                default:
                                    baos.write(UnknownOpcode.of(opcode[1], operands));
                                    break;
                            }
                            break;
                        case "09":
                            switch (secondByte) {
                                case "26":
                                    baos.write(9);
                                    baos.write(0x26);
                                    baos.write(SEQ_RegCMD2(op1, op2));
                                    break;
                                default:
                                    baos.write(UnknownOpcode.of(opcode[1], operands));
                                    break;
                            }
                            break;
                        case "0E":
                            switch (secondByte) {
                                case "06":
                                    baos.write(0x0E);
                                    baos.write(0x06);
                                    baos.write(SEQ_RegCMD2(op1, op2));
                                    break;
                                default:
                                    baos.write(UnknownOpcode.of(opcode[1], operands));
                                    break;
                            }
                            break;
                        default:
                            baos.write(UnknownOpcode.of(opcode[1], operands));
                            break;
                    }
                    currentOpcode = new UnknownOpcode(offset,baos.toByteArray());
                    break;
                default:
                    System.err.println(opcode[0]);
                    break;
            }
            opcodes.add(currentOpcode);
            offset += currentOpcode.getBytes().length;
        }
        for (Opcode opcode : opcodes) {
            if (BranchingOpcode.class.isInstance(opcode)) {
                Integer destination = labelMap.get(((BranchingOpcode) opcode).getDestinationFunctionName());
                if (destination != null) {
                    ((BranchingOpcode) opcode).setDestination(destination);
                } else {
                    destination = globalLabelMap.get(((BranchingOpcode) opcode).getDestinationFunctionName());
                    if (destination != null) {
                        ((BranchingOpcode) opcode).setDestination(destination);
                    }
                }
                Function label = globalFunctionMap.get(((BranchingOpcode) opcode).getDestination());
                if (label != null) {
                    ((BranchingOpcode) opcode).setDestinationFunctionName(label.name());
                }
            } else if (BranchTable.class.isInstance(opcode)) {
                if (((BranchTable) opcode).getBranches().size() > 0) {
                    List<Integer> offsets = new LinkedList<>();
                    for (String label : ((BranchTable) opcode).getBranches()) {
                        Integer destination = labelMap.get(label);
                        if (destination != null) {
                            offsets.add(destination);
                        } else {
                            destination = globalLabelMap.get(label);
                            if (destination != null) {
                                offsets.add(destination);
                            }
                        }
                    }
                    ((BranchTable) opcode).setOffsets(offsets);
                }
            } else if (BranchTableLink.class.isInstance(opcode)) {
                if (((BranchTableLink) opcode).getBranches().size() > 0) {
                    List<Integer> offsets = new LinkedList<>();
                    for (String label : ((BranchTableLink) opcode).getBranches()) {
                        Integer destination = labelMap.get(label);
                        if (destination != null) {
                            offsets.add(destination);
                        } else {
                            destination = globalLabelMap.get(label);
                            if (destination != null) {
                                offsets.add(destination);
                            }
                        }
                    }
                    ((BranchTableLink) opcode).setOffsets(offsets);
                }
            }
        }
        return new Pair(opcodes,offset);
    }

    static private int getFlags(String group, String operands) {
        int flags = 0;
        Map<String, Integer> flagValues;
        switch (group) {
            case "af":
                flagValues = Seq.AF_FLAGS_GET;
                break;
            case "nf":
                flagValues = Seq.NF_FLAGS_GET;
                break;
            case "pf":
                flagValues = Seq.PF_FLAGS_GET;
                break;
            case "kf":
                flagValues = Seq.KF_FLAGS_GET;
                break;
            case "df":
                flagValues = Seq.DF_FLAGS_GET;
                break;
            case "ef":
                flagValues = Seq.EF_FLAGS_GET;
                break;
            case "mf":
                flagValues = Seq.MF_FLAGS_GET;
                break;
            case "rf":
                flagValues = Seq.RF_FLAGS_GET;
                break;
            case "sf":
                flagValues = Seq.SF_FLAGS_GET;
                break;
            case "cf":
                flagValues = Seq.CF_FLAGS_GET;
                break;
            case "chr":
            case "cmf":
                flagValues = Seq.CHR_MOD_FLAGS_GET;
                break;
            case "k2f":
                flagValues = Seq.K2F_FLAGS_GET;
                break;
            case "d2f":
                flagValues = Seq.D2F_FLAGS_GET;
                break;
            case "n2f":
                flagValues = Seq.N2F_FLAGS_GET;
                break;
            case "unknown":
            default:
                throw new IllegalStateException("Unexpected value: " + group);
        }
        for (String s : operands.toUpperCase().split(",")) {
            int flag = flagValues.getOrDefault(s,0);
            flags |= flag;
        }
        return flags;
    }

    static private Opcode getInt(byte group, String opcode, String[] operands) throws IOException {
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
                buffer.put(SEQ_RegCMD1(operands[0].replace(" ", ""),group));
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
                buffer.put(SEQ_RegCMD2(operands[0].replace(" ", ""), operands[1].replace(" ", ""), group));
                break;
        }
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        ByteStream bs = new ByteStream(bytes);
        return SeqHelper.getSeqOpcode(bs, bytes[0], bytes[1]);
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
        return SEQ_RegCMD1(op,4);
    }

    static private byte[] SEQ_RegCMD1(String op, int type) {
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
        if (registerVal > 0x3F) {
            buffer.putInt(opDirect.intValue());
        }
        switch (registerVal & 0x3F) {
            case 0x3F:
                switch (type) {
                    case 4:
                        buffer.putInt(opDirect.intValue());
                        break;
                    case 5:
                        buffer.put(opDirect.byteValue());
                        buffer.put((byte) 0);
                        buffer.put((byte) 0);
                        buffer.put((byte) 0);
                        break;
                    case 6:
                        buffer.putShort(opDirect.shortValue());
                        buffer.putShort((short) 0);
                        break;
                }
                break;
            case 0x3E:
                //TODO
                break;
        }

        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }

    static private byte[] SEQ_RegCMD2(String op1, String op2) {
        return SEQ_RegCMD2(op1, op2, 4);
    }

    static private byte[] SEQ_RegCMD2(String op1, String op2, int type) {
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

                } else {
                    op1Direct = asd;
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
                    if (op2.contains("(")) {
                        op2 = op2.substring(op2.indexOf("(") + 1);
                        if (op2.contains(")")) {
                            op2 = op2.replace(")", "");
                        }
                    }
                    break;
            }
            op2Direct = Long.decode(op2).intValue();
        } else if(op1Parts.length > 1) {
            String offset2 = op2Parts[1];
            //String indirectOffset;
            op2v = (byte) (op2v | 0x10 << op2Parts.length);
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
        if (op1v > 0x3F) {
            buffer.put((byte) 0);
            buffer.putInt(op1Direct.intValue());
            buffer.put(op2v);
            buffer.put((byte)0);
            buffer.put((byte)0);
            buffer.put((byte)0);
            if (op2v > 0x3F) {
                buffer.putInt(op2Direct.intValue());
            } else if (op2v >= 0x3E) {
                switch (type) {
                    case 0x4:
                        buffer.putInt(op2Direct);
                        break;
                    case 0x5:
                        buffer.put(op2Direct.byteValue());
                        buffer.putShort((short) 0);
                        buffer.put((byte) 0);
                        break;
                    case 0x6:
                        buffer.putShort(op2Direct.shortValue());
                        buffer.putShort((short) 0);
                        break;
                }
            }
        } else {
            buffer.put(op2v);
            if (op2v > 0x3F && op2v < 0x80) {
                buffer.putInt(op2Direct.intValue());
            }
            switch (type) {
                case 0x4:
                    buffer.putInt(op2Direct);
                    break;
                case 0x5:
                    buffer.put(op2Direct.byteValue());
                    buffer.putShort((short) 0);
                    buffer.put((byte) 0);
                    break;
                case 0x6:
                    buffer.putShort(op2Direct.shortValue());
                    buffer.putShort((short) 0);
                    break;
            }
        }
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        return bytes;
    }
}
