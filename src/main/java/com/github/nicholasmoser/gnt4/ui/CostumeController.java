package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class CostumeController {

  private static final Logger LOGGER = Logger.getLogger(CostumeController.class.getName());
  public ListView<String> costume3;
  public ListView<String> characters;
  public ListView<String> costume4;
  private Path charSel;
  private Path charSel4;

  public void init(Path uncompressedDirectory) throws IOException {
    costume3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getItems().addAll(GNT4Characters.CHARACTERS);

    // Check for existing costume extensions
    this.charSel = uncompressedDirectory.resolve(Seqs.CHARSEL);
    this.charSel4 = uncompressedDirectory.resolve(Seqs.CHARSEL_4);
    List<SeqEdit> charSelEdits = SeqExt.getEdits(charSel);
    List<SeqEdit> charSel4Edits = SeqExt.getEdits(charSel4);
    if (CostumeExtender.hasExistingEdits(charSelEdits, charSel4Edits)) {

    } else {
      // Default init: add Haku, Sakura, Ino
      costume3.getItems().add(GNT4Characters.HAKU);
      costume3.getItems().add(GNT4Characters.SAKURA);
      costume3.getItems().add(GNT4Characters.INO);
      costume4.getItems().add(GNT4Characters.HAKU);
      costume4.getItems().add(GNT4Characters.SAKURA);
      costume4.getItems().add(GNT4Characters.INO);
    }
  }

  public void addCostume3() {
    for (String item : characters.getSelectionModel().getSelectedItems()) {
      if (!costume3.getItems().contains(item)) {
        costume3.getItems().add(item);
      }
    }
  }

  public void removeCostume3() {
    costume3.getItems().removeAll(costume3.getSelectionModel().getSelectedItems());
  }

  public void saveCostume3() {
    List<String> characters = costume3.getItems();
    if (costume3.getItems().isEmpty()) {
      LOGGER.log(Level.INFO, "Must have at least one custom costume to save.");
      Message.info("Must Have One Costume", "Must have at least one custom costume to save.");
      return;
    }
    try {
      byte[] bytes = CostumeExtender.getCodeBytes(characters, 3, 1, false);
      bytes = CostumeExtender.getCodeBytes(characters, 3, 2, false);
      bytes = CostumeExtender.getCodeBytes(characters, 3, 1, true);
      bytes = CostumeExtender.getCodeBytes(characters, 3, 2, true);
      bytes = CostumeExtender.getCodeBytes(characters, 3, 3, true);
      bytes = CostumeExtender.getCodeBytes(characters, 3, 4, true);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to write costume bytes", e);
      Message.info("Failed to write costume bytes", e.getMessage());
    }
  }

  public void addCostume4() {
    for (String item : characters.getSelectionModel().getSelectedItems()) {
      if (!costume4.getItems().contains(item)) {
        costume4.getItems().add(item);
      }
    }
  }

  public void removeCostume4() {
    costume4.getItems().removeAll(costume4.getSelectionModel().getSelectedItems());
  }

  public void saveCostume4() {
    if (costume4.getItems().isEmpty()) {
      LOGGER.log(Level.INFO, "Must have at least one custom costume to save.");
      Message.info("Must Have One Costume", "Must have at least one custom costume to save.");
      return;
    }
  }
}
