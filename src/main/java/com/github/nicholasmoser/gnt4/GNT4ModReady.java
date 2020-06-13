package com.github.nicholasmoser.gnt4;

/**
 * Class to handle making the GNT4 files mod ready. Mod ready in this case means that it can be run
 * as-is in Dolphin. GNT4 was programmed to be able to be run both with the files compressed into
 * FPK files AND not compressed in FPK files. The issue though is that when extracted from the FPK
 * files the structure is not the structure that the game expects to be able to run them
 * uncompressed. Therefore this class contains operations to make that the case. This mostly
 * involves changing the location of files and directory names.
 */
public class GNT4ModReady {

  /**
   * Fixes and returns filenames for compressed files that are cut off. When compressed into an FPK,
   * certain files will have their paths cut off. One such example can be found in seq0000.fpk which
   * has a compressed file with the name hr/ank/0000.seq that is not correct. The first directory
   * should be chr, not hr. Therefore this method returns the filename with this fixed.
   * 
   * @param fileName The file name to fix.
   * @return The fixed file name or original if no fix is required.
   */
  public static String fixBrokenFileName(String fileName) {
    if (fileName.startsWith("aki/")) {
      fileName = fileName.replace("aki/", "maki/");
    } else if (fileName.startsWith("ame/")) {
      fileName = fileName.replace("ame/", "game/");
    } else if (fileName.startsWith("g/")) {
      fileName = fileName.replace("g/", "stg/");
    } else if (fileName.startsWith("hr/")) {
      fileName = fileName.replace("hr/", "chr/");
    } else if (fileName.startsWith("ki/")) {
      fileName = fileName.replace("ki/", "maki/");
    } else if (fileName.startsWith("me/")) {
      fileName = fileName.replace("me/", "game/");
    } else if (fileName.startsWith("ru/")) {
      fileName = fileName.replace("ru/", "furu/");
    } else if (fileName.startsWith("te/")) {
      fileName = fileName.replace("te/", "unite/");
    } else if (fileName.startsWith("tg/")) {
      fileName = fileName.replace("tg/", "stg/");
    } else if (fileName.startsWith("uro/")) {
      fileName = fileName.replace("uro/", "kuro/");
    } else if (fileName.endsWith("4201.txg")) {
      fileName = "vs/" + fileName;
    }
    return fileName;
  }
}
