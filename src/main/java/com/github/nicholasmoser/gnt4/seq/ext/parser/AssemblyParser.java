package com.github.nicholasmoser.gnt4.seq.ext.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssemblyParser {

  /**
   * Parses a bytes String. Will also parse opcodes out of the SEQ HTML report, ignoring comments
   * newlines, and labels.
   *
   * @return An array of the opcodes.
   * @throws IOException If there is an I/O related exception.
   */
  public static String parseBytesField(String text) throws IOException {
    if (text.contains(" | ")) {
      // Text is copied from SEQ HTML report, try and parse it
      StringBuilder bytes = new StringBuilder();
      for (String line : text.split("\n")) {
        if (line.startsWith("//") || line.isBlank() || line.endsWith(":")) {
          // Ignore comments, newlines, and labels
          continue;
        }
        String opcodeAndBytes = line.split(" \\| ")[1];
        // Find end of opcode and beginning of bytes
        String firstBytes = null;
        for (String part : opcodeAndBytes.split(" ")) {
          if (part.length() == 8 && isHex(part)) {
            firstBytes = part;
            break;
          }
        }
        if (firstBytes == null) {
          throw new IOException("Unable to find start of bytes");
        }
        int startOfBytes = opcodeAndBytes.indexOf(firstBytes);
        bytes.append(opcodeAndBytes.substring(startOfBytes));
      }
      return bytes.toString();
    }
    return text;
  }

  /**
   * Parses an opcodes String. Will also parse opcodes out of the SEQ HTML report, ignoring comments
   * newlines, and labels.
   *
   * @return An array of the opcodes.
   * @throws IOException If there is an I/O related exception.
   */
  public static String[] parseOpcodesField(String text) throws IOException {
    if (text.contains(" | ")) {
      // Text is copied from SEQ HTML report, try and parse it
      List<String> opcodes = new ArrayList<>();
      for (String line : text.split("\n")) {
        if (line.startsWith("//") || line.isBlank() || line.endsWith(":")) {
          // Ignore comments, newlines, and labels
          continue;
        }
        String opcodeAndBytes = line.split(" \\| ")[1];
        // Find end of opcode and beginning of bytes
        String firstBytes = null;
        for (String part : opcodeAndBytes.split(" ")) {
          if (part.length() == 8 && isHex(part)) {
            firstBytes = part;
            break;
          }
        }
        if (firstBytes == null) {
          throw new IOException("Unable to find start of bytes");
        }
        int startOfBytes = opcodeAndBytes.indexOf(firstBytes);
        opcodes.add(opcodeAndBytes.substring(0, startOfBytes));
      }
      return opcodes.toArray(String[]::new);
    }
    return text.split("\n");
  }

  public static boolean isHex(String part) {
    try {
      Long.parseLong(part,16);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
