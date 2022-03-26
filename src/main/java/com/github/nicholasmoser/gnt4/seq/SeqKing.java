package com.github.nicholasmoser.gnt4.seq;

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

      // Check if this is the seq extension
      if (SeqSection.isSeqExtension(bs)) {
        List<Opcode> section = SeqSection.handleSeqExtension(bs);
        for (Opcode sectionPart : section) {
          if (verbose) {
            System.out.println(sectionPart);
          }
        }
        opcodes.addAll(section);
        break; // EOF
      }

      // Otherwise, parse the seq opcode
      opcodes.add(SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode));
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
    } else if (path.endsWith("game/player00.seq")) {
      binaryOffsetToSize.put(0x70, 0x10);
    } else if (path.endsWith("game/camera01.seq")) {
      binaryOffsetToSize.put(0x3E0, 0x10);
    } else if (path.endsWith("furu/f_camera.seq")) {
      binaryOffsetToSize.put(0x4C0, 0x10);
    } else if (path.endsWith("chr/cmn/1000.seq")) {
      binaryOffsetToSize.put(0x1430, 0x3BF0); // TODO: Is this all REALLY binary data???
    } else if (path.endsWith("game/game00.seq")) {
      binaryOffsetToSize.put(0x18, 0x18);
      binaryOffsetToSize.put(0x9E04, 0xE6C);
      binaryOffsetToSize.put(0xAC70, 0x80); // array of strings
      binaryOffsetToSize.put(0xACF0, 0x3F0);
      binaryOffsetToSize.put(0xBFD0, 0x10);
      binaryOffsetToSize.put(0xBFF8, 0x10);
      binaryOffsetToSize.put(0xC020, 0x10);
      binaryOffsetToSize.put(0xC048, 0x10);
      binaryOffsetToSize.put(0xC070, 0x10);
      binaryOffsetToSize.put(0xC098, 0x10);
      binaryOffsetToSize.put(0xC710, 0x38); // array of strings
      binaryOffsetToSize.put(0xD7AC, 0x34); // array of offsets
      binaryOffsetToSize.put(0x11A68, 0x34); // array of offsets
      binaryOffsetToSize.put(0x12FDC, 0x34); // array of offsets
      binaryOffsetToSize.put(0x15C58, 0x10);
    } else if (path.endsWith("maki/m_title.seq")) {
      binaryOffsetToSize.put(0x6EC0, 0x30);
      binaryOffsetToSize.put(0x11A00, 0x3A8);
      binaryOffsetToSize.put(0x11DA8, 0x70); // array of offsets
      binaryOffsetToSize.put(0x11E18, 0x338);
      binaryOffsetToSize.put(0x12150, 0x70); // array of offsets
      binaryOffsetToSize.put(0x121C0, 0x17A0);
      binaryOffsetToSize.put(0x1C040, 0xF0); // array of offsets?
      binaryOffsetToSize.put(0x1C130, 0x162E0);
      binaryOffsetToSize.put(0x32410, 0x8C0); // array of offsets?
    }
    return binaryOffsetToSize;
  }

  // TODO: Add notes
  // 0xB30 For Iruka, this is where the Action ID offset is read from the Action ID table
}
