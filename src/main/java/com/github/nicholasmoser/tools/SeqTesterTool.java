package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.gnt4.seq.ext.SeqTester;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A tool to test seq opcodes and bytecode. This will allow a user to convert between the two.
 */
public class SeqTesterTool {

  /**
   * Opens a seq file with the SeqTesterTool.
   *
   * @throws IOException If any I/O exception occurs.
   */
  public static void open() throws IOException {
    FXMLLoader loader = new FXMLLoader(SeqEditor.class.getResource("tester.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    SeqTester seqTester = loader.getController();
    Stage stage = new Stage();
    seqTester.init(stage);
    GUIUtils.setIcons(stage);
    stage.setScene(scene);
    stage.setTitle("SEQ Tester");
    stage.centerOnScreen();
    stage.show();
  }
}
