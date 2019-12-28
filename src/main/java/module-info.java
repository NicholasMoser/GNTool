module com.github.nicholasmoser {
  exports com.github.nicholasmoser;
  exports com.github.nicholasmoser.gamecube;
  exports com.github.nicholasmoser.gnt4;
  exports com.github.nicholasmoser.utils;

  opens com.github.nicholasmoser.gnt4 to javafx.fxml;

  requires com.google.common;
  requires com.google.protobuf;

  requires java.desktop;
  requires java.logging;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;

  requires org.apache.commons.io;
}
