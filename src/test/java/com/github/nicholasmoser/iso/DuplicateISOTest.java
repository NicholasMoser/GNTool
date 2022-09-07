package com.github.nicholasmoser.iso;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.CRC32;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Integration test that duplicates an existing copy of GNT4.
 */
@TestMethodOrder(OrderAnnotation.class)
public class DuplicateISOTest {

  private static Path gnt4Path;
  private static Path testDirectory;
  private static Path testIso;

  @BeforeAll
  public static void init() throws Exception {
    gnt4Path = Prereqs.getGNT4Iso();
    String unique = Long.toString(System.currentTimeMillis());
    testDirectory = Files.createDirectory(Paths.get(unique));
    testIso = Files.createFile(Paths.get(unique + ".iso"));
  }

  @AfterAll
  public static void cleanup() throws Exception {
    if (testIso != null) {
      Files.deleteIfExists(testIso);
    }
    if (testDirectory != null) {
      MoreFiles.deleteRecursively(testDirectory, RecursiveDeleteOption.ALLOW_INSECURE);
    }
  }

  /**
   * Parse an ISO to retrieve the ISOHeader and then validate the contents of the ISOHeader.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Order(1)
  public void parseISOHeaderFromISO() throws Exception {
    ISOParser parser = new ISOParser(gnt4Path);
    ISOHeader isoHeader = parser.getISOHeader();
    validateHeaderFiles(isoHeader);
  }

  /**
   * Extracts an ISO to a directory.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Order(2)
  public void extractISOToDirectory() throws Exception {
    ISOExtractor extractor = new ISOExtractor(gnt4Path, testDirectory);
    extractor.extract();
  }

  /**
   * Parse an ISO to retrieve an ISOHeader and parse the directory from the previous test to
   * retrieve an ISOHeader. Validate the two ISOHeaders are equals.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Order(3)
  public void compareParsers() throws Exception {
    ISOParser isoParser = new ISOParser(gnt4Path);
    ISOHeader isoParserHeader = isoParser.getISOHeader();
    DirectoryParser dirParser = new DirectoryParser(testDirectory, true);
    ISOHeader dirParserHeader = dirParser.getISOHeader();
    assertEquals(isoParserHeader, dirParserHeader);
  }

  /**
   * Create a new ISO from the extracted directory. Validate that the new ISO is the same as the
   * original ISO.
   *
   * @throws Exception If any exception occurs.
   */
  @Test
  @Order(4)
  public void createISOFromDirectory() throws Exception {
    DirectoryParser dirParser = new DirectoryParser(testDirectory, true);
    ISOHeader isoHeader = dirParser.getISOHeader();
    ISOCreator creator = new ISOCreator(testDirectory, testIso);
    creator.create(true, isoHeader);
    validateISO(testIso);
  }

  /**
   * Validate the ISO is the correct size and hash for a vanilla ISO.
   *
   * @param testIso The ISO to validate.
   * @throws IOException If an I/O error occurs
   */
  private void validateISO(Path testIso) throws IOException {
    assertEquals(ISO.DISC_SIZE, Files.size(testIso));
    int hash = CRC32.getHash(testIso);
    assertEquals(0x55EE8B1A, hash, "Hash of new ISO does not match original");
  }

  /**
   * Validate the header file values are correct for a vanilla ISO.
   *
   * @param isoHeader The ISOHeader.
   */
  private void validateHeaderFiles(ISOHeader isoHeader) {
    ISOFile bootBin = isoHeader.getBootBin();
    assertEquals(ISO.BOOT_BIN_LEN, bootBin.getLen());
    ISOFile bi2Bin = isoHeader.getBi2Bin();
    assertEquals(ISO.BI_2_LEN, bi2Bin.getLen());
    ISOFile apploaderImg = isoHeader.getApploaderImg();
    assertEquals(0x1DE58, apploaderImg.getLen());
    ISOFile mainDol = isoHeader.getMainDol();
    assertEquals(0x224A00, mainDol.getLen());
    ISOFile fstBin = isoHeader.getFstBin();
    assertEquals(0x33E7, fstBin.getLen());
    List<ISOItem> files = isoHeader.getFiles();
    assertEquals(ISO.GNT4_ISO_ITEMS_SIZE, files.size());
  }
}
