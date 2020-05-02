package com.github.nicholasmoser.iso;

/**
 * Constants for ISOs.
 *
 * http://hitmen.c02.at/files/yagcd/yagcd/chap13.html
 */
public class ISO {

  public static final int BOOT_BIN_POS = 0;
  public static final int BOOT_BIN_LEN = 0x440;
  public static final int BI_2_POS = 0x440;
  public static final int BI_2_LEN = 0x2000;
  public static final int APPLOADER_POS = 0x2440;
  public static final int DISC_SIZE = 0x57058000;

  /* Number of files and directories in the files directory in Naruto GNT4 */
  public static final int GNT4_ISO_ITEMS_SIZE = 563;
}
