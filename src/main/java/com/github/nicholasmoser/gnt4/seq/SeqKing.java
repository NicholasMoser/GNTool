package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup02;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup03;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup04;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup06;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0F;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup11;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup12;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup15;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup16;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup34;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup36;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup37;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup38;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup39;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup42;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup46;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup61;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.groups.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup01;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup44;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup40;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeqKing {

  public static void generate(Path seqPath, Path outputPath) throws IOException {
    // Known offsets of binary data
    Map<Integer, Integer> binaryOffsetToSize = getBinaryOffsets(seqPath);

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

      // Check if this is binary data. If so, skip it.
      int offset = bs.offset();
      Integer size = binaryOffsetToSize.get(offset);
      if (size != null) {
        byte[] binaryData = new byte[size];
        if (bs.read(binaryData) != size) {
          throw new IOException("Failed to read binary data at offset " + bs.offset());
        }
        Opcode binaryOpcode = new BinaryData(offset, binaryData);
        opcodes.add(binaryOpcode);
        System.out.println(binaryOpcode);
        if (bs.offset() == bytes.length) {
          break; // EOF
        }
        continue;
      }

      switch (opcodeGroup) {
        case 0x00 -> opcodes.add(OpcodeGroup00.parse(bs, opcode));
        case 0x01 -> opcodes.add(OpcodeGroup01.parse(bs, opcode));
        case 0x02 -> opcodes.add(OpcodeGroup02.parse(bs, opcode));
        case 0x03 -> opcodes.add(OpcodeGroup03.parse(bs, opcode));
        case 0x04 -> opcodes.add(OpcodeGroup04.parse(bs, opcode));
        case 0x06 -> opcodes.add(OpcodeGroup06.parse(bs, opcode));
        case 0x09 -> opcodes.add(OpcodeGroup09.parse(bs, opcode));
        case 0x0F -> opcodes.add(OpcodeGroup0F.parse(bs, opcode));
        case 0x11 -> opcodes.add(OpcodeGroup11.parse(bs, opcode));
        case 0x12 -> opcodes.add(OpcodeGroup12.parse(bs, opcode));
        case 0x15 -> opcodes.add(OpcodeGroup15.parse(bs, opcode));
        case 0x16 -> opcodes.add(OpcodeGroup16.parse(bs, opcode));
        case 0x1D -> opcodes.add(OpcodeGroup1D.parse(bs, opcode));
        case 0x1E -> opcodes.add(OpcodeGroup1E.parse(bs, opcode));
        case 0x34 -> opcodes.add(OpcodeGroup34.parse(bs, opcode));
        case 0x36 -> opcodes.add(OpcodeGroup36.parse(bs, opcode));
        case 0x37 -> opcodes.add(OpcodeGroup37.parse(bs, opcode));
        case 0x38 -> opcodes.add(OpcodeGroup38.parse(bs, opcode));
        case 0x39 -> opcodes.add(OpcodeGroup39.parse(bs, opcode));
        case 0x3A -> opcodes.add(OpcodeGroup3A.parse(bs, opcode));
        case 0x3B -> opcodes.add(OpcodeGroup3B.parse(bs, opcode));
        case 0x3C -> opcodes.add(OpcodeGroup3C.parse(bs, opcode));
        case 0x3D -> opcodes.add(OpcodeGroup3D.parse(bs, opcode));
        case 0x3E -> opcodes.add(OpcodeGroup3E.parse(bs, opcode));
        case 0x40 -> opcodes.add(OpcodeGroup40.parse(bs, opcode));
        case 0x42 -> opcodes.add(OpcodeGroup42.parse(bs, opcode));
        case 0x44 -> opcodes.add(OpcodeGroup44.parse(bs, opcode));
        case 0x46 -> opcodes.add(OpcodeGroup46.parse(bs, opcode));
        case 0x61 -> opcodes.add(OpcodeGroup61.parse(bs, opcode));
        default -> throw new IllegalStateException(
            String.format("Unknown opcode group: %02X", opcodeGroup));
      }
      System.out.println(opcodes.get(opcodes.size() - 1));
      if (bs.offset() == bytes.length) {
        break; // EOF
      }
    }
  }

  /**
   * Returns known binary offsets and sizes in seq files.
   *
   * @param seqPath The path to the seq file.
   * @return The binary offsets and sizes of that seq file.
   */
  private static Map<Integer, Integer> getBinaryOffsets(Path seqPath) {
    Map<Integer, Integer> binaryOffsetToSize = new HashMap<>();
    String fileName = seqPath.getFileName().toString();
    switch (fileName) {
      case "char_sel.seq":
        binaryOffsetToSize.put(0x950, 0x14);
        binaryOffsetToSize.put(0x9A0, 0xc);
        binaryOffsetToSize.put(0x2370, 0x100);
        binaryOffsetToSize.put(0xF3E0, 0x43C);
        binaryOffsetToSize.put(0xFAA0, 0x14B4);
        break;
      default:
        break;
    }
    return binaryOffsetToSize;
  }
}
