package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventTarget;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
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
  private Path seqPath;
  private Stage stage;
  private Map<String, SeqEdit> editsByName;
  private Mode mode;
  private SeqEdit selectedEdit;

  public ListView editList;
  public TextArea name;
  public TextField offset;
  public TextField hijackedBytesLength;
  public TextArea hijackedBytesTextArea;
  public TextArea newBytesTextArea;
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
    this.seqPath = seqPath;
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    this.leftStatus.setText(seqPath.toAbsolutePath().toString());
    this.editsByName = new HashMap<>();
    updateSeqEditsFromPath();
  }

  public void clear() {
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    name.setText("");
    offset.setText("");
    hijackedBytesLength.setText("");
    hijackedBytesTextArea.setText("");
    newBytesTextArea.setText("");
    opcodes.setText("");
  }

  public void newEdit() {
    if (mode == Mode.CREATE || mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (!confirm) {
        return;
      }
    }
    clear();
    this.mode = Mode.CREATE;
    this.rightStatus.setText(mode.toString());
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
    hijackedBytesTextArea.setText(ByteUtils.bytesToHexStringWords(oldBytes));
    newBytesTextArea.setText(ByteUtils.bytesToHexStringWords(newBytes));
    opcodes.setText(getOpcodesString(newBytes));
  }

  public void quit() {
    stage.close();
  }

  public void apply() {
    if (mode == Mode.CREATE) {
      if (verifyEditValid()) {
        applyNewEdit();
      }
    } else if (mode == Mode.EDIT) {
      if (verifyEditValid()) {
        applyExistingEdit();
      }
    } else {
      Message.error("No Edit Opened", "Cannot apply, no edit is opened.");
    }
  }

  public void reset() {
    if (mode == Mode.CREATE) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (confirm) {
        clear();
      }
    } else if (selectedEdit != null && mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
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
   * Adds a new edit to the seq file. The new edit will be appended to the end of the edit list.
   * This method assumes that {@link #verifyEditValid()} has already been called and returned true.
   */
  private void applyNewEdit() {
  }

  /**
   * Replaces the old instance of the current edit with the new information filled out by the user.
   * The new edit will be appended to the end of the edit list. This method assumes that {@link
   * #verifyEditValid()} has already been called and returned true.
   */
  private void applyExistingEdit() {
  }

  /**
   * Read the seq edits from the seq path and update the list of seq edits.
   *
   * @throws IOException If an I/O error occurs
   */
  private void updateSeqEditsFromPath() throws IOException {
    List<SeqEdit> seqEdits = SeqExt.getEdits(seqPath);
    for (SeqEdit seqEdit : seqEdits) {
      String editName = seqEdit.getName();
      editsByName.put(editName, seqEdit);
      editList.getItems().add(editName);
    }
  }

  /**
   * Checks if the event target is targeting the given edit name.
   *
   * @param target   The event target.
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

  /**
   * Verifies that the fields for the current edit are valid. Will display an error message and
   * return false if not. Otherwise, return true.
   *
   * @return If the edit fields are valid.
   */
  private boolean verifyEditValid() {
    try {
      // Name is filled out
      if (name.getText() == null || name.getText().isBlank()) {
        throw new IllegalStateException("Name is blank or empty.");
      }
      // Offset is filled out
      String offsetText = offset.getText();
      if (offsetText == null || offsetText.isBlank()) {
        throw new IllegalStateException("Offset is blank or empty");
      }
      // Offset is a number
      int offset;
      if (offsetText.startsWith("0x")) {
        try {
          offset = Integer.decode(offsetText);
          if (offset % 4 != 0) {
            throw new IllegalStateException("Offset must be multiple of 4.");
          }
        } catch (NumberFormatException e) {
          throw new IllegalStateException("Offset is not a valid hex number", e);
        }
      } else {
        try {
          offset = Integer.parseInt(offsetText);
          if (offset % 4 != 0) {
            throw new IllegalStateException("Offset must be multiple of 4.");
          }
        } catch (NumberFormatException e) {
          throw new IllegalStateException("Offset is not a valid decimal number", e);
        }
      }
      // Hijacked bytes length is filled out
      String hijackedBytesLengthText = hijackedBytesLength.getText();
      if (hijackedBytesLengthText == null || hijackedBytesLengthText.isBlank()) {
        throw new IllegalStateException("Hijacked bytes length is blank or empty");
      }
      // Hijacked bytes length is a number
      int hijackedBytesLength;
      if (hijackedBytesLengthText.startsWith("0x")) {
        try {
          hijackedBytesLength = Integer.decode(hijackedBytesLengthText);
          if (hijackedBytesLength % 4 != 0) {
            throw new IllegalStateException("Hijacked bytes length must be multiple of 4.");
          } else if (hijackedBytesLength == 0) {
            throw new IllegalStateException("Hijacked bytes length must be at least 4.");
          }
        } catch (NumberFormatException e) {
          throw new IllegalStateException("Hijacked bytes length is not a valid hex number", e);
        }
      } else {
        try {
          hijackedBytesLength = Integer.parseInt(hijackedBytesLengthText);
          if (hijackedBytesLength % 4 != 0) {
            throw new IllegalStateException("Hijacked bytes length must be multiple of 4.");
          } else if (hijackedBytesLength == 0) {
            throw new IllegalStateException("Hijacked bytes length must be at least 4.");
          }
        } catch (NumberFormatException e) {
          throw new IllegalStateException("Hijacked bytes length is not a valid decimal number", e);
        }
      }
      // Hijacked bytes is filled out
      String hijackedBytesText = hijackedBytesTextArea.getText();
      if (hijackedBytesText == null || hijackedBytesText.isBlank()) {
        throw new IllegalStateException("Hijacked bytes field is blank or empty");
      }
      // Hijacked bytes is only hex and whitespace
      verifyIsHex(hijackedBytesText, "Hijacked bytes");
      // New bytes is filled out
      String newBytesText = newBytesTextArea.getText();
      if (newBytesText == null || newBytesText.isBlank()) {
        throw new IllegalStateException("New bytes field is blank or empty");
      }
      // New bytes is only hex and whitespace
      verifyIsHex(newBytesText, "New bytes");
      // Verify that the new code does not conflict with existing codes
      for (SeqEdit existingEdit : editsByName.values()) {
        if (name.getText().equals(existingEdit.getName())) {
          throw new IllegalStateException("Edit name already exists: " + existingEdit.getName());
        }
        int existingStart = existingEdit.getOffset();
        int existingEnd = existingStart + existingEdit.getOldBytes().length;
        int newStart = offset;
        int newEnd = offset + hijackedBytesLength;
        boolean hasNoOverlap = (existingStart <= newEnd) && (existingEnd <= newStart);
        if (!hasNoOverlap) {
          throw new IllegalStateException(
              "New edit location conflicts with existing edit: " + existingEdit.getName());
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Edit Invalid", e);
      Message.error("Edit Invalid", e.getMessage());
      return false;
    }
    return true;
  }

  /**
   * Verifies that the text with the given field name is only hex or whitespace. Also verifies that
   * the total amount of hex results in 4-byte words. Throws an IllegalStateException when it is
   * not.
   *
   * @param text      The text to verify is hex or whitespace.
   * @param fieldName The name of the field to use in the case of an Exception being thrown.
   */
  private void verifyIsHex(String text, String fieldName) {
    int count = 0;
    for (char c : text.toCharArray()) {
      switch (c) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
          count++;
          break;
        case ' ':
        case '\r':
        case '\n':
        case '\t':
          break; // do nothing
        default:
          throw new IllegalStateException(fieldName + " contain invalid character: " + c);
      }
    }
    if (count % 8 != 0) {
      throw new IllegalStateException(fieldName + " must only have four-byte words");
    }
  }
}
