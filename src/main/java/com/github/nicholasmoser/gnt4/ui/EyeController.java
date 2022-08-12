package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EyeController {

  public VBox costume2;
  public VBox costume3;
  public VBox costume4;
  private Path dol;
  private static final String FIRST = "1300.txg";
  private static final String SECOND = "1301.txg";
  private static final String THIRD = "1302.txg";
  private static final String FOURTH = "1303.txg";
  private static final List<String> ALL_EYE_TEXTURES = List.of(FIRST, SECOND, THIRD, FOURTH);

  public void init(Path uncompressedDirectory) {
    this.dol = uncompressedDirectory.resolve("sys/main.dol");
    EyeSettings eyes = getSelections();
    for (String character : GNT4Characters.CHARACTERS) {
      costume2.getChildren().add(getRow(character, eyes.costumeTwoSelections().get(character)));
      costume3.getChildren().add(getRow(character, eyes.costumeThreeSelections().get(character)));
      costume4.getChildren().add(getRow(character, eyes.costumeFourSelections().get(character)));
    }
  }

  private EyeSettings getSelections() {
    HashMap<String, String> costumeTwoSelections = new HashMap<>();
    HashMap<String, String> costumeThreeSelections = new HashMap<>();
    HashMap<String, String> costumeFourSelections = new HashMap<>();

    // TODO - Read eye texture selections from the dol

    // Default selections
    costumeTwoSelections.put(GNT4Characters.SASUKE, SECOND);
    costumeThreeSelections.put(GNT4Characters.SASUKE, SECOND);
    costumeFourSelections.put(GNT4Characters.SASUKE, SECOND);
    costumeTwoSelections.put(GNT4Characters.KANKURO, SECOND);
    costumeThreeSelections.put(GNT4Characters.KANKURO, SECOND);
    costumeFourSelections.put(GNT4Characters.KANKURO, SECOND);
    return new EyeSettings(costumeTwoSelections, costumeThreeSelections, costumeFourSelections);
  }

  private void writeSelections(EyeSettings eyes) {
    // TODO - Write new eye texture selections to the dol
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

  public void save() {
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
    writeSelections(eyes);
  }
}
