package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.ProcessUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Wrapper class for Struggleton's TXG2TPL.exe. https://github.com/Struggleton/TXG2TPL An (un)packer
 * for Naruto GNT4's TXG files to a more editable format.
 */
public class TXG2TPL {

  /**
   * Unpacks a .txg file to a directory. The output files will be .tpl.
   *
   * @param inputFile       The input .txg file.
   * @param outputDirectory The output directory to put the .tpl files.
   * @return The output text of TXG2TPL.exe
   * @throws IOException If an I/O error occurs.
   */
  public static String unpack(Path inputFile, Path outputDirectory) throws IOException {
    if (!Files.isRegularFile(inputFile)) {
      throw new IOException(inputFile + " is not a readable file.");
    } else if (!Files.isDirectory(outputDirectory)) {
      throw new IOException(outputDirectory + " is not a valid directory.");
    }
    try {
      Process process = new ProcessBuilder("TXG2TPL.exe", "-u", inputFile.toString(),
          outputDirectory.toString()).start();
      String output = ProcessUtils.readOutputFromProcess(process);
      process.waitFor();
      return output;
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * Packs a directory of .tpl files into a .txg file.
   *
   * @param inputDirectory The input directory of .tpl files.
   * @param outputFile     The output .txg file.
   * @return The output text of TXG2TPL.exe
   * @throws IOException If an I/O error occurs.
   */
  public static String pack(Path inputDirectory, Path outputFile) throws IOException {
    if (!Files.isDirectory(inputDirectory)) {
      throw new IOException(inputDirectory + " is not a valid directory.");
    }
    try {
      Process process = new ProcessBuilder("TXG2TPL.exe", "-p", inputDirectory.toString(),
          outputFile.toString()).start();
      String output = ProcessUtils.readOutputFromProcess(process);
      process.waitFor();
      return output;
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
}
