package com.github.nicholasmoser;

/**
 * Launches GNTool GUI application. Needed for uber jar to run.
 * https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing
 */
public class Main {
  public static void main(String[] args) {
    GNTool.main(args);
}
}
