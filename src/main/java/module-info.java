module com.github.nicholasmoser {
  exports com.github.nicholasmoser;
  exports com.github.nicholasmoser.gamecube;
  exports com.github.nicholasmoser.gnt4;
  exports com.github.nicholasmoser.utils;
  exports com.github.nicholasmoser.fpk;

  opens com.github.nicholasmoser to javafx.fxml;
  opens com.github.nicholasmoser.gnt4 to javafx.fxml;
  opens com.github.nicholasmoser.gnt4.seq.ext to javafx.fxml;
  opens com.github.nicholasmoser.gnt4.mot to javafx.fxml;

  requires com.google.common;
  requires com.google.protobuf;

  requires java.desktop;
  requires java.logging;
  requires java.net.http;
  requires jdk.crypto.ec;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;

  requires org.json;
  requires j2html;
}
