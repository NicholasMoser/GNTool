package com.github.nicholasmoser;

import com.github.nicholasmoser.iso.ISOExtractor;
import com.github.nicholasmoser.iso.ISOItem;
import com.github.nicholasmoser.iso.ISOParser;
import com.google.common.base.Stopwatch;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ISOParserTest {

  @Test
  public void test() throws Exception {
    Path isoPath = Paths.get("D:/GNT/GNT4.iso");
    Stopwatch watch = Stopwatch.createStarted();
    ISOParser parser = new ISOParser(isoPath);
    List<ISOItem> tableOfContents = parser.getISOItems();
    System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println(tableOfContents);
  }

  @Test
  public void test2() throws Exception {
    Path isoPath = Paths.get("D:/GNT/GNT4.iso");
    Path outputPath = Paths.get("D:/GNT/Test");
    Stopwatch watch = Stopwatch.createStarted();
    ISOExtractor extractor = new ISOExtractor(isoPath, outputPath);
    extractor.extract();
    System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
  }
}
