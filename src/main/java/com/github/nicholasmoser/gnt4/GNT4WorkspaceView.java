package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.WorkspaceView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GNT4WorkspaceView implements WorkspaceView {

  Workspace workspace;

  public GNT4WorkspaceView(Workspace workspace) {
    this.workspace = workspace;
  }

  public void init(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("GNT4 Workspace");
    stage.show();
  }
}
