package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChrOrderSaveTask extends SaveTask<Void> {
  private final Path dolPath;
  private List<String> characters;

  public ChrOrderSaveTask(Path dolPath) {
    this.dolPath = dolPath;
  }

  public void setValues(List<String> values) {
    this.characters = values;
  }

  @Override
  protected Void call() throws Exception {
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
      return null;
    }
  }
}