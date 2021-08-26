package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup04;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup15;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup16;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup36;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup37;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup38;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup39;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup61;
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

      // Data also appears to be stored in the seq, in the middle of opcodes...
      // Need a temporary way to handle this while I figure out a better way.
      if (bs.offset() == 0x950) {
        // Temporary hack, not sure what's going on at these offsets
        System.out.printf("Temporary hack at 0x%x\n", bs.offset());
        bs.skip(0x14);
        continue;
      } else if (bs.offset() == 0x9A0) {
        // Temporary hack, not sure what's going on at these offsets
        System.out.printf("Temporary hack at 0x%x\n", bs.offset());
        bs.skip(0xc);
        continue;
      } else if (bs.offset() == 0x2370) {
        // Temporary hack, not sure what's going on at these offsets
        System.out.printf("Temporary hack at 0x%x\n", bs.offset());
        bs.skip(0x100);
        continue;
      }

      switch(opcodeGroup) {
        case 0x00:
          opcodes.add(OpcodeGroup00.parse(bs, opcode));
          break;
        case 0x01:
          opcodes.add(OpcodeGroup01.parse(bs, opcode));
          break;
        case 0x04:
          opcodes.add(OpcodeGroup04.parse(bs, opcode));
          break;
        case 0x09:
          opcodes.add(OpcodeGroup09.parse(bs, opcode));
          break;
        case 0x15:
          opcodes.add(OpcodeGroup15.parse(bs, opcode));
          break;
        case 0x16:
          opcodes.add(OpcodeGroup16.parse(bs, opcode));
          break;
        case 0x36:
          opcodes.add(OpcodeGroup36.parse(bs, opcode));
          break;
        case 0x37:
          opcodes.add(OpcodeGroup37.parse(bs, opcode));
          break;
        case 0x38:
          opcodes.add(OpcodeGroup38.parse(bs, opcode));
          break;
        case 0x39:
          opcodes.add(OpcodeGroup39.parse(bs, opcode));
          break;
        case 0x3a:
          opcodes.add(OpcodeGroup3A.parse(bs, opcode));
          break;
        case 0x3b:
          opcodes.add(OpcodeGroup3B.parse(bs, opcode));
          break;
        case 0x3c:
          opcodes.add(OpcodeGroup3C.parse(bs, opcode));
          break;
        case 0x44:
          opcodes.add(OpcodeGroup44.parse(bs, opcode));
          break;
        case 0x61:
          opcodes.add(OpcodeGroup61.parse(bs, opcode));
          break;
        default:
          throw new IllegalStateException(String.format("Unknown opcode group: %02X", opcodeGroup));
      }
      System.out.println(opcodes.get(opcodes.size() - 1));
    }
  }
}
