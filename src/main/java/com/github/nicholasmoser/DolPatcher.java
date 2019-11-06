package com.github.nicholasmoser;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Patches the Start.dol file to fix an audio offset issue. The audio issue is that if the offset
 * for an audio file in the game.toc does not end with 0x0000 or 0x8000 the game will crash. This is
 * because the instruction at 0x8016fc08 is <code>rlwinm. r0, r0, 0, 17, 31 (00007fff)</code> and
 * the instruction at 0x8016fc0c is <code>beq- 0x8016FC2C</code>. The end result of these two
 * operations is that it will not branch if the offset is something like 0x0c3e7800 and will instead
 * enter OSPanic (crash). The reason that the offset can be changed in the game.toc is because
 * GCRebuilder.exe will use new offsets for the audio files if the files placed before them in the
 * ISO have a larger size than they previously did. Since GCRebuilder.exe cannot be modified, the
 * only two solutions right now are to try and use padding or modify the game code. Modifying the
 * game code does not seem to have any side effects, so this is the currently accepted solution.
 * More precisely, the instruction at 0x8016fc08 is a conditional branch. The fix is to change it to
 * an unconditional branch so that it always branches. Therefore it will never encounter the OSPanic
 * from this method under any circumstances. May be related to
 * https://dolphin-emu.org/blog/2019/02/01/dolphin-progress-report-dec-2018-and-jan-2019/#50-9232-report-dtk-audio-in-increments-of-0x8000-by-booto
 */
public class DolPatcher {

  private Path dol;

  // Whether or not to actually modify the files (disable for unit tests).
  private boolean dryRun;

  private static final byte[] INSTRUCTIONS_8016fc00 =
      new byte[] {0x3b, (byte) 0xe3, 0x61, 0x78, 0x7c, 0x07, (byte) 0xea, 0x14};

  private static final byte[] INSTRUCTIONS_8016fc08 =
      new byte[] {0x54, 0x00, 0x04, 0x7f, 0x41, (byte) 0x82, 0x00, 0x20};

  private static final byte[] INSTRUCTIONS_8016fc08_PATCHED =
      new byte[] {0x54, 0x00, 0x04, 0x7f, 0x48, 0x00, 0x00, 0x21};

  /**
   * Creates a new DolPatcher.
   * 
   * @param dol The path to the dol you wish to patch.
   */
  public DolPatcher(Path dol) {
    this.dol = dol;
    this.dryRun = false;
  }

  /**
   * Creates a new DolPatcher.
   * 
   * @param dol The path to the dol you wish to patch.
   * @param dryRun Whether or not to actually write the patched bytes. Set to true for unit tests.
   */
  public DolPatcher(Path dol, boolean dryRun) {
    this.dol = dol;
    this.dryRun = dryRun;
  }

  /**
   * Patches the audio fix into the dol file. This will replace the instruction bytes at 0x8016fc08
   * from 0x41820020 to 0x48000021. Or in plain terms, changes the conditional branch to an
   * unconditional branch.
   * 
   * @return True if the dol was patched, false if not.
   * @throws IOException If the instruction could not be found or file could not be read.
   */
  public boolean patchAudioOffset() throws IOException {
    boolean codeFound = false;
    int totalBytesRead = 0;
    try (InputStream in = Files.newInputStream(dol)) {
      byte[] buffer = new byte[8];
      int bytesRead = 0;

      while ((bytesRead = in.read(buffer)) >= 0) {
        totalBytesRead += bytesRead;
        if (codeFound) {
          if (Arrays.equals(buffer, INSTRUCTIONS_8016fc08)) {
            break;
          } else if (Arrays.equals(buffer, INSTRUCTIONS_8016fc08_PATCHED)) {
            return false;
          } else {
            throw new IOException("Unexpected instruction at 0x8016fc08 in Start.dol, actually:"
                + Arrays.toString(buffer));
          }
        } else if (Arrays.equals(buffer, INSTRUCTIONS_8016fc00)) {
          codeFound = true;
        }
      }
    }
    if (codeFound) {
      if (!dryRun) {
        writePatchedBytes(totalBytesRead - 8);
      }
      return true;
    } else {
      throw new IOException("Unable to find instruction at 0x8016fc00 in Start.dol.");
    }
  }

  /**
   * Writes the audio fix to the dol file.
   * 
   * @param index The location of instruction 0x8016fc08.
   * @throws IOException If the bytes were unable to be written.
   */
  private void writePatchedBytes(int index) throws IOException {
    try (RandomAccessFile file = new RandomAccessFile(dol.toFile(), "rw")) {
      file.seek(index);
      file.write(INSTRUCTIONS_8016fc08_PATCHED);
    }
  }
}
