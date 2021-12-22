package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.skin.VirtualContainerBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SeqEditor {

  private static final Logger LOGGER = Logger.getLogger(SeqEditor.class.getName());
  private Stage stage;
  private Path seqPath;
  private Map<String, SeqEdit> editsByName;
  private Mode mode;
  private SeqEdit selectedEdit;

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
    NONE_SELECTED,
    CREATE,
    EDIT
  }

  public void init(Path seqPath, Stage stage) throws IOException {
    this.stage = stage;
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    this.leftStatus.setText(seqPath.toAbsolutePath().toString());
    this.editsByName = new HashMap<>();

    List<SeqEdit> seqEdits = SeqExt.getEdits(seqPath);
    for (SeqEdit seqEdit : seqEdits) {
      String editName = seqEdit.getName();
      editsByName.put(editName, seqEdit);
      editList.getItems().add(editName);
    }
  }

  public void clear() {
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    name.setText("");
    offset.setText("");
    hijackedBytesLength.setText("");
    hijackedBytesText.setText("");
    newBytesText.setText("");
    opcodes.setText("");
  }

  public void newEdit() {
  }

  public void openEdit() {
    Optional<String> selectedEdit = getSelectedEdit();
    if (selectedEdit.isPresent()) {
      SeqEdit seqEdit = editsByName.get(selectedEdit.get());
      if (seqEdit == null) {
        Message.error("Cannot Find Edit",
            "Cannot find edit with name: " + selectedEdit.get());
      } else {
        openEdit(seqEdit);
      }
    } else {
      Message.error("No Edit Selected", "Cannot open the edit because no edit is selected.");
    }
  }

  private void openEdit(SeqEdit seqEdit) {
    this.mode = Mode.EDIT;
    this.rightStatus.setText(mode.toString());
    this.selectedEdit = seqEdit;
    String editName = seqEdit.getName();
    byte[] oldBytes = seqEdit.getOldBytes();
    byte[] newBytes = seqEdit.getNewBytes();
    name.setText(editName);
    offset.setText(Integer.toString(seqEdit.getOffset()));
    hijackedBytesLength.setText(Integer.toString(oldBytes.length));
    hijackedBytesText.setText(ByteUtils.bytesToHexStringWords(oldBytes));
    newBytesText.setText(ByteUtils.bytesToHexStringWords(newBytes));
    opcodes.setText(getOpcodesString(newBytes));
  }

  public void quit() {
  }

  public void apply() {
  }

  public void reset() {
    if (mode == Mode.CREATE) {
      boolean confirm = Message.warnConfirmation("Confirm Reset", "You will lose your current changes!");
      if (confirm) {
        clear();
      }
    } else if (selectedEdit != null && mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset", "You will lose your current changes!");
      if (confirm) {
        openEdit(selectedEdit);
      }
    } else {
      Message.error("No Edit Opened", "Cannot undo, no edit is opened.");
    }
  }

  /**
   * Performs mouse related logic when existing edits are selected.
   *
   * @param mouseEvent The mouse event.
   */
  public void selectEdit(MouseEvent mouseEvent) {
    Optional<String> selectedEdit = getSelectedEdit();
    EventTarget target = mouseEvent.getTarget();
    if (selectedEdit.isPresent()) {
      if (targetingSelectedListItem(target, selectedEdit.get())) {
        // Handle right click
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
          ContextMenu contextMenu = new ContextMenu();
          MenuItem openFile = new MenuItem("Open Edit");
          openFile.setOnAction(event -> openEdit());
          contextMenu.getItems().addAll(openFile);
          contextMenu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
        // Handle double left click
        else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            openEdit();
          }
        }
      }
    }
  }

  /**
   * Checks if the event target is targeting the given edit name.
   *
   * @param target The event target.
   * @param editName The name of the edit.
   * @return If the event target is targeting the given edit name.
   */
  private boolean targetingSelectedListItem(EventTarget target, String editName) {
    if (target instanceof Labeled) {
      Labeled labeled = (Labeled) target;
      return editName.equals(labeled.getText());
    } else if (target instanceof Text) {
      Text text = (Text) target;
      return editName.equals(text.getText());
    }
    return false;
  }

  /**
   * @return An optional selected edit from the list of existing edits.
   */
  private Optional<String> getSelectedEdit() {
    int index = editList.getSelectionModel().getSelectedIndex();
    if (index < 0) {
      return Optional.empty();
    }
    return Optional.of((String) editList.getItems().get(index));
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
