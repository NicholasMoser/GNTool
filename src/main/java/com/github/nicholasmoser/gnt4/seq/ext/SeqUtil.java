package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.dest.Destination;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTable;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchTableLink;
import com.github.nicholasmoser.gnt4.seq.opcodes.BranchingOpcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

public class SeqUtil {

  /**
   * Get the opcodes, and bytes in human-readable text form from a given opcode list.
   *
   * @param opcodes The list of opcodes to create assembly from
   * @return Pair of the human-readable text of the bytes and opcodes.
   */
  public static Pair<String, String> getOpcodesStrings(List<Opcode> opcodes) {
    Map<Integer, String> labelMap = new HashMap<>();
    StringBuilder newBytesText = new StringBuilder();
    StringBuilder opcodesText = new StringBuilder();
    for (Opcode opcode : opcodes) {
      String label = labelMap.get(opcode.getOffset());
      if (label != null) {
        opcodesText.append(String.format("%s:\n", label));
      }
      newBytesText.append(String.format("%s\n",
          ByteUtils.bytesToHexStringWords(opcode.getBytes())));//opcode.getBytes(position, size))));
      opcodesText.append(String.format("%s\n", opcode.toAssembly()));//toAssembly(position)));
    }
    return new Pair<>(newBytesText.toString(), opcodesText.toString());
  }
}
