package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
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

/**
 * A class to represent the GUI for the seq editor tool. This allows the user to modify a seq file
 * without having to manually edit the hex of the file. The edits are accomplished by branching from
 * the user-provided offset to the end of the seq file where new code is added.
 */
public class SeqEditor {

  private static final Logger LOGGER = Logger.getLogger(SeqEditor.class.getName());
  private static final String SEQ_EDITOR_INFO_URL = "https://github.com/NicholasMoser/GNTool/blob/master/docs/sequence.md#modify-seq";
  private Path seqPath;
  private Stage stage;
  private Map<String, SeqEdit> editsByName;
  private Mode mode;
  private SeqEdit selectedEdit;

  public ListView editList;
  public TextArea nameTextArea;
  public TextField offsetTextField;
  public TextField hijackedBytesLengthTextField;
  public TextArea hijackedBytesTextArea;
  public TextArea newBytesTextArea;
  public TextArea opcodesTextArea;
  public Label leftStatus;
  public Label rightStatus;

  /**
   * The current mode that the seq editor application is in.
   */
  public enum Mode {
    NONE_SELECTED,
    CREATE,
    EDIT
  }

  /**
   * Initialize the seq editor.
   *
   * @param seqPath The path to the seq file to edit.
   * @param stage   The stage of the application.
   * @throws IOException If there is an issue reading from the seq file.
   */
  public void init(Path seqPath, Stage stage) throws IOException {
    this.stage = stage;
    this.seqPath = seqPath;
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    this.leftStatus.setText(seqPath.toAbsolutePath().toString());
    this.editsByName = new HashMap<>();
    updateSeqEditsFromPath();
    setDisableFields(true);
    offsetTextField.textProperty()
        .addListener((observable, oldValue, newValue) -> tryToUpdateHijackedBytes());
    hijackedBytesLengthTextField.textProperty()
        .addListener((observable, oldValue, newValue) -> tryToUpdateHijackedBytes());
  }

  /**
   * Clear the editable seq edit fields.
   */
  public void clear() {
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    nameTextArea.setText("");
    offsetTextField.setText("");
    hijackedBytesLengthTextField.setText("");
    hijackedBytesTextArea.setText("");
    newBytesTextArea.setText("");
    opcodesTextArea.setText("");
  }

  /**
   * Attempt to open a new seq edit to be created. This may switch to {@link Mode#CREATE}.
   */
  public void newEdit() {
    if (mode == Mode.CREATE || mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (!confirm) {
        return;
      }
    }
    setDisableFields(false);
    clear();
    this.mode = Mode.CREATE;
    this.rightStatus.setText(mode.toString());
  }

  /**
   * Attempt to retrieve the currently selected edit and open it. This may switch to {@link
   * Mode#EDIT}.
   */
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

  /**
   * Open the given seq edit. This will switch to {@link Mode#EDIT}.
   *
   * @param seqEdit The seq edit to open.
   */
  private void openEdit(SeqEdit seqEdit) {
    setDisableFields(false);
    this.mode = Mode.EDIT;
    this.rightStatus.setText(mode.toString());
    this.selectedEdit = seqEdit;
    String editName = seqEdit.getName();
    byte[] oldBytes = seqEdit.getOldBytes();
    byte[] newBytes = seqEdit.getNewBytes();
    nameTextArea.setText(editName);
    offsetTextField.setText(Integer.toString(seqEdit.getOffset()));
    hijackedBytesLengthTextField.setText(Integer.toString(oldBytes.length));
    hijackedBytesTextArea.setText(ByteUtils.bytesToHexStringWords(oldBytes));
    newBytesTextArea.setText(ByteUtils.bytesToHexStringWords(newBytes));
    opcodesTextArea.setText(getOpcodesString(newBytes));
  }

  /**
   * Attempt to delete the currently selected edit.
   */
  public void deleteEdit() {
    Optional<String> selectedEdit = getSelectedEdit();
    if (selectedEdit.isPresent()) {
      SeqEdit seqEdit = editsByName.get(selectedEdit.get());
      if (seqEdit == null) {
        Message.error("Cannot Find Edit",
            "Cannot find edit with name: " + selectedEdit.get());
      } else {
        String msg = String.format("Are you sure you wish to delete edit \"%s\"?",
            seqEdit.getName());
        if (Message.warnConfirmation("Confirm Deletion", msg)) {
          deleteEdit(seqEdit);
        }
      }
    } else {
      Message.error("No Edit Selected", "Cannot delete the edit because no edit is selected.");
    }
  }

  /**
   * Delete the given seq edit.
   *
   * @param seqEdit The seq edit to delete.
   */
  private void deleteEdit(SeqEdit seqEdit) {
    try {
      SeqExt.removeEdit(seqEdit, seqPath);
      editsByName.remove(seqEdit.getName());
      editList.getItems().remove(seqEdit.getName());
      if (mode == Mode.EDIT && seqEdit.equals(selectedEdit)) {
        // Edit currently being edited was deleted, clear the fields
        clear();
        setDisableFields(true);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Delete Edit", e);
      Message.error("Failed to Delete Edit", e.getMessage());
    }
  }

  /**
   * Quit the application.
   */
  public void quit() {
    stage.close();
  }

  /**
   * Apply the changes of the seq edit.
   */
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

  /**
   * Reset the fields for the seq edit.
   */
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
          MenuItem openEdit = new MenuItem("Open Edit");
          openEdit.setOnAction(event -> openEdit());
          contextMenu.getItems().add(openEdit);
          MenuItem deleteEdit = new MenuItem("Delete Edit");
          deleteEdit.setOnAction(event -> deleteEdit());
          contextMenu.getItems().add(deleteEdit);
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

  public void aboutSEQEditor() {
    try {
      Desktop.getDesktop().browse(new URI(SEQ_EDITOR_INFO_URL));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Help Page", e);
      Message.error("Error Opening Help Page", e.getMessage());
    }
  }

  /**
   * Adds a new edit to the seq file. The new edit will be appended to the end of the edit list.
   * This method assumes that {@link #verifyEditValid()} has already been called and returned true.
   */
  private void applyNewEdit() {
    try {
      String name = nameTextArea.getText();
      int offset = readNumber(offsetTextField.getText());
      int hijackedBytesLength = readNumber(hijackedBytesLengthTextField.getText());
      byte[] newBytes = readHex(newBytesTextArea.getText());
      SeqEdit seqEdit = SeqEditBuilder.getBuilder()
          .name(name)
          .startOffset(offset)
          .endOffset(offset + hijackedBytesLength)
          .newBytes(newBytes)
          .seqPath(seqPath)
          .create();
      // Add the new edit
      SeqExt.addEdit(seqEdit, seqPath);
      editsByName.put(name, seqEdit);
      editList.getItems().add(name);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Create New Edit", e);
      Message.error("Failed to Create New Edit", e.getMessage());
    }
  }

  /**
   * Replaces the old instance of the current edit with the new information filled out by the user.
   * The new edit will be appended to the end of the edit list. This method assumes that {@link
   * #verifyEditValid()} has already been called and returned true.
   */
  private void applyExistingEdit() {
    try {
      // Remove the existing edit
      SeqExt.removeEdit(selectedEdit, seqPath);
      editsByName.remove(selectedEdit.getName());
      editList.getItems().remove(selectedEdit.getName());
      // Create the new edit
      String name = nameTextArea.getText();
      int offset = readNumber(offsetTextField.getText());
      int hijackedBytesLength = readNumber(hijackedBytesLengthTextField.getText());
      byte[] newBytes = readHex(newBytesTextArea.getText());
      SeqEdit seqEdit = SeqEditBuilder.getBuilder()
          .name(name)
          .startOffset(offset)
          .endOffset(offset + hijackedBytesLength)
          .newBytes(newBytes)
          .seqPath(seqPath)
          .create();
      // Add the new edit
      SeqExt.addEdit(seqEdit, seqPath);
      editsByName.put(name, seqEdit);
      editList.getItems().add(name);
      selectedEdit = seqEdit;
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Modify Edit", e);
      Message.error("Failed to Modify Edit", e.getMessage());
    }
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
   * Sets the disabled status of the fields.
   *
   * @param value If the fields are to be disabled.
   */
  private void setDisableFields(boolean value) {
    nameTextArea.setDisable(value);
    offsetTextField.setDisable(value);
    hijackedBytesLengthTextField.setDisable(value);
    hijackedBytesTextArea.setDisable(value);
    newBytesTextArea.setDisable(value);
    opcodesTextArea.setDisable(value);
  }

  /**
   * Tries to update the hijacked bytes display using the offset and hijacked bytes length.
   */
  private void tryToUpdateHijackedBytes() {
    try {
      int offset = readNumber(offsetTextField.getText());
      int hijackedBytesLength = readNumber(hijackedBytesLengthTextField.getText());
      try (RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
        byte[] bytes = new byte[hijackedBytesLength];
        raf.seek(offset);
        if (raf.read(bytes) != hijackedBytesLength) {
          throw new IOException("Failed to read " + hijackedBytesLength + " bytes.");
        }
        hijackedBytesTextArea.setText(ByteUtils.bytesToHexStringWords(bytes));
      }
    } catch (Exception e) {
      hijackedBytesTextArea.setText(e.getMessage());
    }
  }

  /**
   * Reads a decimal or hex number from a String. By default, decimal will be used. If the number
   * String starts with 0x then hex will be used. This method will throw an IllegalStateException is
   * the number is not valid.
   *
   * @param number The number String to read.
   * @return The number as an int.
   */
  private int readNumber(String number) throws IllegalStateException {
    try {
      return Integer.decode(number);
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Not a valid number: " + number, e);
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
      if (nameTextArea.getText() == null || nameTextArea.getText().isBlank()) {
        throw new IllegalStateException("Name is blank or empty.");
      }
      // Offset is filled out
      String offsetText = offsetTextField.getText();
      if (offsetText == null || offsetText.isBlank()) {
        throw new IllegalStateException("Offset is blank or empty");
      }
      // Offset is a number and a multiple of 4
      int offset = readNumber(offsetText);
      if (offset % 4 != 0) {
        throw new IllegalStateException("Offset must be multiple of 4.");
      }
      // Hijacked bytes length is filled out
      String hijackedBytesLengthText = hijackedBytesLengthTextField.getText();
      if (hijackedBytesLengthText == null || hijackedBytesLengthText.isBlank()) {
        throw new IllegalStateException("Hijacked bytes length is blank or empty");
      }
      // Hijacked bytes length is a number, a multiple of 4, and not 0
      int hijackedBytesLength = readNumber(hijackedBytesLengthText);
      if (hijackedBytesLength % 4 != 0) {
        throw new IllegalStateException("Hijacked bytes length must be multiple of 4.");
      } else if (hijackedBytesLength == 0) {
        throw new IllegalStateException("Hijacked bytes length must not be zero.");
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
        if (mode == Mode.EDIT && existingEdit == selectedEdit) {
          continue; // Skip the existing edit we are editing
        }
        if (nameTextArea.getText().equals(existingEdit.getName())) {
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

  /**
   * Read a hex String, ignoring whitespace, and return a byte array. Courtesy of Dave L. via
   * https://stackoverflow.com/a/140861
   *
   * @param s The String to read.
   * @return The byte array.
   */
  public byte[] readHex(String s) {
    // Remove whitespace
    s = s.replace(" ", "");
    s = s.replace("\r", "");
    s = s.replace("\n", "");
    s = s.replace("\t", "");
    // Original code from stack overflow
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}
