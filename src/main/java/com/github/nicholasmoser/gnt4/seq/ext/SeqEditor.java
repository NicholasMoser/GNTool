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
   * Clear the editable symbol fields.
   */
  public void clear() {
    this.mode = Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    bytes.setText("");
    opcodes.setText("");
  }

  /**
   * Attempt to open a new symbol to be created.
   */
  public void newSymbol() {
    if (mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (!confirm) {
        return;
      }
    }
    clear();
    this.mode = Mode.EDIT;
    this.rightStatus.setText(mode.toString());
  }

  /**
   * Attempt to retrieve the currently selected symbol and open it.
   */
  public void openSymbol() {
    Optional<String> selectedSymbol = getSelectedSymbol();
    if (selectedSymbol.isPresent()) {
      Symbol seqEdit = symbolsByName.get(selectedSymbol.get());
      if (seqEdit == null) {
        Message.error("Cannot Find Symbol",
            "Cannot find symbol with name: " + selectedSymbol.get());
      } else {
        openSymbol(seqEdit);
      }
    } else {
      Message.error("No Symbol Selected", "Cannot open the symbol because no symbol is selected.");
    }
  }

  /**
   * Open the given symbol. This will switch to {@link Mode#EDIT}.
   *
   * @param symbol The symbol to open.
   */
  private void openSymbol(Symbol symbol) {
    this.mode = Mode.EDIT;
    this.rightStatus.setText(mode.toString());
    this.selectedSymbol = symbol;
    String editName = symbol.name();
  }

  /**
   * Attempt to delete the currently selected edit.
   */
  public void deleteSymbol() {
    Optional<String> selectedSymbol = getSelectedSymbol();
    if (selectedSymbol.isPresent()) {
      Symbol symbol = symbolsByName.get(selectedSymbol.get());
      if (symbol == null) {
        Message.error("Cannot Find Symbol",
            "Cannot find symbol with name: " + selectedSymbol.get());
      } else {
        String msg = String.format("Are you sure you wish to delete symbol \"%s\"?",
            symbol.name());
        if (Message.warnConfirmation("Confirm Deletion", msg)) {
          deleteSymbol(symbol);
        }
      }
    } else {
      Message.error("No Edit Selected", "Cannot delete the edit because no edit is selected.");
    }
  }

  /**
   * Delete the given symbol.
   *
   * @param symbol The symbol to delete.
   */
  private void deleteSymbol(Symbol symbol) {
    try {
      // TODO
      refresh();
      if (mode == Mode.EDIT && symbol.equals(selectedSymbol)) {
        // Symbol currently being edited was deleted, clear the fields
        clear();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Delete Symbol", e);
      Message.error("Failed to Delete Symbol", e.getMessage());
    }
  }

  /**
   * Quit the application.
   */
  public void quit() {
    stage.close();
  }

  /**
   * Apply the changes of the symbol.
   */
  public void apply() {
    if (mode == Mode.EDIT) {
      if (verifyEditValid()) {
        // TODO
      }
    } else {
      Message.error("No Symbol Opened", "Cannot apply, no symbol is opened.");
    }
  }

  public void exportSymbol() {
    Optional<String> selectedSymbol = getSelectedSymbol();
    if (selectedSymbol.isPresent()) {
      Symbol symbol = symbolsByName.get(selectedSymbol.get());
      if (symbol == null) {
        Message.error("Cannot Find Symbol",
            "Cannot find symbol with name: " + selectedSymbol.get());
      } else {
        try {
          // TODO
          //SeqEditPatcher.askExportToFile(symbol, seqPath, seqPath.getParent());
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to Export Symbol", e);
          Message.error("Failed to Export Symbol", e.getMessage());
        }
      }
    } else {
      Message.error("No Symbol Selected", "Cannot export the symbol because no symbol is selected.");
    }
  }

  public void importSymbol() {
    try {
      // TODO
      //SeqEditPatcher.askImportFromFile(seqPath.getParent(), seqPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Import Symbol", e);
      Message.error("Failed to Import Symbol", e.getMessage());
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
    if (selectedSymbol != null && mode == Mode.EDIT) {
      boolean confirm = Message.warnConfirmation("Confirm Reset",
          "You will lose your current changes!");
      if (confirm) {
        openSymbol(selectedSymbol); //selectedSymbol
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
    Optional<String> selectedEdit = getSelectedSymbol();
    EventTarget target = mouseEvent.getTarget();
    if (selectedEdit.isPresent()) {
      if (targetingSelectedListItem(target, selectedEdit.get())) {
        // Handle right click
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
          ContextMenu contextMenu = new ContextMenu();
          MenuItem openEdit = new MenuItem("Open Edit");
          openEdit.setOnAction(event -> openSymbol());
          contextMenu.getItems().add(openEdit);
          MenuItem deleteEdit = new MenuItem("Delete Edit");
          deleteEdit.setOnAction(event -> deleteSymbol());
          contextMenu.getItems().add(deleteEdit);
          MenuItem exportEdit = new MenuItem("Export Edit");
          exportEdit.setOnAction(event -> exportSymbol());
          contextMenu.getItems().add(exportEdit);
          contextMenu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
        // Handle double left click
        else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            openSymbol();
          }
        }
      }
    }
  }

  public void aboutSEQEditor() {
    GUIUtils.browse(SEQ_EDITOR_INFO_URL);
  }

  /**
   * Read the symbols from the seq path and update the list of symbols.
   *
   * @throws IOException If an I/O error occurs
   */
  private void updateSymbolsFromPath() throws IOException {
    symbolsByName.clear();
    symbolList.getItems().clear();
    List<Symbol> symbols = SeqExt.getSymbols(seqPath);
    for (Symbol symbol : symbols) {
      String editName = symbol.name();
      symbolsByName.put(editName, symbol);
      symbolList.getItems().add(editName);
    }
  }

  /**
   * Checks if the event target is targeting the given symbol name.
   *
   * @param target   The event target.
   * @param symbolName The name of the symbol.
   * @return If the event target is targeting the given edit name.
   */
  private boolean targetingSelectedListItem(EventTarget target, String symbolName) {
    if (target instanceof Labeled labeled) {
      return symbolName.equals(labeled.getText());
    } else if (target instanceof Text text) {
      return symbolName.equals(text.getText());
    }
    return false;
  }

  /**
   * @return An optional selected symbol from the list of existing symbols.
   */
  private Optional<String> getSelectedSymbol() {
    int index = symbolList.getSelectionModel().getSelectedIndex();
    if (index < 0) {
      return Optional.empty();
    }
    return Optional.of(symbolList.getItems().get(index));
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
}
