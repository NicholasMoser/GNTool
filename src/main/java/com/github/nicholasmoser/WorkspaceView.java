package com.github.nicholasmoser;

import java.io.IOException;
import javafx.stage.Stage;

/**
 * A view for a GNTool Workspace.
 */
public interface WorkspaceView {

  public void init(Stage stage) throws IOException;
}
