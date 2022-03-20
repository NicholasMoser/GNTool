package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.gnt4.seq.DolphinSeqListener;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DolphinSeqListenerTool {

  public static void run() throws IOException {

    FXMLLoader loader = new FXMLLoader(DolphinSeqListener.class.getResource("dolphin_seq_listener.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    DolphinSeqListener seqEditor = loader.getController();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);
    seqEditor.init(stage);
    stage.setScene(scene);
    stage.setTitle("SEQ Dolphin Listener");
    stage.centerOnScreen();
    stage.setOnCloseRequest(event -> {
      seqEditor.killListener();
    });
    stage.show();
  }
}
