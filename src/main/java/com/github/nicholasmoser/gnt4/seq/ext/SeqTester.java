package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SeqTester {
  private static final Logger LOGGER = Logger.getLogger(SeqTester.class.getName());
  private Stage stage;
  private Path seqFile;
  public TextArea opcodesField;
  public TextArea bytesField;
  public Label leftStatus;

  public void init(Stage stage, Path seqFile) {
    this.stage = stage;
    this.seqFile = seqFile;
    if (seqFile != null) {
      leftStatus.setText("Loaded " + seqFile);
    } else {
      leftStatus.setText("");
    }
  }

  /**
   * Assemble the opcodes to bytes
   */
  public void assemble() {
    try {
      String[] lines = parseOpcodesField();
      Pair<List<Opcode>, Integer>  opcodes = SeqAssembler.assembleLines(lines, seqFile);
      StringBuilder sb = new StringBuilder();
      for (Opcode op : opcodes.getKey()) {
        sb.append(String.format("%s\n", ByteUtils.bytesToHexStringWords(op.getBytes())));
      }
      bytesField.setText(sb.toString());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to assemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
    }
  }

  public void disassemble() {
    try {
      String bytesFieldText = parseBytesField();
      byte[] bytes = ByteUtils.hexTextToBytes(bytesFieldText);
      ByteStream bs = new ByteStream(bytes);
      StringBuilder opcodeFields = new StringBuilder();
      while (bs.bytesAreLeft()) {
        bs.mark();
        byte opcodeGroup = (byte) bs.read();
        byte opcode = (byte) bs.read();
        bs.reset();
        String asm = SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode).toAssembly();
        opcodeFields.append(asm);
        opcodeFields.append('\n');
      }
      opcodesField.setText(opcodeFields.toString());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to disassemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
    }
  }

  private String parseBytesField() throws IOException {
    String text =  bytesField.getText();
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
   * Parses the opcodes field. Will also parse opcodes out of the SEQ HTML report.
   *
   * @return An array of the opcodes.
   * @throws IOException If there is an I/O related exception.
   */
  private String[] parseOpcodesField() throws IOException {
    String text = opcodesField.getText();
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

  private boolean isHex(String part) {
    try {
      Long.parseLong(part,16);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Quit the application.
   */
  public void quit() {
    stage.close();
  }
}
