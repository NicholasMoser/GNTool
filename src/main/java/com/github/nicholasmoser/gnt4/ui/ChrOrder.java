package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChrOrder {

  public static final long CSS_CHR_ID_ORDER = 0x210F7C;
  public static final long CSS_CHR_IDS = 0x211018;

  /**
   * Read the css chr id order from the given dol RandomAccessFile.
   *
   * @param raf The dol RandomAccessFile.
   * @return The css chr id order.
   * @throws IOException If any I/O exception occurs.
   */
  public static List<Integer> readCssChrIdOrder(RandomAccessFile raf) throws IOException {
    raf.seek(ChrOrder.CSS_CHR_ID_ORDER);
    List<Integer> cssChrIds = new ArrayList<>(39);
    for (int i = 0; i < 39; i++) {
      cssChrIds.add(ByteUtils.readInt32(raf));
    }
    return cssChrIds;
  }

  /**
   * Read the css chr ids from the given dol RandomAccessFile.
   *
   * @param raf The dol RandomAccessFile.
   * @return The list of css chr ids.
   * @throws IOException If any I/O exception occurs.
   */
  public static List<Integer> readCssChrIds(RandomAccessFile raf) throws IOException {
    raf.seek(ChrOrder.CSS_CHR_IDS);
    List<Integer> cssChrIds = new ArrayList<>(39);
    for (int i = 0; i < 39; i++) {
      cssChrIds.add(ByteUtils.readInt32(raf));
    }
    return cssChrIds;
  }

  /**
   * Get the chr id for a character name.
   *
   * @param character The character name.
   * @return The chr id.
   * @throws IOException If any I/O exception occurs.
   */
  public static int getChrId(String character) throws IOException {
    int id = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
    if (id == -1) {
      throw new IOException("Could not find character " + character);
    }
    return id;
  }

  /**
   * Given a chr id, return the index in a list of css chr ids the index of that chr id.
   *
   * @param id The chr id to find the index of.
   * @param cssChrIds The list of chr ids.
   * @return The index of the chr id in the list of chr ids.
   * @throws IOException If any I/O exception occurs.
   */
  public static int getChrIdIndex(int id, List<Integer> cssChrIds) throws IOException {
    for (int i = 0; i < cssChrIds.size(); i++) {
      if (id == cssChrIds.get(i)) {
        return i;
      }
    }
    throw new IOException("Could not find character id " + id);
  }

  public static List<String> getCurrentChrOrder(Path dolPath) throws IOException {
    try(RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "r")) {
      List<Integer> cssChrIdOrder = readCssChrIdOrder(raf);
      List<Integer> cssChrIds = readCssChrIds(raf);
      List<String> characters = new ArrayList<>(39);
      for (int i = 0; i < 39; i++) {
        int chrId = cssChrIds.get(cssChrIdOrder.get(i));
        characters.add(GNT4Characters.INTERNAL_CHAR_ORDER.inverse().get(chrId));
      }
      return characters;
    }
  }
}
