package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventTarget;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SeqEditor {

  private static final Logger LOGGER = Logger.getLogger(SeqEditor.class.getName());
  private Stage stage;
  private Path seqPath;
  private Map<String, SeqEdit> editsByName;

  public ListView editList;
  public TextArea name;
  public TextField offset;
  public TextField hijackedBytesLength;
  public TextArea hijackedBytesText;
  public TextArea newBytesText;
  public TextArea opcodes;
  public Label leftStatus;
  public Label rightStatus;

  public enum Mode {
    CREATE,
    EDIT
  }

  public void apply(MouseEvent mouseEvent) {
  }

  public void undo(MouseEvent mouseEvent) {
  }

  public void selectEdit(MouseEvent event) {
    int index = editList.getSelectionModel().getSelectedIndex();
    // Handle right click
    if (event.getButton() == MouseButton.SECONDARY && index >= 0) {
      String text = (String) editList.getItems().get(index);
      ContextMenu menu = getContextMenu(text);
      menu.show(stage, event.getScreenX(), event.getScreenY());
    }
  }

  public void init(Path seqPath, Stage stage) throws IOException {
    this.stage = stage;
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

  private ContextMenu getContextMenu(String editName) {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem openFile = new MenuItem("Open Edit");
    openFile.setOnAction(event -> {
      // Open editName
    });
    contextMenu.getItems().addAll(openFile);
    return contextMenu;
  }

  /**
   * Get the opcodes in human-readable text form from a given opcode byte array.
   *
   * @param bytes The opcode byte array.
   * @return The human-readable text of the opcodes.
   */
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
