package com.github.nicholasmoser;

import com.github.nicholasmoser.tools.GenericFPKRepacker;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ToolController {

  private static final Logger LOGGER = Logger.getLogger(ToolController.class.getName());

  private static final String GENERIC_FPK_REPACKER = "Generic FPK Repacker";

  @FXML
  private ListView<String> tools;

  public void init() {
    tools.getItems().add(GENERIC_FPK_REPACKER);
  }

  @FXML
  protected void toolSelected(MouseEvent mouseEvent) {
    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
      if(mouseEvent.getClickCount() == 2) {
        EventTarget target =  mouseEvent.getTarget();
        if (target instanceof Labeled) {
          Labeled label = (Labeled) target;
          runTool(label.getText());
        }
        else if (target instanceof Text) {
          Text text = (Text) target;
          runTool(text.getText());
        }
      }
    }
  }

  /**
   * Runs the specified tool by name.
   *
   * @param tool The tool name.
   */
  private void runTool(String tool) {
    try {
      if (GENERIC_FPK_REPACKER.equals(tool)) {
        GenericFPKRepacker.genericFPKRepack();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error was encountered when running the tool.", e);
    }
  }
}
