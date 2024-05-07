package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.ext.parser.AssemblyParser;
import com.github.nicholasmoser.gnt4.seq.ext.symbol.Symbol;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * A class to represent the GUI for the seq editor tool. This allows the user to modify a seq file
 * without having to manually edit the hex of the file. Each edit is a symbol. Symbols can be
 * new code or binary, references to existing code or binary, or something else entirely.
 * Symbols are referenced by name and can be branched to for code execution or as a data structure.
 */
public class SeqEditor {

  private static final Logger LOGGER = Logger.getLogger(SeqEditor.class.getName());
  private static final String SEQ_EDITOR_INFO_URL = "https://github.com/NicholasMoser/GNTool/blob/master/docs/sequence.md#modify-seq";
  private Path seqPath;
  private Stage stage;
  private Map<String, Symbol> symbolsByName;
  private Mode mode;
  private Symbol selectedSymbol;
  public ListView<String> symbolList;
  public TextArea bytes;
  public TextArea opcodes;
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
    this.symbolsByName = new HashMap<>();
    updateSymbolsFromPath();
  }

  public void refresh() {
    try {
      updateSymbolsFromPath();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to Refresh", e);
      Message.error("Failed to Refresh", e.getMessage());
      quit();
    }
  }

  /**
   * Clear the editable seq edit fields.
   */
  public void clear() {
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    bytes.setText("");
    opcodes.setText("");
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
      SeqEdit seqEdit = null; //symbolsByName.get(selectedEdit.get());
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
    this.mode = Mode.EDIT;
    this.rightStatus.setText(mode.toString());
    this.selectedSymbol = null;// seqEdit;
    String editName = seqEdit.getName();
    byte[] oldBytes = seqEdit.getOldBytes();
    List<Opcode> oldCodes = new LinkedList<>();
    ByteStream bs = new ByteStream(oldBytes);
    while (bs.bytesAreLeft()) {
      try {
        oldCodes.add(SeqHelper.getSeqOpcode(bs, bs.peekBytes(2)[0], bs.peekBytes(2)[1]));
      } catch (IOException e) {
        break;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (Opcode op : oldCodes) {
      sb.append(String.format("%s\n", ByteUtils.bytesToHexStringWords(op.getBytes())));
    }
    List<Opcode> newCodes = seqEdit.getNewCodes();
  }

  /**
   * Attempt to delete the currently selected edit.
   */
  public void deleteEdit() {
    Optional<String> selectedEdit = getSelectedEdit();
    if (selectedEdit.isPresent()) {
      SeqEdit seqEdit = null;//symbolsByName.get(selectedEdit.get());
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
      refresh();
      if (mode == Mode.EDIT && seqEdit.equals(selectedSymbol)) {
        // Edit currently being edited was deleted, clear the fields
        clear();
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

  public void exportEdit() {
    Optional<String> selectedEdit = getSelectedEdit();
    if (selectedEdit.isPresent()) {
      SeqEdit seqEdit = null;//symbolsByName.get(selectedEdit.get());
      if (seqEdit == null) {
        Message.error("Cannot Find Edit",
            "Cannot find edit with name: " + selectedEdit.get());
      } else {
        try {
          SeqEditPatcher.askExportToFile(seqEdit, seqPath, seqPath.getParent());
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to Export Edit", e);
          Message.error("Failed to Export Edit", e.getMessage());
        }
      }
    } else {
      Message.error("No Edit Selected", "Cannot export the edit because no edit is selected.");
    }
  }

  public void importEdit() {
    try {
      SeqEditPatcher.askImportFromFile(seqPath.getParent(), seqPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Import Edit", e);
      Message.error("Failed to Import Edit", e.getMessage());
    }
    refresh();
  }

  /**
   * Disassemble the bytes to opcodes
   */
  public void disassemble() {
    try {
      String bytesFieldText = AssemblyParser.parseBytesField(bytes.getText());
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
      opcodes.setText(opcodeFields.toString());
      leftStatus.setText("");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to disassemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
    }
  }

  /**
   * Assemble the opcodes to bytes
   */
  public void assemble() {
    try {
      String[] lines = AssemblyParser.parseOpcodesField(opcodes.getText());
      Pair<List<Opcode>, Integer> opcodes = SeqAssembler.assembleLines(lines, seqPath);
      StringBuilder sb = new StringBuilder();
      for (Opcode op : opcodes.getKey()) {
        sb.append(String.format("%s\n", ByteUtils.bytesToHexStringWords(op.getBytes())));
      }
      bytes.setText(sb.toString());
      leftStatus.setText("");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to assemble lines.", e);
      String message = e.getMessage() != null ? e.getMessage() : e.getClass().toString();
      leftStatus.setText(message);
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
    } else if (selectedSymbol != null && mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (confirm) {
        openEdit(null); //selectedSymbol
      }
    } else {
      Message.error("No Edit Opened", "Cannot undo, no edit is opened.");
    }
    refresh();
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
          MenuItem exportEdit = new MenuItem("Export Edit");
          exportEdit.setOnAction(event -> exportEdit());
          contextMenu.getItems().add(exportEdit);
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
    GUIUtils.browse(SEQ_EDITOR_INFO_URL);
  }

  /**
   * Adds a new edit to the seq file. The new edit will be appended to the end of the edit list.
   * This method assumes that {@link #verifyEditValid()} has already been called and returned true.
   */
  private void applyNewEdit() {
    try {
      refresh();
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
      refresh();
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
  private void updateSymbolsFromPath() throws IOException {
    symbolsByName.clear();
    symbolList.getItems().clear();
    List<SeqEdit> seqEdits = SeqExt.getEdits(seqPath);
    for (SeqEdit seqEdit : seqEdits) {
      String editName = seqEdit.getName();
      symbolsByName.put(editName, null); //seqEdit
      symbolList.getItems().add(editName);
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
    if (target instanceof Labeled labeled) {
      return editName.equals(labeled.getText());
    } else if (target instanceof Text text) {
      return editName.equals(text.getText());
    }
    return false;
  }

  /**
   * @return An optional selected edit from the list of existing edits.
   */
  private Optional<String> getSelectedEdit() {
    int index = symbolList.getSelectionModel().getSelectedIndex();
    if (index < 0) {
      return Optional.empty();
    }
    return Optional.of(symbolList.getItems().get(index));
  }

  /**
   * Tries to update the hijacked bytes display using the offset and hijacked bytes length.
   */
  private void tryToUpdateHijackedBytes() {

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
        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' ->
            count++;
        case ' ', '\r', '\n', '\t' -> {
        } // do nothing
        default -> throw new IllegalStateException(fieldName + " contain invalid character: " + c);
      }
    }
    if (count % 8 != 0) {
      throw new IllegalStateException(fieldName + " must only have four-byte words");
    }
  }

  /**
   * Read a hex String, ignoring whitespace, and return a byte array. Courtesy of Dave L. via
   * <a href="https://stackoverflow.com/a/140861">Stack Overflow</a>
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
