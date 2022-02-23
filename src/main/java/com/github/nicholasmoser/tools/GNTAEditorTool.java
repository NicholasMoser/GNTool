package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.gnt4.mot.GNTAEditor;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GNTAEditorTool {

  private static File currentDirectory = GNTool.USER_HOME;

  public static void open() throws IOException {
    Optional<Path> optionalGnta = Choosers.getInputGnta(currentDirectory);
    if (optionalGnta.isEmpty()) {
      return;
    }
    Path gntaPath = optionalGnta.get();
    currentDirectory = gntaPath.getParent().toFile();
    open(gntaPath);
  }

  public static void open(Path gntaPath) throws IOException {
    FXMLLoader loader = new FXMLLoader(GNTAEditor.class.getResource("motedit.fxml"));
    Scene scene = new Scene(loader.load());
    GUIUtils.initDarkMode(scene);
    GNTAEditor gntaEditor = loader.getController();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);
    gntaEditor.init(gntaPath, stage);
    stage.setScene(scene);
    stage.setTitle("GNTA Editor: " + gntaPath.getFileName());
    stage.centerOnScreen();
    stage.show();
  }
}
