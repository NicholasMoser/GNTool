package com.github.nicholasmoser.gnt4.chr;

import com.github.nicholasmoser.utils.CRC32;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * A class to fix the issue where only Kisame only the player 1 side can Phantom Sword. Phantom Sword
 * is where JA is unblockable if you land with it on a specific frame. The issue is fixed by adding
 * the unblockable flag for the KF flags on the first flag of landing.
 */
public class KisamePhantomSwordFix {

  public static final String KISAME_0000_SEQ = "files/chr/kis/0000.seq";

  /**
   * Returns whether or not Kisame's 0000.seq has been modified in the given uncompressed directory.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return If Kisame's 0000.seq has been modified.
   * @throws IOException If any I/O issues occur
   */
  public static boolean isSeqModified(Path uncompressedDir) throws IOException {
    return CRC32.getHash(uncompressedDir.resolve(KISAME_0000_SEQ)) != 0x8dc540a8;
  }

  /**
   * Applies the fix to Kisame's 0000.seq in the given uncompressed directory. Will probably break
   * the seq file if you run it against anything that a vanilla Kisame 0000.seq. You can comfirm
   * this is the case by first calling {@link #isSeqModified(Path)}.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void apply(Path uncompressedDir) throws IOException {
    File filePath = uncompressedDir.resolve(KISAME_0000_SEQ).toFile();
    try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
      raf.seek(0x1C30C);
      raf.write(0x43);
      raf.seek(0x1C31A);
      raf.write(0x12);
      raf.seek(0x1C31C);
      raf.write(0x10);
      raf.seek(0x1C31D);
      raf.write(0x10);
      raf.seek(0x1C31E);
      raf.write(0x42);
      raf.seek(0x1C31F);
      raf.write(0xA0);
    }
  }
}
