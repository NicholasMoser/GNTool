package com.github.nicholasmoser.gnt4;

import com.google.common.base.Verify;

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
   * Converts a mod ready expected file path to the extracted GNT4 file path. Mod ready paths are
   * the file paths expected when running the game with files extracted from their respective FPK
   * files.
   * 
   * @param filePath The file path to get the extracted file path for.
   * @return The extracted file path.
   */
  public static String fromModReadyPath(String filePath) {
    Verify.verifyNotNull(filePath);
    filePath = filePath.replace("files", "fpack");
    filePath = filePath.replace("fpack/audio", "audio");
    filePath = filePath.replace("fpack/movie", "movie");
    filePath = filePath.replace("fpack/opening.bnr", "opening.bnr");
    switch (filePath) {
      case "sys/apploader.img":
        filePath = "&&systemdata/AppLoader.ldr";
        break;
      case "sys/main.dol":
        filePath = "&&systemdata/Start.dol";
        break;
      case "sys/fst.bin":
        filePath = "&&systemdata/Game.toc";
        break;
      case "sys/ISO.hdr":
        filePath = "&&systemdata/ISO.hdr";
        break;
      case "fpack/vs/gar/旧/4201.txg":
        filePath = "fpack/gar/旧/4201.txg";
        break;
      case "fpack/vs/ino/長/4201.txg":
        filePath = "fpack/ino/長/4201.txg";
        break;
      case "fpack/vs/kan/旧/4201.txg":
        filePath = "fpack/kan/旧/4201.txg";
        break;
      case "fpack/vs/sak/長/4201.txg":
        filePath = "fpack/sak/長/4201.txg";
        break;
      case "fpack/vs/sik/旧/4201.txg":
        filePath = "fpack/sik/旧/4201.txg";
        break;
      case "fpack/vs/tem/旧/4201.txg":
        filePath = "fpack/tem/旧/4201.txg";
        break;
      default:
        break;
    }
    return filePath;
  }

  /**
   * Converts an extracted GNT4 file path to the mod ready expected file path. Mod ready paths are
   * the file paths expected when running the game with files extracted from their respective FPK
   * files.
   * 
   * @param filePath The file path to get the mod ready path for.
   * @return The mod ready path.
   */
  public static String toModReadyPath(String filePath) {
    Verify.verifyNotNull(filePath);
    filePath = filePath.replace("fpack", "files");
    filePath = filePath.replace("audio", "files/audio");
    filePath = filePath.replace("movie", "files/movie");
    filePath = filePath.replace("opening.bnr", "files/opening.bnr");
    switch (filePath) {
      case "&&systemdata/AppLoader.ldr":
        filePath = "sys/apploader.img";
        break;
      case "&&systemdata/Start.dol":
        filePath = "sys/main.dol";
        break;
      case "&&systemdata/Game.toc":
        filePath = "sys/fst.bin";
        break;
      case "&&systemdata/ISO.hdr":
        filePath = "sys/ISO.hdr";
        break;
      case "files/gar/旧/4201.txg":
        filePath = "files/vs/gar/旧/4201.txg";
        break;
      case "files/ino/長/4201.txg":
        filePath = "files/vs/ino/長/4201.txg";
        break;
      case "files/kan/旧/4201.txg":
        filePath = "files/vs/kan/旧/4201.txg";
        break;
      case "files/sak/長/4201.txg":
        filePath = "files/vs/sak/長/4201.txg";
        break;
      case "files/sik/旧/4201.txg":
        filePath = "files/vs/sik/旧/4201.txg";
        break;
      case "files/tem/旧/4201.txg":
        filePath = "files/vs/tem/旧/4201.txg";
        break;
      default:
        break;
    }
    return filePath;
  }

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
