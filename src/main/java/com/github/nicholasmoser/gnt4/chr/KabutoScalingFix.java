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

  private static final byte[] NEW_BYTES = new byte[]{0x20, 0x11, 0x26, 0x3F, 0x00, 0x00, 0x00,
      0x01, 0x20, 0x12, 0x00, 0x26, 0x01, 0x3C, 0x00, 0x00, 0x00, 0x00, 0x13, 0x74};
  private static final int OFFSET = 0x20770;

  /**
   * Returns the SeqEdit object for this fix.
   *
   * @param seqPath The path to the Kabuto 0000.seq file.
   * @return The SeqEdit object.
   * @throws IOException If any I/O exception occurs.
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

  /**
   * Returns if this Kabuto 0000.seq file is using the old version of the 2A scaling fix that
   * wrote bytes directly to the seq file.
   *
   * @param seqPath The seq file to check.
   * @return If it is using the old fix.
   * @throws IOException If any I/O exception occurs.
   */
  public static boolean isUsingOldFix(Path seqPath) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
      raf.seek(0x26614);
      byte[] bytes = new byte[4];
      if (raf.read(bytes) != 4) {
        throw new IOException("Failed to read 4 bytes from " + seqPath);
      }
      return Arrays.equals(bytes, new byte[] { 0x20, 0x11, 0x26, 0x3F });
    }
  }

  /**
   * Returns if this Kabuto 0000.seq file is using the new version of the 2A scaling fix that
   * uses seq extensions.
   *
   * @param seqPath The seq file to check.
   * @return If it is using the new fix.
   * @throws IOException If any I/O exception occurs.
   */
  public static boolean isUsingNewFix(Path seqPath) throws IOException {
    List<SeqEdit> edits = SeqExt.getEdits(seqPath);
    return edits.contains(getSeqEdit(seqPath));
  }
}
