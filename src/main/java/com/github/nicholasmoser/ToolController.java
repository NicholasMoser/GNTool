package com.github.nicholasmoser;

import com.github.nicholasmoser.tools.FPKRepackerTool;
import com.github.nicholasmoser.tools.FPKUnpackerTool;
import com.github.nicholasmoser.tools.ISOCompareTool;
import com.github.nicholasmoser.tools.ISOExtractorTool;
import com.github.nicholasmoser.tools.ISOPatcher;
import com.github.nicholasmoser.tools.TXG2TPLTool;
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

/**
 * UI controller for the list of tools.
 */
public class ToolController {

  private static final Logger LOGGER = Logger.getLogger(ToolController.class.getName());

  private static final String ISO_PATCHER_GC = "ISO Patcher (GameCube)";
  private static final String ISO_EXTRACTOR_GC = "ISO Extractor (GameCube)";
  private static final String ISO_COMPARE_GC = "ISO Compare (GameCube)";
  private static final String FPK_UNPACKER_GC = "FPK Unpacker (GameCube)";
  private static final String FPK_UNPACKER_WII = "FPK Unpacker (Wii)";
  private static final String FPK_UNPACKER_PS2 = "FPK Unpacker (PS2/PSP)";
  private static final String FPK_REPACKER_GC = "FPK Repacker (GameCube)";
  private static final String FPK_REPACKER_WII = "FPK Repacker (Wii)";
  private static final String TXG2TPL = "TXG2TPL";

  @FXML
  private ListView<String> tools;

  /**
   * Initialize the list of tools.
   */
  public void init() {
    List<String> items = tools.getItems();
    items.add(ISO_PATCHER_GC);
    items.add(ISO_EXTRACTOR_GC);
    items.add(ISO_COMPARE_GC);
    items.add(FPK_UNPACKER_GC);
    items.add(FPK_UNPACKER_WII);
    items.add(FPK_UNPACKER_PS2);
    items.add(FPK_REPACKER_GC);
    items.add(FPK_REPACKER_WII);
    items.add(TXG2TPL);
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
      switch (tool) {
        case FPK_REPACKER_GC -> FPKRepackerTool.repackGamecubeFPK();
        case FPK_REPACKER_WII -> FPKRepackerTool.repackWiiFPK();
        case ISO_EXTRACTOR_GC -> ISOExtractorTool.extractGameCubeISO();
        case FPK_UNPACKER_GC -> FPKUnpackerTool.unpackGamecubeFPK();
        case FPK_UNPACKER_WII -> FPKUnpackerTool.unpackWiiFPK();
        case FPK_UNPACKER_PS2 -> FPKUnpackerTool.unpackPS2FPK();
        case ISO_PATCHER_GC -> ISOPatcher.patchGameCubeISO();
        case ISO_COMPARE_GC -> ISOCompareTool.compareGameCubeISO();
        case TXG2TPL -> TXG2TPLTool.run();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An error was encountered when running the tool.", e);
    }
  }
}
