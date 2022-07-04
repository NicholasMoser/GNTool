package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChrOrderSave extends OrderSave {
  private static final Logger LOGGER = Logger.getLogger(ChrOrderSave.class.getName());
  private final Path dolPath;
  private List<String> characters;

  public ChrOrderSave(Path dolPath) {
    this.dolPath = dolPath;
  }

  public void setValues(List<String> values) {
    this.characters = values;
  }

  @Override
  public void run() {
    if (characters == null) {
      throw new IllegalArgumentException("Must first call setValues");
    }
    try(RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "rw")) {
      List<Integer> cssChrIds = ChrOrder.readCssChrIds(raf);
      raf.seek(ChrOrder.CSS_CHR_ID_ORDER);
      for (String character : characters) {
        // Get each chr id, find the index of that chr id, and write it out to the dol
        int id = ChrOrder.getChrId(character);
        int index = ChrOrder.getChrIdIndex(id, cssChrIds);
        raf.write(ByteUtils.fromInt32(index));
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Changing Order", e);
      Message.error("Error Changing Order", e.getMessage());
    }
  }
}