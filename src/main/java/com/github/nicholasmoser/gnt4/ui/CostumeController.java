package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
  private Path uncompressedDirectory;
  private Path charSel;
  private Path charSel4;
  private boolean hasCodes = false;

  public void init(Path uncompressedDirectory) throws IOException {
    costume3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getItems().addAll(GNT4Characters.CHARACTERS);

    // Check for existing costume extensions
    this.uncompressedDirectory = uncompressedDirectory;
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
    Set<String> allCostumes = new HashSet<>(costumes3);
    allCostumes.addAll(costumes4);
    if (allCostumes.contains(GNT4Characters.KANKURO)) {
      allCostumes.add(GNT4Characters.KARASU);
    } else if (allCostumes.contains(GNT4Characters.TAYUYA)) {
      allCostumes.add(GNT4Characters.TAYUYA_DOKI);
    } else if (allCostumes.contains(GNT4Characters.KIBA)) {
      allCostumes.add(GNT4Characters.AKAMARU);
    }
    try {
      verifyIntegrity(costumes3, costumes4);
      checkDupeCostumeFix(charSel);
      if (hasCodes) {
        // Codes already exist, delete them before adding new codes
        CostumeExtender.removeCodes(charSel, charSel4);
      } else {
        // No codes, initialize the alternate costumes model code
        CostumeExtender.allowAlternateCostumeModels(charSel, charSel4);
      }
      hasCodes = true;
      CostumeBranchTableFix.enable(uncompressedDirectory, allCostumes);
      CostumeExtender.writeCostumeThreeCodes(charSel, charSel4, costumes3);
      CostumeExtender.writeCostumeFourCodes(charSel, charSel4, costumes4);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to write costume bytes", e);
      Message.info("Failed to write costume bytes", e.getMessage());
    }
  }

  private void checkDupeCostumeFix(Path charSel) throws IOException {
    List<SeqEdit> edits = SeqExt.getEdits(charSel);
    if (!DupeCostumeFix.isEnabled(edits)) {
      String msg = """
            The code "Fix Costume Duplicate Check" is not currently applied.
            Without it, costumes 1 and 3, and 2 and 4 cannot fight each other when players select
            the same character. Also, crashes can occur when the default game logic attempts to
            fallback to costume 4 if it does not exist.
            Would you like to enable the duplicate costume fix in order to fix this behavior?
            """;
      if (Message.warnConfirmation("Missing Dupe Costume Fix", msg)) {
        DupeCostumeFix.enable(charSel);
      }
    }
  }

  /**
   * Verify the integrity of the costumes and offer fixes where appropriate.
   *
   * @param costumes3 The list of characters with third costumes.
   * @param costumes4 The list of characters with fourth costumes.
   * @throws IOException If an I/O error occurs
   */
  private void verifyIntegrity(List<String> costumes3, List<String> costumes4) throws IOException {
    if (costumes3.contains(GNT4Characters.KANKURO)) {
      Path kar0200dat = uncompressedDirectory.resolve("files/chr/kar/0200.dat");
      if (!Files.exists(kar0200dat)) {
        String msg = "Kankuro's third costume requires Karasu's third costume. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to copy Karasu's first costume into the third costume slot?";
        if (Message.warnConfirmation("Missing Karasu Third Costume", msg)) {
          Path kar0000dat = uncompressedDirectory.resolve("files/chr/kar/0000.dat");
          Path kar0000jcv = uncompressedDirectory.resolve("files/chr/kar/0000.jcv");
          Path kar0200jcv = uncompressedDirectory.resolve("files/chr/kar/0200.jcv");
          Files.copy(kar0000dat, kar0200dat);
          Files.copy(kar0000jcv, kar0200jcv);
        }
      }
    }
    if (costumes4.contains(GNT4Characters.KANKURO)) {
      Path kar0300dat = uncompressedDirectory.resolve("files/chr/kar/0300.dat");
      if (!Files.exists(kar0300dat)) {
        String msg = "Kankuro's fourth costume requires Karasu's fourth costume. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to copy Karasu's first costume into the fourth costume slot?";
        if (Message.warnConfirmation("Missing Karasu Fourth Costume", msg)) {
          Path kar0000dat = uncompressedDirectory.resolve("files/chr/kar/0000.dat");
          Path kar0000jcv = uncompressedDirectory.resolve("files/chr/kar/0000.jcv");
          Path kar0300jcv = uncompressedDirectory.resolve("files/chr/kar/0300.jcv");
          Files.copy(kar0000dat, kar0300dat);
          Files.copy(kar0000jcv, kar0300jcv);
        }
      }
    }
    if (costumes3.contains(GNT4Characters.TAYUYA)) {
      Path ta20200dat = uncompressedDirectory.resolve("files/chr/ta2/0200.dat");
      if (!Files.exists(ta20200dat)) {
        String msg = "Tayuya's third costume requires Doki Demon's third costume. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to copy Doki Demon's first costume into the third costume slot?";
        if (Message.warnConfirmation("Missing Doki Demon Third Costume", msg)) {
          Path ta20000dat = uncompressedDirectory.resolve("files/chr/ta2/0000.dat");
          Path ta20000jcv = uncompressedDirectory.resolve("files/chr/ta2/0000.jcv");
          Path ta20200jcv = uncompressedDirectory.resolve("files/chr/ta2/0200.jcv");
          Files.copy(ta20000dat, ta20200dat);
          Files.copy(ta20000jcv, ta20200jcv);
        }
      }
    }
    if (costumes4.contains(GNT4Characters.TAYUYA)) {
      Path ta20300dat = uncompressedDirectory.resolve("files/chr/ta2/0300.dat");
      if (!Files.exists(ta20300dat)) {
        String msg = "Tayuya's fourth costume requires Doki Demon's fourth costume. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to copy Doki Demon's first costume into the fourth costume slot?";
        if (Message.warnConfirmation("Missing Doki Demon Fourth Costume", msg)) {
          Path ta20000dat = uncompressedDirectory.resolve("files/chr/ta2/0000.dat");
          Path ta20000jcv = uncompressedDirectory.resolve("files/chr/ta2/0000.jcv");
          Path ta20300jcv = uncompressedDirectory.resolve("files/chr/ta2/0300.jcv");
          Files.copy(ta20000dat, ta20300dat);
          Files.copy(ta20000jcv, ta20300jcv);
        }
      }
    }
    if (costumes3.contains(GNT4Characters.CHOJI)) {
      Path cho0210dat = uncompressedDirectory.resolve("files/chr/cho/0210.dat");
      if (!Files.exists(cho0210dat)) {
        String msg = "Choji's third costume requires a separate model for large Choji. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to copy the first large Choji costume into the third costume slot?";
        if (Message.warnConfirmation("Missing large Choji Third Costume", msg)) {
          Path cho0010dat = uncompressedDirectory.resolve("files/chr/cho/0010.dat");
          Path cho0010jcv = uncompressedDirectory.resolve("files/chr/cho/0010.jcv");
          Path cho0210jcv = uncompressedDirectory.resolve("files/chr/cho/0210.jcv");
          Files.copy(cho0010dat, cho0210dat);
          Files.copy(cho0010jcv, cho0210jcv);
        }
      }
    }
    if (costumes4.contains(GNT4Characters.CHOJI)) {
      Path cho0310dat = uncompressedDirectory.resolve("files/chr/cho/0310.dat");
      if (!Files.exists(cho0310dat)) {
        String msg = "Choji's fourth costume requires a separate model for large Choji. ";
        msg += "If you do not do this, the game will error in the VS screen. ";
        msg += "Would you like to the first large Choji costume into the fourth costume slot?";
        if (Message.warnConfirmation("Missing large Choji Fourth Costume", msg)) {
          Path cho0010dat = uncompressedDirectory.resolve("files/chr/cho/0010.dat");
          Path cho0010jcv = uncompressedDirectory.resolve("files/chr/cho/0010.jcv");
          Path cho0310jcv = uncompressedDirectory.resolve("files/chr/cho/0310.jcv");
          Files.copy(cho0010dat, cho0310dat);
          Files.copy(cho0010jcv, cho0310jcv);
        }
      }
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
