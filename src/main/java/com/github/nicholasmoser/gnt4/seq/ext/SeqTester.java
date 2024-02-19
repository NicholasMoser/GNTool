package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.ext.parser.AssemblyParser;
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
      String[] lines = AssemblyParser.parseOpcodesField(opcodesField.getText());
      Pair<List<Opcode>, Integer>  opcodes = SeqAssembler.assembleLines(lines, seqFile);
      StringBuilder sb = new StringBuilder();
      for (Opcode op : opcodes.getKey()) {
        sb.append(String.format("%s\n", ByteUtils.bytesToHexStringWords(op.getBytes())));
      }
      bytesField.setText(sb.toString());
      leftStatus.setText("");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to assemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
    }
  }

  /**
   * Disassemble the bytes to opcodes
   */
  public void disassemble() {
    try {
      String bytesFieldText = AssemblyParser.parseBytesField(bytesField.getText());
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
      leftStatus.setText("");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to disassemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
    }
  }

  /**
   * Quit the application.
   */
  public void quit() {
    stage.close();
  }
}
