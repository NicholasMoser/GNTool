package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.WorkspaceView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for the GNT4 workspace view. This window is determined by the
 * menu.fxml resource under com.github.nicholasmoser.gnt4
 */
public class GNT4WorkspaceView implements WorkspaceView {

  Workspace workspace;

  /**
   * Creates a new GNT4 workspace view.
   * 
   * @param workspace The workspace to use for the view.
   */
  public GNT4WorkspaceView(Workspace workspace) {
    this.workspace = workspace;
  }

  @Override
  public void init(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
    Scene scene = new Scene(loader.load());
    MenuController controller = loader.<MenuController>getController();
    controller.init(workspace);
    stage.setScene(scene);
    stage.setTitle("GNT4 Workspace");
    stage.centerOnScreen();
    stage.show();
  }
}
