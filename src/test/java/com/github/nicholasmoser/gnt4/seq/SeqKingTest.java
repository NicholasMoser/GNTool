package com.github.nicholasmoser.gnt4.seq;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqKingTest {
  @Test
  public void test() throws Exception {
    Path seqPath = Paths.get("D:/GNT/aaa/uncompressed/files/maki/char_sel.seq");
    Path outputPath = Paths.get("D:/GNT/aaa/uncompressed/files/maki/test.html");
    SeqKing.generate(seqPath, outputPath);
  }
}
