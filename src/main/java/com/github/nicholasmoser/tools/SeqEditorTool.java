package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SeqEditorTool {

  private static File currentDirectory = GNTool.USER_HOME;

  public static void editSeq() throws IOException {
    editSeq(currentDirectory);
  }

  public static void editSeq(File initialDirectory) throws IOException {
    Optional<Path> optionalSeq = Choosers.getInputSeq(initialDirectory);
    if (optionalSeq.isEmpty()) {
      return;
    }
    Path seqPath = optionalSeq.get();
    FXMLLoader loader = new FXMLLoader(SeqEditor.class.getResource("seqedit.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    SeqEditor seqEditor = loader.getController();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);
    seqEditor.init(seqPath, stage);
    stage.setScene(scene);
    stage.setTitle("GNT4 Workspace");
    stage.centerOnScreen();
    stage.show();
  }
}
