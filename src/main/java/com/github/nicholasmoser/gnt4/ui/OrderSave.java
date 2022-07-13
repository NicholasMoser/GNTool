package com.github.nicholasmoser.gnt4.ui;

import java.util.List;

public class OrderSave implements Runnable {

  public void setValues(List<String> values) {
    throw new RuntimeException("Must override");
  }

  @Override
  public void run() {
    throw new RuntimeException("Must override");
  }
}
