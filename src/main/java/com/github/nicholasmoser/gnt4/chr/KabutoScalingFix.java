package com.github.nicholasmoser.gnt4.chr;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * A class to fix the issue where Kabuto 2A does less damage if used by player 2, compared to being
 * used by player 1. The fix is to add a single frame to the activated 2A action. This appears to
 * fix it due to the issue occurring on things that apply on the first frame. Since we've added a
 * frame, the issue no longer occurs and both player 1 and player 2 do full damage.
 */
public class KabutoScalingFix {
  private static final byte[] NEW_BYTES = new byte[] { 0x20, 0x11, 0x26, 0x3F, 0x00, 0x00, 0x00, 0x01, 0x20, 0x12, 0x00,
      0x26, 0x01, 0x3C, 0x00, 0x00, 0x00, 0x00, 0x13, 0x74};
  private static final int OFFSET = 0x20770;

  /**
   * Get the seq edit for the Kabuto scaling fix.
   *
   * @param seqPath The path to Kabuto's 0000.seq
   * @return The seq edit for the Kabuto 2A scaling fix.
   */
  public static SeqEdit getSeqEdit(Path seqPath) throws IOException {
    return SeqEditBuilder.getBuilder()
        .name("Kabuto Scaling Fix")
        .newBytes(NEW_BYTES)
        .seqPath(seqPath)
        .startOffset(OFFSET)
        .endOffset(OFFSET + 8)
        .create();
  }

  public static boolean isUsingOldFix(Path seqPath) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
      raf.seek(OFFSET);
      byte[] bytes = new byte[NEW_BYTES.length];
      if (raf.read(bytes) != NEW_BYTES.length) {
        throw new IOException("Failed to read " + NEW_BYTES.length + " bytes from " + seqPath);
      }
      return Arrays.equals(bytes, NEW_BYTES);
    }
  }

  public static boolean isUsingNewFix(Path seqPath) throws IOException {
    List<SeqEdit> edits = SeqExt.getEdits(seqPath);
    return edits.contains(getSeqEdit(seqPath));
  }
}
