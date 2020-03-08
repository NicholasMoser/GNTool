package com.github.nicholasmoser;

import com.github.nicholasmoser.iso.ISOParser;
import com.github.nicholasmoser.iso.TOC;
import com.google.common.base.Stopwatch;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ISOParserTest {

  @Test
  public void test() throws Exception {
    Path iso = Paths.get("D:/GNT/GNT4.iso");
    Stopwatch watch = Stopwatch.createStarted();
    ISOParser parser = new ISOParser(iso);
    TOC tableOfContents = parser.getTOC();
    System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println(tableOfContents);
  }
}
