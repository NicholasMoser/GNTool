package com.github.nicholasmoser;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * A class to create and display message windows to the user.
 */
public class Message {

  /**
   * Display an error window to the user with the given header and message.
   * 
   * @param header The header of the error window.
   * @param message The message of the error window.
   */
  public static void error(String header, String message) {
    Alert alert = new Alert(AlertType.ERROR, message);
    alert.setTitle("Error");
    alert.setHeaderText(header);
    alert.showAndWait();
  }
  
  /**
   * Display an info window to the user with the given header and message.
   * 
   * @param header The header of the info window.
   * @param message The message of the info window.
   */
  public static void info(String header, String message) {
    Alert alert = new Alert(AlertType.INFORMATION, message);
    alert.setTitle("Info");
    alert.setHeaderText(header);
    alert.showAndWait();
  }
}
