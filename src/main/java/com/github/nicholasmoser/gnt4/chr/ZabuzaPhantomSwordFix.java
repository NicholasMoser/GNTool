package com.github.nicholasmoser.gnt4.chr;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.CRC32;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * A class to fix the issue where only Zabuza only the player 1 side can Phantom Sword. Phantom
 * Sword is where JA is unblockable if you land with it on a specific frame. The issue is fixed by
 * adding the unblockable flag for the KF flags on the first flag of landing.
 * <p>
 * More specifically, this changes:
 * <ul>
 *   <li>1B864 | add_nf_flags "GUARD, TDOWN" {241A0900 41000000}</li>
 *   <li>1B86C | op_2406 {24060400 05550000}</li>
 *   <li>1B874 | add_nf_flags "AUTODIR" {241A0900 02000000}</li>
 * </ul>
 *
 * <p>
 * to:
 * <ul>
 *   <li>1B864 | add_nf_flags "AUTODIR, GUARD, TDOWN" {241A0900 43000000}</li>
 *   <li>1B86C | op_2406 {24060400 05550000}</li>
 *   <li>1B874 | add_kf_flags "NOGUARD" {241A1500 00004000}</li>
 * </ul>
 */
public class ZabuzaPhantomSwordFix {
  private static final byte[] OLD_BYTES = new byte[]{0x24, 0x1A, 0x09, 0x00, 0x41, 0x00, 0x00, 0x00,
      0x24, 0x06, 0x04, 0x00, 0x05, 0x55, 0x00, 0x00, 0x24, 0x1A, 0x09, 0x00, 0x02, 0x00, 0x00,
      0x00};
  private static final byte[] NEW_BYTES = new byte[]{0x24, 0x1A, 0x09, 0x00, 0x43, 0x00, 0x00, 0x00,
      0x24, 0x06, 0x04, 0x00, 0x05, 0x55, 0x00, 0x00, 0x24, 0x1A, 0x15, 0x00, 0x00, 0x00, 0x40,
      0x00};
  private static final int OFFSET = 0x1B864;

  public static SeqEdit getSeqEdit(Path seqPath) throws IOException {
    return SeqEditBuilder.getBuilder()
        .name("Zabuza Phantom Sword Fix")
        .newBytes(NEW_BYTES)
        .seqPath(seqPath)
        .startOffset(OFFSET)
        .endOffset(OFFSET + NEW_BYTES.length)
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

  public static void removeOldFix(Path seqPath) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(seqPath.toFile(), "r")) {
      raf.seek(OFFSET);
      raf.write(OLD_BYTES);
    }
  }
}
