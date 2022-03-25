package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.gnt4.seq.DolphinSeqListener;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.IOException;
import java.nio.file.Path;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A tool to view SEQ file execution from Dolphin.
 */
public class DolphinSeqListenerTool {


  /**
   * Run the Dolphin SEQ Listener tool.
   *
   * @throws IOException If an I/O error occurs.
   */
  public static void run() throws IOException {
    run(null);
  }

  /**
   * Run the Dolphin SEQ Listener tool with the given GNT4 uncompressed files.
   *
   * @param gnt4Files Path to GNT4 uncompressed files or null if unknown.
   * @throws IOException If an I/O error occurs.
   */
  public static void run(Path gnt4Files) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        DolphinSeqListener.class.getResource("dolphin_seq_listener.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    DolphinSeqListener seqEditor = loader.getController();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);
    seqEditor.init(stage, gnt4Files);
    stage.setScene(scene);
    stage.setTitle("SEQ Dolphin Listener");
    stage.centerOnScreen();
    stage.setOnCloseRequest(event -> seqEditor.killListener());
    stage.show();
  }
}
