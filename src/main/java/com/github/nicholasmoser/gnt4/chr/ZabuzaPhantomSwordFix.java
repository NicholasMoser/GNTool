package com.github.nicholasmoser.gnt4.chr;

import com.github.nicholasmoser.utils.CRC32;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * A class to fix the issue where only Zabuza only the player 1 side can Phantom Sword. Phantom Sword
 * is where JA is unblockable if you land with it on a specific frame. The issue is fixed by adding
 * the unblockable flag for the KF flags on the first flag of landing.
 */
public class ZabuzaPhantomSwordFix {

  public static final String ZABUZA_0000_SEQ = "files/chr/zab/0000.seq";

  /**
   * Returns whether or not Zabuza's 0000.seq has been modified in the given uncompressed directory.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return If Zabuza's 0000.seq has been modified.
   * @throws IOException If any I/O issues occur
   */
  public static boolean isSeqModified(Path uncompressedDir) throws IOException {
    return CRC32.getHash(uncompressedDir.resolve(ZABUZA_0000_SEQ)) != 0x75a82238;
  }

  /**
   * Applies the fix to Zabuza's 0000.seq in the given uncompressed directory. Will probably break
   * the seq file if you run it against anything that a vanilla Zabuza 0000.seq. You can confirm
   * this is the case by first calling {@link #isSeqModified(Path)}.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void apply(Path uncompressedDir) throws IOException {
    File filePath = uncompressedDir.resolve(ZABUZA_0000_SEQ).toFile();
    try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
      raf.seek(0x1B868);
      raf.write(0x43);
      raf.seek(0x1B876);
      raf.write(0x15);
      raf.seek(0x1B878);
      raf.write(0x0);
      raf.seek(0x1B87A);
      raf.write(0x40);
    }
  }
}
