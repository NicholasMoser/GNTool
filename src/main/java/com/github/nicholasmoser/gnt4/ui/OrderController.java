package com.github.nicholasmoser.gnt4.ui;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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

  private class ImageCell extends ListCell<String> {

    private final ImageView imageView = new ImageView();
    private final ObservableList<Image> images;

    public ImageCell(ObservableList<Image> images) {
      this.images = images;
      ListCell thisCell = this;

      setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      setAlignment(Pos.CENTER);

      setOnDragDetected(event -> {
        if (getItem() == null) {
          return;
        }

        ObservableList<String> items = getListView().getItems();

        Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(getItem());
        dragboard.setDragView(
            images.get(
                items.indexOf(
                    getItem()
                )
            )
        );
        dragboard.setContent(content);

        event.consume();
      });

      setOnDragOver(event -> {
        if (event.getGestureSource() != thisCell &&
            event.getDragboard().hasString()) {
          event.acceptTransferModes(TransferMode.MOVE);
        }

        event.consume();
      });

      setOnDragEntered(event -> {
        if (event.getGestureSource() != thisCell &&
            event.getDragboard().hasString()) {
          setOpacity(0.3);
        }
      });

      setOnDragExited(event -> {
        if (event.getGestureSource() != thisCell &&
            event.getDragboard().hasString()) {
          setOpacity(1);
        }
      });

      setOnDragDropped(event -> {
        if (getItem() == null) {
          return;
        }

        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
          ObservableList<String> items = getListView().getItems();
          int draggedIdx = items.indexOf(db.getString());
          int thisIdx = items.indexOf(getItem());

          Image temp = images.get(draggedIdx);
          images.set(draggedIdx, images.get(thisIdx));
          images.set(thisIdx, temp);

          items.set(draggedIdx, getItem());
          items.set(thisIdx, db.getString());

          List<String> itemscopy = new ArrayList<>(getListView().getItems());
          getListView().getItems().setAll(itemscopy);

          success = true;
        }
        event.setDropCompleted(success);

        event.consume();
      });

      setOnDragDone(DragEvent::consume);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      if (empty || item == null) {
        setGraphic(null);
      } else {
        imageView.setImage(
            images.get(
                getListView().getItems().indexOf(item)
            )
        );
        setGraphic(imageView);
      }
    }
  }
}
