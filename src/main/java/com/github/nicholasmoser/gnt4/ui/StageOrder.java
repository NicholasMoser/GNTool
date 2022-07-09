package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Stages;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StageOrder {
  public static final String CHAR_SEL = "files/maki/char_sel.seq";
  public static final String CHAR_SEL_4 = "files/maki/charsel4.seq";
  public static final long STAGE_IDS_OFFSET = 0x2370;
  public static final long STAGE_DISPLAY_IDS_OFFSET = 0x23F0;

  public static int getStageId(String stage) {
    return GNT4Stages.STAGE_IDS.inverse().get(stage);
  }

  public static int getStageOrderId(String stage) {
    return GNT4Stages.STAGE_DISPLAY_IDS.inverse().get(stage);
  }

  public static List<String> getCurrentStageOrder(Path uncompressedDirectory) throws IOException {
    List<String> stages = new ArrayList<>(31);
    try(RandomAccessFile raf = new RandomAccessFile(uncompressedDirectory.resolve(CHAR_SEL).toFile(), "r")) {
      raf.seek(STAGE_IDS_OFFSET);
      for (int i = 0; i < 31; i++) {
        stages.add(GNT4Stages.STAGE_IDS.get(ByteUtils.readInt32(raf)));
      }
    }
    try(RandomAccessFile raf = new RandomAccessFile(uncompressedDirectory.resolve(CHAR_SEL_4).toFile(), "r")) {
      raf.seek(STAGE_IDS_OFFSET);
      for (int i = 0; i < 31; i++) {
        String actual = GNT4Stages.STAGE_IDS.get(ByteUtils.readInt32(raf));
        String expected = stages.get(i);
        if (!actual.equals(expected)) {
          throw new IOException(String.format("Expected: %s\nActual: %s", expected, actual));
        }
      }
    }
    return stages;
  }
}
