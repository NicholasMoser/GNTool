package com.github.nicholasmoser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.gnt4.GNT4Files;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;


public class GNT4FilesTest {

  private static GNT4Files files;

  // /**
  // * Load the data file for the GNT4 files.
  // */
  // @BeforeAll
  // public static void setUp() {
  // files = new GNT4Files();
  // }

  /**
   * Tests that you can successfully get the parent fpk of a child file.
   */
  @Test
  @Disabled("Not ready yet.")
  public void testGetParentFPK() {
    String parent = files.getParentFPK("fpack\\chr\\hr\\ita\\0000.mot");
    Verify.verify(parent.equals("fpack\\chr\\ita1000.fpk"));
  }

  /**
   * Tests that you can successfully get the children of an fpk file.
   */
  @Test
  @Disabled("Not ready yet.")
  public void testGetFPKChildren() {
    String[] children = files.getFPKChildren("fpack\\chr\\ita1000.fpk");
    Verify.verify(children.length == 26);
    Verify.verify(Arrays.stream(children).anyMatch("fpack\\chr\\hr\\ita\\0000.mot"::equals));
  }

  /**
   * Tests the check for whether or not a child file is compressed.
   */
  @Test
  @Disabled("Not ready yet.")
  public void testChildCompressedCheck() {
    Verify.verify(files.isChildCompressed("hr/ita/0000.mot"));
    Verify.verify(!files.isChildCompressed("hr/ank/3000.sam"));
  }

  /**
   * Tests the crc32 comparison method.
   */
  @Test
  @Disabled("Not ready yet.")
  public void testCrc32() {
    Map<String, String> comparisonCRC32Values = Maps.newHashMapWithExpectedSize(2);

    // Unchanged
    comparisonCRC32Values.put("fpack\\chr\\hr\\ita\\0000.mot", "c72d12fa");

    // Changed
    comparisonCRC32Values.put("fpack\\chr\\hr\\ita\\0001.mot", "ffffffff");

    List<String> filesChanged = files.getFilesChanges(comparisonCRC32Values);
    Verify.verify(filesChanged.size() == 1);
    Verify.verify(filesChanged.get(0).equals("fpack\\chr\\hr\\ita\\0001.mot"));
  }
}
