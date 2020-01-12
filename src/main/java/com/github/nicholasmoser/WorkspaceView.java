package com.github.nicholasmoser;

import java.io.IOException;
import javafx.stage.Stage;

/**
 * A view for a GNTool Workspace.
 */
public interface WorkspaceView {

  /**
   * Initializes the workspace view.
   * 
   * @param stage The stage to use for the workspace view.
   * @throws IOException If any I/O exception occurs.
   */
  void init(Stage stage) throws IOException;
}
