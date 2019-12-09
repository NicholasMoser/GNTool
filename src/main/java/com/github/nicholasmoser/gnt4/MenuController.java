package com.github.nicholasmoser.gnt4;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {
  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

  /**
   * Refreshes the current workspace for any changes having occurred outside of GNTool.
   * @param event The action event.
   */
  @FXML
  protected void refresh(ActionEvent event) {
    System.out.println("refresh");
  }

  /**
   * Builds the GNT4 ISO for the current workspace.
   * @param event The action event.
   */
  @FXML
  protected void build(ActionEvent event) {
    File isoFile = Choosers.getOutputISO(GNTool.USER_HOME);
    System.out.println(isoFile);
  }

  /**
   * Quits GNTool.
   * @param event The action event.
   */
  @FXML
  protected void quit(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Opens the Github repository web page for GNTool, which serves as the about page.
   * @param event The action event.
   */
  @FXML
  protected void about(ActionEvent event) {
    try {
      Desktop.getDesktop().browse(new URI("https://github.com/NicholasMoser/GNTool"));
    } catch (IOException | URISyntaxException e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
      Message.error("Error Opening About Page", e.getMessage());
    }
  }
}
