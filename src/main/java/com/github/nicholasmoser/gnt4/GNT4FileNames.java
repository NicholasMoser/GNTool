package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.fpk.FileNames;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Class to help fix GNT4 file names extracted from fpk files.
 */
public class GNT4FileNames implements FileNames {

  @Override
  public String fix(String fileName) {
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

  @Override
  public String getCompressedName(String fileName) {
    try {
      byte[] bytes = fileName.getBytes("shift-jis");
      if (bytes.length < 16) {
        return fileName;
      }
      byte[] concatBytes = Arrays.copyOfRange(bytes, bytes.length - 15, bytes.length);
      return new String(concatBytes, "shift-jis");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
