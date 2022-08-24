package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.*;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteStream;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SeqDisassembler {

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
        if (seqType != SeqType.CHR_0000) {
            return null;
        }
        byte opcodeGroup = bs.peekBytes(2)[0];
        byte opcode = bs.peekBytes(2)[1];
        Opcode firstBranch = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
        if (!(firstBranch instanceof Branch)) {
            return null;
        }
        bs.seek(((Branch) firstBranch).getDestination());
        disassemble(bs, "init", blobs, registers, chr_p, handled);
        chr_p.position(Chr.getOffset("chr_tbl").get());
        bs.seek(chr_p.getInt());
        getActions(bs, blobs, handled, functionMap);
        return blobs;
    }

    private static void getActions(ByteStream bs, LinkedHashMap<Integer, Pair<String, List<Opcode>>> rs, boolean[] handled,
                                   Map<Integer, Function> functionMap) throws IOException {
        for (int i = 0; i < 0x260; i++) {
            int actOffset = bs.readWord();
            int nextPosition = bs.offset();
            bs.seek(actOffset);
            LinkedList<Opcode> opcodes = new LinkedList<>();
            getAction(bs, opcodes, handled, functionMap);
            bs.seek(nextPosition);
            rs.put(actOffset, new Pair(String.format("ACT_%X", i),opcodes));
        }
    }

    private static void getAction(ByteStream bs, LinkedList<Opcode> opcodes, boolean[] handled,
                                  Map<Integer, Function> functionMap) throws IOException {
        Queue<BranchingOpcode> branches = new LinkedList<>();
        while (true) {
            byte opcodeGroup = bs.peekBytes(2)[0];
            byte opcode = bs.peekBytes(2)[1];
            Opcode newOpcode = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
            opcodes.add(newOpcode);
            if (newOpcode instanceof BranchLink bl) {
                if (functionMap.get(bl.getDestination()).name().equals("CallAction")) {
                    break;
                }
                branches.add(bl);
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
                  if (branch instanceof BranchingLinkingOpcode bl) {
                      functionMap.put(branch.getDestination(), new Function(String.format("fun_%X",
                              branch.getDestination()), new LinkedList<>()));
                  } else {
                      functionMap.put(branch.getDestination(), new Function(String.format("branch_%X",
                              branch.getDestination()), new LinkedList<>()));
                  }
              }
            }
        }
    }

    private static void getOpcodes (ByteStream bs, Object[] registers, ByteBuffer chr_p, boolean[] handled,
                                    LinkedList<Opcode> opcodes, Queue<BranchingOpcode> branches) throws IOException {
        while (!(opcodes.getLast() instanceof BranchLinkReturn || opcodes.getLast() instanceof End)) {
            byte opcodeGroup = bs.peekBytes(2)[0];
            byte opcode = bs.peekBytes(2)[1];
            Opcode newOpcode = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
            opcodes.add(newOpcode);
            for (int i = newOpcode.getOffset(); i < bs.offset(); i++) {
                handled[i] = true;
            }
            if (newOpcode instanceof BranchingOpcode) {
                branches.add((BranchingOpcode) newOpcode);
            }
        }
    }

    private static void disassemble (ByteStream bs, String name, LinkedHashMap<Integer, Pair<String,List<Opcode>>> rs,
                                     Object[] registers, ByteBuffer chr_p, boolean[] handled) throws IOException {
        int offset = bs.offset();
        LinkedList<Opcode> opcodes = new LinkedList<>();
        Queue<BranchingOpcode> branches = new LinkedList<>();
        getOpcodes(bs, registers, chr_p, handled, opcodes, branches);
        HashSet<BranchingOpcode> unhandledBranches = new HashSet<>();
        while (branches.peek() != null) {
            BranchingOpcode branch = branches.poll();
            if (handled[branch.getDestination()]) {
                continue;
            }
            if (branch instanceof BranchingLinkingOpcode) {
                int currentOffset = bs.offset();
                bs.seek(branch.getDestination());
                disassemble(bs, String.format("fun_%X", branch.getDestination()), rs, registers, chr_p, handled);
                bs.seek(currentOffset);
            } else if (branch.getDestination() == bs.offset()) {
                getOpcodes(bs, registers, chr_p, handled, opcodes, branches);
            } else if (branch.getDestination() < offset) {
                int currentOffset = bs.offset();
                bs.seek(branch.getDestination());
                disassemble(bs, String.format("branch_%X", branch.getDestination()), rs, registers, chr_p, handled);
                bs.seek(currentOffset);
            } else if (branch.getDestination() > offset && branch.getDestination() < bs.offset()) {

            } else if (!unhandledBranches.contains(branch)){
                unhandledBranches.add(branch);
            } else {
                int currentOffset = bs.offset();
                bs.seek(branch.getDestination());
                disassemble(bs, String.format("branch_%X", branch.getDestination()), rs, registers, chr_p, handled);
                bs.seek(currentOffset);
            }
        }
        rs.put(offset, new Pair(name, opcodes));
    }
}
