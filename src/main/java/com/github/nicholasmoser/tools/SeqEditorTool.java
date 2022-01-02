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

  public static void open() throws IOException {
    Optional<Path> optionalSeq = Choosers.getInputSeq(currentDirectory);
    if (optionalSeq.isEmpty()) {
      return;
    }
    Path seqPath = optionalSeq.get();
    currentDirectory = seqPath.getParent().toFile();
    open(seqPath);
  }

  public static void open(Path seqPath) throws IOException {
    FXMLLoader loader = new FXMLLoader(SeqEditor.class.getResource("seqedit.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    SeqEditor seqEditor = loader.getController();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);
    seqEditor.init(seqPath, stage);
    stage.setScene(scene);
    stage.setTitle("SEQ Editor: " + seqPath.getFileName());
    stage.centerOnScreen();
    stage.show();
  }
}
