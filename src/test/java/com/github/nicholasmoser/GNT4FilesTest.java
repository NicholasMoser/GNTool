package com.github.nicholasmoser;

import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.gnt4.GNT4Files;

public class GNT4FilesTest {
  
  @Test
  public void test() {
    GNT4Files gnt4Files = GNT4Files.getInstance();
    gnt4Files.getParentFPK("lol");
  }
}
