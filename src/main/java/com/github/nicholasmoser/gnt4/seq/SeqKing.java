package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.comment.Function;
import com.github.nicholasmoser.gnt4.seq.comment.Functions;
import com.github.nicholasmoser.gnt4.seq.opcodes.ActionID;
import com.github.nicholasmoser.gnt4.seq.opcodes.BinaryData;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchingLinkingOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchingOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.InvalidBytes;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.google.common.collect.Range;
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
   * @param fileName   The name of the seq file from {@link Seqs}
   * @param outputPath The output HTML report file path.
   * @param verbose    If the txt output should be printed to the console.
   * @param permissive If invalid opcodes should be ignored.
   * @throws IOException If an I/O error occurs.
   */
  public static void generateHTML(Path seqPath, String fileName, Path outputPath, boolean verbose, boolean permissive) throws IOException {
    List<Opcode> opcodes = getOpcodes(seqPath, fileName, verbose, permissive);
    SeqKingHtml.generate(fileName, opcodes, outputPath);
  }

  /**
   * Parse the given seq file and create an HTML report at the given output path.
   *
   * @param seqPath    The seq file path.
   * @param fileName   The name of the seq file from {@link Seqs}
   * @param outputPath The output HTML report file path.
   * @param verbose    If the txt output should be printed to the console.
   * @param permissive If invalid opcodes should be ignored.
   * @throws IOException If an I/O error occurs.
   */
  public static void generateTXT(Path seqPath, String fileName, Path outputPath, boolean verbose, boolean permissive) throws IOException {
    List<Opcode> opcodes = getOpcodes(seqPath, fileName, verbose, permissive);
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
   * @param fileName   The name of the seq file from {@link Seqs}
   * @param verbose If the txt output should be printed to the console.
   * @param permissive If invalid opcodes should be ignored.
   * @return The list of opcodes.
   * @throws IOException If an I/O error occurs.
   */
  public static List<Opcode> getOpcodes(Path seqPath, String fileName, boolean verbose, boolean permissive) throws IOException {
    // Known offsets of binary data
    Map<Integer, Integer> binaryOffsetToSize = getBinaryOffsets(fileName);
    List<Range<Integer>> eftRanges = getEftRanges(fileName);
    SeqType seqType = SeqHelper.getSeqType(fileName);

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
      Integer binSize = binaryOffsetToSize.get(offset);
      if (binSize != null) {
        byte[] binaryData = new byte[binSize];
        if (bs.read(binaryData) != binSize) {
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
      // Check if this isEJS seq code. If so, execute it.
      boolean inEftSeq = eftRanges.stream().anyMatch(r -> r.contains(offset));
      if (inEftSeq) {
        // Parse the eft opcode
        Opcode newOpcode;
        int tempOffset = bs.offset();
        try {
          newOpcode = SeqHelper.getEftOpcode(bs, opcodeGroup, opcode);
        } catch (Exception e) {
          if (permissive) {
            // Invalid opcode, mark it as invalid bytes and continue
            bs.seek(tempOffset);
            newOpcode = new InvalidBytes(tempOffset, bs.readNBytes(4));
          } else {
            throw e;
          }
        }
        opcodes.add(newOpcode);
        if (verbose) {
          System.out.println(newOpcode);
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
          if (sectionPart instanceof ActionID actionID) {
            Function act = Functions.getFunctions(fileName).get(actionID.getActionOffset());
            if (act == null) {
              Functions.getFunctions(fileName).put(actionID.getActionOffset(), new Function(String.format("ACT %X", actionID.getActionId()), List.of(actionID.getInfo())));
            } else {
              Functions.getFunctions(fileName).put(actionID.getActionOffset(), new Function(String.format("%s, ACT %X", act.name(), actionID.getActionId()), List.of(actionID.getInfo())));
            }
          }
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
      Opcode newOpcode;
      int tempOffset = bs.offset();
      try {
        newOpcode = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode);
      } catch (Exception e) {
        if (permissive) {
          // Invalid opcode, mark it as invalid bytes and continue
          bs.seek(tempOffset);
          newOpcode = new InvalidBytes(tempOffset, bs.readNBytes(4));
        } else {
          throw e;
        }
      }
      opcodes.add(newOpcode);
      if (verbose) {
        System.out.println(newOpcode);
      }
      // This is a very hacky way of setting the function name on a branching opcode
      if (newOpcode instanceof BranchingOpcode branchingOpcode) {
        Map<Integer, Function> functions = Functions.getFunctions(fileName);
        Function function = functions.get(branchingOpcode.getDestination());
        if (function != null) {
          branchingOpcode.setDestinationFunctionName(function.name());
        } else if (branchingOpcode instanceof BranchingLinkingOpcode) {
          String destinationFunctionName = String.format("fun_%X",branchingOpcode.getDestination());
          branchingOpcode.setDestinationFunctionName(destinationFunctionName);
          functions.put(branchingOpcode.getDestination(), new Function(destinationFunctionName,List.of("")));
        }
      }
      if (bs.offset() == bytes.length) {
        if (verbose) {
          System.out.printf("%s%n", Functions.getFunctions(fileName));
        }
        break; // EOF
      }
    }
    return opcodes;
  }

  private static List<Range<Integer>> getEftRanges(String fileName) {
    List<Range<Integer>> eftOffsetsToSize = new ArrayList<>();
    switch(fileName) {
      case Seqs.SAK_1000 -> eftOffsetsToSize.add(Range.closed(0x12D0, 0x1C7C));
    }
    return eftOffsetsToSize;
  }

  /**
   * Returns known binary offsets and sizes in seq files.
   *
   * @param fileName   The name of the seq file from {@link Seqs}
   * @return The binary offsets and sizes of that seq file.
   */
  private static Map<Integer, Integer> getBinaryOffsets(String fileName) {
    Map<Integer, Integer> binaryOffsetToSize = new HashMap<>();
    switch (fileName) {
      case Seqs.CHARSEL -> {
        binaryOffsetToSize.put(0x950, 0x14);
        binaryOffsetToSize.put(0x9A0, 0xc);
        binaryOffsetToSize.put(0x2370, 0x80);
        binaryOffsetToSize.put(0x23F0, 0x80);
        binaryOffsetToSize.put(0xF3E0, 0x43C);
        binaryOffsetToSize.put(0xFAA0, 0x14B4);
        // GNT4 TE
        //binaryOffsetToSize.put(0x950, 0x14);
        //binaryOffsetToSize.put(0x9A0, 0xc); maybe remove this
        //binaryOffsetToSize.put(0x9E0, 0xc);
        //binaryOffsetToSize.put(0x23B0, 0x100);
        //binaryOffsetToSize.put(0xF420, 0x43C);
        //binaryOffsetToSize.put(0xFAE0, 0x14B4);
      }
      case Seqs.CHARSEL_4 -> {
        binaryOffsetToSize.put(0x950, 0x14);
        binaryOffsetToSize.put(0x9A0, 0xc);
        binaryOffsetToSize.put(0x2370, 0x80);
        binaryOffsetToSize.put(0x23F0, 0x80);
        binaryOffsetToSize.put(0x13FF0, 0x4EC);
        binaryOffsetToSize.put(0x149E0, 0xE88);
      }
      case Seqs.NAR_0000 -> {
        binaryOffsetToSize.put(0x30C4C, 0x14);
        binaryOffsetToSize.put(0x319B0, 0x10);
      }
      case Seqs.KAK_0000 -> {
        binaryOffsetToSize.put(0x32CB0, 0x10);
        binaryOffsetToSize.put(0x344C0, 0x10);
      }
      case Seqs.KIM_0000 -> binaryOffsetToSize.put(0x28BC4, 0x10);
      case Seqs.IRU_0010 -> {
        binaryOffsetToSize.put(0x2C34, 0x200);
        binaryOffsetToSize.put(0x2EE8, 0x18);
        binaryOffsetToSize.put(0x2F08, 0x10);
      }
      case Seqs.SAK_1000 -> {
        binaryOffsetToSize.put(0x460, 0x100);
        binaryOffsetToSize.put(0xFB4, 0x16C);
        binaryOffsetToSize.put(0x1C7C, 0x24);
      }
      case Seqs.PLAYER_00 -> binaryOffsetToSize.put(0x70, 0x10);
      case Seqs.CAMERA_01 -> binaryOffsetToSize.put(0x3E0, 0x10);
      case Seqs.F_CAMERA -> binaryOffsetToSize.put(0x4C0, 0x10);
      case Seqs.CMN_1000 -> binaryOffsetToSize.put(0x1430,
          0x3BF0); // TODO: Is this all REALLY binary data???
      case Seqs.GAME_00 -> {
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
      }
      case Seqs.M_TITLE -> {
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
      case Seqs.STG_001_0000 -> {
        binaryOffsetToSize.put(0x4C0, 0x220);
        binaryOffsetToSize.put(0x8CBC, 0xB0);
        binaryOffsetToSize.put(0x9260, 0x10);
        binaryOffsetToSize.put(0x92B0, 0x10);
        binaryOffsetToSize.put(0x9300, 0x10);
      }
      case Seqs.STG_001_0100 -> binaryOffsetToSize.put(0x20, 0x10);
      case Seqs.M_GAL -> binaryOffsetToSize.put(0x28C0, 0x10);
      case Seqs.M_NFILE -> binaryOffsetToSize.put(0x3AD0, 0xA44);
      case Seqs.M_SNDPLR -> binaryOffsetToSize.put(0x50B0, 0x1CC0);
      case Seqs.M_VIEWER -> binaryOffsetToSize.put(0x7610, 0x64A0);
    }
    return binaryOffsetToSize;
  }
}
