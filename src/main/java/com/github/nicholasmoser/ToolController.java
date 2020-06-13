package com.github.nicholasmoser;

import com.github.nicholasmoser.tools.GenericFPKRepacker;
import java.util.List;
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

  private static final String GENERIC_FPK_REPACKER_GC = "Generic FPK Repacker (GameCube)";
  private static final String GENERIC_FPK_REPACKER_WII = "Generic FPK Repacker (Wii)";

  @FXML
  private ListView<String> tools;

  public void init() {
    List<String> items = tools.getItems();
    items.add(GENERIC_FPK_REPACKER_GC);
    items.add(GENERIC_FPK_REPACKER_WII);
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
      switch(tool) {
        case GENERIC_FPK_REPACKER_GC:
          GenericFPKRepacker.genericFPKRepackGamecube();
          break;
        case GENERIC_FPK_REPACKER_WII:
          GenericFPKRepacker.genericFPKRepackWii();
          break;
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error was encountered when running the tool.", e);
    }
  }
}
