package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import java.nio.file.Path;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class EyeController {

  public ListView<String> characters;
  public ListView<String> costume2;
  public ListView<String> costume3;
  public ListView<String> costume4;
  private Path dol;

  public void init(Path uncompressedDirectory) {
    characters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getItems().addAll(GNT4Characters.CHARACTERS);

    // Check for existing eye extensions
    this.dol = uncompressedDirectory.resolve("sys/main.dol");
  }

  public void save() {
  }

  public void addCostume2() {
    for (String item : characters.getSelectionModel().getSelectedItems()) {
      if (!costume2.getItems().contains(item)) {
        costume2.getItems().add(item);
      }
    }
  }

  public void removeCostume2() {
    costume2.getItems().removeAll(costume2.getSelectionModel().getSelectedItems());
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
