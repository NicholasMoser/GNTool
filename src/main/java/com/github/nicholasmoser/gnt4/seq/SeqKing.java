package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup01;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup02;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup03;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup04;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup05;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup06;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup07;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup08;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0F;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.io.CountingInputStream;
import java.io.ByteArrayInputStream;
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
      byte opcodeGroup = (byte) bs.read();
      switch(opcodeGroup) {
        case 0x00:
          opcodes.add(OpcodeGroup00.parse(bs));
          break;
        case 0x01:
          opcodes.add(OpcodeGroup01.parse(bs));
          break;
        case 0x02:
          opcodes.add(OpcodeGroup02.parse(bs));
          break;
        case 0x03:
          opcodes.add(OpcodeGroup03.parse(bs));
          break;
        case 0x04:
          opcodes.add(OpcodeGroup04.parse(bs));
          break;
        case 0x05:
          opcodes.add(OpcodeGroup05.parse(bs));
          break;
        case 0x06:
          opcodes.add(OpcodeGroup06.parse(bs));
          break;
        case 0x07:
          opcodes.add(OpcodeGroup07.parse(bs));
          break;
        case 0x08:
          opcodes.add(OpcodeGroup08.parse(bs));
          break;
        case 0x09:
          opcodes.add(OpcodeGroup09.parse(bs));
          break;
        case 0x0A:
          opcodes.add(OpcodeGroup0A.parse(bs));
          break;
        case 0x0B:
          opcodes.add(OpcodeGroup0B.parse(bs));
          break;
        case 0x0C:
          opcodes.add(OpcodeGroup0C.parse(bs));
          break;
        case 0x0D:
          opcodes.add(OpcodeGroup0D.parse(bs));
          break;
        case 0x0E:
          opcodes.add(OpcodeGroup0E.parse(bs));
          break;
        case 0x0F:
          opcodes.add(OpcodeGroup0F.parse(bs));
          break;
        default:
          throw new IllegalStateException(String.format("Unknown opcode group: %02X", opcodeGroup));
      }
      System.out.println(opcodes.get(opcodes.size() - 1));
    }
  }
}
