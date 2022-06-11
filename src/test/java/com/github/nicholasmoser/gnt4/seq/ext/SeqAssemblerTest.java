package com.github.nicholasmoser.gnt4.seq.ext;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class SeqAssemblerTest {

    @Test
    void testSeqAssemblyOneWay() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/branch_action.seqa"));
        byte [] reference = Files.readAllBytes(Path.of("src/test/resources/gnt4/seq/ext/naruto5B.seq"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            baos.write(opcode.getBytes(0x1DA84, opcodes.getValue()));
        }
        byte [] bytes = baos.toByteArray();
        assertArrayEquals(reference, bytes);
    }

    @Test
    void testSeqAssemblyTwoWay() throws IOException {
        String assembly = Files.readString(Path.of("src/test/resources/gnt4/seq/ext/branch_action.seqa"));
        Pair<List <Opcode>, Integer> opcodes = SeqAssembler.assembleLines(assembly.split("\n"), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Opcode opcode : opcodes.getKey()) {
            baos.write(opcode.getBytes(0x1000, opcodes.getValue()));
        }
        List<Opcode> assembledOpcodes = new LinkedList<>();
        byte [] bytes = baos.toByteArray();
        ByteStream bs = new ByteStream(bytes);
        while (bs.bytesAreLeft()) {
            assembledOpcodes.add(SeqHelper.getSeqOpcode(bs,bs.peekBytes(2)[0],bs.peekBytes(2)[1]));
        }
        Pair<String, String> reassembled = SeqEditor.getOpcodesStrings(assembledOpcodes, 0x1000, bytes.length);
        assertEquals(assembly, reassembled.getValue());
    }

}
