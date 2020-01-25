package com.github.nicholasmoser.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utilities for working with a Process in Java.
 */
public class ProcessUtils {

  /**
   * When using a Process, sometimes the application will hang on Process.waitFor() This can occur
   * if the output fills the buffer, waiting for the application to read the output. This method
   * will read lines from the process input stream to prevent this. See
   * https://stackoverflow.com/questions/5483830/process-waitfor-never-returns
   *
   * @param process The Process to read the output from.
   * @return The output of the process.
   * @throws IOException If an I/O error occurs.
   */
  public static String readOutputFromProcess(Process process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuilder output = new StringBuilder();
    String line = reader.readLine();
    while (line != null) {
      output.append(line);
      output.append('\n');
      line = reader.readLine();
    }
    return output.toString();
  }
}
