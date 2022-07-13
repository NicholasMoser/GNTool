package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class StageOrderSave extends OrderSave {
  private static final Logger LOGGER = Logger.getLogger(ChrOrderSave.class.getName());
  private final Path charSel;
  private final Path charSel4;
  private List<String> stages;

  public StageOrderSave(Path uncompressedDir) {
    this.charSel = uncompressedDir.resolve(StageOrder.CHAR_SEL);
    this.charSel4 = uncompressedDir.resolve(StageOrder.CHAR_SEL_4);
  }

  public void setValues(List<String> values) {
    this.stages = values;
  }

  @Override
  public void run() {
    if (stages == null) {
      Platform.runLater(() -> {
        LOGGER.log(Level.SEVERE, "Must first call setValues");
        Message.error("Must First Call setValues", "Must first call setValues");
      });
      return;
    }
    try(RandomAccessFile raf = new RandomAccessFile(charSel.toFile(), "rw")) {
      raf.seek(StageOrder.STAGE_IDS_OFFSET);
      for (String stage : stages) {
        // Get each chr id, find the index of that chr id, and write it out to the dol
        int id = StageOrder.getStageId(stage);
        raf.write(ByteUtils.fromInt32(id));
      }
      raf.seek(StageOrder.STAGE_DISPLAY_IDS_OFFSET);
      for (String stage : stages) {
        // Get each chr id, find the index of that chr id, and write it out to the dol
        int id = StageOrder.getStageOrderId(stage);
        raf.write(ByteUtils.fromInt32(id));
      }
    } catch (Exception e) {
      Platform.runLater(() -> {
        LOGGER.log(Level.SEVERE, "Error Changing Order of " + charSel.getFileName(), e);
        Message.error("Error Changing Order of " + charSel.getFileName(), e.getMessage());
      });
      return;
    }
    try(RandomAccessFile raf = new RandomAccessFile(charSel4.toFile(), "rw")) {
      raf.seek(StageOrder.STAGE_IDS_OFFSET);
      for (String stage : stages) {
        // Get each chr id, find the index of that chr id, and write it out to the dol
        int id = StageOrder.getStageId(stage);
        raf.write(ByteUtils.fromInt32(id));
      }
      raf.seek(StageOrder.STAGE_DISPLAY_IDS_OFFSET);
      for (String stage : stages) {
        // Get each chr id, find the index of that chr id, and write it out to the dol
        int id = StageOrder.getStageOrderId(stage);
        raf.write(ByteUtils.fromInt32(id));
      }
    } catch (Exception e) {
      Platform.runLater(() -> {
        LOGGER.log(Level.SEVERE, "Error Changing Order of " + charSel4.getFileName(), e);
        Message.error("Error Changing Order of " + charSel4.getFileName(), e.getMessage());
      });
      return;
    }
  }
}