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

/**
 * A tool to edit seq files. This will allow a user to branch from seq code locations to run
 *  an arbitrary amount of new code and branch back when complete, similar to a Gecko code
 *  injection.
 */
public class SeqEditorTool {

  private static File currentDirectory = GNTool.USER_HOME;

  /**
   * Query the user to open a seq file with the SeqEditorTool. Defaults to the user home but will
   * remember the last directory selected from for subsequent calls.
   *
   * @throws IOException If any I/O exception occurs.
   */
  public static void open() throws IOException {
    Optional<Path> optionalSeq = Choosers.getInputSeq(currentDirectory);
    if (optionalSeq.isEmpty()) {
      return;
    }
    Path seqPath = optionalSeq.get();
    currentDirectory = seqPath.getParent().toFile();
    open(seqPath);
  }

  /**
   * Opens a seq file with the SeqEditorTool.
   *
   * @param seqPath The seq to open.
   * @throws IOException If any I/O exception occurs.
   */
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
