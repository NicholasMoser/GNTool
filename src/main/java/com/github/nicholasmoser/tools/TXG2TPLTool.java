package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.graphics.TXG2TPL;
import java.io.File;
import javafx.concurrent.Task;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A tool to run TXG2TPL.
 */
public class TXG2TPLTool {

  private static final Logger LOGGER = Logger.getLogger(TXG2TPLTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  /**
   * Runs the TXG2TPL tool.
   *
   * @throws IOException If an I/O error occurs
   */
  public static void run() throws IOException {
    Optional<Path> inputPath = Choosers.getInputTXG(currentDirectory);
    if (inputPath.isEmpty()) {
      return;
    }
    Path input = inputPath.get();
    Optional<Path> outputPath = Choosers.getOutputDirectory(input.getParent().toFile());
    if (outputPath.isEmpty()) {
      return;
    }
    Path output = outputPath.get();
    currentDirectory = input.getParent().toFile();
    TXG2TPL.unpack(input, output);
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(output.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable,oldValue, e) -> {
      if (e!=null){
        LOGGER.log(Level.SEVERE, "Failed to Open Directory", e.getMessage());
      }
    });
    new Thread(task).start();
  }
}
