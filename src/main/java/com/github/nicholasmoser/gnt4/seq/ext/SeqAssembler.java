package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD1;
import com.github.nicholasmoser.gnt4.seq.SEQ_RegCMD2;
import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.TCG;
import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchDecrementNotZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchEqualToZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchGreaterThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchGreaterThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanEqualZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanOrEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLessThanZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnGreaterThanEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnGreaterThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnLessThanEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnLessThanZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturnNotEqualZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchNotEqualToZero;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchNotEqualZeroLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTable;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTableLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchingOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.End;
import com.github.nicholasmoser.gnt4.seq.opcodes.HardReset;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.SetTimerDecrement;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

public class SeqAssembler {

    private static final Logger LOGGER = Logger.getLogger(SeqAssembler.class.getName());

    /**
     *
     * Parses a list of strings, and outputs the corresponding Opcodes
     *
     * @param lines The lines with assembly to be assembled to bytecode
     * @param seqPath The path to the SEQ file that is edited
     * @return A pair containing the list of Opcodes representing the lines, and an integer representing the total size of the byte code in bytes
     */
    public static Pair<List<Opcode>, Integer> assembleLines(String[] lines, Path seqPath) throws IOException {
        LinkedList<Opcode> opcodes = new LinkedList<>();
        int offset = 0;
        Map<String, Integer> labelMap = new HashMap<>();
        Map<String, Integer> globalLabelMap = new HashMap<>();
        Map<Integer, Function> globalFunctionMap = new HashMap<>();
        if (seqPath != null) {
            globalFunctionMap = Functions.getFunctions(seqPath.toString());
            for (Entry<Integer, Function> me : globalFunctionMap.entrySet()) {
                globalLabelMap.put(me.getValue().name(), me.getKey());
            }
        }
        for (String line : lines) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String operation = line;
            // Strip comments
            if (operation.contains("//")) {
                operation = operation.substring(0, operation.indexOf("//"));
            }
            // Remove spaces from end
            operation = operation.replaceAll("\\s+$", "");
            // Add label to label map
            if (operation.endsWith(":")) {
                labelMap.put(operation.replace(":", "").replace(" ", ""), offset);
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
            //String op1 = "";
            //String op2 = "";
            //if (op.length > 1) {
            //    op1 = op[0];
            //    op2 = op[1];
            //}
            Opcode currentOpcode = new UnknownOpcode(offset, new byte[]{0, 0, 0, 0});
            switch (opcode[0].replace(" ", "")) {
                case "":
                    continue;
                case "end":
                    currentOpcode = new End(offset);
                    break;
                case "hard":
                    currentOpcode = switch (opcode[1]) {
                        case "reset" -> new HardReset(offset);
                        default -> currentOpcode;
                    };
                    break;
                case "b":
                    try {
                        currentOpcode = new Branch(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new Branch(offset, operands);
                    }
                    break;
                case "beqz":
                    try {
                        currentOpcode = new BranchEqualToZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchEqualToZero(offset, operands);
                    }
                    break;
                case "bnez":
                    try {
                        currentOpcode = new BranchNotEqualToZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchNotEqualToZero(offset, operands);
                    }
                    break;
                case "bgtz":
                    try {
                        currentOpcode = new BranchGreaterThanZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchGreaterThanZero(offset, operands);
                    }
                    break;
                case "bgez":
                    try {
                        currentOpcode = new BranchGreaterThanOrEqualToZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchGreaterThanOrEqualToZero(offset, operands);
                    }
                    break;
                case "bltz":
                    try {
                        currentOpcode = new BranchLessThanZero(offset, Integer.decode(operands), (byte) 0x37);
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanZero(offset, operands);
                    }
                    break;
                case "blez":
                    try {
                        currentOpcode = new BranchLessThanOrEqualToZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanOrEqualToZero(offset, operands);
                    }
                    break;
                case "bdnz":
                    try {
                        currentOpcode = new BranchDecrementNotZero(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchDecrementNotZero(offset, operands);
                    }
                    break;
                case "bl":
                    try {
                        currentOpcode = new BranchLink(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLink(offset, operands);
                    }
                    break;
                case "beqzal":
                    try {
                        currentOpcode = new BranchEqualToZeroLink(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchEqualToZeroLink(offset, operands);
                    }
                    break;
                case "bnezal":
                    try {
                        currentOpcode = new BranchNotEqualZeroLink(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchNotEqualZeroLink(offset, operands);
                    }
                    break;
                case "bgtzal":
                    try {
                        currentOpcode = new BranchingOpcode("bgtzal", new byte[]{0x01, 0x3F, 0x00, 0x00}, offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchingOpcode("bgtzal", new byte[]{0x01, 0x3F, 0x00, 0x00}, offset, operands);
                    }
                    break;
                case "bgezal":
                    try {
                        currentOpcode = new BranchingOpcode("bgezal", new byte[]{0x01, 0x40, 0x00, 0x00}, offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchingOpcode("bgezal", new byte[]{0x01, 0x40, 0x00, 0x00}, offset, operands);
                    }
                    break;
                case "bltzal":
                    try {
                        currentOpcode = new BranchLessThanZeroLink(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanZeroLink(offset, operands);
                    }
                    break;
                case "blezal":
                    try {
                        currentOpcode = new BranchLessThanEqualZeroLink(offset, Integer.decode(operands));
                    } catch (Exception e) {
                        currentOpcode = new BranchLessThanEqualZeroLink(offset, operands);
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
                                baos.write(new byte[]{0x01, 0x50});
                                baos.write(SEQ_RegCMD1.parseDescription(operands));
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
                                if (!offsets.isEmpty()) {
                                    currentOpcode = new BranchTable(offset, baos.toByteArray(), op[0], offsets);
                                } else if (!branches.isEmpty()) {
                                    currentOpcode = new BranchTable(offset, baos.toByteArray(), op[0], offsets, branches);
                                } else {
                                    LOGGER.log(Level.SEVERE, String.format("Neither offsets nor label names given for branch table at offset %d%n", offset));
                                    continue;
                                }
                            } else {
                                baos.write(new byte[]{0x01, 0x51});
                                baos.write(SEQ_RegCMD1.parseDescription(operands));
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
                                if (!offsets.isEmpty()) {
                                    currentOpcode = new BranchTableLink(offset, baos.toByteArray(), op[0], offsets);
                                } else if (!branches.isEmpty()) {
                                    currentOpcode = new BranchTableLink(offset, baos.toByteArray(), op[0], offsets, branches);
                                } else {
                                    System.err.printf("Neither offsets nor label names given for branch table link at offset %d%n", offset);
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
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                            break;
                        case "update":
                            if (opcode.length > 2) {
                                if (opcode[2].equals("2")) {
                                    baos.write(0x20);
                                    baos.write(3);
                                    baos.write(SEQ_RegCMD2.parseDescription(operands));
                                }
                            } else {
                                baos.write(0x20);
                                baos.write(2);
                                baos.write(SEQ_RegCMD2.parseDescription(operands));
                            }
                            break;
                        case "CmdPAUSE":
                            baos.write(0x15);
                            baos.write(3);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
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
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
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
                    baos.write(SEQ_RegCMD2.parseDescription(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "push":
                    baos.write(3);
                    baos.write(3);
                    baos.write(SEQ_RegCMD1.parseDescription(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "pop":
                    baos.write(3);
                    baos.write(4);
                    baos.write(SEQ_RegCMD1.parseDescription(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), (byte) 3, (byte) 4);
                    break;
                case "i32":
                    currentOpcode = getInt((byte) 0x04, opcode[1], operands);
                    break;
                case "i8":
                    currentOpcode = getInt((byte) 0x05, opcode[1], operands);
                    break;
                case "i16":
                    currentOpcode = getInt((byte) 0x06, opcode[1], operands);
                    break;
                case "i64":
                    currentOpcode = getInt((byte) 0x07, opcode[1], operands);
                    break;
                case "f32":
                    baos.write(8);
                    switch (opcode[1]) {
                        case "move" -> baos.write(2);
                        case "sub" -> baos.write(4);
                        case "mul" -> baos.write(5);
                        case "div" -> baos.write(6);
                        case "cmp" -> baos.write(7);
                    }
                    baos.write(SEQ_RegCMD2.parseDescription(operands));
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "ptr":
                    baos.write(9);
                    switch (opcode[1]) {
                        case "debug" -> {
                            baos.write(0);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                        case "mov" -> {
                            baos.write(1);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "add" -> {
                            baos.write(4);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "move" -> {
                            baos.write(7);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "from" -> {
                            baos.write(8);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "to" -> {
                            baos.write(9);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "push" -> {
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                        case "table" -> {
                            baos.write(0xC);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "b" -> {
                            baos.write(0x14);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                        case "bl" -> {
                            baos.write(0x1D);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "fvec":
                    baos.write(0xB);
                    switch (opcode[1]) {
                        case "mov" -> {
                            baos.write(2);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "add" -> {
                            baos.write(3);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "sub" -> {
                            baos.write(4);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "muls" -> {
                            baos.write(7);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "movs" -> {
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                        case "mulm" -> {
                            baos.write(0x13);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "mtx":
                    baos.write(0xC);
                    switch (opcode[1]) {
                        case "copy" -> {
                            baos.write(1);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "concat" -> {
                            baos.write(2);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "transpose" -> {
                            baos.write(3);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "inverse" -> {
                            baos.write(4);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
                        case "identity" -> {
                            baos.write(6);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                        }
                        case "scale" -> {
                            baos.write(9);
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                        }
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
                                    case "run" -> baos.write(ByteUtils.fromUint32(0x20120026));
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
                            baos.write(SEQ_RegCMD2.parseDescription(operands));
                            break;
                        case "dir":
                            if (opcode[2].equals("ang")) {
                                baos.write(0x47);
                                baos.write(7);
                                baos.write(SEQ_RegCMD2.parseDescription(operands));
                            }
                            break;
                        case "velocity":
                            baos.write(0x47);
                            baos.write(0xA);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
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
                                case "timer" -> {
                                    op = operands.split(",");
                                    baos.write(Bytes.concat(ByteUtils.fromUint32(0x21070026), ByteUtils.fromUint16(Integer.decode(op[0])), ByteUtils.fromUint16(Integer.decode(op[1]))));
                                }
                            }
                            break;
                        case "pause":
                            baos.write(0x15);
                            baos.write(0x03);
                            baos.write(SEQ_RegCMD1.parseDescription(operands));
                            break;
                        case "timer":
                            switch (opcode[2]) {
                                case "decrement" -> {
                                    // Remove default value info
                                    operands = operands.replace(SetTimerDecrement.DEFAULT, "");
                                    baos.write(0x21);
                                    baos.write(0x12);
                                    baos.write(SEQ_RegCMD2.parseDescription(operands));
                                }
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
                                //baos.write(ByteUtils.fromInt32(Integer.decode(op1)));
                                //baos.write(ByteUtils.fromInt32(Integer.decode(op2)));
                                throw new IOException("FIX TODO");
                                //break;
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
                                case "model" -> {
                                    baos.write(0x21);
                                    baos.write(0x08);
                                    //baos.write(SEQ_RegCMD1("chr_p"));
                                    throw new IOException("FIX TODO");
                                }
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
                    try {
                        baos.write(UnknownOpcode.of(opcode[1], operands));
                    } catch (Exception e) {
                        throw new IOException("Failed to unknown opcode: " + line, e);
                    }
                    currentOpcode = new UnknownOpcode(offset, baos.toByteArray());
                    break;
                case "play":
                    if ("sound".equals(opcode[1])) {
                        int type = switch(op[0]) {
                            case "general" -> 0x0;
                            case "hit" -> 0x1;
                            case "stored_1" -> 0x2;
                            case "stored_2" -> 0x3;
                            case "run" -> 0x5;
                            case "unused" -> 0x6;
                            case "land_on_feet" -> 0x8;
                            case "land_on_body" -> 0x9;
                            case "grunt" -> 0xB;
                            case "hiki" -> 0xC;
                            case "walk" -> 0xD;
                            case "3MC" -> 0xE;
                            default -> throw new IllegalArgumentException("Unknown type: " + op[0]);
                        };
                        baos.write(0x24);
                        baos.write(0x17);
                        baos.write(type);
                        baos.write(0);
                        baos.write(ByteUtils.fromUint16(Integer.decode(op[1])));
                        baos.write(ByteUtils.fromUint16(Integer.decode(op[2])));
                    } else {
                        throw new IllegalArgumentException("Unknown play opcode: " + opcode[1]);
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                case "tcg":
                    switch(opcode[1]) {
                        case "mov":
                            baos.write(new byte[] {0x3C, 0x00, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "add":
                            baos.write(new byte[] {0x3C, 0x01, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "sub":
                            baos.write(new byte[] {0x3C, 0x02, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "and":
                            baos.write(new byte[] {0x3C, 0x03, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "or":
                            baos.write(new byte[] {0x3C, 0x04, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "mul":
                            baos.write(new byte[] {0x3C, 0x05, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "div":
                            baos.write(new byte[] {0x3C, 0x06, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "mod":
                            baos.write(new byte[] {0x3C, 0x07, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        case "rand":
                            baos.write(new byte[] {0x3C, 0x0A, 0, 0});
                            baos.write(ByteUtils.fromInt32(TCG.writeValue(op[0])));
                            baos.write(ByteUtils.fromInt32(TCG.writePointer(op[1])));
                            break;
                        default:
                            throw new IllegalArgumentException("Opcode not yet supported: " + line);
                    }
                    currentOpcode = SeqHelper.getSeqOpcode(new ByteStream(baos.toByteArray()), baos.toByteArray()[0], baos.toByteArray()[1]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown assembly: " + opcode[0]);
            }
            opcodes.add(currentOpcode);
            offset += currentOpcode.getBytes().length;
        }
        for (Opcode opcode : opcodes) {
            if (opcode instanceof BranchingOpcode branchingOpcode) {
                Integer destination = labelMap.get(branchingOpcode.getDestinationFunctionName());
                if (destination != null) {
                    ((BranchingOpcode) opcode).setDestination(destination);
                } else {
                    destination = globalLabelMap.get(branchingOpcode.getDestinationFunctionName());
                    if (destination != null) {
                        branchingOpcode.setDestination(destination);
                    }
                }
                Function label = globalFunctionMap.get(branchingOpcode.getDestination());
                if (label != null) {
                    branchingOpcode.setDestinationFunctionName(label.name());
                }
            } else if (opcode instanceof BranchTable branchTable) {
                if (!branchTable.getBranches().isEmpty()) {
                    List<Integer> offsets = new LinkedList<>();
                    for (String label : branchTable.getBranches()) {
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
                    branchTable.setOffsets(offsets);
                }
            } else if (opcode instanceof BranchTableLink branchTableLink) {
                if (!branchTableLink.getBranches().isEmpty()) {
                    List<Integer> offsets = new LinkedList<>();
                    for (String label : branchTableLink.getBranches()) {
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
                    branchTableLink.setOffsets(offsets);
                }
            }
        }
        return new Pair(opcodes,offset);
    }

    static private int getFlags(String group, String operands) {
        int flags = 0;
        Map<String, Integer> flagValues = switch (group) {
            case "af" -> Seq.AF_FLAGS_GET;
            case "nf" -> Seq.NF_FLAGS_GET;
            case "pf" -> Seq.PF_FLAGS_GET;
            case "kf" -> Seq.KF_FLAGS_GET;
            case "df" -> Seq.DF_FLAGS_GET;
            case "ef" -> Seq.EF_FLAGS_GET;
            case "mf" -> Seq.MF_FLAGS_GET;
            case "rf" -> Seq.RF_FLAGS_GET;
            case "sf" -> Seq.SF_FLAGS_GET;
            case "cf" -> Seq.CF_FLAGS_GET;
            case "chr", "cmf" -> Seq.CHR_MOD_FLAGS_GET;
            case "k2f" -> Seq.K2F_FLAGS_GET;
            case "d2f" -> Seq.D2F_FLAGS_GET;
            case "n2f" -> Seq.N2F_FLAGS_GET;
            default -> throw new IllegalStateException("Unexpected value: " + group);
        };
        for (String s : operands.toUpperCase().split(",")) {
            int flag = flagValues.getOrDefault(s,0);
            flags |= flag;
        }
        return flags;
    }

    static private Opcode getInt(byte group, String opcode, String operands) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(0x24);
        buffer.put(group);
        switch (opcode) {
            case "debug" -> {
                buffer.put((byte) 0);
                buffer.putShort((short) 0);
            }
            case "float" -> buffer.put((byte) 0x01);
            case "mov" -> buffer.put((byte) 0x02);
            case "andc" -> buffer.put((byte) 0x03);
            case "nimply" -> buffer.put((byte) 0x04);
            case "inc" -> buffer.put((byte) 0x05);
            case "dec" -> buffer.put((byte) 0x06);
            case "add" -> buffer.put((byte) 0x07);
            case "sub" -> buffer.put((byte) 0x08);
            case "mul" -> buffer.put((byte) 0x09);
            case "div" -> buffer.put((byte) 0x0A);
            case "shl" -> buffer.put((byte) 0x0B);
            case "shr" -> buffer.put((byte) 0x0C);
            case "and" -> buffer.put((byte) 0x0D);
            case "or" -> buffer.put((byte) 0x0E);
            case "xor" -> buffer.put((byte) 0x0F);
            case "not" -> buffer.put((byte) 0x10);
            case "subc" -> buffer.put((byte) 0x11);
            case "chs" -> buffer.put((byte) 0x12);
            case "cuhw" -> buffer.put((byte) 0x13);
            case "range" -> buffer.put((byte) 0x14);
            case "rand" -> buffer.put((byte) 0x15);
            case "andcz" -> buffer.put((byte) 0x16);
            case "mod" -> buffer.put((byte) 0x17);
            case "abs" -> buffer.put((byte) 0x18);
        }
        switch (opcode) {
            case "inc", "dec", "abs" -> buffer.put(
                SEQ_RegCMD1.parseDescription(operands));
            case "float", "mov", "andc", "nimply", "add", "sub", "mul", "div", "shl", "shr", "and", "or", "xor", "not", "subc", "chs", "cuhw", "range", "rand", "andcz", "mod" -> buffer.put(
                SEQ_RegCMD2.parseDescription(operands));
        }
        byte[] bytes = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(bytes);
        ByteStream bs = new ByteStream(bytes);
        return SeqHelper.getSeqOpcode(bs, bytes[0], bytes[1]);
    }
}
