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
  @Test
  public void test2() throws Exception {
    Path seqPath = Paths.get("D:/GNT/aaa/uncompressed/files/chr/iru/0000.seq");
    Path outputPath = Paths.get("D:/GNT/aaa/uncompressed/files/chr/iru/0000.html");
    SeqKing.generate(seqPath, outputPath);
  }
  @Test
  public void test3() throws Exception {
    Path seqPath = Paths.get("D:/GNT/aaa/uncompressed/files/chr/miz/0000.seq");
    Path outputPath = Paths.get("D:/GNT/aaa/uncompressed/files/chr/miz/0000.html");
    SeqKing.generate(seqPath, outputPath);
  }
}
