package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class CostumeController {

  public ListView<String> costume3;
  public ListView<String> characters;
  public ListView<String> costume4;

  public void init() {
    costume3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    costume4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    characters.getItems().addAll(GNT4Characters.CHARACTERS);
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
  }
}
