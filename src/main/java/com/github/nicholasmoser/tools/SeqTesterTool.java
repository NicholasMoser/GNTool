package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.gnt4.seq.ext.SeqTester;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.IOException;
import java.nio.file.Path;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A tool to test seq opcodes and bytecode. This will allow a user to convert between the two.
 */
public class SeqTesterTool {

  /**
   * Open an SEQ tester without a reference SEQ file.
   *
   * @throws IOException If an I/O error occurs.
   */
  public static void open() throws IOException {
    open(null);
  }

  /**
   * Open an SEQ tester with a reference SEQ file.
   *
   * @throws IOException If an I/O error occurs.
   */
  public static void open(Path seqPath) throws IOException {
    FXMLLoader loader = new FXMLLoader(SeqEditor.class.getResource("tester.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    SeqTester seqTester = loader.getController();
    Stage stage = new Stage();
    seqTester.init(stage, seqPath);
    GUIUtils.setIcons(stage);
    stage.setScene(scene);
    stage.setTitle("SEQ Tester");
    stage.centerOnScreen();
    stage.show();
  }
}
