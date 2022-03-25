package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities to deal with the fact that Java on Windows will not open a local HTML document with a
 * fragment as per https://bugs.openjdk.java.net/browse/JDK-7073184
 */
public class Browser {
  private static final Logger LOGGER = Logger.getLogger(Browser.class.getName());

  /**
   * Open the given path with a web browser. First will try Firefox and then Chrome.
   *
   * @param path The path to open.
   * @throws IOException If any I/O exception occurs.
   */
  public static void open(String path) throws IOException {
    String browser = findFirefox();
    if (browser == null) {
      browser = findChrome();
      if (browser == null) {
        throw new IOException("Unable to find firefox or chrome in C:/Program Files and C:/Program Files (x86)");
      }
    }
    try {
      Process process = new ProcessBuilder(browser, path).start();
      process.waitFor();
      String output = new String(process.getInputStream().readAllBytes());
      if (!output.isBlank()) {
        LOGGER.log(Level.INFO, "Browser Output: " + output);
      }
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * Attempts to find Chrome in Program Files.
   *
   * @return The path to Chrome or null if it cannot be found.
   */
  private static String findChrome() {
    Path firstTry = Paths.get("C:/Program Files/Google/Chrome/Application/chrome.exe");
    if (Files.exists(firstTry)) {
      return firstTry.toString();
    }
    Path secondTry = Paths.get("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
    if (Files.exists(secondTry)) {
      return secondTry.toString();
    }
    return null;
  }

  /**
   * Attempts to find Firefox in Program Files.
   *
   * @return The path to Firefox or null if it cannot be found.
   */
  private static String findFirefox() {
    Path firstTry = Paths.get("C:/Program Files/Mozilla Firefox/firefox.exe");
    if (Files.exists(firstTry)) {
      return firstTry.toString();
    }
    Path secondTry = Paths.get("C:/Program Files (x86)/Mozilla Firefox/firefox.exe");
    if (Files.exists(secondTry)) {
      return secondTry.toString();
    }
    return null;
  }
}
