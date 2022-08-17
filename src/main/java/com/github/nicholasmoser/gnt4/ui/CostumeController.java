package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * UI class for modifying character costumes.
 */
public class CostumeController {

  private static final Logger LOGGER = Logger.getLogger(CostumeController.class.getName());
  public ListView<String> costume3;
  public ListView<String> characters;
  public ListView<String> costume4;
  private Path charSel;
  private Path charSel4;
  private boolean hasCodes = false;

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
      LOGGER.info("Found existing costume extensions");
      hasCodes = true;
      costume3.getItems().addAll(CostumeExtender.getCostumeThreeCharacters(charSelEdits));
      costume4.getItems().addAll(CostumeExtender.getCostumeFourCharacters(charSelEdits));
    } else {
      // Default init: add Haku, Sakura, Ino
      LOGGER.info("No costume extensions found");
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

  public void save() {
    List<String> costumes3 = costume3.getItems();
    List<String> costumes4 = costume4.getItems();
    try {
      if (hasCodes) {
        // Codes already exist, delete them before adding new codes
        CostumeExtender.removeCodes(charSel, charSel4);
      } else {
        // No codes, initialize the alternate costumes model code
        CostumeExtender.allowAlternateCostumeModels(charSel, charSel4);
      }
      hasCodes = true;
      CostumeExtender.writeCostumeThreeCodes(charSel, charSel4, costumes3);
      CostumeExtender.writeCostumeFourCodes(charSel, charSel4, costumes4);
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
}
