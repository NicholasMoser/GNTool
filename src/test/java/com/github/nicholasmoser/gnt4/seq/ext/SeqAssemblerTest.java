package com.github.nicholasmoser.gnt4.seq.ext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.dest.AbsoluteDestination;
import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import com.github.nicholasmoser.gnt4.seq.dest.LabelDestination;
import com.github.nicholasmoser.gnt4.seq.dest.RelativeDestination;
import com.github.nicholasmoser.gnt4.seq.opcodes.Branch;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.awt.Label;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SeqAssemblerTest {

    void assertBytesEquals(byte[] expected, byte[] actual) {
        int offset = Arrays.mismatch(expected, actual);
        if (offset == -1) {
            return;
        }
        fail(String.format("Mismatch at offset 0x%X is 0x%X but should be 0x%X\n", offset, actual[offset], expected[offset]));
    }

    @Test
    void testRelativeBranching() throws IOException {
        int startingOffset = 0x100;
        String asm = """
            b +0x10
            blr
            blr
            blr
            b -0x8
            b +0x4
            b -0x4
            """;
        String[] lines = asm.lines().toArray(String[]::new);
        Pair<List<Opcode>, Integer> pair = SeqAssembler.assembleLines(lines, null, startingOffset);
        List<Opcode> opcodes = pair.getKey();
        Integer size = pair.getValue();
        assertThat(opcodes.size()).isEqualTo(7);
        assertThat(size).isEqualTo(0x2C);

        assertThat(opcodes.get(0)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(1)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(2)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(3)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(4)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(5)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(6)).isInstanceOf(Branch.class);

        Branch op1 = (Branch) opcodes.get(0);
        BranchLinkReturn op2 = (BranchLinkReturn) opcodes.get(1);
        BranchLinkReturn op3 = (BranchLinkReturn) opcodes.get(2);
        BranchLinkReturn op4 = (BranchLinkReturn) opcodes.get(3);
        Branch op5 = (Branch) opcodes.get(4);
        Branch op6 = (Branch) opcodes.get(5);
        Branch op7 = (Branch) opcodes.get(6);

        assertThat(op1.getOffset()).isEqualTo(startingOffset);
        assertThat(op2.getOffset()).isEqualTo(0x108);
        assertThat(op3.getOffset()).isEqualTo(0x10C);
        assertThat(op4.getOffset()).isEqualTo(0x110);
        assertThat(op5.getOffset()).isEqualTo(0x114);
        assertThat(op6.getOffset()).isEqualTo(0x11C);
        assertThat(op7.getOffset()).isEqualTo(0x124);

        Destination dest1 = op1.getDestination();
        assertThat(dest1).isInstanceOf(RelativeDestination.class);
        assertThat(dest1.toString()).isEqualTo("0x110");
        assertThat(dest1.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x01, 0x10});
        assertThat(dest1.offset()).isEqualTo(0x110);

        Destination dest2 = op5.getDestination();
        assertThat(dest2).isInstanceOf(RelativeDestination.class);
        assertThat(dest2.toString()).isEqualTo("0x10C");
        assertThat(dest2.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x01, 0x0C});
        assertThat(dest2.offset()).isEqualTo(0x10C);

        Destination dest3 = op6.getDestination();
        assertThat(dest3).isInstanceOf(RelativeDestination.class);
        assertThat(dest3.toString()).isEqualTo("0x120");
        assertThat(dest3.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x01, 0x20});
        assertThat(dest3.offset()).isEqualTo(0x120);

        Destination dest4 = op7.getDestination();
        assertThat(dest4).isInstanceOf(RelativeDestination.class);
        assertThat(dest4.toString()).isEqualTo("0x120");
        assertThat(dest4.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x01, 0x20});
        assertThat(dest4.offset()).isEqualTo(0x120);
    }

    @Test
    void testAbsoluteBranching() throws IOException {
        int startingOffset = 0x0;
        String asm = """
            b 0x2C
            blr
            blr
            blr
            b 0x0
            b 0x0
            b 0x10
            """;
        String[] lines = asm.lines().toArray(String[]::new);
        Pair<List<Opcode>, Integer> pair = SeqAssembler.assembleLines(lines, null, startingOffset);
        List<Opcode> opcodes = pair.getKey();
        Integer size = pair.getValue();
        assertThat(opcodes.size()).isEqualTo(7);
        assertThat(size).isEqualTo(0x2C);

        assertThat(opcodes.get(0)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(1)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(2)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(3)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(4)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(5)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(6)).isInstanceOf(Branch.class);

        Branch op1 = (Branch) opcodes.get(0);
        BranchLinkReturn op2 = (BranchLinkReturn) opcodes.get(1);
        BranchLinkReturn op3 = (BranchLinkReturn) opcodes.get(2);
        BranchLinkReturn op4 = (BranchLinkReturn) opcodes.get(3);
        Branch op5 = (Branch) opcodes.get(4);
        Branch op6 = (Branch) opcodes.get(5);
        Branch op7 = (Branch) opcodes.get(6);

        assertThat(op1.getOffset()).isEqualTo(startingOffset);
        assertThat(op2.getOffset()).isEqualTo(0x8);
        assertThat(op3.getOffset()).isEqualTo(0xC);
        assertThat(op4.getOffset()).isEqualTo(0x10);
        assertThat(op5.getOffset()).isEqualTo(0x14);
        assertThat(op6.getOffset()).isEqualTo(0x1C);
        assertThat(op7.getOffset()).isEqualTo(0x24);

        Destination dest1 = op1.getDestination();
        assertThat(dest1).isInstanceOf(AbsoluteDestination.class);
        assertThat(dest1.toString()).isEqualTo("0x2C");
        assertThat(dest1.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x2C});
        assertThat(dest1.offset()).isEqualTo(0x2C);

        Destination dest2 = op5.getDestination();
        assertThat(dest2).isInstanceOf(AbsoluteDestination.class);
        assertThat(dest2.toString()).isEqualTo("0x0");
        assertThat(dest2.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x00});
        assertThat(dest2.offset()).isEqualTo(0x0);

        Destination dest3 = op6.getDestination();
        assertThat(dest3).isInstanceOf(AbsoluteDestination.class);
        assertThat(dest3.toString()).isEqualTo("0x0");
        assertThat(dest3.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x00});
        assertThat(dest3.offset()).isEqualTo(0x0);

        Destination dest4 = op7.getDestination();
        assertThat(dest4).isInstanceOf(AbsoluteDestination.class);
        assertThat(dest4.toString()).isEqualTo("0x10");
        assertThat(dest4.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x10});
        assertThat(dest4.offset()).isEqualTo(0x10);
    }

    @Test
    void testLabelBranching() throws IOException {
        Path seqPath = Paths.get(Seqs.ANK_0000);
        String asm = """
            start:
            b end
            blr
            blr
            mid:
            blr
            b start
            b CallAction
            b mid
            end:
            """;
        String[] lines = asm.lines().toArray(String[]::new);
        Pair<List<Opcode>, Integer> pair = SeqAssembler.assembleLines(lines, seqPath);
        List<Opcode> opcodes = pair.getKey();
        Integer size = pair.getValue();
        assertThat(opcodes.size()).isEqualTo(7);
        assertThat(size).isEqualTo(0x2C);

        assertThat(opcodes.get(0)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(1)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(2)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(3)).isInstanceOf(BranchLinkReturn.class);
        assertThat(opcodes.get(4)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(5)).isInstanceOf(Branch.class);
        assertThat(opcodes.get(6)).isInstanceOf(Branch.class);

        Branch op1 = (Branch) opcodes.get(0);
        BranchLinkReturn op2 = (BranchLinkReturn) opcodes.get(1);
        BranchLinkReturn op3 = (BranchLinkReturn) opcodes.get(2);
        BranchLinkReturn op4 = (BranchLinkReturn) opcodes.get(3);
        Branch op5 = (Branch) opcodes.get(4);
        Branch op6 = (Branch) opcodes.get(5);
        Branch op7 = (Branch) opcodes.get(6);

        assertThat(op1.getOffset()).isEqualTo(0x0);
        assertThat(op2.getOffset()).isEqualTo(0x8);
        assertThat(op3.getOffset()).isEqualTo(0xC);
        assertThat(op4.getOffset()).isEqualTo(0x10);
        assertThat(op5.getOffset()).isEqualTo(0x14);
        assertThat(op6.getOffset()).isEqualTo(0x1C);
        assertThat(op7.getOffset()).isEqualTo(0x24);

        Destination dest1 = op1.getDestination();
        assertThat(dest1).isInstanceOf(LabelDestination.class);
        assertThat(dest1.toString()).isEqualTo("0x2C");
        assertThat(dest1.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x2C});
        assertThat(dest1.offset()).isEqualTo(0x2C);

        Destination dest2 = op5.getDestination();
        assertThat(dest2).isInstanceOf(LabelDestination.class);
        assertThat(dest2.toString()).isEqualTo("0x0");
        assertThat(dest2.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x00});
        assertThat(dest2.offset()).isEqualTo(0x0);

        Destination dest3 = op6.getDestination();
        assertThat(dest3).isInstanceOf(LabelDestination.class);
        assertThat(dest3.toString()).isEqualTo("0xA1C");
        assertThat(dest3.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x0A, 0x1C});
        assertThat(dest3.offset()).isEqualTo(0xA1C);

        Destination dest4 = op7.getDestination();
        assertThat(dest4).isInstanceOf(LabelDestination.class);
        assertThat(dest4.toString()).isEqualTo("0x10");
        assertThat(dest4.bytes()).isEqualTo(new byte[] { 0x00, 0x00, 0x00, 0x10});
        assertThat(dest4.offset()).isEqualTo(0x10);
    }

    @Test
    void testSeqAssemblyOneWay() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/branch_action.seqa"));
        byte [] reference = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/naruto5B.seq"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            byte[] opcodeCodes = opcode.getBytes();
            baos.write(opcodeCodes);
        }
        byte [] bytes = baos.toByteArray();
        assertBytesEquals(reference, bytes);
    }

    @Test
    void testSeqAssemblyTwoWay() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/branch_action.seqa"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            baos.write(opcode.getBytes());
        }
        List<Opcode> assembledOpcodes = new LinkedList<>();
        byte [] bytes = baos.toByteArray();
        ByteStream bs = new ByteStream(bytes);
        while (bs.bytesAreLeft()) {
            assembledOpcodes.add(SeqHelper.getSeqOpcode(bs,bs.peekBytes(2)[0],bs.peekBytes(2)[1]));
        }
        Pair<String, String> reassembled = SeqUtil.getOpcodesStrings(assembledOpcodes);
        assertEquals(assembly, reassembled.getValue());
    }

    @Test
    void testSeqAssemblyBranchTable() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/naruto0x41D4.seqa"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            baos.write(opcode.getBytes());
        }
        byte [] bytes = baos.toByteArray();
        byte [] reference = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/naruto0x41D4.seq"));
        assertArrayEquals(bytes, reference);
    }

    @Test
    @Disabled("Does not work yet")
    void testSeqDisassemblyBranchTable() throws IOException {
        String reference = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/naruto0x41D4.seqa"));
        byte [] assembled = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/naruto0x41D4.seq"));
        List<Opcode> assembledOpcodes = new LinkedList<>();
        ByteStream bs = new ByteStream(assembled);
        while (bs.bytesAreLeft()) {
            assembledOpcodes.add(SeqHelper.getSeqOpcode(bs,bs.peekBytes(2)[0],bs.peekBytes(2)[1]));
        }
        Pair<String, String> reassembled = SeqUtil.getOpcodesStrings(assembledOpcodes);
        assertEquals(reference, reassembled.getValue());
    }

    @Test
    @Disabled("Does not work yet")
    void testSeqAssemblyRecording() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/recording.seqa"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            baos.write(opcode.getBytes());
        }
        byte [] bytes = baos.toByteArray();
        byte [] reference = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/recording.seq"));
        assertArrayEquals(bytes, reference);
    }

    @Test
    void testSeqDisassemblyRecording() throws IOException {
        String reference = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/recording.seqa"));
        byte [] assembled = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/recording.seq"));
        List<Opcode> assembledOpcodes = new LinkedList<>();
        ByteStream bs = new ByteStream(assembled);
        while (bs.bytesAreLeft()) {
            assembledOpcodes.add(SeqHelper.getSeqOpcode(bs,bs.peekBytes(2)[0],bs.peekBytes(2)[1]));
        }
        Pair<String, String> reassembled = SeqUtil.getOpcodesStrings(assembledOpcodes);
        assertEquals(reference, reassembled.getValue());
    }
}
