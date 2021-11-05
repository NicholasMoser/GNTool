package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup00;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup01;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup02;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup03;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup04;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup05;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup06;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup08;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup09;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup0F;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup10;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup11;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup12;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup13;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup14;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup15;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup16;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup1E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup20;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup21;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup22;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup24;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup26;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup27;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup28;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup2A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup2B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup31;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup34;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup36;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup37;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup38;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup39;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup3E;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup40;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup41;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup42;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup43;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup44;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup46;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup47;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup48;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup49;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4A;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4C;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup4D;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup50;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup55;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup56;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup5B;
import com.github.nicholasmoser.gnt4.seq.groups.OpcodeGroup61;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeqKing {

  /**
   * Parse the given seq file and create an HTML report at the given output path.
   *
   * @param seqPath    The seq file path.
   * @param outputPath The output HTML report file path.
   * @param verbose    If the txt output should be printed to the console.
   * @throws IOException If an I/O error occurs.
   */
  public static void generateHTML(Path seqPath, Path outputPath, boolean verbose) throws IOException {
    List<Opcode> opcodes = getOpcodes(seqPath, verbose);
    SeqKingHtml.generate(seqPath.getFileName().toString(), opcodes, outputPath);
  }

  /**
   * Parse the given seq file and create an HTML report at the given output path.
   *
   * @param seqPath    The seq file path.
   * @param outputPath The output HTML report file path.
   * @param verbose    If the txt output should be printed to the console.
   * @throws IOException If an I/O error occurs.
   */
  public static void generateTXT(Path seqPath, Path outputPath, boolean verbose) throws IOException {
    List<Opcode> opcodes = getOpcodes(seqPath, verbose);
    try(OutputStream os = Files.newOutputStream(outputPath)) {
      for (Opcode opcode : opcodes) {
        os.write(opcode.toString().getBytes(StandardCharsets.UTF_8));
        os.write('\n');
      }
    }
  }

  /**
   * Get the list of opcodes for the given seq file.
   *
   * @param seqPath The path to the seq file.
   * @param verbose If the txt output should be printed to the console.
   * @return The list of opcodes.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> getOpcodes(Path seqPath, boolean verbose) throws IOException {
    // Known offsets of binary data
    Map<Integer, Integer> binaryOffsetToSize = getBinaryOffsets(seqPath);
    SeqType seqType = SeqHelper.getSeqType(seqPath);

    // A set of the unique binaries that have been parsed, used to fail when multiple instances of
    // a unique binary are found, implying an error in the parsing.
    Set<String> uniqueBinaries = new HashSet<>();

    byte[] bytes = Files.readAllBytes(seqPath);
    ByteStream bs = new ByteStream(bytes);

    // Process the seq header
    byte[] header = new byte[16];
    if (bs.read(header) != 16) {
      throw new IOException("Failed to read header, not enough bytes.");
    }

    // Process the opcodes
    List<Opcode> opcodes = new ArrayList<>();
    while (true) {
      bs.mark();
      byte opcodeGroup = (byte) bs.read();
      byte opcode = (byte) bs.read();
      bs.reset();

      // Check if this is manually defined binary data. If so, skip it.
      int offset = bs.offset();
      Integer size = binaryOffsetToSize.get(offset);
      if (size != null) {
        byte[] binaryData = new byte[size];
        if (bs.read(binaryData) != size) {
          throw new IOException("Failed to read binary data at offset " + bs.offset());
        }
        Opcode binaryOpcode = new BinaryData(offset, binaryData);
        opcodes.add(binaryOpcode);
        if (verbose) {
          System.out.println(binaryOpcode);
        }
        if (bs.offset() == bytes.length) {
          break; // EOF
        }
        continue;
      }

      // Check for known binary data in the seq
      List<Opcode> binaries = SeqHelper.getBinaries(bs, opcodes, seqType, uniqueBinaries);
      if (!binaries.isEmpty()) {
        for (Opcode binary : binaries) {
          if (verbose) {
            System.out.println(binary);
          }
        }
        opcodes.addAll(binaries);
        continue;
      }

      // Check if this is an seq section
      if (SeqSection.isSeqSectionTitle(bs)) {
        List<Opcode> section = SeqSection.handleSeqSection(bs);
        for (Opcode sectionPart : section) {
          if (verbose) {
            System.out.println(sectionPart);
          }
        }
        opcodes.addAll(section);
        continue;
      }

      // Otherwise, parse the seq opcode
      switch (opcodeGroup) {
        case 0x00 -> opcodes.add(OpcodeGroup00.parse(bs, opcode));
        case 0x01 -> opcodes.add(OpcodeGroup01.parse(bs, opcode));
        case 0x02 -> opcodes.add(OpcodeGroup02.parse(bs, opcode));
        case 0x03 -> opcodes.add(OpcodeGroup03.parse(bs, opcode));
        case 0x04 -> opcodes.add(OpcodeGroup04.parse(bs, opcode));
        case 0x05 -> opcodes.add(OpcodeGroup05.parse(bs, opcode));
        case 0x06 -> opcodes.add(OpcodeGroup06.parse(bs, opcode));
        case 0x08 -> opcodes.add(OpcodeGroup08.parse(bs, opcode));
        case 0x09 -> opcodes.add(OpcodeGroup09.parse(bs, opcode));
        case 0x0B -> opcodes.add(OpcodeGroup0B.parse(bs, opcode));
        case 0x0C -> opcodes.add(OpcodeGroup0C.parse(bs, opcode));
        case 0x0F -> opcodes.add(OpcodeGroup0F.parse(bs, opcode));
        case 0x10 -> opcodes.add(OpcodeGroup10.parse(bs, opcode));
        case 0x11 -> opcodes.add(OpcodeGroup11.parse(bs, opcode));
        case 0x12 -> opcodes.add(OpcodeGroup12.parse(bs, opcode));
        case 0x13 -> opcodes.add(OpcodeGroup13.parse(bs, opcode));
        case 0x14 -> opcodes.add(OpcodeGroup14.parse(bs, opcode));
        case 0x15 -> opcodes.add(OpcodeGroup15.parse(bs, opcode));
        case 0x16 -> opcodes.add(OpcodeGroup16.parse(bs, opcode));
        case 0x1A -> opcodes.add(OpcodeGroup1A.parse(bs, opcode));
        case 0x1C -> opcodes.add(OpcodeGroup1C.parse(bs, opcode));
        case 0x1D -> opcodes.add(OpcodeGroup1D.parse(bs, opcode));
        case 0x1E -> opcodes.add(OpcodeGroup1E.parse(bs, opcode));
        case 0x20 -> opcodes.add(OpcodeGroup20.parse(bs, opcode));
        case 0x21 -> opcodes.add(OpcodeGroup21.parse(bs, opcode));
        case 0x22 -> opcodes.add(OpcodeGroup22.parse(bs, opcode));
        case 0x24 -> opcodes.add(OpcodeGroup24.parse(bs, opcode));
        case 0x26 -> opcodes.add(OpcodeGroup26.parse(bs, opcode));
        case 0x27 -> opcodes.add(OpcodeGroup27.parse(bs, opcode));
        case 0x28 -> opcodes.add(OpcodeGroup28.parse(bs, opcode));
        case 0x2A -> opcodes.add(OpcodeGroup2A.parse(bs, opcode));
        case 0x2B -> opcodes.add(OpcodeGroup2B.parse(bs, opcode));
        case 0x31 -> opcodes.add(OpcodeGroup31.parse(bs, opcode));
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
        case 0x41 -> opcodes.add(OpcodeGroup41.parse(bs, opcode));
        case 0x42 -> opcodes.add(OpcodeGroup42.parse(bs, opcode));
        case 0x43 -> opcodes.add(OpcodeGroup43.parse(bs, opcode));
        case 0x44 -> opcodes.add(OpcodeGroup44.parse(bs, opcode));
        case 0x46 -> opcodes.add(OpcodeGroup46.parse(bs, opcode));
        case 0x47 -> opcodes.add(OpcodeGroup47.parse(bs, opcode));
        case 0x48 -> opcodes.add(OpcodeGroup48.parse(bs, opcode));
        case 0x49 -> opcodes.add(OpcodeGroup49.parse(bs, opcode));
        case 0x4A -> opcodes.add(OpcodeGroup4A.parse(bs, opcode));
        case 0x4B -> opcodes.add(OpcodeGroup4B.parse(bs, opcode));
        case 0x4C -> opcodes.add(OpcodeGroup4C.parse(bs, opcode));
        case 0x4D -> opcodes.add(OpcodeGroup4D.parse(bs, opcode));
        case 0x50 -> opcodes.add(OpcodeGroup50.parse(bs, opcode));
        case 0x55 -> opcodes.add(OpcodeGroup55.parse(bs, opcode));
        case 0x56 -> opcodes.add(OpcodeGroup56.parse(bs, opcode));
        case 0x5B -> opcodes.add(OpcodeGroup5B.parse(bs, opcode));
        case 0x61 -> opcodes.add(OpcodeGroup61.parse(bs, opcode));
        case (byte) 0xCC -> opcodes.add(SeqHelper.getNullBytes(bs)); // SCON4-specific
        default -> throw new IllegalStateException(
            String.format("Unknown opcode group: %02X", opcodeGroup));
      }
      if (verbose) {
        System.out.println(opcodes.get(opcodes.size() - 1));
      }
      if (bs.offset() == bytes.length) {
        break; // EOF
      }
    }
    return opcodes;
  }

  /**
   * Returns known binary offsets and sizes in seq files.
   *
   * @param seqPath The path to the seq file.
   * @return The binary offsets and sizes of that seq file.
   */
  private static Map<Integer, Integer> getBinaryOffsets(Path seqPath) {
    Map<Integer, Integer> binaryOffsetToSize = new HashMap<>();
    String path = seqPath.toString().replace('\\', '/');
    if (path.endsWith("char_sel.seq")) {
      binaryOffsetToSize.put(0x950, 0x14);
      binaryOffsetToSize.put(0x9A0, 0xc);
      binaryOffsetToSize.put(0x2370, 0x100);
      binaryOffsetToSize.put(0xF3E0, 0x43C);
      binaryOffsetToSize.put(0xFAA0, 0x14B4);
    } else if (path.endsWith("chr/nar/0000.seq")) {
      binaryOffsetToSize.put(0x30C4C, 0x14);
      binaryOffsetToSize.put(0x319B0, 0x10);
    } else if (path.endsWith("chr/kak/0000.seq")) {
      binaryOffsetToSize.put(0x32CB0, 0x10);
      binaryOffsetToSize.put(0x344C0, 0x10);
    } else if (path.endsWith("chr/kim/0000.seq")) {
      binaryOffsetToSize.put(0x28BC4, 0x10);
    } else if (path.endsWith("chr/iru/0010.seq")) {
      binaryOffsetToSize.put(0x2C34, 0x200);
      binaryOffsetToSize.put(0x2EE8, 0x18);
      binaryOffsetToSize.put(0x2F08, 0x10);
    }
    return binaryOffsetToSize;
  }

  // TODO: Add notes
  // 0xB30 For Iruka, this is where the Action ID offset is read from the Action ID table
}
