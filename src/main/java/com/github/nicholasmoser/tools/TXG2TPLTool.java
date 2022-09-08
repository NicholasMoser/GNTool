package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.graphics.TXG2TPL;
import com.github.nicholasmoser.utils.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A tool to run TXG2TPL.
 */
public class TXG2TPLTool {

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
    GUIUtils.open(output);
  }
}
