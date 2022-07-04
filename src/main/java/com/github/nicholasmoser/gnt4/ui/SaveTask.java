package com.github.nicholasmoser.gnt4.ui;

import java.util.List;
import javafx.concurrent.Task;

public class SaveTask<Void> extends Task<Void> {

  public void setValues(List<String> values) {
    throw new RuntimeException("Must override");
  }

  @Override
  protected Void call() throws Exception {
    throw new RuntimeException("Must override");
  }
}
