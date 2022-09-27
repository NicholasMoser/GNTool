package com.github.nicholasmoser.gnt4.ui;

import static com.github.nicholasmoser.gnt4.ui.EyeExtender.*;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.GeckoWriter;
import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.dol.CodeWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * UI class for modifying character costume eyes.
 */
public class EyeController {

  private static final Logger LOGGER = Logger.getLogger(EyeController.class.getName());
  private static final List<String> ALL_EYE_TEXTURES = List.of(FIRST, SECOND, THIRD, FOURTH);
  private Path dol;
  private List<GeckoCodeGroup> codeGroups;
  private Path codesFiles;
  public VBox costume2;
  public VBox costume3;
  public VBox costume4;

  public void init(Path dol, List<GeckoCodeGroup> codeGroups, Path codesFiles) throws IOException {
    this.dol = dol;
    this.codeGroups = codeGroups;
    this.codesFiles = codesFiles;
    EyeSettings eyes = getSelections();
    for (String character : GNT4Characters.CHARACTERS) {
      costume2.getChildren().add(getRow(character, eyes.costumeTwoSelections().get(character)));
      costume3.getChildren().add(getRow(character, eyes.costumeThreeSelections().get(character)));
      costume4.getChildren().add(getRow(character, eyes.costumeFourSelections().get(character)));
    }
  }

  private EyeSettings getSelections() throws IOException {
    Map<String, String> costumeTwoSelections = new HashMap<>();
    Map<String, String> costumeThreeSelections = new HashMap<>();
    Map<String, String> costumeFourSelections = new HashMap<>();

    // Read eye settings from the dol Gecko codes if they exist
    Optional<EyeSettings> eyes = EyeExtender.getEyeSettings(codeGroups);
    if (eyes.isPresent()) {
      costumeTwoSelections = eyes.get().costumeTwoSelections();
      costumeThreeSelections = eyes.get().costumeThreeSelections();
      costumeFourSelections = eyes.get().costumeFourSelections();
    } else {
      // Default selections
      costumeTwoSelections.put(GNT4Characters.SASUKE, SECOND);
      costumeThreeSelections.put(GNT4Characters.SASUKE, SECOND);
      costumeFourSelections.put(GNT4Characters.SASUKE, SECOND);
      costumeTwoSelections.put(GNT4Characters.KANKURO, SECOND);
      costumeThreeSelections.put(GNT4Characters.KANKURO, SECOND);
      costumeFourSelections.put(GNT4Characters.KANKURO, SECOND);
    }
    return new EyeSettings(costumeTwoSelections, costumeThreeSelections, costumeFourSelections);
  }

  private void writeSelections(EyeSettings eyes) throws IOException {
    List<GeckoCode> codes = EyeExtender.getGeckoCodes(eyes);
    CodeWriter writer = new CodeWriter(dol, codesFiles, codeGroups);
    writer.addCodes(CODE_NAME, codes);
  }

  private void removeExistingCode() throws IOException {
    Iterator<GeckoCodeGroup> iterator = codeGroups.iterator();
    while (iterator.hasNext()) {
      GeckoCodeGroup codeGroup = iterator.next();
      if (codeGroup.getName().equals(CODE_NAME)) {
        GeckoWriter writer = new GeckoWriter(dol);
        writer.removeCodes(codeGroup);
        iterator.remove();
        break;
      }
    }
  }

  private HBox getRow(String character, String selection) {
    selection = selection == null ? FIRST : selection;
    HBox row = new HBox();
    row.setPadding(new Insets(4, 0, 0, 4));
    row.setSpacing(8);
    ComboBox<String> eyeTextures = new ComboBox<>();
    eyeTextures.getItems().addAll(ALL_EYE_TEXTURES);
    eyeTextures.getSelectionModel().select(selection);
    row.getChildren().add(eyeTextures);
    Text text = new Text(character);
    text.getStyleClass().add("text-id");
    text.setFont(new Font(16));
    row.getChildren().add(text);
    return row;
  }

  @FXML
  public void save() {
    try {
      doSave();
      LOGGER.info("Saved new eye textures");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Saving Codes", e);
      Message.error("Error Saving Codes", e.getMessage());
    }
  }

  private void doSave() throws IOException {
    HashMap<String, String> costumeTwoSelections = new HashMap<>();
    HashMap<String, String> costumeThreeSelections = new HashMap<>();
    HashMap<String, String> costumeFourSelections = new HashMap<>();
    for (Node node : costume2.getChildren()) {
      if (node instanceof HBox row) {
        if (row.getChildren().get(0) instanceof ComboBox comboBox) {
          if (row.getChildren().get(1) instanceof Text chr &&
              comboBox.getValue() instanceof String selection) {
            if (!selection.equals(FIRST)) {
              costumeTwoSelections.put(chr.getText(), selection);
            }
          }
        }
      }
    }
    for (Node node : costume3.getChildren()) {
      if (node instanceof HBox row) {
        if (row.getChildren().get(0) instanceof ComboBox comboBox) {
          if (row.getChildren().get(1) instanceof Text chr &&
              comboBox.getValue() instanceof String selection) {
            if (!selection.equals(FIRST)) {
              costumeThreeSelections.put(chr.getText(), selection);
            }
          }
        }
      }
    }
    for (Node node : costume4.getChildren()) {
      if (node instanceof HBox row) {
        if (row.getChildren().get(0) instanceof ComboBox comboBox) {
          if (row.getChildren().get(1) instanceof Text chr &&
              comboBox.getValue() instanceof String selection) {
            if (!selection.equals(FIRST)) {
              costumeFourSelections.put(chr.getText(), selection);
            }
          }
        }
      }
    }
    EyeSettings eyes = new EyeSettings(costumeTwoSelections, costumeThreeSelections,
        costumeFourSelections);
    removeExistingCode();
    writeSelections(eyes);
  }
}
