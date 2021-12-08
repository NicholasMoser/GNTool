package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SeqEditor {

  private static final Logger LOGGER = Logger.getLogger(SeqEditor.class.getName());
  private Stage stage;
  private Path seqPath;
  private Map<String, SeqEdit> editsByName;

  public ListView editList;
  public TextField name;
  public TextField offset;
  public TextField hijackedBytesLength;
  public TextArea hijackedBytesText;
  public TextArea newBytesText;
  public TextArea opcodes;
  public Label leftStatus;
  public Label rightStatus;

  public void init(Path seqPath, Stage stage) throws IOException {
    editsByName = new HashMap<>();
    leftStatus.setText(seqPath.toAbsolutePath().toString());
    rightStatus.setText("");

    List<SeqEdit> seqEdits = SeqExt.getEdits(seqPath);
    for (SeqEdit seqEdit : seqEdits)
    {
      String editName = seqEdit.getName();
      byte[] oldBytes = seqEdit.getOldBytes();
      byte[] newBytes = seqEdit.getNewBytes();
      editList.getItems().add(editName);
      name.setText(editName);
      offset.setText(Integer.toString(seqEdit.getOffset()));
      hijackedBytesLength.setText(Integer.toString(oldBytes.length));
      hijackedBytesText.setText(ByteUtils.bytesToHexStringWords(oldBytes));
      newBytesText.setText(ByteUtils.bytesToHexStringWords(newBytes));
      opcodes.setText(getOpcodesString(newBytes));
      editsByName.put(editName, seqEdit);
    }
  }

  private String getOpcodesString(byte[] bytes) {
    try {
      ByteStream bs = new ByteStream(bytes);
      StringBuilder builder = new StringBuilder();
      while (bs.bytesAreLeft()) {
        bs.mark();
        byte opcodeGroup = (byte) bs.read();
        byte opcode = (byte) bs.read();
        bs.reset();
        builder.append(SeqHelper.getSeqOpcode(bs, opcodeGroup, opcode).toString());
        builder.append('\n');
      }
      return builder.toString();
    } catch (Exception e) {
      LOGGER.log(Level.INFO, "Failed to process new bytes as opcodes", e);
      return "Unable to process opcodes";
    }
  }
}
