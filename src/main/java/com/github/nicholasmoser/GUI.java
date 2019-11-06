package com.github.nicholasmoser;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * GUI utilities.
 */
public class GUI {
  /**
   * Sets the application icons on the stage.
   * 
   * @param primaryStage The primary stage to set the icons for.
   */
  public static void setIcons(Stage primaryStage) {
    ObservableList<Image> icons = primaryStage.getIcons();
    icons.add(new Image(GUI.class.getResourceAsStream("naru16.gif")));
    icons.add(new Image(GUI.class.getResourceAsStream("naru32.gif")));
    icons.add(new Image(GUI.class.getResourceAsStream("naru64.gif")));
    icons.add(new Image(GUI.class.getResourceAsStream("naru128.gif")));
  }
}
