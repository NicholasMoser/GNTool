package com.github.nicholasmoser.gnt4.chr;

import com.github.nicholasmoser.utils.CRC32;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class to fix the issue where Kabuto 2A does less damage if used by player 2, compared to being
 * used by player 1. The fix is to add a single frame to the activated 2A action. This appears to
 * fix it due to the issue occurring on things that apply on the first frame. Since we've added a
 * frame, the issue no longer occurs and both player 1 and player 2 do full damage.
 */
public class KabutoScalingFix {

  public static final String KABUTO_0000_SEQ = "files/chr/kab/0000.seq";

  /**
   * Returns whether or not Kabuto's 0000.seq has been modified in the given uncompressed directory.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return If Kabuto's 0000.seq has been modified.
   * @throws IOException If any I/O issues occur
   */
  public static boolean isSeqModified(Path uncompressedDir) throws IOException {
    return CRC32.getHash(uncompressedDir.resolve(KABUTO_0000_SEQ)) != 0xe5d95d8e;
  }

  /**
   * Applies the fix to Kabuto's 0000.seq in the given uncompressed directory. Will probably break
   * the seq file if you run it against anything that a vanilla Kabuto 0000.seq. You can confirm
   * this is the case by first calling {@link #isSeqModified(Path)}.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void apply(Path uncompressedDir) throws IOException {
    Path filePath = uncompressedDir.resolve(KABUTO_0000_SEQ);
    byte[] oldBytes = Files.readAllBytes(filePath);
    byte[] newBytes = new byte[oldBytes.length + 0x14];
    // Copy the existing seq bytes up until near the end and update offsets
    System.arraycopy(oldBytes, 0, newBytes, 0, 0x26614);
    newBytes[0x17] = 0x28;
    newBytes[0x7C2] = 0x66;
    newBytes[0x7C3] = 0x14;
    newBytes[0x7C6] = 0x66;
    newBytes[0x7C7] = 0x14;
    newBytes[0x7CEB] = (byte) 0xB8;
    newBytes[0x84E3] = (byte) 0xC4;
    newBytes[0x8957] = 0x50;
    newBytes[0x8F47] = 0x50;
    newBytes[0x904F] = (byte) 0x8c;
    newBytes[0x90BF] = 0x50;
    newBytes[0x9483] = (byte) 0xB4;
    // Add the fix
    byte[] fix = new byte[] { 0x20, 0x11, 0x26, 0x3F, 0x00, 0x00, 0x00, 0x01, 0x20, 0x12, 0x00,
        0x26, 0x01, 0x32, 0x00, 0x00, 0x00, 0x02, 0x07, 0x70 };
    System.arraycopy(fix, 0, newBytes, 0x26614, 0x14);
    // Add the seq footer with updated offsets after the fix
    System.arraycopy(oldBytes, 0x26614, newBytes, 0x26614 + 0x14, 0xAC);
    newBytes[0x26637] = 0x50;
    newBytes[0x26677] = (byte) 0x88;
    Files.write(filePath, newBytes);
  }
}
