package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.*;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteStream;
import javafx.util.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SeqDisassembler {

    public static void disassemble(Path outputPath, Path seqPath, String fileName) throws IOException {
        disassemble(outputPath, seqPath, fileName, false);
    }
    public static void disassemble(Path outputPath, Path seqPath, String fileName, boolean verbose) throws IOException {
        LinkedHashMap<Integer, Pair<String,List<Opcode>>> functions = getAllOpcodes(seqPath, fileName, verbose);
        Map<Integer, Function> fm = Functions.getFunctions(fileName);
        System.err.println(fm);
        for (Map.Entry<Integer, Pair<String,List<Opcode>>> fun : functions.entrySet()) {
            try(OutputStream os = Files.newOutputStream(outputPath.resolve(fun.getValue().getKey()))) {
                for (Opcode op : fun.getValue().getValue()) {
                    if (op instanceof BranchingOpcode b && fm.get(b.getDestination()) != null) {
                        os.write(b.toAssembly(fun.getKey(), fm.get(b.getDestination()).name()).getBytes(StandardCharsets.UTF_8));
                    } else {
                        os.write(op.toAssembly(fun.getKey()).getBytes(StandardCharsets.UTF_8));
                    }
                    os.write('\n');
                }
            }
        }
    }

    public static LinkedHashMap<Integer, Pair<String,List<Opcode>>> getAllOpcodes(Path seqPath, String fileName, boolean verbose) throws IOException {
        SeqType seqType = SeqHelper.getSeqType(fileName);

        byte[] bytes = Files.readAllBytes(seqPath);
        ByteStream bs = new ByteStream(bytes);
        boolean[] handled = new boolean[bytes.length];

        byte[] header = new byte[16];
        if (bs.read(header) != 16) {
            throw new IOException("Failed to read header, not enough bytes.");
        }

        LinkedHashMap<Integer, Pair<String,List<Opcode>>> blobs = new LinkedHashMap<>();
        Map<Integer, Function> functionMap = Functions.getFunctions(fileName);
        ByteBuffer chr_p = ByteBuffer.allocate(0xA08);
        Object[] registers = new Object[0x40];
        registers[0x26] = chr_p;
        registers[0x27] = ByteBuffer.allocate(0xA08);
        if (seqType != SeqType.CHR_0000) {
            return null;
        }
        byte opcodeGroup = bs.peekBytes(2)[0];
        byte opcode = bs.peekBytes(2)[1];
        Opcode firstBranch = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
        if (!(firstBranch instanceof Branch fb)) {
            return null;
        }
        for (int i = 0; i < bs.offset(); i++) {
            handled[i] = true;
        }
        bs.seek((fb).getDestination());
        disassembleFunctions(bs, "init", blobs, registers, handled, functionMap);
        chr_p.position(0);
        if (verbose) {
            while (chr_p.hasRemaining()) {
                System.err.println(String.format("Offset: 0x%04X\t0x%04X", chr_p.position(), chr_p.getInt()));
            }
        }
        chr_p.position(Chr.getOffset("chr_tbl").get());
        //bs.seek(chr_p.getInt());
        bs.seek(0x30); // Standard position
        getActions(bs, blobs, handled, functionMap);
        return blobs;
    }

    private static void getActions(ByteStream bs, LinkedHashMap<Integer, Pair<String, List<Opcode>>> rs, boolean[] handled,
                                   Map<Integer, Function> functionMap) throws IOException {
        try {
            for (int i = 0; i < 0x260; i++) {
                int actOffset = bs.readWord();
                if (functionMap.get(Integer.valueOf(actOffset)) == null) {
                    functionMap.put(Integer.valueOf(actOffset), new Function(String.format("ACT_%X", i), new LinkedList<>()));
                } else {
                    functionMap.get(Integer.valueOf(actOffset)).comments().add(String.format("ACT_%X", i));
                }
                int nextPosition = bs.offset();
                bs.seek(actOffset);
                LinkedList<Opcode> opcodes = new LinkedList<>();
                getAction(bs, opcodes, handled, functionMap);
                bs.seek(nextPosition);
                rs.put(actOffset, new Pair(String.format("ACT_%X", i), opcodes));
            }
        } catch (IOException e) {
            for (Map.Entry<Integer, Pair<String, List<Opcode>>> entry : rs.entrySet()) {
                System.err.println(String.format("Offset: 0x%X;\t%s", entry.getKey(), entry.getValue().getKey()));
            }
            System.err.println(String.format("Pointer offset in SEQ file:\t0x%05X", bs.offset()));
            throw e;
        }
    }

    private static void getAction(ByteStream bs, LinkedList<Opcode> opcodes, boolean[] handled,
                                  Map<Integer, Function> functionMap) throws IOException {
        Queue<BranchingOpcode> branches = new LinkedList<>();
        try {
            while (true) {
                byte opcodeGroup = bs.peekBytes(2)[0];
                byte opcode = bs.peekBytes(2)[1];
                Opcode newOpcode = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
                opcodes.add(newOpcode);
                if (newOpcode instanceof BranchLink bl) {
                    if (functionMap.get(bl.getDestination()) != null &&
                            functionMap.get(bl.getDestination()).name().equals("CallAction")) {
                        break;
                    }
                    branches.add(bl);
                } else if (newOpcode instanceof End) {
                    break;
                }
            }
            while (branches.peek() != null) {
                BranchingOpcode branch = branches.poll();
                if (handled[branch.getDestination()]) {
                    continue;
                }
                if (functionMap.get(branch.getDestination()) == null) {
                    if (branch.getDestination() == bs.offset()) {
                        getAction(bs, opcodes, handled, functionMap);
                    } else {
                        if (branch instanceof BranchingLinkingOpcode) {
                            functionMap.put(branch.getDestination(), new Function(String.format("fun_%X",
                                    branch.getDestination()), new LinkedList<>()));
                        } else {
                            functionMap.put(branch.getDestination(), new Function(String.format("branch_%X",
                                    branch.getDestination()), new LinkedList<>()));
                        }
                    }
                }
            }
        } catch (IllegalStateException e) {
            // Ignore illegal state for now
        }
    }

    private static void getOpcodes (ByteStream bs, Object[] registers, boolean[] handled,
                                    LinkedList<Opcode> opcodes, Queue<BranchingOpcode> branches) throws IOException {
        boolean first = true;
        while (first || !(opcodes.getLast() instanceof BranchLinkReturn || opcodes.getLast() instanceof End)) {
            first = false;
            byte opcodeGroup = bs.peekBytes(2)[0];
            byte opcode = bs.peekBytes(2)[1];
            Opcode newOpcode = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode, registers);
            opcodes.add(newOpcode);
            for (int i = newOpcode.getOffset(); i < bs.offset(); i++) {
                handled[i] = true;
            }
            if (newOpcode instanceof BranchingOpcode) {
                branches.add((BranchingOpcode) newOpcode);
            } else if (newOpcode instanceof BranchLinkReturn || newOpcode instanceof End) {
                break;
            }
        }
    }

    private static void disassembleFunctions (ByteStream bs, String name, LinkedHashMap<Integer, Pair<String,List<Opcode>>> rs,
                                     Object[] registers, boolean[] handled, Map<Integer, Function> functionMap) throws IOException {
        int offset = bs.offset();
        LinkedList<Opcode> opcodes = new LinkedList<>();
        Queue<BranchingOpcode> branches = new LinkedList<>();
        getOpcodes(bs, registers, handled, opcodes, branches);
        HashSet<BranchingOpcode> unhandledBranches = new HashSet<>();
        while (branches.peek() != null) {
            BranchingOpcode branch = branches.poll();
            if (handled[branch.getDestination()]) {
                continue;
            }
            if (branch instanceof BranchingLinkingOpcode) {
                if (!functionMap.containsKey(branch.getDestination())) {
                    functionMap.put(branch.getDestination(), new Function(
                            String.format("fun_%X", branch.getDestination()), new LinkedList<>()));
                    int currentOffset = bs.offset();
                    bs.seek(branch.getDestination());
                    disassembleFunctions(bs, functionMap.get(branch.getDestination()).name(), rs, registers,
                            handled, functionMap);
                    bs.seek(currentOffset);
                }
            } else if (branch.getDestination() == bs.offset()) {
                getOpcodes(bs, registers, handled, opcodes, branches);
            } else if (branch.getDestination() < offset) {
                int currentOffset = bs.offset();
                bs.seek(branch.getDestination());
                disassembleFunctions(bs, String.format("branch_%X", branch.getDestination()), rs, registers,
                        handled, functionMap);
                bs.seek(currentOffset);
            } else if (branch.getDestination() > offset && branch.getDestination() < bs.offset()) {

            } else if (!unhandledBranches.contains(branch)){
                unhandledBranches.add(branch);
            } else {
                int currentOffset = bs.offset();
                bs.seek(branch.getDestination());
                disassembleFunctions(bs, String.format("branch_%X", branch.getDestination()), rs, registers,
                        handled, functionMap);
                bs.seek(currentOffset);
            }
        }
        rs.put(offset, new Pair(name, opcodes));
    }
}
