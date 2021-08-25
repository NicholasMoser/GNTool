package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup37;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup39;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3A;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup01;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup44;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3C;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SeqKing {
  public static void generate(Path seqPath, Path outputPath) throws IOException {
    byte[] bytes = Files.readAllBytes(seqPath);
    ByteStream bs = new ByteStream(bytes);

    // Process the seq header
    byte[] header = new byte[16];
    if (bs.read(header) != 16) {
      throw new IOException("Failed to read header, not enough bytes.");
    }

    // Process the opcodes
    List<Opcode> opcodes = new ArrayList<>();
    boolean done = false;
    while (!done) {
      bs.mark();
      byte opcodeGroup = (byte) bs.read();
      byte opcode = (byte) bs.read();
      bs.reset();
      switch(opcodeGroup) {
        case 0x00:
          opcodes.add(OpcodeGroup00.parse(bs, opcode));
          break;
        case 0x01:
          opcodes.add(OpcodeGroup01.parse(bs, opcode));
          break;
        case 0x09:
          opcodes.add(OpcodeGroup09.parse(bs, opcode));
          break;
        case 0x37:
          opcodes.add(OpcodeGroup37.parse(bs, opcode));
          break;
        case 0x39:
          opcodes.add(OpcodeGroup39.parse(bs, opcode));
          break;
        case 0x3a:
          opcodes.add(OpcodeGroup3A.parse(bs, opcode));
          break;
        case 0x3c:
          opcodes.add(OpcodeGroup3C.parse(bs, opcode));
          break;
        case 0x44:
          opcodes.add(OpcodeGroup44.parse(bs, opcode));
          break;
        default:
          throw new IllegalStateException(String.format("Unknown opcode group: %02X", opcodeGroup));
      }
      System.out.println(opcodes.get(opcodes.size() - 1));
    }
  }
}
