package com.github.nicholasmoser.gnt4.seq;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqReporterTest {
  @Test
  public void test() throws Exception {
    Path seqPath = Paths.get("D:/GNT/aaa/uncompressed/files/maki/char_sel.seq");
    Path outputPath = Paths.get("D:/GNT/aaa/uncompressed/files/maki/test.html");
    Path executionPath = Paths.get("D:/test.log");
    SeqReporter.generate(seqPath, outputPath, executionPath);
  }
}
