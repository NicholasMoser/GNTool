package com.github.nicholasmoser.gnt4.ui;

import java.util.List;
import javafx.scene.control.ListView;

public class OrderController {

  public ListView<String> listView;
  private OrderSave orderSave;

  public void moveUp() {
    int curr = listView.getSelectionModel().getSelectedIndex();
    if (curr == -1 || curr == 0) {
      return;
    }
    String itemToMove = listView.getItems().remove(curr);
    listView.getItems().add(curr - 1, itemToMove);
    listView.getSelectionModel().select(curr - 1);
  }

  public void moveDown() {
    int curr = listView.getSelectionModel().getSelectedIndex();
    if (curr == -1 || curr == listView.getItems().size() - 1) {
      return;
    }
    String itemToMove = listView.getItems().remove(curr);
    listView.getItems().add(curr + 1, itemToMove);
    listView.getSelectionModel().select(curr + 1);
  }

  public void init(List<String> items, OrderSave orderSave) {
    listView.getItems().setAll(items);
    this.orderSave = orderSave;
  }

  public void save() {
    orderSave.setValues(listView.getItems());
    new Thread(orderSave).start();
  }
}
