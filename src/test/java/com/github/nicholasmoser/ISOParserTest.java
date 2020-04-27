package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.iso.DirectoryParser;
import com.github.nicholasmoser.iso.FileSystemTable;
import com.github.nicholasmoser.iso.ISOCreator;
import com.github.nicholasmoser.iso.ISOExtractor;
import com.github.nicholasmoser.iso.ISOHeader;
import com.github.nicholasmoser.iso.ISOItem;
import com.github.nicholasmoser.iso.ISOParser;
import com.google.common.base.Stopwatch;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class ISOParserTest {

  /**
   * Parses and prints the ISOHeader from an ISO.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void parseISOHeaderFromISO() throws Exception {
    Path isoPath = Paths.get("D:/GNT/GNT4.iso");
    Stopwatch watch = Stopwatch.createStarted();
    ISOParser parser = new ISOParser(isoPath);
    ISOHeader isoHeader = parser.getISOHeader();
    System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println(isoHeader);
  }

  /**
   * Extracts an ISO to a directory.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void extractISOToDirectory() throws Exception {
    Path isoPath = Paths.get("D:/GNT/GNT4.iso");
    Path outputPath = Paths.get("D:/GNT/Testing");
    Stopwatch watch = Stopwatch.createStarted();
    ISOExtractor extractor = new ISOExtractor(isoPath, outputPath);
    extractor.extract();
    System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
  }

  /**
   * Parses the ISOHeader from an ISO and creates a new duplicate ISO from it.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void createDuplicateISOFromHeader() throws Exception {
    Path inputPath = Paths.get("D:/GNT/Testing");
    Path inputIso = Paths.get("D:/GNT/GNT4.iso");
    Path outputIso = Paths.get("D:/GNT/test.iso");
    ISOParser parser = new ISOParser(inputIso);
    ISOHeader isoHeader = parser.getISOHeader();
    System.out.println(isoHeader);
    ISOCreator creator = new ISOCreator(inputPath, outputIso);
    creator.create(isoHeader);
  }

  /**
   * Creates an ISO from a directory.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void createISOFromDirectory() throws Exception {
    Path inputPath = Paths.get("D:/GNT/Testing");
    Path outputIso = Paths.get("D:/GNT/nickmoser.iso");
    DirectoryParser parser = new DirectoryParser(inputPath);
    ISOHeader isoHeader = parser.getISOHeader();
    System.out.println(isoHeader);
    ISOCreator creator = new ISOCreator(inputPath, outputIso);
    creator.create(isoHeader);
  }

  @Test
  public void compareParsers() throws Exception {
    Path isoPath = Paths.get("D:/GNT/GNT4.iso");
    ISOParser isoParser = new ISOParser(isoPath);
    ISOHeader isoHeader1 = isoParser.getISOHeader();
    Path inputPath = Paths.get("D:/GNT/Testing");
    DirectoryParser dirParser = new DirectoryParser(inputPath);
    ISOHeader isoHeader2 = dirParser.getISOHeader();
    assertEquals(isoHeader1, isoHeader2);
  }

  /**
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  public void rewriteHeader() throws Exception {
    Path inputPath = Paths.get("D:/GNT/Testing");
    DirectoryParser parser = new DirectoryParser(inputPath);
    ISOHeader isoHeader = parser.getISOHeader();
    System.out.println(isoHeader);
    FileSystemTable.rewrite(inputPath, isoHeader);
  }

  @Test
  public void ok() throws Exception {
    try(RandomAccessFile raf = new RandomAccessFile(new File("D:/GNT/Testing/sys/fst.bin"), "r")) {
      List<ISOItem> isoItems = FileSystemTable.read(raf);
      for (ISOItem item : isoItems)
      {
        System.out.println(item);
      }
    }
  }
}
