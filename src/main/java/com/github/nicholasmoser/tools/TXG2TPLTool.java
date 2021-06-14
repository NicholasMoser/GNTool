package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.graphics.TXG2TPL;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A tool to run TXG2TPL.
 */
public class TXG2TPLTool {

  /**
   * Runs the TXG2TPL tool.
   *
   * @throws IOException If an I/O error occurs
   */
  public static void run() throws IOException {
    Optional<Path> inputPath = Choosers.getInputTXG(GNTool.USER_HOME);
    if (inputPath.isEmpty()) {
      return;
    }
    Path input = inputPath.get();
    Optional<Path> outputPath = Choosers.getOutputDirectory(input.getParent().toFile());
    if (inputPath.isEmpty()) {
      return;
    }
    Path output = outputPath.get();
    TXG2TPL.unpack(input, output);
    Desktop.getDesktop().open(output.toFile());
  }
}
