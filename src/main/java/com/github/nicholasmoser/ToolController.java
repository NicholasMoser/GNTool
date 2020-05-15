package com.github.nicholasmoser;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ToolController {

  @FXML
  private ListView<String> tools;

  public void init() {
  }

  @FXML
  protected void toolSelected(MouseEvent mouseEvent) {
    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
      if(mouseEvent.getClickCount() == 2) {
        EventTarget target =  mouseEvent.getTarget();
        if (target instanceof Labeled) {
          Labeled label = (Labeled) target;
          System.out.println(label.getText());
        }
      }
    }
  }
}
