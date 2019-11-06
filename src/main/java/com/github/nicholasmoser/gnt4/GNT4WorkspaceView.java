package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.WorkspaceView;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.text.Text;

public class GNT4WorkspaceView implements WorkspaceView
{
	Workspace workspace;
	
	public GNT4WorkspaceView(Workspace workspace)
	{
		this.workspace = workspace;
	}
	
	public void init()
	{
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("GNT4 Workspace");
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Example workspace."));
        Scene dialogScene = new Scene(dialogVbox, 800, 800);
	    stage.setScene(dialogScene);
	    stage.show();
	}
}
